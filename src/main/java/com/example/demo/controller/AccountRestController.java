package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.ChangePassword;
import com.example.demo.dto.ForgotPassword;
import com.example.demo.dto.Login;
import com.example.demo.model.User;
import com.example.demo.repository.ParameterRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.dto.Register;
import com.example.demo.dto.ResponseChangePassword;
import com.example.demo.dto.ResponseLogin;
import com.example.demo.handler.CustomResponse;
import com.example.demo.model.Employee;
import com.example.demo.model.Role;
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
    @Autowired
    private ParameterRepository parameterRepository;

    @PostMapping("account/form-change-password")
    public String checkPassword(@RequestBody ChangePassword changePassword) {
        ResponseChangePassword responChangePassword = userRepository.findUser(changePassword.getEmail());
        // harus menggunakan 1x request saja untuk mendapatkan email dan password

        if (responChangePassword.getEmail().equals(null )&& responChangePassword.getPassword().equals(null)) {
            return "password atau email tidak ada";
        }else if (changePassword.getNewPassword()== responChangePassword.getPassword()) {
            return "password baru anda tidak boleh sama dengan password lama anda";
        }else if (!changePassword.getOldPassword().equals(responChangePassword.getPassword())) {
            return "password lama anda tidak sesuai";
        }else if (changePassword.getNewPassword().isEmpty() || changePassword.getNewPassword().equals(null)) {
            
        }else{
            User user = userRepository.findByEmail(changePassword.getEmail());
            user.setPassword(changePassword.getNewPassword());
            userRepository.save(user);
            return "berhasil menyimpan password baru";
        }
        return "nice";
    }

    @PostMapping("account/register")
    public ResponseEntity<Object> save(@RequestBody Register register) {
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
                return CustomResponse.generate(HttpStatus.OK, "Register Successfully");
            }
        }
        return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Register Failed");
    }

    @PostMapping("account/authenticating")
    public String login(@RequestBody Login login) {
        ResponseLogin responseLogin = employeeRepository.authenticate(login.getEmail());

        if (responseLogin.getEmail().equals(login.getEmail())) {
            return "Login Successfully";
        } else {
            return "Login Failed";
        }
    }

    @PostMapping("account/forgot-password")
    public String checkEmail(@RequestBody ForgotPassword forgotPassword, @RequestHeader(name = "fp-nsr") String token) {
        if (token.equals(parameterRepository.findById("fp-nsr").get().getValue())) {
            // menggunakan line dibawah ini untuk get data sekaligus dengan cek email
            User user = userRepository.findUserByEmail(forgotPassword.getEmail());
            if (user.getEmployee().getEmail().equals(forgotPassword.getEmail())) {
                user.setPassword(forgotPassword.getPassword());
                userRepository.save(user);
                return "Password succesfully update";
            }
        }
        return "Error";
    }
}
