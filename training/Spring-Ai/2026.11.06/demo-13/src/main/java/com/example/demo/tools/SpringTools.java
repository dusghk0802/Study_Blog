package com.example.demo.tools;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringTools {
	
	private static final Map<String, String> dictionary = new HashMap<>();
	
	//static member field 초기화
	//static 초기자
	static {
		dictionary.put("이순신", "등산");
		dictionary.put("김유신", "축구");
		dictionary.put("유관순", "영화감상");
		dictionary.put("아무개", "독서");
	}
	
	//시스템 날짜를 return 하는 Tool
	@Tool
	public String getCurrentDateTime() {
//		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
//		return dateTime.toString();
		LocalDate today = LocalDate.now();
		System.out.println(today);
		return today.toString();
	}
	
	 @Tool
	   public String UserHobby(String name) {
		   return dictionary.getOrDefault(name, "해당 직원 없음");
	   }
	/*@Tool(description = "두 정수를 더합니다.")
    public int add(
        @ToolParam(description = "첫 번째 숫자") int a,
        @ToolParam(description = "두 번째 숫자") int b) {

        return a + b;*/
}

