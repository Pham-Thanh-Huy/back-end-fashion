package com.example.fashion.controller;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.CategoryPostDTO;
import com.example.fashion.service.CategoryPostService;
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
@RequestMapping("/api/category-post")
public class CategoryPostController {
    @Autowired
    private CategoryPostService categoryPostService;

    @Operation(summary = "Lấy toàn bộ danh mục bài viết")
    @GetMapping("/get/all")
    public ResponseEntity<BaseResponse<List<CategoryPostDTO>>> getAllCategoryPost() {
        BaseResponse<List<CategoryPostDTO>> baseResponse = categoryPostService.getAllCategoryPost();
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Lấy toàn bộ danh mục bài viết có phân trang")
    @GetMapping("/get/all/pagination")
    public ResponseEntity<BaseResponse<Page<CategoryPostDTO>>> getListCategoryPostPagination(Pageable pageable){
        BaseResponse<Page<CategoryPostDTO>> baseResponse = categoryPostService.getListCategoryPostPagination(pageable);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Lấy danh mục bài viết theo ID")
    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<CategoryPostDTO>> getCategoryPostById(@PathVariable("id") Long categoryId) {
        BaseResponse<CategoryPostDTO> baseResponse = categoryPostService.getCategoryPostById(categoryId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Thêm danh mục bài viết")
    @PostMapping("/add")
    public ResponseEntity<BaseResponse<CategoryPostDTO>> addCategoryPost(@Valid @RequestBody CategoryPostDTO categoryPostDTO,
                                                                         @RequestParam Long userId) {
        BaseResponse<CategoryPostDTO> baseResponse = categoryPostService.addCategoryPost(categoryPostDTO, userId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }


    @Operation(summary = "Sửa danh mục bài viết")
    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<CategoryPostDTO>> updateCategoryPost(@PathVariable("id") Long categoryId, @Valid @RequestBody CategoryPostDTO categoryPostDTO) {
        BaseResponse<CategoryPostDTO> baseResponse = categoryPostService.updateCategoryPost(categoryId, categoryPostDTO);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Xoá danh mục bài viết")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<CategoryPostDTO>> deleteCategoryPost(@PathVariable("id") Long categoryId) {
        BaseResponse<CategoryPostDTO> baseResponse = categoryPostService.deleteCategoryPostById(categoryId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
}
