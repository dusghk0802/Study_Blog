package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.config.Assistant;
import com.example.demo.dto.RagDtos.DocumentInfo;
import com.example.demo.dto.RagDtos.UploadResult;

@Service
public class DocumentService {

	private static final byte[] PDF_SIGNATURE = "%PDF-".getBytes(StandardCharsets.US_ASCII);

	private final VectorStore vectorStore;
	private final JdbcClient jdbcClient;

	public DocumentService(VectorStore vectorStore, JdbcClient jdbcClient) {
		this.vectorStore = vectorStore;
		this.jdbcClient = jdbcClient;
	}

	public UploadResult upload(Assistant assistant, MultipartFile file) throws IOException {
		String fileName = validate(file);

		File tempFile = File.createTempFile("rag-upload", ".pdf");
		try {
			file.transferTo(tempFile);

			// F02 텍스트 추출
			List<Document> pages = readPages(tempFile);
			if (pages.isEmpty()) {
				throw new InvalidPdfException("글자를 추출하지 못했습니다. 스캔 이미지로만 된 PDF는 올릴 수 없습니다.");
			}

			// F03 청크 분할
			TokenTextSplitter splitter = new TokenTextSplitter(500, 200, 10, 5000, true);
			List<Document> chunks = splitter.apply(pages);

			// 임시 파일 이름 대신 원본 파일명과 과제를 저장한다 (F08)
			String uploadedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			chunks.forEach(chunk -> {
				chunk.getMetadata().put("file_name", fileName);
				chunk.getMetadata().put("domain", assistant.name());
				chunk.getMetadata().put("uploaded_at", uploadedAt);
			});

			// 같은 파일을 다시 올리면 중복 저장하지 않고 교체한다
			delete(assistant, fileName);

			// F04 임베딩 저장
			vectorStore.add(chunks);

			return new UploadResult(fileName, pages.size(), chunks.size());
		} finally {
			tempFile.delete();
		}
	}

	public List<DocumentInfo> list(Assistant assistant) {
		return jdbcClient.sql("""
				select metadata->>'file_name' as file_name, count(*) as chunks, max(metadata->>'uploaded_at') as uploaded_at
				from rag_vector
				where metadata->>'domain' = ?
				group by metadata->>'file_name'
				order by 1
				""")
				.param(assistant.name())
				.query((rs, i) -> new DocumentInfo(rs.getString(1), rs.getInt(2), rs.getString(3)))
				.list();
	}

	public void delete(Assistant assistant, String fileName) {
		FilterExpressionBuilder b = new FilterExpressionBuilder();
		Filter.Expression expression = b.and(b.eq("domain", assistant.name()), b.eq("file_name", fileName)).build();
		vectorStore.delete(expression);
	}

	/** F01 유효성 검사. 경로를 뺀 파일 이름을 돌려준다. */
	private String validate(MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			throw new InvalidPdfException("비어 있는 파일입니다.");
		}

		String original = file.getOriginalFilename();
		String fileName = original == null ? "" : Paths.get(original.replace('\\', '/')).getFileName().toString();
		if (!fileName.toLowerCase().endsWith(".pdf")) {
			throw new InvalidPdfException("PDF 파일만 올릴 수 있습니다.");
		}

		// 확장자만 바꾼 파일을 막기 위해 파일 내용의 시작 부분을 확인한다
		try (InputStream in = file.getInputStream()) {
			byte[] head = in.readNBytes(PDF_SIGNATURE.length);
			if (!java.util.Arrays.equals(head, PDF_SIGNATURE)) {
				throw new InvalidPdfException("PDF 형식이 아닌 파일입니다.");
			}
		}
		return fileName;
	}

	private List<Document> readPages(File file) {
		PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder().withPagesPerDocument(1).build();
		try {
			return new PagePdfDocumentReader(new FileSystemResource(file), config).get().stream()
					.filter(page -> page.getText() != null && !page.getText().isBlank())
					.toList();
		} catch (RuntimeException e) {
			throw new InvalidPdfException("PDF를 읽지 못했습니다. 파일이 손상되었거나 암호가 걸려 있을 수 있습니다.");
		}
	}
}
