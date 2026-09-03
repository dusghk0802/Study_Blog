package com.example.demo;

public class LombokApplication {
    public static void main(String[] args) {
//        Product p = new Product("바나나", "맛있는 과일", 1000);
        Product p = Product.builder()
                .name("바나나")
                .description("맛있는 과일")
                .price(1000)
                .build();

        Product p1 = Product.builder()
                .name("바나나")
                .description("설명이 다른 과일")
                .price(1000)
                .build();

/*        p.setName("바나나");
        p.setDescription("맛있는 과일");
        p.setPrice(1000);*/

/*        System.out.println(p.getName());
        System.out.println(p.getDescription());
        System.out.println(p.getPrice());*/
        System.out.println(p);
        System.out.println(p1);
        System.out.println("두 상품 비교: " + p.equals(p1));

    }
}
