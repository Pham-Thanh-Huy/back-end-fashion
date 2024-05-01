package com.example.backendfruitable.controller;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.UserDTO;
import com.example.backendfruitable.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Lấy danh sách User")
    @GetMapping("/get/all")
    public ResponseEntity<BaseResponse<List<UserDTO>>> getAllUser(){
        BaseResponse<List<UserDTO>> baseResponse = userService.getAllUser();
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Lấy user theo ID")
    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<UserDTO>> getUserById(@PathVariable("id") Long id){
        BaseResponse<UserDTO> baseResponse = userService.getUserById(id);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Thêm user")
    @PostMapping("/add")
    public ResponseEntity<BaseResponse<UserDTO>> addUser(@Valid @RequestBody UserDTO userDTO){
        BaseResponse<UserDTO> baseResponse = userService.addUser(userDTO);
        return  new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Update user")
    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<UserDTO>> updateUser(@PathVariable("id") Long userId, @Valid @RequestBody UserDTO userDTO){
        BaseResponse<UserDTO> baseResponse = userService.updateUser(userId,userDTO);
        return  new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<UserDTO>> deleteUserById(@PathVariable("id") Long id){
        BaseResponse<UserDTO> baseResponse = userService.deleteUserById(id);
        return  new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }


}
