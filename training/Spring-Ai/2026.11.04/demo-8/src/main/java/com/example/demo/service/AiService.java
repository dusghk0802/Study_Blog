package com.example.demo.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImageMessage;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AiService {
	
	private ChatClient chatClient;
	
	public AiService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}
	
	@Autowired
	private ImageModel imageModel;
	
	//이미지 분석하기
	public Flux<String> imageAnalysis(String question , String contentType, byte[] bytes){
		//시스템 메시지 생성
		SystemMessage systemMessage = SystemMessage.builder()
				                      .text("""
				                              당신은 이미지 분석 전문가입니다.   
				                              사용자 질문에 맞게 이미지를 분석하고 답변을 한국어로 하세요. 
				                            """).build();
		//미디어 ...생성하기
		Media media = Media
				      .builder()
				      .mimeType(MimeType.valueOf(contentType))
				      .data(new ByteArrayResource(bytes))
				      .build();
		
		//사용자 메시지 생성
		UserMessage userMessage = UserMessage
				                  .builder()
				                  .text(question)
				                  .media(media) //Media media = Media 미디어 정보도 같이
				                  .build();
		
		Prompt prompt = Prompt
				        .builder()
				        .messages(systemMessage,userMessage)
				        .build();
		
		//준비 자원을  LLL 전달 ChatClient
		Flux<String> flux = this.chatClient
				            .prompt(prompt)
				            .stream()
				            .content();
		
		return flux;
	}
	
    //한글을 영문으로 번역 기능
	private String koToEn(String text) {
		    String question = """
		          당신은 번역사입니다. 아래 한글 문장을 영어 문장으로 번역해주세요.
		          %s
		        """.formatted(text);

		    // UserMessage 생성
		    UserMessage userMessage = UserMessage.builder()
		        .text(question)
		        .build();

		    // Prompt 생성
		    Prompt prompt = Prompt.builder()
		        .messages(userMessage)
		        .build();

		    // LLM을 호출하고 텍스트 답변 얻기
		    String englishDescription = chatClient.prompt(prompt).call().content();
		    return englishDescription;
		  }

    //이미지를 생성하는 함수 
	public String generateImage(String question) {
		//한글 질문을  AI 인지하기 편하게 영문으로 
		String englishDescription =  koToEn(question);
		
		OpenAiImageOptions imageOptions =   OpenAiImageOptions.builder()
											.model("gpt-image-2")
											.quality("low")
											.width(1024)
											.height(1024)
											.N(1)
											.build();
		
		//이미지를 설명 
		ImageMessage imageMessage = new ImageMessage(englishDescription); //영어로 설명
		//프롬프트 생성하기
		List<ImageMessage> imageMessageList = List.of(imageMessage);
		ImagePrompt imagePrompt = new ImagePrompt(imageMessageList,imageOptions);
		
		//모델 호출 응답 처리
		ImageResponse imageResponse = imageModel.call(imagePrompt);
		
		//base64 인코딩된 이미지를 문자열로 받기
		String b64Json = imageResponse.getResult().getOutput().getB64Json();

		return b64Json;
	}
}