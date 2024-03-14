package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("region")
public class RegionController {
    @GetMapping
    public String index() {
        return "region/index";
    }

}
