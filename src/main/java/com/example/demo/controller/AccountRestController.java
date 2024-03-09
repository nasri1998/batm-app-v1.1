package com.example.demo.controller;

import org.hibernate.dialect.MySQL55Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.JwtTokenUtil;
import com.example.demo.config.MyUserDetails;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MyUserDetails myUserDetails;

    @Autowired
	private JwtTokenUtil jwtTokenUtil;

    @PostMapping("account/form-change-password")
    public ResponseEntity<Object> checkPassword(@RequestBody ChangePassword changePassword) {
        ResponseChangePassword responChangePassword = userRepository.findUser(changePassword.getEmail());
        // harus menggunakan 1x request saja untuk mendapatkan email dan password
        if (responChangePassword.getEmail().equals(null) && responChangePassword.getPassword().equals(null)) {
            return CustomResponse.generate(HttpStatus.OK, "user not found");
        } else if (changePassword.getNewPassword().equals(responChangePassword.getPassword())) {
            return CustomResponse.generate(HttpStatus.BAD_REQUEST,
                    "The new password cannot be the same as the old password");
        } else if (!changePassword.getOldPassword().equals(responChangePassword.getPassword())) {
            return CustomResponse.generate(HttpStatus.BAD_REQUEST, "password does not match");
        } else if (changePassword.getNewPassword().isEmpty() || changePassword.getNewPassword().equals(null)) {
            return CustomResponse.generate(HttpStatus.BAD_REQUEST,
                    "the field cannot be empty, check your input");
        } else {
            User user = userRepository.findByEmail(changePassword.getEmail());
            user.setPassword(changePassword.getNewPassword());
            userRepository.save(user);
            return CustomResponse.generate(HttpStatus.CREATED, "successfully changed your password");
        }
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
                user.setPassword(passwordEncoder.encode(register.getPassword()));
                Role role = roleRepository.findById(2).orElse(null);
                user.setRole(role);
                userRepository.save(user);
                return CustomResponse.generate(HttpStatus.OK, "Register Successfully");
            }
        }
        return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Register Failed");
    }

    @PostMapping("account/authenticating")
    public ResponseEntity<Object> login(@RequestBody Login login) {

        try {
            // authenticate(...): Ini adalah metode yang dipanggil pada objek authenticationManager untuk melakukan otentikasi.
            //  Metode ini menerima objek Authentication, yang mewakili kredensial pengguna yang ingin diautentikasi. Dalam contoh ini, 
            // UsernamePasswordAuthenticationToken digunakan, yang merupakan implementasi dari Authentication 
            // dan menyediakan kredensial berupa nama pengguna dan kata sandi.

            //intinya adalah pengecekan pengguna/user
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            myUserDetails = (MyUserDetails) myUserDetails.loadUserByUsername(login.getEmail());
            //  UserDetails userDetails = this.userDetails.loadUserByUsername(login.getEmail());
            System.out.println("3" +myUserDetails.getUsername());
            
            final String token = jwtTokenUtil.generateToken(myUserDetails);
            return CustomResponse.generate(HttpStatus.OK, "berhasil login",token);
        } catch (Exception e) {
            return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Cannot login", null);
        }
    }

    @PostMapping("account/forgot-password")
    public ResponseEntity<Object> checkEmail(@RequestBody ForgotPassword forgotPassword,
            @RequestHeader(name = "fp-nsr") String token) {
        if (token.equals(parameterRepository.findById("fp-nsr").get().getValue())) {
            // menggunakan line dibawah ini untuk get data sekaligus dengan cek email
            User user = userRepository.findUserByEmail(forgotPassword.getEmail());
            if (user.getEmployee().getEmail().equals(forgotPassword.getEmail())) {
                user.setPassword(forgotPassword.getPassword());
                userRepository.save(user);
                // Method dalam Class CustomResponse dibuat static sehingga hanya perlu
                // memanggil classnya saja
                return CustomResponse.generate(HttpStatus.OK, "Your Password has been Reset");
            }
        }
        return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Wrong Token");
    }
}
