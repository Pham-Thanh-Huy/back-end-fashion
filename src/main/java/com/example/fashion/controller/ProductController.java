package com.example.fashion.controller;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.ProductDTO;
import com.example.fashion.service.ProductService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Operation(summary = "Lấy danh sách sản phẩm")
    @GetMapping("/get/all")
    public ResponseEntity<BaseResponse<Page<ProductDTO>>> getAllProduct(Pageable pageable) {
        BaseResponse<Page<ProductDTO>> baseResponse = productService.getAllProduct(pageable);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }


    @Operation(summary = "Lấy sản phẩm theo ID")
    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        BaseResponse<ProductDTO> baseResponse = productService.getProductById(id);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary =  "Lấy sản phẩm nổi bật")
    @GetMapping("/get/outstanding")
    public ResponseEntity<BaseResponse<Page<ProductDTO>>> getOutstandingProduct(@RequestParam Boolean outstanding, Pageable pageable) {
        BaseResponse<Page<ProductDTO>> baseResponse = productService.getProductOutstanding(outstanding, pageable);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Lấy sản phẩm theo danh mục sản phẩm")
    @GetMapping("/get/by/category-product-id/{categoryId}")
    public ResponseEntity<BaseResponse<List<ProductDTO>>>getProductByCategoryProductId(@PathVariable("categoryId") Long categoryId){
            BaseResponse<List<ProductDTO>> baseResponse = productService.getProductByCategoryProductId(categoryId);
            return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Thêm sản phẩm")
    @PostMapping("/add")
    public ResponseEntity<BaseResponse<ProductDTO>> addProduct(@Valid @RequestBody ProductDTO productDTO,
                                                               @RequestParam Long categoryProductId,
                                                               @RequestParam Long userId) {
        BaseResponse<ProductDTO> baseResponse = productService.addProduct(productDTO, categoryProductId, userId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PostMapping("/add/inventory")
    public ResponseEntity<BaseResponse<ObjectNode>> addInventoryProduct(@RequestBody JsonNode jsonNode,
                                                                        @RequestParam("productId") Long productId){
        BaseResponse<ObjectNode> baseResponse = productService.addInventoryProduct(productId, jsonNode);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Sửa sản phẩm")
    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<ProductDTO>> updateProduct(@Valid @RequestBody ProductDTO productDTO,
                                                                  @PathVariable("id") Long productId,
                                                                  @RequestParam Long categoryProductId,
                                                                  @RequestParam Long userId) {
        BaseResponse<ProductDTO> baseResponse = productService.updateProduct(productId, productDTO, categoryProductId, userId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PutMapping("/update/inventory/{id}")
    public ResponseEntity<BaseResponse<ObjectNode>> updateQuantityInventoryProduct(@PathVariable("id") Long productId,
                                                                                    @RequestBody JsonNode jsonNode){
        BaseResponse<ObjectNode> baseResponse = productService.updateQuantityInventoryProduct(productId, jsonNode);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Xoá sản phẩm")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<ProductDTO>> deleteProduct(@PathVariable("id") Long productId) {
        BaseResponse<ProductDTO> baseResponse = productService.deleteById(productId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
}
