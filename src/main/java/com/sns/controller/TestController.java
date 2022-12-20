package com.sns.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
