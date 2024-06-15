package com.example.fashion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Authorize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authorize_id")
    private int authorizeId;

    @Column(name = "authorize_name")
    private String authorizeName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "authorizeList")
    @JsonIgnore
    private List<User> userList;
}
