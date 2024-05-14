package com.example.backendfruitable.service;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.OrderDTO;
import com.example.backendfruitable.entity.Order;
import com.example.backendfruitable.repository.OrderDetailRepository;
import com.example.backendfruitable.repository.OrderRepository;
import com.example.backendfruitable.repository.UserRepository;
import com.example.backendfruitable.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository  orderDetailRepository;
    @Autowired
    private UserRepository userRepository;
    public BaseResponse<OrderDTO> createOrder(OrderDTO orderDTO){
        BaseResponse<OrderDTO> baseResponse = new BaseResponse<>();
        try{
            Order order = new Order();

        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_ADD_ORDER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }
}
