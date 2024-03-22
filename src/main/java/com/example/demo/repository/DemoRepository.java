package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Demo;

@Repository
public interface DemoRepository extends JpaRepository<Demo, Integer> {
    @Query(value = "SELECT image FROM tb_m_demo WHERE image = ?1", nativeQuery = true)
    public String findImage(String image);

    @Query(value = "SELECT * FROM tb_m_demo WHERE id= ?1", nativeQuery = true)
    public Demo findDemo(Integer id);
}
