package com.example.backendfruitable.Repository;

import com.example.backendfruitable.entity.Authorize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizeRepository  extends JpaRepository<Authorize, Long> {

    @Query("SELECT a FROM Authorize a WHERE a.authorizeId = :id ")
    public Authorize getAuthorizeById(@Param("id") Long id);
}
