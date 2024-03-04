package com.example.backendfruitable.Repository;

import com.example.backendfruitable.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT  u FROM User u WHERE u.userId = :id")
    public User getUserById(@Param("id") Long id);
}
