package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import org.springframework.ai.image.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CreateImageController {
	
	 private final ImageModel imageModel;
	 
	 @GetMapping("/image-generate")
	 public String initPage() {
		 return "index";
	 }
	 
	 @GetMapping("/imgGen")
	 @ResponseBody
	 public String generateImage(@RequestParam("request") String request) throws IOException {
		 System.out.println("request : " + request);
		 
		 //1.1.x > 2.x  동일
		 ImageOptions options = ImageOptionsBuilder.builder()
				 				.model("gpt-image-2.5-flare")  //"gpt-image-1.5
				 				.width(1024)
				 				.height(1024)
				 				.build();
				                
		 ImagePrompt prompt = new ImagePrompt(request, options);

		 ImageResponse response = imageModel.call(prompt); //자원 (파일 받기)
		
		//String imageUrl = response.getResult().getOutput().getUrl();
		String b64Json = response.getResult().getOutput().getB64Json();
		byte[] imageBytes = Base64.getDecoder().decode(b64Json);
		
		Files.write(Path.of("new.png"), imageBytes);
		System.out.println("생성된 이미지의 URL : " + b64Json);
		 
		return b64Json;
	 }
	
}
/*

Spring AI 버전	메서드	상태
0.8.x		withModel()	사용
1.0.0-M4	withModel()	사용
1.0.0-M5	withModel() / model()	기존 메서드 Deprecated
1.0.7		model()	새로운 방식만 제공
1.1.x		model()	새로운 방식 사용 
 
*/