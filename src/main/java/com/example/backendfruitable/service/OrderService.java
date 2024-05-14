package com.example.backendfruitable.service;

import com.example.backendfruitable.repository.OrderDetailRepository;
import com.example.backendfruitable.repository.OrderRepository;
import com.example.backendfruitable.repository.UserRepository;
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
}
