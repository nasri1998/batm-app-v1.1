package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.handler.CustomResponse;
import com.example.demo.repository.DemoRepository;

@RestController
@RequestMapping("api")
public class DemoRestController {
    
    @Autowired
    private DemoRepository demoRepository;

    @GetMapping("demos")
    public ResponseEntity<Object> get() {
        return CustomResponse.generate(HttpStatus.OK, "Data Successfully Fetched", demoRepository.findAll());
    }
}
