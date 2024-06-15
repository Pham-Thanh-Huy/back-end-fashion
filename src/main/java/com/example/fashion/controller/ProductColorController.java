package com.example.fashion.controller;


import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.ProductColorDTO;
import com.example.fashion.service.ProductColorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<ProductColorDTO>> getProductColorById(@PathVariable("id") Integer productColorId){
        BaseResponse<ProductColorDTO> baseResponse = productColorService.getProductColorById(productColorId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<ProductColorDTO>> addProductColor(@Valid @RequestBody ProductColorDTO productColorDTO){
        BaseResponse<ProductColorDTO> baseResponse = productColorService.addProductColor(productColorDTO);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

}
