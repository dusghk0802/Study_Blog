package com.example.demo.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ClaimDtos.OcrResult;
import com.google.cloud.documentai.v1.Document;
import com.google.cloud.documentai.v1.DocumentProcessorServiceClient;
import com.google.cloud.documentai.v1.DocumentProcessorServiceSettings;
import com.google.cloud.documentai.v1.OcrConfig;
import com.google.cloud.documentai.v1.ProcessOptions;
import com.google.cloud.documentai.v1.ProcessRequest;
import com.google.cloud.documentai.v1.ProcessorName;
import com.google.cloud.documentai.v1.RawDocument;
import com.google.protobuf.ByteString;

/** Google Document AI로 글자를 읽는다. */
@Service
public class OcrService {

	private final String projectId;
	private final String location;
	private final String processorId;
	private final ReceiptParser parser;

	public OcrService(@Value("${ocr.project-id}") String projectId,
			@Value("${ocr.location}") String location,
			@Value("${ocr.processor-id}") String processorId,
			ReceiptParser parser) {
		this.projectId = projectId;
		this.location = location;
		this.processorId = processorId;
		this.parser = parser;
	}

	public OcrResult read(byte[] bytes, String contentType) throws IOException {
		DocumentProcessorServiceSettings settings = DocumentProcessorServiceSettings.newBuilder()
				.setEndpoint(location + "-documentai.googleapis.com:443")
				.build();

		// 키가 없어도 서버는 뜨고 OCR 단계만 실패하도록 요청할 때 연결한다
		try (DocumentProcessorServiceClient client = DocumentProcessorServiceClient.create(settings)) {
			ProcessRequest request = ProcessRequest.newBuilder()
					.setName(ProcessorName.of(projectId, location, processorId).toString())
					.setRawDocument(RawDocument.newBuilder()
							.setContent(ByteString.copyFrom(bytes))
							.setMimeType(contentType))
					.setProcessOptions(ProcessOptions.newBuilder()
							.setOcrConfig(OcrConfig.newBuilder()
									.setHints(OcrConfig.Hints.newBuilder().addLanguageHints("ko"))))
					.build();

			Document document = client.processDocument(request).getDocument();
			String text = document.getText();
			return new OcrResult(text, confidence(document), parser.findTotal(text), parser.findDate(text),
					parser.findBusinessNumber(text), parser.findAmounts(text));
		}
	}

	/** 단어별 신뢰도의 평균(%) */
	private int confidence(Document document) {
		double sum = 0;
		int count = 0;
		for (Document.Page page : document.getPagesList()) {
			for (Document.Page.Token token : page.getTokensList()) {
				sum += token.getLayout().getConfidence();
				count++;
			}
		}
		return count == 0 ? 0 : (int) Math.round(sum / count * 100);
	}
}
