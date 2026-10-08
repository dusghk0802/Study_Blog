package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoffeeMaker {
    @Autowired
//    @Qualifier("dripCoffeeMachine")
    private List<CoffeeMachine> coffeeMachines;

    //스프림 부트가 클래스 객체에 직접 주입하므로 의존성을 주입받기 위해 setCoffeeMachine()은 더 이상 필요 없음.
    /*public void setCoffeeMachine(CoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }*/

    //모든 스프링 빈 객체를 생성하고, 필요한 의존성이 주입된 후에 자동으로 호출하게 됨.
    @PostConstruct
    public void makeCoffee(){
        for (CoffeeMachine coffeeMachine : coffeeMachines)
        System.out.println(coffeeMachine.brew());
    }
}
