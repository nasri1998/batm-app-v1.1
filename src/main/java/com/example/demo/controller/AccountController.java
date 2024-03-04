package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.ForgotPassword;
import com.example.demo.model.Employee;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Controller
@RequestMapping("account")
public class AccountController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;


    //Method Forgot Password
    @GetMapping("forgot-password")
    public String forgot(Model model){
        model.addAttribute("forgotPassword", new ForgotPassword());
        return "account/forgot-password";
    }

    @PostMapping("forgot-password/check")
    public String checkEmail(ForgotPassword forgotPassword) {
        String emailExist = employeeRepository.findEmail(forgotPassword.getEmail());
        if (emailExist.equals(forgotPassword.getEmail())) {
            User user = userRepository.findUserByEmail(emailExist);
            user.setPassword(forgotPassword.getPassword());

            userRepository.save(user);
            return "account/forgot-password";
        }
        return "account/forgot-password";
    }

}
