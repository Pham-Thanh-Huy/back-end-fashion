package com.example.fashion.exception;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.utils.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ValidateExceptionHandler {
   @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Map<String, Object>>> handleValidationExceptions(MethodArgumentNotValidException ex){
       Map<String, Object> body  = new LinkedHashMap<>();
       BaseResponse<Map<String, Object>> baseResponse = new BaseResponse<>();
       body.put("Error", ex.getBindingResult()
               .getFieldErrors()
               .stream()
               .map(fieldError -> fieldError.getDefaultMessage())
               .collect(Collectors.toList()));
       body.put("TimeStamp", LocalDateTime.now());


       baseResponse.setData(body);
       baseResponse.setMessage(Constant.VALIDATION_FAILED_MESSAGE);
       baseResponse.setCode(Constant.BAD_REQUEST_CODE);
       return  new ResponseEntity<>(baseResponse, HttpStatus.valueOf(baseResponse.getCode()));
   }
}

