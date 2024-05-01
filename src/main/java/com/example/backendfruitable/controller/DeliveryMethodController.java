package com.example.backendfruitable.controller;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.DeliveryMethodDTO;
import com.example.backendfruitable.service.DeliveryMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/delivery-method")
public class DeliveryMethodController {
    @Autowired
    private DeliveryMethodService deliveryMethodService;

    @GetMapping("/get/all")
    public ResponseEntity<BaseResponse<List<DeliveryMethodDTO>>> getAllDeliveryMethod(){
        BaseResponse<List<DeliveryMethodDTO>> baseResponse = deliveryMethodService.getAllDeliveryMethod();
        return  new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<DeliveryMethodDTO>> getDeliveryMethodById(@PathVariable("id") Long id){
        BaseResponse<DeliveryMethodDTO> baseResponse = deliveryMethodService.getDeliveryMethodById(id);
        return  new ResponseEntity<>(baseResponse,  HttpStatus.valueOf(baseResponse.getCode()));
    }
}
