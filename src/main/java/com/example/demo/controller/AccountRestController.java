package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Login;
import com.example.demo.dto.ResponseLogin;
import com.example.demo.repository.EmployeeRepository;

@RestController
@RequestMapping("api")
public class AccountRestController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("account/authenticating")
    public boolean login(@RequestBody Login login){
        ResponseLogin responseLogin = employeeRepository.authenticate(login.getEmail());
        return employeeRepository.findEmail(responseLogin.getEmail()).equals(login.getEmail());
    }

    
}
