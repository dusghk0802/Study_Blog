package com.example.demo.controller;

import java.util.List;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.HotelEmbeddingService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hotel")
@CrossOrigin(origins = "http://localhost:5173/")  //개발 테스트 할때나...
public class HotelController {

//	private final HotelEmbeddingService hotelEmbeddingService;
//	private final DocumentUploadController documentUploadController;
	
	private final VectorStore vectorStore;
	private final ChatModel chatModel;
	
	@GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
	public Flux<String> hotelChatbot(@RequestParam("question") String question) {
		
		ChatClient chatClient = ChatClient.builder(chatModel).build();
		System.out.println("생성된 LLM 모델 :" + chatClient);
		
		//유사도 검색
		List<Document> results = vectorStore.similaritySearch(SearchRequest.builder()
															.query(question)
															.similarityThreshold(0.5)
															.topK(1)
															.build());
		
		//검색 결과를 LLM에게 가공해서..주면...이쁘게 만들어서 전달
		System.out.println("Vector Store 유사도 검색 결과 : " + results);
		String template = """
				당신은 어느 호텔의 직원입니다. 문맥에 따라서 고객의 질문에 정중하게 답변해주세요.
				컨텍스트가 질문에 대답할 수 없는 경우, "죄송합니다. 모르겠습니다."라고 대답하세요.
				컨텍스트:
				{context}
				질문:
				{question}
				""";
		return chatClient.prompt()
						.user(promptUserSpec -> promptUserSpec.text(template)
							.param("context", results)
							.param("question", question))
							.stream().content();
	}
}
