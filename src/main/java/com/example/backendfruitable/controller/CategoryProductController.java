package com.example.backendfruitable.controller;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.CategoryProductDTO;
import com.example.backendfruitable.service.CategoryProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/category-product")
public class CategoryProductController {
    @Autowired
    private CategoryProductService categoryProductService;

    @GetMapping("/get/all")
    public ResponseEntity<BaseResponse<List<CategoryProductDTO>>> getAllCategoryProduct() {
        BaseResponse<List<CategoryProductDTO>> baseResponse = categoryProductService.getAllCategoryProduct();
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<CategoryProductDTO>> getCategoryProductById(@PathVariable("id") Long id) {
        BaseResponse<CategoryProductDTO> baseResponse = categoryProductService.getCategoryProductById(id);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<CategoryProductDTO>> addCategoryProduct(@Valid @RequestBody CategoryProductDTO categoryProductDTO) {
        BaseResponse<CategoryProductDTO> baseResponse = categoryProductService.addCategoryProduct(categoryProductDTO);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<CategoryProductDTO>> updateCategoryProduct(@PathVariable("id") Long id,
                                                                                  @Valid @RequestBody CategoryProductDTO categoryProductDTO) {
        BaseResponse<CategoryProductDTO> baseResponse = categoryProductService.updateCategoryProduct(id, categoryProductDTO);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<CategoryProductDTO>> deleteCategoryProduct(@PathVariable("id") Long id){
        BaseResponse<CategoryProductDTO> baseResponse = categoryProductService.deleteCategoryProductById(id);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
}
