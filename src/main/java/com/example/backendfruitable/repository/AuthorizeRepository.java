package com.example.backendfruitable.repository;

import com.example.backendfruitable.entity.Authorize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizeRepository  extends JpaRepository<Authorize, Long> {

    @Query("SELECT a FROM Authorize a WHERE a.authorizeId = :id ")
     Authorize getAuthorizeById(@Param("id") Long id);

    @Query("SELECT a FROM Authorize a WHERE a.authorizeName = :authorizeName")
    Authorize getAuthorizeByName(@Param("authorizeName") String authorizeName);
}
