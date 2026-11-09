package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.dto.OrderInfo;

@Mapper
public interface OrderMapper {
	OrderInfo findById(@Param("orderId") Long orderId);
}
