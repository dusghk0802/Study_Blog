package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.AiService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@Slf4j //로그 찍어서 결과 확인하기
public class AiController {
	
	@Autowired
	private AiService aiService; //여기에 함수를 사용하니까 
	
	@PostMapping(value="/image-analysis",
			 	consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
			    produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Flux<String> imageAnlaysis(
				@RequestParam("question") String question,
				@RequestParam(value="attach",required = false) MultipartFile attach) throws IOException
	{
		
		//이미지가 업로드 되지 않았어요
		if(attach == null || !attach.getContentType().contains("image/")) {
			Flux<String> response = Flux.just("이미지를 올려주세요");
		}
		
		Flux<String> flux = aiService.imageAnalysis(question, 
				                                    attach.getContentType(), 
				                                    attach.getBytes()); 
		
		return flux;
	}

	@PostMapping(
		    value = "/image-generate",
		    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
		    produces = MediaType.TEXT_PLAIN_VALUE
		  )
		  public String imageGenerate(@RequestParam("description") String description) {
		    try {
		      String b64Json = aiService.generateImage(description);
		      return b64Json;
		    } catch(Exception e) {
		      e.printStackTrace();
		      return "Error: " + e.getMessage();
		    }
		  }

}