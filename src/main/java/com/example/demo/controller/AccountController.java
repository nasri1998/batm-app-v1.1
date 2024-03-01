package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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


    @GetMapping("formchangepassword")
    public String formchange( Model model) {
        
    
            model.addAttribute("employee", employeeRepository.findById(1).orElse(null));
            model.addAttribute("user", userRepository.findById(1).orElse(null));
        
        return "account/formchangepassword";
    }
    
    @PostMapping("check")
    public String check(@RequestParam(name = "newpassword")String newpassword,Employee employee, User user) {

    


        Boolean resultemployee = employeeRepository.findById(1).isPresent();
       
        if (!resultemployee) {
            return "redirect:/account";
        }

        Boolean  resultuser = userRepository.findById(1).isPresent();

        user.setPassword(newpassword);
        user.getPassword();
        userRepository.save(user);
        if (!resultuser) {
            return "redirect:/account";
        }
        return "redirect:/account/formchangepassword";

    }
}
