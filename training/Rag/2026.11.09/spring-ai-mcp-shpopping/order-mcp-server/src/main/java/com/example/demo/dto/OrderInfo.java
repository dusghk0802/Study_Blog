package com.example.demo.dto;

import java.math.BigDecimal;  //숫자 (화폐를 다룰때 정확한 값 표기 : BigDecimal)
import java.time.LocalDateTime;

public record OrderInfo (
		
			Long orderId,
	        String customerName,
	        String productName,
	        Integer quantity,
	        BigDecimal totalAmount,
	        String orderStatus,
	        LocalDateTime orderDate
		) {}
	

