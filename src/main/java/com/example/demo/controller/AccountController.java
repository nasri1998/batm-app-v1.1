package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.Register;
import com.example.demo.model.Employee;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Controller
//http://localhost:8080/account
@RequestMapping("account")
public class AccountController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    //http://localhost:8080/account/register
    @GetMapping("register")
    public String form(Model model){
        model.addAttribute("register", new Register());
        return "account/register";
    }

    //http://localhost:8080/account/register/save
    @PostMapping("register/save")
    public String save(Register register){
        String emailExist = employeeRepository.findEmail(register.getEmail());
        if (!emailExist.equals(register.getEmail())) {
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
                return "redirect:/account/login";
            }
        }
        return "redirect:/account/register";
    }


    @GetMapping("formchangepassword")
    public String formchange( Model model) {
        
    
            model.addAttribute("employee", employeeRepository.findById(2).orElse(null));
            model.addAttribute("user", userRepository.findById(2).orElse(null));
        
        return "account/formchangepassword";
    }
    
    @PostMapping("check")
    public String check(@RequestParam(name = "newpassword")String newpassword,Employee employee, User user) {

    


        Boolean resultemployee = employeeRepository.findById(2).isPresent();
       
        if (!resultemployee) {
            return "redirect:/account";
        }

        Boolean  resultuser = userRepository.findById(2).isPresent();

        user.setPassword(newpassword);
        user.getPassword();
        userRepository.save(user);
        if (!resultuser) {
            return "redirect:/account";
        }
        return "redirect:/account/formchangepassword";

    }
}
