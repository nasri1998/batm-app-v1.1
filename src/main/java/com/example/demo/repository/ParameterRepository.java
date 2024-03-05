package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Parameter;

public interface ParameterRepository extends JpaRepository<Parameter, String>{
    
}
