package com.example.fashion.controller;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.PaymentMethodDTO;
import com.example.fashion.service.PaymentMethodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/payment-method")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("/get/all")
    public ResponseEntity<BaseResponse<List<PaymentMethodDTO>>> getAllPaymentMethod() {
        BaseResponse<List<PaymentMethodDTO>> baseResponse = paymentMethodService.getAllPaymentMethod();
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @GetMapping("/get/{paymentId}")
    public ResponseEntity<BaseResponse<PaymentMethodDTO>> getPaymentMethodById(@PathVariable("paymentId") Long paymentId) {
        BaseResponse<PaymentMethodDTO> baseResponse = paymentMethodService.getPaymentMethodById(paymentId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<PaymentMethodDTO>> addPaymentMethod(@RequestBody @Valid PaymentMethodDTO paymentMethodDTO,
                                                                           @RequestParam Long userId) {
        BaseResponse<PaymentMethodDTO> baseResponse = paymentMethodService.addPaymentMethod(paymentMethodDTO, userId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @PutMapping("/update/{paymentId}")
    public ResponseEntity<BaseResponse<PaymentMethodDTO>> updatePaymentMethod(@RequestBody @Valid PaymentMethodDTO paymentMethodDTO,
                                                                              @PathVariable("paymentId") Long paymentId) {
        BaseResponse<PaymentMethodDTO> baseResponse = paymentMethodService.updatePaymentMethod(paymentMethodDTO, paymentId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }

    @DeleteMapping("/delete/{paymentId}")
    public ResponseEntity<BaseResponse<PaymentMethodDTO>> deletePaymentMethod(@PathVariable("paymentId") Long paymentId) {
        BaseResponse<PaymentMethodDTO> baseResponse = paymentMethodService.deletePaymentMethod(paymentId);
        return new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
    }
}

