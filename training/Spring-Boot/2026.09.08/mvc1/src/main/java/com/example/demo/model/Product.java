package com.example.demo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private Long id;
    private String name;
    private Integer price;
    private Integer stock;
}
