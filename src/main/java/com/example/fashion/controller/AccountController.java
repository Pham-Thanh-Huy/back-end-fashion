package com.example.fashion.controller;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.JWTResponse;
import com.example.fashion.DTO.LoginRequest;
import com.example.fashion.DTO.UserDTO;
import com.example.fashion.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {


    @Autowired
    private UserService userService;

    @Operation(summary = "Đăng nhập")
    @PostMapping("/login")
    private ResponseEntity<BaseResponse<JWTResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest){
        BaseResponse<JWTResponse> baseResponse = userService.loginUser(loginRequest);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
    @Operation(summary = "Đăng ký")
    @PostMapping("/register")
    private ResponseEntity<BaseResponse<UserDTO>> loginUser(@Valid @RequestBody UserDTO userDTO){
        BaseResponse<UserDTO> baseResponse = userService.registerUser(userDTO);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Xác thực người dùng")
    @GetMapping("/active")
    private ResponseEntity<BaseResponse<UserDTO>> loginUser(@RequestParam("userId") Long userId,
                                                                @RequestParam("codeActive") String codeActive){
        BaseResponse<UserDTO> baseResponse = userService.activeUser(userId, codeActive);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

}
