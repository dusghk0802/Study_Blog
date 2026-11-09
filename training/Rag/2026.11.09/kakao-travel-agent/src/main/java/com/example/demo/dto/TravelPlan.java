package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TravelPlan(
		Long planId,
		String tripTitle,
		LocalDate tripDate,
		Integer visitOrder,
		String placeName,
		String category,
		String address,
		String placeUrl,
		String memo,
		LocalDateTime createdAt
) {}
