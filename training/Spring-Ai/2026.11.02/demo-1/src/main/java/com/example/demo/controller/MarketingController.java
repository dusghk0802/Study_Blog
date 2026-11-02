package com.example.demo.controller;

import com.example.demo.Demo1Application;

import java.text.MessageFormat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.product;

@Controller
public class MarketingController {
	
	private final ChatClient chatClient;
	
	//생성자 주입
	public MarketingController(ChatClient.Builder builder) {
		this.chatClient = builder.build(); //OpenAi 객체 주입 (ChatModel를 통해서)
	}
	
	//화면 보여주세요 (view)
	@GetMapping("/marketing")
	public  String getMarketing() {
		return "marketing_request.html";  //templates 패키지 안에 있는 파일 이름  (marketing_request.html)
	}
	
	//화면 결과 실행해 주세요 (view 데이터 받아서 처리)
	@PostMapping("/marketing")
	public String postMarketing(product product , Model model) {
		System.out.println(product);
		
		//1. 시스템 메시지
		//javaScript var 과 동일
		//java var  script 단점 극복 
		var systemMessage = new SystemMessage(  //java 15부터  """ 텍스트 블럭 여러줄 문자열표현 """
			    """
                나는 마케팅 카피라이터 전문가야.

                입력된 제품 정보를 기반으로
                온라인 쇼핑몰/블로그/홍보 페이지에
                사용할 마케팅 문구를 작성해 줘.

                작성조건
                1. 첫 문장은 강렬하거나 공감 가는 표현 사용
                2. 제품 특징과 장점을 자연스럽게 설명
                3. 가격과 구매 링크를 포함
                4. 글자 수는 약 300~500자
                5. 캐주얼하지만 설득력 있는 톤
                6. 필요하면 이모지 활용
                """	
		);
		
		//2. 사용자 메시지
		var userMessage = new UserMessage(
			MessageFormat.format("""
					              입력정보
					              
					              -제품명 :{0}
					              -가격 :{1}
					              -구매링크  :{2}
					              -제품특징 :{3}   
					             """,
					             product.getName(),
					             product.getPrice(),
					             product.getLink(),
					             product.getFeatures())	
				
		);
		
		//3. ChatClient AI 호출
		String result =chatClient.prompt()
		                         .messages(systemMessage,userMessage)
		                         .call()
		                         .content(); //결과를 문자열로 
		
		
		//결과를 웹 페이지에서 보여주기
		//4. Thymeleaf 페이지 데이터 전달하기
		model.addAttribute("marketingResult",result);
		return "marketing_response.html"; //view page 정의 (담긴 객체가 view page forward )
	}
}