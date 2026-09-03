package com.example.demo;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class EspressoMachine implements CoffeeMachine{
    @Override
    public String brew() {
        return "Brewing coffee with Expresso Machine";
    }
}
