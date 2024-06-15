package com.example.fashion.controller;

import com.example.fashion.DTO.AuthorizeDTO;
import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/authorize")
public class AuthorizeController {
    @Autowired
    private AuthorizeService authorizeService;

    @GetMapping("/get/all")
    public ResponseEntity<BaseResponse<List<AuthorizeDTO>>> getAllAuthorize(){
        BaseResponse<List<AuthorizeDTO>> baseResponse = authorizeService.getAllAuthorize();
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @GetMapping("/get/{authorizeId}")
    public ResponseEntity<BaseResponse<AuthorizeDTO>> getAuthorizeById(@PathVariable("authorizeId") Integer authorizeId){
        BaseResponse<AuthorizeDTO> baseResponse = authorizeService.getAuthorizeById(authorizeId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
}
