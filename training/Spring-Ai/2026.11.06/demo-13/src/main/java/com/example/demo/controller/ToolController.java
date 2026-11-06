package com.example.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.tools.SpringTools;

@RestController
@RequestMapping("/tool")
public class ToolController {

	private final ChatClient chatClient;
	
	public ToolController(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}
	
	@GetMapping("/simple")
	public String toolSimple() {
		return chatClient.prompt("내일은 몇일일까?")
				.call().content();
	}
	
	@GetMapping("/date")
	public String toolDate() {
		return chatClient.prompt("내일은 몇일일까?")
				.tools(new SpringTools()) //도구 제시
				.call()
				.content();
	}
	
	@GetMapping("/emp")
	public String toolEmp(@RequestParam("name") String name) {
		String promptText = String.format(
				"아래 도구를 사용하여 직원 '%s'의 취미를 알려줘. 만약 지원이 없으면 '해당 직원이 없습니다'라고 답변해줘\n"
						+ "도구: UserHobby(name) -  직원 이름을 넣으면 취미를 알려줌"
						, name);
		
		return chatClient.prompt(promptText)
				.tools(new SpringTools())
				.call()
				.content();
	}
}

//localhost:8080/tool/simple

//localhost:8080/tool/date


//localhost:8080/tool/emp?name=이순신
//localhost:8080/tool/emp?name=트럼프
