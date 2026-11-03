package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
	
	@GetMapping("/")
	public String home() {
		return "home";
	}
	@GetMapping("/bean-output-converter")
	public String beanOutputConverter() {
	return "bean-output-converter";
	}

}
