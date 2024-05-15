package com.example.backendfruitable.service;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.OrderDTO;
import com.example.backendfruitable.DTO.OrderDetailDTO;
import com.example.backendfruitable.entity.*;
import com.example.backendfruitable.repository.*;
import com.example.backendfruitable.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository  orderDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeliveryMethodRepository deliveryMethodRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private ProductRepository productRepository;

    public BaseResponse<OrderDTO> createOrder(Long userId, OrderDTO orderDTO){
        BaseResponse<OrderDTO> baseResponse = new BaseResponse<>();
        try{
            Order order = new Order();
           List<OrderDetail> orderDetailList = new ArrayList<>();
            User user = userRepository.getUserById(userId);
            if(user == null){
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            // lấy phương thức thanh toán và phương thức vận chuyển rồi kiểm tra
            if(orderDTO.getDeliveryMethod() == null ||orderDTO.getDeliveryMethod().getDeliveryId() == null ||
                    orderDTO.getDeliveryMethod().getDeliveryId().describeConstable().isEmpty()){
                baseResponse.setMessage(Constant.DELIVERY_METHOD_ID_REQUIRED);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }

            if(orderDTO.getPaymentMethod() == null || orderDTO.getPaymentMethod().getPaymentId() == null ||
                    orderDTO.getPaymentMethod().getPaymentId().describeConstable().isEmpty()){
                baseResponse.setMessage(Constant.PAYMENT_METHOD_ID_REQUIRED);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }
            Long deliveryId = orderDTO.getDeliveryMethod().getDeliveryId();
            Long paymentId = orderDTO.getPaymentMethod().getPaymentId();
            DeliveryMethod deliveryMethod = deliveryMethodRepository.findDeliveryMethodByDeliveryId(deliveryId);
            PaymentMethod paymentMethod = paymentMethodRepository.findPaymentMethodById(paymentId);
            if(deliveryMethod == null){
                baseResponse.setMessage(Constant.EMPTY_DELIVERY_METHOD_BY_ID + deliveryId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            if(paymentMethod == null){
                baseResponse.setMessage(Constant.EMPTY_PAYMENT_METHOD_BY_ID + paymentId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            //băt đầu convert
            order.setAddress(orderDTO.getAddress());
            order.setDeliveryMethod(deliveryMethod);
            order.setPaymentMethod(paymentMethod);
            if(orderDTO.getNote() != null){
                order.setNote(orderDTO.getNote());
            }
            order.setUser(user);
            order.setCreatedAt(LocalDate.now());

            if(orderDTO.getOrderDetailList()== null || orderDTO.getOrderDetailList().isEmpty()){
                baseResponse.setMessage(Constant.ORDER_DETAIL_REQUIRED);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return  baseResponse;
            }
            // tiếp tục check các sản phẩm trong orderDetails
            for(OrderDetailDTO orderDetailDTO : orderDTO.getOrderDetailList()){
                OrderDetail orderDetail = new OrderDetail();

                if(orderDetailDTO.getQuantity() == null){
                    baseResponse.setMessage(Constant.QUANTITY_ORDER_DETAIL_REQUIRED);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return  baseResponse;
                }

                // tiếp tục check productId;
                if( orderDetailDTO.getProduct() == null || orderDetailDTO.getProduct().getProductId() == null){
                    baseResponse.setMessage(Constant.PRODUCT_ID_ORDER_DETAIL_REQUIRED);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return  baseResponse;
                }
                Long productId = orderDetailDTO.getProduct().getProductId();
                Product product = productRepository.getProductById(productId);
                if(product == null){
                    baseResponse.setMessage(Constant.EMPTY_PRODUCT_BY_ID + productId);
                    baseResponse.setCode(Constant.NOT_FOUND_CODE);
                    return  baseResponse;
                }
                //tính tổng giá tiền
                Double totalPrice = orderDetailDTO.getQuantity().doubleValue() * product.getProductPrice();
                orderDetail.setTotalPrice(totalPrice);
                orderDetail.setQuantity(orderDetailDTO.getQuantity());
                orderDetail.setProduct(product);
                orderDetail.setOrder(order);
                orderDetailList.add(orderDetail);
            }

            orderRepository.save(order);
            orderDetailRepository.saveAll(orderDetailList);

            baseResponse.setData(orderDTO);
            baseResponse.setMessage(Constant.SUCCESS_ADD_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_ADD_ORDER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }
}
