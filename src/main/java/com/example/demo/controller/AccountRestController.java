package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.ChangePassword;
import com.example.demo.model.User;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("api")
public class AccountRestController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("account/form-change-password")
    public String CheckPasswordUser(@RequestBody ChangePassword changePassword) {
        String email = employeeRepository.findEmail(changePassword.getEmail());
        String password = userRepository.FindPassword(changePassword.getOldPassword());
        User user = userRepository.FindByEmail(changePassword.getEmail());

        if (email.equals(null) && password.equals(null)) {
            return "password atau email tidak ada";
        } else if (changePassword.getNewPassword() == password) {
            return "password baru anda tidak boleh sama";
        } else if (!changePassword.getOldPassword().equals(password)) {
            return "password lama anda tidak sesuai";
        } else if (changePassword.getNewPassword().isEmpty() || changePassword.getNewPassword().equals(null)) {
            return "tidak boleh kosong";
        } else {
            user.setPassword(changePassword.getNewPassword());
            userRepository.save(user);
            return "password baru sudah di simpan";
        }

    }
}
