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

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public String index(Model model){
        
        return "account/index";
    }

    //localhost://account/register
    @GetMapping("register")
    public String form(Model model){
        model.addAttribute("employee", new Employee());
        model.addAttribute("user", new User());
        return "account/register";
    }

    @PostMapping("save")
    public String save(Employee employee, User user){
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
    

    //Method Forgot Password
    @GetMapping("forgot-password")
    public String forgot(Model model){
        model.addAttribute("forgotPassword", new ForgotPassword());
        return "account/forgot-password";
    }

    @PostMapping("forgot-password/check")
    public String CheckEmail(ForgotPassword forgotPassword) {
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
