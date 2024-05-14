package com.example.backendfruitable.DTO;

import com.example.backendfruitable.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizeDTO {
    private int authorizeId;
    private String authorizeName;

    private List<UserDTO> userList;
}
