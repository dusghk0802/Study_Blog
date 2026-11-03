package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class Hotel {
	
	//도시이름
	private String city;
	//호텔 이름 목록
	private List<String> name;

}
