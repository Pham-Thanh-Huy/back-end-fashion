package com.example.fashion.controller;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.OrderDTO;
import com.example.fashion.service.OrderService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/get/totalprice/expected")
    public ResponseEntity<BaseResponse<ObjectNode>> getTotalPriceOrderExpected(){
        BaseResponse<ObjectNode> baseResponse = orderService.getTotalPriceOrderAndQuantityExpected();
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
    @GetMapping("/get/totalprice/by/status")
    public ResponseEntity<BaseResponse<ObjectNode>> getTotalPriceOrder(@RequestParam String status){
        BaseResponse<ObjectNode> baseResponse = orderService.getTotalPriceAndQuantityOrderByStatus(status);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
    @GetMapping("/get/by/status")
    public ResponseEntity<BaseResponse<Page<OrderDTO>>> getOrderByStatus(@RequestParam String status,
                                                                         Pageable pageable){
        BaseResponse<Page<OrderDTO>> baseResponse = orderService.getOrderByStatus(status, pageable);
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
