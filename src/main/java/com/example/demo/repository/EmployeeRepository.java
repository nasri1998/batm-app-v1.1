package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Employee;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
    @Query(value = "SELECT email FROM tb_m_employee Where email = ?1", nativeQuery = true)
    public String findEmail(String email);
}
