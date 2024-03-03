package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Employee;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
    // @Query(value = "select e.email, u.password from tb_m_user u , tb_m_employee e where u.id=e.id and u.id=?0",nativeQuery = true)
    // public changepasword findbyiduser(Integer id);

    @Query(value = "SELECT email FROM tb_m_employee WHERE id= ?1",nativeQuery = true)
    public String findByIdEmail(Integer id);
}
