package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.ResponseChangePassword;
import com.example.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    @Query(value = "SELECT password FROM tb_m_user WHERE  password= ?1",nativeQuery = true)
    public String findPassword(String password);

    @Query(value = "SELECT * FROM tb_m_employee e, tb_m_user u WHERE u.id=e.id and e.email = ?1 ",nativeQuery = true)
    public User findByEmail(String Email);
    
    @Query(value = "SELECT * FROM tb_m_employee e JOIN tb_m_user u ON e.id = u.id Where e.email = ?1", nativeQuery = true)
    public User findUserByEmail(String email);

     @Query(
        """
            SELECT new com.example.demo.dto.ResponseChangePassword(e.email, u.password) FROM User u join u.employee e where e.email = ?1
    """)
    public ResponseChangePassword findUser(String email);
}
