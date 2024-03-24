package com.example.backendfruitable.controller;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.CategoryPostDTO;
import com.example.backendfruitable.service.CategoryPostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/category-post")
public class CategoryPostController {
    @Autowired
    private CategoryPostService categoryPostService;

    @GetMapping("/get/all")
    public ResponseEntity<BaseResponse<List<CategoryPostDTO>>> getAllCategoryPost() {
        BaseResponse<List<CategoryPostDTO>> baseResponse = categoryPostService.getAllCategoryPost();
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<CategoryPostDTO>> getCategoryPostById(@PathVariable("id") Long categoryId) {
        BaseResponse<CategoryPostDTO> baseResponse = categoryPostService.getCategoryPostById(categoryId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<CategoryPostDTO>> addCategoryPost(@Valid @RequestBody CategoryPostDTO categoryPostDTO) {
        BaseResponse<CategoryPostDTO> baseResponse = categoryPostService.addCategoryPost(categoryPostDTO);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<CategoryPostDTO>> updateCategoryPost(@PathVariable("id") Long categoryId, @Valid @RequestBody CategoryPostDTO categoryPostDTO) {
        BaseResponse<CategoryPostDTO> baseResponse = categoryPostService.updateCategoryPost(categoryId, categoryPostDTO);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<CategoryPostDTO>> deleteCategoryPost(@PathVariable("id") Long categoryId) {
        BaseResponse<CategoryPostDTO> baseResponse = categoryPostService.deleteCategoryPostById(categoryId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
}
