package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.ChangePassword;
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

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public String index(Model model) {

        return "account/index";
    }

    // localhost://account/register
    @GetMapping("register")
    public String form(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("user", new User());
        return "account/register";
    }

    @PostMapping("save")
    public String save(Employee employee, User user) {
        employeeRepository.save(employee);
        Boolean result = employeeRepository.findById(employee.getId()).isPresent();
        if (result) {
            user.setId(employee.getId());
            Role role = roleRepository.findById(5).orElse(null);
            user.setRole(role);
            userRepository.save(user);
            return "redirect:/account";
        }
        return "redirect:/account";
    }

    @GetMapping("form-change-password")
    public String formChange(Model model) {
        model.addAttribute("changePassword", new ChangePassword());

        return "account/form-change-password";
    }

    @PostMapping("check")
    public String check(ChangePassword changePassword, Model model) {
       
        User user = userRepository.FindbyEmail(changePassword.getEmail());

        if (user == null) {
            return "account/index";
        } else if (changePassword.getNewPassword() == changePassword.getOldPassword()) {
            model.addAttribute("error", "Password Baru Anda tidak boleh sama");
            return "account/form-change-password";
        } else if (changePassword.getNewPassword().isEmpty() || changePassword.getNewPassword().equals(null)) {
            return "redirect:/account/form-change-password";
        } else if (changePassword.getOldPassword() != user.getPassword()) {
            return "redirect:/account/form-change-password";
        } else {
            user.setPassword(changePassword.getNewPassword());
            userRepository.save(user);
        }
        return "redirect:/account/form-change-password";

    }

}
