package com.example.fashion.controller;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.PostDTO;
import com.example.fashion.service.PostService;
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
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;

    @Operation(summary = "Lấy danh sách bài viết")
    @GetMapping("/get/all")
    public ResponseEntity<BaseResponse<Page<PostDTO>>> getListPost(Pageable pageable) {
        BaseResponse<Page<PostDTO>> baseResponse = postService.getListPost(pageable);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }


    @Operation(summary = "Lấy bài viết theo ID")
    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<PostDTO>> getPostById(@PathVariable("id") Long postId) {
        BaseResponse<PostDTO> baseResponse = postService.getPostById(postId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Lấy bài viết theo Id Danh mục bài viết")
    @GetMapping("/get/by/category-id/{categoryId}")
    public ResponseEntity<BaseResponse<List<PostDTO>>> getPostByCategoryPostId(@PathVariable("categoryId") Long categoryId) {
        BaseResponse<List<PostDTO>> baseResponse = postService.getListPostByCategoryPostId(categoryId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Thêm bài viết")
    @PostMapping("/add")
    public ResponseEntity<BaseResponse<PostDTO>> addPost(@RequestParam Long categoryPostId,
                                                         @RequestParam Long userId, @Valid @RequestBody PostDTO postDTO) {
        BaseResponse<PostDTO> baseResponse = postService.addPost(categoryPostId, userId, postDTO);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Sửa bài viết theo ID")
    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<PostDTO>> updatePost(@PathVariable("id") Long postId,
                                                            @RequestParam Long categoryPostId, @RequestParam Long userId,
                                                            @Valid @RequestBody PostDTO postDTO){
        BaseResponse<PostDTO> baseResponse = postService.updatePost(postId ,categoryPostId, userId, postDTO);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Xoá bài viết")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<PostDTO>> deletePost(@PathVariable("id") Long postId){
        BaseResponse<PostDTO> baseResponse = postService.deletePost(postId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
}
