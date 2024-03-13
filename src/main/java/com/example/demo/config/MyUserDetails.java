package com.example.demo.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

@Service
public class MyUserDetails implements UserDetails, UserDetailsService {
    private String username;
    private String password;
    private GrantedAuthority authority;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    public MyUserDetails() {
    }

    public MyUserDetails(String username, String password, String authority) {
        this.username = username;
        this.password = password;
        this.authority = new SimpleGrantedAuthority(authority);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthority = new HashSet<>();
        grantedAuthority.add(authority);
        return grantedAuthority;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee responseLogin = new Employee();
        try { 
            responseLogin = employeeRepository.authenticate(username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            responseLogin = employeeRepository.authenticate(username);
        }

        // System.out.println("6"+ responseLogin.getEmail());
        // System.out.println("7"+ responseLogin.getUser().getPassword());
        return new MyUserDetails(responseLogin.getEmail(),
        responseLogin.getUser().getPassword(),
        responseLogin.getUser().getRole().getName());
        // return new MyUserDetails("frizky861@gmail.com",
        //         "$2a$10$6k.Jfgv6ibW4lsgccJLtKeCQ4uPM80o.iXdCFtIJeEbNUu7gKeOSK",
        //         "manager");
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public EmployeeRepository getEmployeeRepository() {
        return employeeRepository;
    }

    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
}
