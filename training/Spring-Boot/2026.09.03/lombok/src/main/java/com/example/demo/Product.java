package com.example.demo;

import lombok.*;

/*@Getter
@Setter
@ToString*/
//equals(), hashcode() 등 묶어서 제공 => @Data
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"name", "price"})
public class Product {
    private String name;
    private String description;
    private int price;
}
