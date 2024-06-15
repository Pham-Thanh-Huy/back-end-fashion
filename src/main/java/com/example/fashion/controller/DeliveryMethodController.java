package com.example.fashion.controller;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.DeliveryMethodDTO;
import com.example.fashion.service.DeliveryMethodService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Thêm phương thức vận chuyển")
    @PostMapping("/add")
    public ResponseEntity<BaseResponse<DeliveryMethodDTO>> addDeliveryMethod(@RequestParam long userId,
                                                                            @Valid @RequestBody DeliveryMethodDTO deliveryMethodDTO){
            BaseResponse<DeliveryMethodDTO> baseResponse = deliveryMethodService.addDeliveryMethod(userId, deliveryMethodDTO);
            return new ResponseEntity<>(baseResponse,  HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Cập nhật phương thức vận chuyển")
    @PutMapping("/update/{deliveryMethodId}")
    public ResponseEntity<BaseResponse<DeliveryMethodDTO>> updateDeliveryMethod(@PathVariable("deliveryMethodId") Long deliveryMethodId,
                                                                                @RequestParam long userId,
                                                                                @Valid @RequestBody DeliveryMethodDTO deliveryMethodDTO){
        BaseResponse<DeliveryMethodDTO> baseResponse = deliveryMethodService.updateDeliveryMethod(deliveryMethodId, userId, deliveryMethodDTO);
        return new ResponseEntity<>(baseResponse,  HttpStatus.valueOf(baseResponse.getCode()));
    }

    @Operation(summary = "Xóa phương thức vận chuyển")
    @DeleteMapping("/delete/{deliveryMethodId}")
    public ResponseEntity<BaseResponse<DeliveryMethodDTO>> deleteDeliveryMethod(@PathVariable("deliveryMethodId") Long deliveryMethodId){
        BaseResponse<DeliveryMethodDTO> baseResponse = deliveryMethodService.deleteDeliveryMethod(deliveryMethodId);
        return new ResponseEntity<>(baseResponse,  HttpStatus.valueOf(baseResponse.getCode()));
    }
}
