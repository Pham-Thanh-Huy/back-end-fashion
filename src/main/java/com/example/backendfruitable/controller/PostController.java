package com.example.backendfruitable.controller;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.PostDTO;
import com.example.backendfruitable.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/get/all")
    public ResponseEntity<BaseResponse<List<PostDTO>>> getListPost() {
        BaseResponse<List<PostDTO>> baseResponse = postService.getListPost();
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<PostDTO>> getPostById(@PathVariable("id") Long postId) {
        BaseResponse<PostDTO> baseResponse = postService.getPostById(postId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<PostDTO>> addPost(@RequestParam Long categoryPostId,
                                                         @RequestParam Long userId, @Valid @RequestBody PostDTO postDTO) {
        BaseResponse<PostDTO> baseResponse = postService.addPost(categoryPostId, userId, postDTO);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<PostDTO>> updatePost(@PathVariable("id") Long postId,
                                                            @RequestParam Long categoryPostId, @RequestParam Long userId,
                                                            @Valid @RequestBody PostDTO postDTO){
        BaseResponse<PostDTO> baseResponse = postService.updatePost(postId ,categoryPostId, userId, postDTO);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<PostDTO>> deletePost(@PathVariable("id") Long postId){
        BaseResponse<PostDTO> baseResponse = postService.deletePost(postId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
}
