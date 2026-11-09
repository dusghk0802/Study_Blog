package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.TravelPlan;

@Repository
public class TravelPlanRepository {

	private final JdbcClient jdbcClient;

	public TravelPlanRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public int save(String tripTitle, LocalDate tripDate, Integer visitOrder, String placeName,
			String category, String address, String placeUrl, String memo) {
		return jdbcClient.sql("""
				INSERT INTO TRAVEL_PLAN
				  (TRIP_TITLE, TRIP_DATE, VISIT_ORDER, PLACE_NAME, CATEGORY, ADDRESS, PLACE_URL, MEMO)
				VALUES
				  (:tripTitle, :tripDate, :visitOrder, :placeName, :category, :address, :placeUrl, :memo)
				""")
				.param("tripTitle", tripTitle)
				.param("tripDate", tripDate)
				.param("visitOrder", visitOrder == null ? 1 : visitOrder)
				.param("placeName", placeName)
				.param("category", category)
				.param("address", address)
				.param("placeUrl", placeUrl)
				.param("memo", memo)
				.update();
	}

	public List<TravelPlan> findAll() {
		return jdbcClient.sql("""
				SELECT * FROM TRAVEL_PLAN
				ORDER BY TRIP_DATE, TRIP_TITLE, VISIT_ORDER, PLAN_ID
				""")
				.query(TravelPlan.class)
				.list();
	}

	public List<TravelPlan> findByTitle(String keyword) {
		return jdbcClient.sql("""
				SELECT * FROM TRAVEL_PLAN
				WHERE TRIP_TITLE LIKE '%' || :keyword || '%'
				ORDER BY TRIP_DATE, VISIT_ORDER, PLAN_ID
				""")
				.param("keyword", keyword)
				.query(TravelPlan.class)
				.list();
	}

	public int deleteById(Long planId) {
		return jdbcClient.sql("DELETE FROM TRAVEL_PLAN WHERE PLAN_ID = :planId")
				.param("planId", planId)
				.update();
	}
}
