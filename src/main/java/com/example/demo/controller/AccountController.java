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
        
        ChangePassword changePassword = new ChangePassword();
        String email = employeeRepository.findByIdEmail(1);

    
        String password = userRepository.findbyidPassword(1);
        changePassword.setEmail(email);
        changePassword.setPassword(password);

    
        model.addAttribute("changePassword", changePassword);
    

        
        return "account/formchangepassword";
    }
    
    @PostMapping("check")
    public String check(@RequestParam(name = "newpassword")String newpassword,ChangePassword changepasword) {

        User user = userRepository.FindbyEmail(changepasword.getEmail());

        if (user == null) {
            return "account/index";
        }
       changepasword.setPassword(newpassword);

       user.setPassword(changepasword.getPassword());

        userRepository.save(user);

    
        return "redirect:/account/formchangepassword";

    }

   
}


// 


// return "account/formchangepassword";

// String Cekemail =employeeRepository.findbyidemail(1);
// if (Cekemail == "" || Cekemail == null) {
//     return "account/index";            
// }

// String cekpassword = userRepository.findbyidPassword(1);

// if (cekpassword == "" || cekpassword == null) {
// return "account/index";  
// }

// changepassword.setEmail(Cekemail);
// changepassword.setPassword(cekpassword);

// System.out.println(changepassword.getEmail()); 

// return "account/formchangepassword";