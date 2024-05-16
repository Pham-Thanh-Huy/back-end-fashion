package com.example.backendfruitable.controller;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.OrderDTO;
import com.example.backendfruitable.service.OrderService;
import com.fasterxml.jackson.databind.JsonNode;
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
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/get/all/pagination")
    public ResponseEntity<BaseResponse<Page<OrderDTO>>> getAllOrders(Pageable pageable) {
        BaseResponse<Page<OrderDTO>> baseResponse = orderService.getAllOrders(pageable);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @GetMapping("/get/{orderId}")
    public ResponseEntity<BaseResponse<OrderDTO>> getOrderById(@PathVariable("orderId") Long orderId){
        BaseResponse<OrderDTO> baseResponse = orderService.getOrderById(orderId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<OrderDTO>> addOrder(@RequestParam Long userId,
                                                 @Valid @RequestBody OrderDTO orderDTO){
            BaseResponse<OrderDTO> baseResponse = orderService.createOrder(userId, orderDTO);
            return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PutMapping("/update/{orderId}")
    public  ResponseEntity<BaseResponse<OrderDTO>> updateOrder(@PathVariable("orderId") Long orderId,
                                                               @RequestBody JsonNode jsonNode){
        BaseResponse<OrderDTO> baseResponse = orderService.updateStatus(orderId, jsonNode);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
}
