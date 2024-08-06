package com.example.exampleproject.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${application.hello}")
    private String helloName;

    @GetMapping("/hello")
    public String helloWorld() {
        return helloName;
    }

}
