package com.example.backendfruitable.service;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.OrderDTO;
import com.example.backendfruitable.DTO.OrderDetailDTO;
import com.example.backendfruitable.DTO.UserDTO;
import com.example.backendfruitable.entity.*;
import com.example.backendfruitable.repository.*;
import com.example.backendfruitable.utils.Constant;
import com.example.backendfruitable.utils.ConvertRelationship;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private ConvertRelationship convertRelationship;

    public BaseResponse<Page<OrderDTO>> getAllOrders(Pageable pageable) {
        BaseResponse<Page<OrderDTO>> baseResponse = new BaseResponse<>();
        try{
            Page<Order> orderPage = orderRepository.findAll(pageable);
            if(orderPage == null || orderPage.isEmpty()){
                baseResponse.setMessage(Constant.EMPTY_ALL_ORDER);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return  baseResponse;
            }
            List<OrderDTO> orderDTOList = new ArrayList<>();
            for(Order order : orderPage){
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setOrderId(order.getOrderId());
                orderDTO.setAddress(order.getAddress());
                orderDTO.setStatus(order.getStatus());
                orderDTO.setNote(order.getNote());
                orderDTO.setUser(convertRelationship.convertToUserDTO(order.getUser()));
                orderDTO.setCreatedAt(order.getCreatedAt());
                orderDTO.setDeliveryMethod(convertRelationship.convertToDeliveryMethodDTO(order.getDeliveryMethod()));
                orderDTO.setPaymentMethod(convertRelationship.convertToPaymentMethodDTO(order.getPaymentMethod()));

                Double totalPrice = order.getDeliveryMethod().getDeliveryCost() + order.getPaymentMethod().getPaymentCost();
                List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
                for(OrderDetail orderDetail : order.getOrderDetailList()){
                    OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
                    orderDetailDTO.setOrderDetailId(orderDetail.getOrderDetailId());
                    orderDetailDTO.setQuantity(orderDetail.getQuantity());
                    orderDetailDTO.setTotalPrice(orderDetail.getTotalPrice());
                    orderDetailDTO.setProduct(convertRelationship.convertToProductDTO(orderDetail.getProduct()));
                    //tính tổng tiền mỗi lần lặp đơn hàng
                    totalPrice = totalPrice + orderDetail.getTotalPrice();
                    orderDetailDTOList.add(orderDetailDTO);
                }
                orderDTO.setOrderDetailList(orderDetailDTOList);
                orderDTO.setTotalPrice(totalPrice);
                orderDTOList.add(orderDTO);
            }
            Page<OrderDTO> orderDTOPage = new PageImpl<>(orderDTOList, pageable, orderPage.getTotalElements());
            baseResponse.setData(orderDTOPage);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_GET_ORDER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<OrderDTO> getOrderById(Long orderId){
        BaseResponse<OrderDTO> baseResponse = new BaseResponse<>();
        try{
                Order order = orderRepository.getOrderByOrderId(orderId);
                if(order == null){
                    baseResponse.setMessage(Constant.EMPTY_ORDER_BY_ID + orderId);
                    baseResponse.setCode(Constant.NOT_FOUND_CODE);
                    return baseResponse;
                }
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderId(order.getOrderId());
            orderDTO.setAddress(order.getAddress());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setNote(order.getNote());
            orderDTO.setUser(convertRelationship.convertToUserDTO(order.getUser()));
            orderDTO.setCreatedAt(order.getCreatedAt());

            orderDTO.setDeliveryMethod(convertRelationship.convertToDeliveryMethodDTO(order.getDeliveryMethod()));
            orderDTO.setPaymentMethod(convertRelationship.convertToPaymentMethodDTO(order.getPaymentMethod()));

            Double totalPrice = order.getDeliveryMethod().getDeliveryCost() + order.getPaymentMethod().getPaymentCost();
            List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
            for(OrderDetail orderDetail : order.getOrderDetailList()){
                OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
                orderDetailDTO.setOrderDetailId(orderDetail.getOrderDetailId());
                orderDetailDTO.setQuantity(orderDetail.getQuantity());
                orderDetailDTO.setTotalPrice(orderDetail.getTotalPrice());
                orderDetailDTO.setProduct(convertRelationship.convertToProductDTO(orderDetail.getProduct()));
                //tính tổng tiền mỗi lần lặp đơn hàng
                totalPrice = totalPrice + orderDetail.getTotalPrice();
                orderDetailDTOList.add(orderDetailDTO);
            }
            orderDTO.setOrderDetailList(orderDetailDTOList);
            orderDTO.setTotalPrice(totalPrice);

            baseResponse.setData(orderDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_GET_ORDER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


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
            order.setStatus(Constant.ORDER_PROCESSING);
            order.setUser(user);
            order.setCreatedAt(LocalDate.now());

            if(orderDTO.getOrderDetailList()== null || orderDTO.getOrderDetailList().isEmpty()){
                baseResponse.setMessage(Constant.ORDER_DETAIL_REQUIRED);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return  baseResponse;
            }

            // tiếp tục check các sản phẩm trong orderDetails
            List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
            for(OrderDetailDTO orderDetailDTO : orderDTO.getOrderDetailList()){
                OrderDetail orderDetail = new OrderDetail();
                OrderDetailDTO orderDetailDTOConvertResponse = new OrderDetailDTO();
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

                // convert tiếp dto để tí trả ra response
                orderDetailDTOConvertResponse.setTotalPrice(totalPrice);
                orderDetailDTOConvertResponse.setQuantity(orderDetailDTO.getQuantity());
                orderDetailDTOConvertResponse.setProduct(convertRelationship.convertToProductDTO(product));
                orderDetailDTOList.add(orderDetailDTOConvertResponse);

            }

            orderRepository.save(order);
            orderDetailRepository.saveAll(orderDetailList);

            // tiếp tục convert vài dto để trả ra cho người dùng xem
            UserDTO  userDTO = convertRelationship.convertToUserDTO(user);
            orderDTO.setUser(userDTO);
            orderDTO.setDeliveryMethod(convertRelationship.convertToDeliveryMethodDTO(deliveryMethod));
            orderDTO.setPaymentMethod(convertRelationship.convertToPaymentMethodDTO(paymentMethod));
            orderDTO.setOrderDetailList(orderDetailDTOList);

            baseResponse.setData(orderDTO);
            baseResponse.setMessage(Constant.SUCCESS_ADD_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_ADD_ORDER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


    public BaseResponse<OrderDTO> updateStatus(Long orderId, JsonNode jsonNode){
        BaseResponse<OrderDTO> baseResponse = new BaseResponse<>();
        try{
            Order order = orderRepository.getOrderByOrderId(orderId);
            if(order == null){
                baseResponse.setMessage(Constant.EMPTY_ORDER_BY_ID + orderId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            JsonNode statusNode = jsonNode.get("status");
            if (statusNode == null || statusNode.isNull() || statusNode.asText().isEmpty()) {
                baseResponse.setMessage(Constant.STATUS_ORDER_REQUIRED);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }
            String status = String.valueOf(statusNode.asText());

            switch (status) {
                case Constant.ORDER_PROCESSING:
                    order.setStatus(Constant.ORDER_PROCESSING);
                    break;
                case Constant.ORDER_CONFIRMED:
                    order.setStatus(Constant.ORDER_CONFIRMED);
                    break;
                case Constant.ORDER_DELIVERING:
                    order.setStatus(Constant.ORDER_DELIVERING);
                    break;
                case Constant.ORDER_COMPLETED:
                    order.setStatus(Constant.ORDER_COMPLETED);
                    break;
                case Constant.ORDER_CANCELLED:
                    order.setStatus(Constant.ORDER_CANCELLED);
                    break;
                default:
                    baseResponse.setMessage(Constant.ERROR_INPUT_ORDER_STATUS);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return baseResponse;
            }
            Order OrderResponse = orderRepository.save(order);

//            bắt đầu convert
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderId(OrderResponse.getOrderId());
            orderDTO.setAddress(OrderResponse.getAddress());
            orderDTO.setStatus(OrderResponse.getStatus());
            orderDTO.setNote(OrderResponse.getNote());
            orderDTO.setCreatedAt(OrderResponse.getCreatedAt());

            baseResponse.setData(orderDTO);
            baseResponse.setMessage(Constant.SUCCESS_UPDATE_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_UPDATE_ORDER_STATUS + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }
}
