package com.example.backendfruitable.Repository;

import com.example.backendfruitable.entity.Authorize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizeRepository  extends JpaRepository<Authorize, Long> {
}
