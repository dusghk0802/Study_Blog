package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.demo.service.OllamaChatService;

@SpringBootApplication
public class Demo6Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Demo6Application.class, args);

		OllamaChatService ollamaChatService = context.getBean(OllamaChatService.class);
		String response = ollamaChatService.getQuestion();

		System.out.println("결과 : " + response);
	}

}
