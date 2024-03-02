package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    @Query(value = "SELECT * FROM tb_m_employee e JOIN tb_m_user u ON e.id = u.id Where e.email = ?1", nativeQuery = true)
    public User findUserByEmail(String email);
}
