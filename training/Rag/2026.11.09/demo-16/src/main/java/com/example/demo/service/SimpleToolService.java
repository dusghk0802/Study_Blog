package com.example.demo.service;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Service;

@Service
public class SimpleToolService {
	
	@McpTool(name="HelloTool", description = "간단한 Hello 메세지를 반환합니다.")
	public String helloTool() {
		System.out.println("** HelloTool 호출 **");
		return "Hello from MCP server Tool";
	}

}
