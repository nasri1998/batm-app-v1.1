package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    @Query(value = "select password from tb_m_user  where  id=?0",nativeQuery = true)
    public String findbyidPassword(Integer id);
}
