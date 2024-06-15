    package com.example.fashion.controller;

    import com.example.fashion.DTO.BaseResponse;
    import com.example.fashion.DTO.UserDTO;
    import com.example.fashion.service.UserService;
    import io.swagger.v3.oas.annotations.Operation;
    import jakarta.validation.Valid;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @Controller
    @Slf4j
    @RequestMapping("/api/user")
    public class UserController {
        @Autowired
        private UserService userService;

        @Operation(summary = "Lấy danh sách User")
        @GetMapping("/get/all")
        public ResponseEntity<BaseResponse<List<UserDTO>>> getAllUser(){
            BaseResponse<List<UserDTO>> baseResponse = userService.getAllUser();
            return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
        }

        @Operation(summary = "Lấy danh sách User theo Authorize")
        @GetMapping("/get/by/authorize-name")
        public ResponseEntity<BaseResponse<Page<UserDTO>>> getListUserByAuthorizeName(Pageable pageable, @RequestParam String authorizeName){
            BaseResponse<Page<UserDTO>> baseResponse = userService.getListUserByAuthorizeName(pageable, authorizeName);
            return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
        }

        @Operation(summary = "Lấy user theo ID")
        @GetMapping("/get/{id}")
        public ResponseEntity<BaseResponse<UserDTO>> getUserById(@PathVariable("id") Long id){
            BaseResponse<UserDTO> baseResponse = userService.getUserById(id);
            return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
        }

        @Operation(summary = "Lấy user theo username")
        @GetMapping("/get/by/username")
        public ResponseEntity<BaseResponse<UserDTO>> getUserByUsername(@RequestParam String username){
            BaseResponse<UserDTO> baseResponse = userService.getByUsername(username);
            return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
        }

        @Operation(summary = "Thêm user")
        @PostMapping("/add")
        public ResponseEntity<BaseResponse<UserDTO>> addUser(@Valid @RequestBody UserDTO userDTO){
            log.info("Controller : Add User");
            BaseResponse<UserDTO> baseResponse = userService.addUser(userDTO);
            return  new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
        }

        @Operation(summary = "Update user")
        @PutMapping("/update/{id}")
        public ResponseEntity<BaseResponse<UserDTO>> updateUser(@PathVariable("id") Long userId, @Valid @RequestBody UserDTO userDTO){
            BaseResponse<UserDTO> baseResponse = userService.updateUser(userId,userDTO);
            return  new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
        }

        @Operation(summary = "Delete user")
        @DeleteMapping("/delete/{id}")
        public ResponseEntity<BaseResponse<UserDTO>> deleteUserById(@PathVariable("id") Long id){
            BaseResponse<UserDTO> baseResponse = userService.deleteUserById(id);
            return  new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
        }


    }
