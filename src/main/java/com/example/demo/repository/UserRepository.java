package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    @Query(value = "SELECT password FROM tb_m_user WHERE  password= ?1",nativeQuery = true)
    public String FindPassword(String password);

    @Query(value = "SELECT * FROM tb_m_employee e, tb_m_user u WHERE u.id=e.id and e.email = ?1 ",nativeQuery = true)
    public User FindByEmail(String Email);
    
    @Query(value = "SELECT * FROM tb_m_employee e JOIN tb_m_user u ON e.id = u.id Where e.email = ?1", nativeQuery = true)
    public User findUserByEmail(String email);
}
