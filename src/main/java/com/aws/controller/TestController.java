package com.aws.controller;

import com.aws.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final TestService testService;
    public TestController(TestService testService) {
        this.testService = testService;
    }
    @GetMapping("api/read-secret")
    ResponseEntity getSecret(){
        return ResponseEntity.ok( testService.getSecret());
    }
}
