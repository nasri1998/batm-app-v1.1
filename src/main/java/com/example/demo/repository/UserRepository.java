package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    @Query(value = "SELECT password FROM tb_m_user WHERE  id= ?1",nativeQuery = true)
    public String findbyidPassword(Integer id);

    @Query(value = "SELECT * FROM tb_m_employee e, tb_m_user u WHERE u.id=e.id and e.email = ?1 ",nativeQuery = true)
    public User FindbyEmail(String Email);
}
