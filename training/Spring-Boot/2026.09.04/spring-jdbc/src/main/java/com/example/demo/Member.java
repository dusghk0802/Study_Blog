package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    private long id;
    private String name;
    private String email;
    private Integer age;

}
