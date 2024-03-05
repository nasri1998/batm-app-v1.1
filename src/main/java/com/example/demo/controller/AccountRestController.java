package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ForgotPassword;
import com.example.demo.model.User;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.ParameterRepository;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("api")
public class AccountRestController {
    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    //Method forgot password
    @PostMapping("account/forgot-password")
    public String checkEmail(@RequestBody ForgotPassword forgotPassword, @RequestHeader(name="fp-nsr") String token) {
        if (token.equals(parameterRepository.findById("fp-nsr").get().getValue())) {
            String emailExist = employeeRepository.findEmail(forgotPassword.getEmail());
            if (emailExist.equals(forgotPassword.getEmail())) {
                User user = userRepository.findUserByEmail(emailExist);
                user.setPassword(forgotPassword.getPassword());
    
                userRepository.save(user);
                return "Data telah ditambahkan";
            }
        }
        return "Error";
    }
}
