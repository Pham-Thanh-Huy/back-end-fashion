package com.example.backendfruitable.controller;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.OrderDTO;
import com.example.backendfruitable.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/get/all")
    public ResponseEntity<BaseResponse<List<OrderDTO>>> getAllOrders() {
        BaseResponse<List<OrderDTO>> baseResponse = orderService.getAllOrders();
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<OrderDTO>> addOrder(@RequestParam Long userId,
                                                 @Valid @RequestBody OrderDTO orderDTO){
            BaseResponse<OrderDTO> baseResponse = orderService.createOrder(userId, orderDTO);
            return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
}
