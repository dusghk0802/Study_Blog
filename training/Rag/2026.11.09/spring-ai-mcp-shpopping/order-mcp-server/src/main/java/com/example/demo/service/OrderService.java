package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderInfo;
import com.example.demo.mapper.OrderMapper;

@Service
public class OrderService {
	
	private final OrderMapper orderMapper;
	
	public OrderService(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}
	
	public OrderInfo getOrder(Long orderId) {
		return orderMapper.findById(orderId);
	}
}
