package com.example.demo.service;

import java.util.List;

import javax.print.attribute.standard.Media;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImageMessage;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import reactor.core.publisher.Flux;

@Service
@S1f4j
public class AiService {
	
	private ChatClient chatClient;
	
	public AiService(ChatClient.Builder chatCliBuilder) {
		this.chatClient = chatCliBuilder.build();
	}
	
	@Autowired
	private ImageModel imageModel;
	
	//이미지 분석하기
	public Flux<String> imageAnalysis(String question, String contentType, byte[] bytes){
		
	//한글을 영문으로 번역 기능
	private String koToEn(String text) {
	
    //이미지를 생성하는 함수
	public String generateImage(String question) {
		//한글 질문을 AI 인지하기 편하게 영문으로
		String englishDescription = koToEn(question);
		
		OpenAiImageOptions imageOptions = OpenAiImageOptions.builder()
										.model("gpt-image-2")
										.quality("low")
										.width(1024)
										.height(1024)
										.N(1)
										.build();
		//이미지를 설명
		ImageMessage imageMessage = new ImageMessage(englishDescription); //영어로 설명
		//프롬프트 생성하기
		List<ImageMessage> imageMessages = List.of(imageMessage);
		ImagePrompt imagePrompt = new ImagePrompt(imageMessagesList, imageOptions);
		
		ImageResponse imageResponse = ImageModel.call(imagePrompt);
		
		String b64Json = imageResponse.getResult().getOutput().getB64Json();
		return b64Json;
	}
	}
		SystemMessage systemMessage = SystemMessage.builder()
				.text("""당신은 이미지 분석 전문가입니다.
						사용자 질문에 맞게 이미지 분석하게 한구어로 답변하세요""").build();

	Media media = Media
				.builder()
				.mimeType(MimeType.valueOf(contentType))
				.data(new ByteArrayResource(bytes))
				.build();
						
	UserMessage userMessage = UserMessage
							.builder()
							.text(question)
							.media(media)  //Media media = Media 미디어 정보도 같이
							.build();
	
	Prompt prompt = Prompt
					.builder()
					.messages(systemMessage, userMessage)
					.build();
	
	//준비 자원을 LLL 전달 ChatClient
	Flux<String> flux = this.chatClient
						.prompt(prompt)
						.stream()
						.content();
	
	return flux;
	}

}
