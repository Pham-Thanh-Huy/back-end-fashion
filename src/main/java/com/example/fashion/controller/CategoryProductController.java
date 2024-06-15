package com.example.fashion.controller;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.CategoryProductDTO;
import com.example.fashion.service.CategoryProductService;
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
@RequestMapping("/api/category-product")
public class CategoryProductController {
    @Autowired
    private CategoryProductService categoryProductService;

    @Operation(summary = "Lấy toàn bộ danh mục sản phẩm")
    @GetMapping("/get/all")
    public ResponseEntity<BaseResponse<List<CategoryProductDTO>>> getAllCategoryProduct() {
        BaseResponse<List<CategoryProductDTO>> baseResponse = categoryProductService.getAllCategoryProduct();
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }


    @Operation(summary = "Lấy toàn bộ danh mục sản phẩm có phân trang")
    @GetMapping("/get/all/pagination")
    public ResponseEntity<BaseResponse<Page<CategoryProductDTO>>> getListCategoryProductPagination(Pageable pageable){
        BaseResponse<Page<CategoryProductDTO>> baseResponse = categoryProductService.getListCategoryProductPagination(pageable);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Lấy danh mục sản phẩm theo ID")
    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<CategoryProductDTO>> getCategoryProductById(@PathVariable("id") Long id) {
        BaseResponse<CategoryProductDTO> baseResponse = categoryProductService.getCategoryProductById(id);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }


    @Operation(summary = "Lấy danh mục sản phẩm theo tên")
    @GetMapping("/get/by/category-name")
    public ResponseEntity<BaseResponse<List<CategoryProductDTO>>> getCategoryProductByCategoryName(@RequestParam String categoryName){
        BaseResponse<List<CategoryProductDTO>> baseResponse = categoryProductService.getCategoryProductByCategoryName(categoryName);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Thêm danh mục sản phẩm")
    @PostMapping("/add")
    public ResponseEntity<BaseResponse<CategoryProductDTO>> addCategoryProduct(@Valid @RequestBody CategoryProductDTO categoryProductDTO,
                                                                               @RequestParam Long userId) {
        BaseResponse<CategoryProductDTO> baseResponse = categoryProductService.addCategoryProduct(categoryProductDTO, userId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Sửa danh mục sản phẩm")
    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<CategoryProductDTO>> updateCategoryProduct(@PathVariable("id") Long id,
                                                                                  @Valid @RequestBody CategoryProductDTO categoryProductDTO,
                                                                                  @RequestParam Long userId) {
        BaseResponse<CategoryProductDTO> baseResponse = categoryProductService.updateCategoryProduct(id, categoryProductDTO, userId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Xoá danh mục sản phẩm")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<CategoryProductDTO>> deleteCategoryProduct(@PathVariable("id") Long id){
        BaseResponse<CategoryProductDTO> baseResponse = categoryProductService.deleteCategoryProductById(id);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
}
