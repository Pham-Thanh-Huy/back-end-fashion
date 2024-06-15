package com.example.fashion.repository;

import com.example.fashion.entity.Authorize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizeRepository  extends JpaRepository<Authorize, Long> {

    @Query("SELECT a FROM Authorize a WHERE a.authorizeId = :id ")
     Authorize getAuthorizeById(@Param("id") Integer id);

    @Query("SELECT a FROM Authorize a WHERE a.authorizeName = :authorizeName")
    Authorize getAuthorizeByName(@Param("authorizeName") String authorizeName);
}
