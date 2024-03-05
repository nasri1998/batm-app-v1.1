package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Parameter;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, String>{
    
}