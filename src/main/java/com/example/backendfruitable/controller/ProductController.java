package com.example.backendfruitable.controller;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.ProductDTO;
import com.example.backendfruitable.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/get/all")
    public ResponseEntity<BaseResponse<List<ProductDTO>>> getAllProduct() {
        BaseResponse<List<ProductDTO>> baseResponse = productService.getAllProduct();
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        BaseResponse<ProductDTO> baseResponse = productService.getProductById(id);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<ProductDTO>> addProduct(@Valid @RequestBody ProductDTO productDTO,
                                                               @RequestParam Long categoryProductId,
                                                               @RequestParam Long userId) {
        BaseResponse<ProductDTO> baseResponse = productService.addProduct(productDTO, categoryProductId, userId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<ProductDTO>> updateProduct(@Valid @RequestBody ProductDTO productDTO,
                                                                  @PathVariable("id") Long productId,
                                                                  @RequestParam Long categoryProductId,
                                                                  @RequestParam Long userId) {
        BaseResponse<ProductDTO> baseResponse = productService.updateProduct(productId, productDTO, categoryProductId, userId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<ProductDTO>> deleteProduct(@PathVariable("id") Long productId) {
        BaseResponse<ProductDTO> baseResponse = productService.deleteById(productId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
}
