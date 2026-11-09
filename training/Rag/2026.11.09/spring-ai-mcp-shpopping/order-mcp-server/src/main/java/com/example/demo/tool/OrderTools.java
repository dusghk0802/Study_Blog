package com.example.demo.tool;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.example.demo.dto.OrderInfo;
import com.example.demo.service.OrderService;

@Component
public class OrderTools {

		private final OrderService orderService;
		
		public OrderTools(OrderService orderService) {
			this.orderService = orderService;
		}
		
		//Tool @Tool   @MCPTool 둘다 호환 가능
		@McpTool(name="getOrder", description = "OracleDB 에서 쇼핑몰 주무번호로 주문정보와 배송상태 조회")
		public String getOrder(@McpToolParam(description = "조회할 주문번호") Long orderId) {
			
			//MCPTool 의 목적이 DB조회 작업
			OrderInfo order = orderService.getOrder(orderId);
			
			if(order == null) {
				return "해당 주문번호의 주문이 없습니다";
			}
			    return """
			            주문번호: %d
			            고객명: %s
			            상품명: %s
			            수량: %d
			            총금액: %s원
			            주문상태: %s
			            주문일자: %s
			            """.formatted(
			                order.orderId(),
			                order.customerName(),
			                order.productName(),
			                order.quantity(),
			                order.totalAmount(),
			                order.orderStatus(),
			                order.orderDate()
			            );
		}
}
