package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/")  //localhost:8080 > 
	public String home() {
		return "home";  //templates 안에 home.html
	}
	
	
	@GetMapping("/multi-messages")  //localhost:8080/multi-messages
	public String multiMessages() {
		return "multi-messages"; //templates 안에 multi-messages.html
	}
		
	
}
