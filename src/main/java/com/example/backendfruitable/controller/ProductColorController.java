package com.example.backendfruitable.controller;


import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.ProductColorDTO;
import com.example.backendfruitable.service.ProductColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/product-color")
public class ProductColorController {
    private final ProductColorService productColorService;

    @GetMapping("/get/all")
    public ResponseEntity<BaseResponse<Page<ProductColorDTO>>> getAllProductColor(Pageable pageable){
        BaseResponse<Page<ProductColorDTO>> baseResponse = productColorService.getAllProductColor(pageable);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

}
