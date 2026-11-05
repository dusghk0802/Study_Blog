package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmbedingService {

	private final VectorStore vectorStore;
	
	public void processUploadPdf(MultipartFile file) throws IOException {
		
		//1.사용자가 업로드한  PDF 파일을 임시 파일로 생성
		File tmpFile = File.createTempFile("uploaded", ".pdf");
		file.transferTo(tmpFile);
		Resource filResource = new FileSystemResource(tmpFile);
		
		try {
				PdfDocumentReaderConfig config = PdfDocumentReaderConfig
						.builder()
						.withPageTopMargin(0) //PDF 페이지 상단에 여백을 설정
						.withPageExtractedTextFormatter(
														ExtractedTextFormatter
														.builder()
														.withNumberOfBottomTextLinesToDelete(0)
														.build())
						.withPagesPerDocument(1) //한번에 처리하는 페이지를 지정
						.build();
				//read PDF 문서에 환경 설정 (상단, 하단, 제목 생략 또는 처리)
				//파일 선택, 환경설정 ....처리
				PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(filResource, config);
				
				//[① 적재(Ingestion) - 미리 1회]
				//문서(PDF, 웹, DB) → 읽기 → 청크 분할 → 임베딩(벡터 변환) → 벡터DB 저장
				//Documnet > TokenTextSplitter 청크분할
				List<Document> documents = pdfReader.get();
				
				//문서를 분할 하기 위한 (읽기 편하게) Token TextSplitter
				TokenTextSplitter splitter = new TokenTextSplitter(1000, 400, 10, 5000, true);
				
				/*
				  new TokenTextSplitter(
  										1000,  // chunkSize
  										400,   // minChunkSizeChars
  										10,    // minChunkLengthToEmbed
  										5000,  // maxNumChunks
  										true   // keepSeparator
				);
				 매개변수						설정값	의미
				chunkSize					1000	청크의 목표 최대 토큰 수
				minChunkSizeChars			400		자연스러운 분할 지점을 찾을 때 사용하는 최소 문자 수
				minChunkLengthToEmbed		10		너무 짧은 청크를 제외하기 위한 최소 길이 기준
				maxNumChunks				5000	생성할 수 있는 최대 청크 개수
				keepSeparator				true	분할 시 구분자를 유지할지 여부

			   PDF 추출한 텍스트가 10000토큰 chunk 1000

			 */
				
				List<Document> splitDocuments = splitter.apply(documents);
				//PGvector store 저장
				vectorStore.accept(splitDocuments);
				System.out.println("처리완료...");
		} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("PDF 처리 실패", e);
		} finally {
			tmpFile.delete(); //Exception 발생하던 발생하지 않던 강제로 임시 파일은 삭제
		}
	}

}
