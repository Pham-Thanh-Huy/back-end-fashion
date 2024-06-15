package com.example.fashion.controller;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.ProductSizeDTO;
import com.example.fashion.service.ProductSizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/product-size")
@RequiredArgsConstructor
public class ProductSizeController {
    private final ProductSizeService productSizeService;

    @GetMapping("/get/all")
    public ResponseEntity<BaseResponse<Page<ProductSizeDTO>>> getAllProductSize(Pageable pageable){
        BaseResponse<Page<ProductSizeDTO>> baseResponse = productSizeService.getAllProductSizes(pageable);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<ProductSizeDTO>> getProductSizeById(@PathVariable("id") Integer productSizeId){
        BaseResponse<ProductSizeDTO> baseResponse = productSizeService.getProductSizeById(productSizeId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<ProductSizeDTO>> addProduct(@Valid @RequestBody ProductSizeDTO productSizeDTO){
        BaseResponse<ProductSizeDTO> baseResponse = productSizeService.createProductSize(productSizeDTO);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
}
