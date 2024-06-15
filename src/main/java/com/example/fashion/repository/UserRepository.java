package com.example.fashion.repository;

import com.example.fashion.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT  u FROM User u WHERE u.userId = :id")
    public User getUserById(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User getUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User getUserByUsername(@Param("username") String username);

    @Query("SELECT DISTINCT  u FROM User u JOIN u.authorizeList a WHERE a.authorizeName = :authorizeName")
    Page<User> getUserByAuthorizeName(@Param("authorizeName") String authorizeName, Pageable pageable);

}
