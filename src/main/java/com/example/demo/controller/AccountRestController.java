package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.ChangePassword;
import com.example.demo.model.User;
import com.example.demo.repository.EmployeeRepository;

import com.example.demo.dto.Register;
import com.example.demo.model.Employee;
import com.example.demo.model.User;
import com.example.demo.model.Role;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("api")
public class AccountRestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
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

    @PostMapping("account/register")
    public String save(@RequestBody Register register){
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

}
