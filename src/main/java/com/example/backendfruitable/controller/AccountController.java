package com.example.backendfruitable.controller;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.JWTResponse;
import com.example.backendfruitable.DTO.LoginRequest;
import com.example.backendfruitable.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;


    @GetMapping("/login")
    private ResponseEntity<BaseResponse<JWTResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest){
        BaseResponse<JWTResponse> baseResponse = userService.loginUser(loginRequest);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

}
