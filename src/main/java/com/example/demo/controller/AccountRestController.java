package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.ChangePassword;
import com.example.demo.dto.ForgotPassword;
import com.example.demo.dto.Login;
import com.example.demo.model.User;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.dto.Register;
import com.example.demo.dto.ResponseLogin;
import com.example.demo.model.Employee;
import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.ParameterRepository;

@RestController
@RequestMapping("api")
public class AccountRestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ParameterRepository parameterRepository;

    @PostMapping("account/form-change-password")
    public String checkPassword(@RequestBody ChangePassword changePassword) {
        String email = employeeRepository.findEmail(changePassword.getEmail());
        String password = userRepository.findPassword(changePassword.getOldPassword());
        User user = userRepository.findByEmail(changePassword.getEmail());

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

    @PostMapping("account/register")
    public String save(@RequestBody Register register) {
        String emailExist = employeeRepository.findEmail(register.getEmail());
        if (emailExist == null) {
            Employee employee = new Employee();
            employee.setName(register.getName());
            employee.setEmail(register.getEmail());
            employeeRepository.save(employee);
            Boolean result = employeeRepository.findById(employee.getId()).isPresent();
            if (result) {
                User user = new User();
                user.setId(employee.getId());
                user.setPassword(register.getPassword());
                Role role = roleRepository.findById(5).orElse(null);
                user.setRole(role);
                userRepository.save(user);
                return "Register Successfully";
            }
        }
        return "Register Error";
    }

    @PostMapping("account/authenticating")
    public boolean login(@RequestBody Login login) {
        ResponseLogin responseLogin = employeeRepository.authenticate(login.getEmail());
        return employeeRepository.findEmail(responseLogin.getEmail()).equals(login.getEmail());
    }

    @PostMapping("account/forgot-password")
    public String checkEmail(@RequestBody ForgotPassword forgotPassword, @RequestHeader(name = "fp-nsr") String token) {
        if (token.equals(parameterRepository.findById("fp-nsr").get().getValue())) {
            String emailExist = employeeRepository.findEmail(forgotPassword.getEmail());
            if (emailExist.equals(forgotPassword.getEmail())) {
                User user = userRepository.findUserByEmail(emailExist);
                user.setPassword(forgotPassword.getPassword());

                userRepository.save(user);
                return "Password telah diupdate";
            }
        }
        return "Error";
    }
}
