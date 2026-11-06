package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

//Text 데이터 받아서 백터 저장소 임베딩 및 저장하기

@Service
@RequiredArgsConstructor
public class HotelEmbeddingService {

	//자동 객체 주입
	private final VectorStore vectorStore;
	private final JdbcClient jdbcClient;  //히카리 connectionPool 사용 연결 작업
	
	public void processUploadText(MultipartFile file) throws IOException {
		//복사본 생성..
		File tmpFile = File.createTempFile("upload", ".text");
		file.transferTo(tmpFile);
		Resource fileResource = new FileSystemResource(tmpFile);
		
		//파일이 준비
		//upload.txt (호텔정보가 있는 임시파일)
		
		//1) 테이블 데이터 확인, count 작업 : 별도의 JDBC
		Integer count = jdbcClient
				.sql("select count(*) from hotel_vector")
				.query(Integer.class)
				.single();
		try {
			if(count == 0) {
				//문서를 read Document 객체
				List<Document> documents = Files.lines(fileResource.getFile().toPath())
												.map(Document::new)
												.collect(Collectors.toList());
				/*
				 * Path path = fileResource.getFile().toPath(); List<String> =
				 * fileResource.getFile().toPTH(); List<Document> documents = new
				 * ArrayList<>(); for(String line : lines) { Document document - new
				 * Document(line); documentss.add(document); }
				 */
				
				//split 처리
				TokenTextSplitter splitter = new TokenTextSplitter();
				List<Document> spliDocuments = splitter.apply(documents);
				
				//백터 저장소 임베딩 및 저장
				vectorStore.accept(spliDocuments);
				
				/*
				 설정 항목				기본값		의미
				defaultChunkSize		800			청크의 목표 최대 토큰 수
				minChunkSizeChars		350			청크를 분할할 때 고려하는 최소 문자 수
				minChunkLengthToEmbed	5			임베딩할 청크의 최소 문자 수
				maxNumChunks			10,000		문서당 생성 가능한 최대 청크 수
				keepSeparator			true		분할 과정에서 구분자 유지 여부 
				  
				 TokenTextSplitter splitter =
				    new TokenTextSplitter(
				        800,     // defaultChunkSize
				        350,     // minChunkSizeChars
				        5,       // minChunkLengthToEmbed
				        10000,   // maxNumChunks
				        true     // keepSeparator
				    ); 
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			tmpFile.delete();
		}
	}
}
