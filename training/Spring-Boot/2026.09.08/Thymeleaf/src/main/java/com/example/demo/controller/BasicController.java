package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BasicController {
    @GetMapping("/book")
    public String getBook(Model model){
        model.addAttribute("title","이것이 스프링부트이다.");
        model.addAttribute("description", "예제를 통해 공부하세요.");
        return "/basic/book";
    }
}
