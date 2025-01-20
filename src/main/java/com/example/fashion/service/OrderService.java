package com.example.fashion.service;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.OrderDTO;
import com.example.fashion.DTO.OrderDetailDTO;
import com.example.fashion.DTO.UserDTO;
import com.example.fashion.entity.*;
import com.example.fashion.repository.*;
import com.example.fashion.utils.Constant;
import com.example.fashion.utils.ConvertRelationship;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final  OrderDetailRepository orderDetailRepository;

    private final UserRepository userRepository;

    private final DeliveryMethodRepository deliveryMethodRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final ProductRepository productRepository;

    private final ConvertRelationship convertRelationship;

    private final ProductColorRepository productColorRepository;

    private final ProductSizeRepository productSizeRepository;

    private final InventoryRepository inventoryRepository;

    public BaseResponse<Page<OrderDTO>> getAllOrders(Pageable pageable) {
        BaseResponse<Page<OrderDTO>> baseResponse = new BaseResponse<>();
        try {
            Page<Order> orderPage = orderRepository.findAll(pageable);
            if (orderPage.isEmpty()) {
                baseResponse.setMessage(Constant.EMPTY_ALL_ORDER);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            List<OrderDTO> orderDTOList = new ArrayList<>();
            for (Order order : orderPage) {
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

                for (OrderDetail orderDetail : order.getOrderDetailList()) {
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
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_ORDER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<OrderDTO> getOrderById(Long orderId) {
        BaseResponse<OrderDTO> baseResponse = new BaseResponse<>();
        try {
            Order order = orderRepository.getOrderByOrderId(orderId);
            if (order == null) {
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
            for (OrderDetail orderDetail : order.getOrderDetailList()) {
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
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_ORDER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    // lấy và tính toàn bộ tiền trong đơn hàng (vì là tính tiền đơn hàng dự tính nên lấy cả những đơn đang xử lý vv... , trừ đơn hàng bị huỷ)
    public BaseResponse<ObjectNode> getTotalPriceOrderAndQuantityExpected() {
        BaseResponse<ObjectNode> baseResponse = new BaseResponse<>();
        try {
            List<Order> orderPage = orderRepository.getOrderByStatusNot(Constant.ORDER_CANCELLED);
            if (orderPage.isEmpty()) {
                baseResponse.setMessage(Constant.EMPTY_ALL_ORDER);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            Double grandTotalPrice = 0.0;
            int totalOrder = orderPage.size();
            for (Order order : orderPage) {
                OrderDTO orderDTO = new OrderDTO();
                Double totalPrice = order.getDeliveryMethod().getDeliveryCost() + order.getPaymentMethod().getPaymentCost();
                List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
                for (OrderDetail orderDetail : order.getOrderDetailList()) {
                    totalPrice = totalPrice + orderDetail.getTotalPrice();
                }

                if (!order.getStatus().equals(Constant.ORDER_CANCELLED)) {
                    grandTotalPrice = grandTotalPrice + totalPrice; // Cập nhật tổng giá trị tất cả các đơn hàng không bị huỷ
                }
            }

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("grandTotalPrice", grandTotalPrice);
            objectNode.put("totalOrder", totalOrder);
            baseResponse.setData(objectNode);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_ORDER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    // lấy và tính tổng tiền với tổng số lượng theo status
    public BaseResponse<ObjectNode> getTotalPriceAndQuantityOrderByStatus(String status) {
        BaseResponse<ObjectNode> baseResponse = new BaseResponse<>();
        try {
            List<Order> orderPage = orderRepository.getOrderByStatus(status);
            if (orderPage.isEmpty()) {
                baseResponse.setMessage(Constant.EMPTY_ORDER_BY_STATUS + status);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            Double grandTotalPrice = 0.0;
            int totalOrder = orderPage.size();
            for (Order order : orderPage) {
                OrderDTO orderDTO = new OrderDTO();
                Double totalPrice = order.getDeliveryMethod().getDeliveryCost() + order.getPaymentMethod().getPaymentCost();
                List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
                for (OrderDetail orderDetail : order.getOrderDetailList()) {
                    totalPrice = totalPrice + orderDetail.getTotalPrice();
                }
                    grandTotalPrice = grandTotalPrice + totalPrice; // Cập nhật tổng giá trị tất cả các đơn hàng không bị huỷ
            }

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("grandTotalPrice", grandTotalPrice);
            objectNode.put("totalOrder", totalOrder);
            baseResponse.setData(objectNode);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_ORDER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }
    // lấy theo trạng thái đơn hàng
    public BaseResponse<Page<OrderDTO>> getOrderByStatus(String status, Pageable pageable) {
        BaseResponse<Page<OrderDTO>> baseResponse = new BaseResponse<>();
        try {
            Page<Order> orderPage = orderRepository.getOrderByStatus(status, pageable);
            if (orderPage.isEmpty()) {
                baseResponse.setMessage(Constant.EMPTY_ORDER_BY_STATUS + status);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            List<OrderDTO> orderDTOList = new ArrayList<>();
            for (Order order : orderPage) {
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
                for (OrderDetail orderDetail : order.getOrderDetailList()) {
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
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_ORDER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


    public BaseResponse<OrderDTO> createOrder(Long userId, OrderDTO orderDTO) {
        BaseResponse<OrderDTO> baseResponse = new BaseResponse<>();
        try {
            Order order = new Order();
            List<OrderDetail> orderDetailList = new ArrayList<>();
            User user = userRepository.getUserById(userId);
            if (user == null) {
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            // lấy phương thức thanh toán và phương thức vận chuyển rồi kiểm tra
            if (orderDTO.getDeliveryMethod() == null || orderDTO.getDeliveryMethod().getDeliveryId() == null ||
                    orderDTO.getDeliveryMethod().getDeliveryId().describeConstable().isEmpty()) {
                baseResponse.setMessage(Constant.DELIVERY_METHOD_ID_REQUIRED);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }

            if (orderDTO.getPaymentMethod() == null || orderDTO.getPaymentMethod().getPaymentId() == null ||
                    orderDTO.getPaymentMethod().getPaymentId().describeConstable().isEmpty()) {
                baseResponse.setMessage(Constant.PAYMENT_METHOD_ID_REQUIRED);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }
            Long deliveryId = orderDTO.getDeliveryMethod().getDeliveryId();
            Long paymentId = orderDTO.getPaymentMethod().getPaymentId();
            DeliveryMethod deliveryMethod = deliveryMethodRepository.findDeliveryMethodByDeliveryId(deliveryId);
            PaymentMethod paymentMethod = paymentMethodRepository.findPaymentMethodById(paymentId);
            if (deliveryMethod == null) {
                baseResponse.setMessage(Constant.EMPTY_DELIVERY_METHOD_BY_ID + deliveryId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            if (paymentMethod == null) {
                baseResponse.setMessage(Constant.EMPTY_PAYMENT_METHOD_BY_ID + paymentId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            //băt đầu convert
            order.setAddress(orderDTO.getAddress());
            order.setDeliveryMethod(deliveryMethod);
            order.setPaymentMethod(paymentMethod);
            if (orderDTO.getNote() != null) {
                order.setNote(orderDTO.getNote());
            }
            order.setStatus(Constant.ORDER_PROCESSING);
            order.setUser(user);
            order.setCreatedAt(LocalDate.now());

            if (orderDTO.getOrderDetailList() == null || orderDTO.getOrderDetailList().isEmpty()) {
                baseResponse.setMessage(Constant.ORDER_DETAIL_REQUIRED);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }

            // tiếp tục check các sản phẩm trong orderDetails
            List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
            // Lưu những sản phẩm sẽ được cập nhật hàng tồn kho
            List<Inventory> inventoryListUpdate = new ArrayList<>();
            for (OrderDetailDTO orderDetailDTO : orderDTO.getOrderDetailList()) {
                OrderDetail orderDetail = new OrderDetail();
                OrderDetailDTO orderDetailDTOConvertResponse = new OrderDetailDTO();
                if (orderDetailDTO.getQuantity() == null) {
                    baseResponse.setMessage(Constant.QUANTITY_ORDER_DETAIL_REQUIRED);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return baseResponse;
                }

                // tiếp tục check productId;
                if (orderDetailDTO.getProduct() == null || orderDetailDTO.getProduct().getProductId() == null) {
                    baseResponse.setMessage(Constant.PRODUCT_ID_ORDER_DETAIL_REQUIRED);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return baseResponse;
                }
                Long productId = orderDetailDTO.getProduct().getProductId();
                Product product = productRepository.getProductById(productId);
                Integer productSizeId = orderDetailDTO.getProductSize().getProductSizeId();
                Integer productColorId = orderDetailDTO.getProductColor().getProductColorId();

                if (product == null) {
                    baseResponse.setMessage(Constant.EMPTY_PRODUCT_BY_ID + productId);
                    baseResponse.setCode(Constant.NOT_FOUND_CODE);
                    return baseResponse;
                }

                if(productSizeId == null){
                    baseResponse.setMessage(Constant.INVENTORY_PRODUCT_SIZE_ID_REQUIRED);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return baseResponse;
                }
                if(productColorId == null){
                    baseResponse.setMessage(Constant.INVENTORY_PRODUCT_COLOR_ID_REQUIRED);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return baseResponse;
                }

                ProductSize productSize = productSizeRepository.findProductSizeByProductSizeId(productSizeId);
                ProductColor productColor = productColorRepository.findProductColorByProductColorId(productColorId);
                if (productSize == null) {
                    baseResponse.setMessage(Constant.EMPTY_PRODUCT_SIZE_BY_ID + productSizeId);
                    baseResponse.setCode(Constant.NOT_FOUND_CODE);
                    return baseResponse;
                }
                if (productColor == null) {
                    baseResponse.setMessage(Constant.EMPTY_PRODUCT_COLOR_BY_ID + productColorId);
                    baseResponse.setCode(Constant.NOT_FOUND_CODE);
                    return baseResponse;
                }

                //tính tổng giá tiền
                Double totalPrice = orderDetailDTO.getQuantity().doubleValue() * product.getProductPrice();
                orderDetail.setTotalPrice(totalPrice);
                orderDetail.setQuantity(orderDetailDTO.getQuantity());
                orderDetail.setProductColor(productColor);
                orderDetail.setProductSize(productSize);
                orderDetail.setProduct(product);
                orderDetail.setOrder(order);
                orderDetailList.add(orderDetail);

                // sau khi mua trừ đi số lượng hàng trong tồn kho
                 List<Inventory> inventoryList = new ArrayList<>(product.getInventoryList());
                 Boolean checkInventory = false;
                 for (Inventory inventory : inventoryList) {
                     if(inventory.getProductColor().getProductColorId() == productColorId && inventory.getProductSize().getProductSizeId() == productSizeId){
                         inventory.setQuantity(inventory.getQuantity() - orderDetailDTO.getQuantity());
                         checkInventory = true;
                         inventoryListUpdate.add(inventory);
                         break;
                     }
                 }

                 if(!checkInventory){
                     baseResponse.setMessage(Constant.NOT_FOUND_PRODUCT_SIZE_ID_AND_PRODUCT_COLOR_ID_IN_THIS_PRODUCT_INVENTORY + productId);
                     baseResponse.setCode(Constant.NOT_FOUND_CODE);
                     return baseResponse;
                 }

                // convert tiếp dto để tí trả ra response
                orderDetailDTOConvertResponse.setTotalPrice(totalPrice);
                orderDetailDTOConvertResponse.setQuantity(orderDetailDTO.getQuantity());
                orderDetailDTOConvertResponse.setProduct(convertRelationship.convertToProductDTO(product));
                orderDetailDTOList.add(orderDetailDTOConvertResponse);

            }

           Order saveOrder =  orderRepository.save(order);
            orderDetailRepository.saveAll(orderDetailList);
            inventoryRepository.saveAll(inventoryListUpdate);

            // tiếp tục convert vài dto để trả ra cho người dùng xem
            UserDTO userDTO = convertRelationship.convertToUserDTO(user);
            orderDTO.setOrderId(saveOrder.getOrderId());
            orderDTO.setUser(userDTO);
            orderDTO.setDeliveryMethod(convertRelationship.convertToDeliveryMethodDTO(deliveryMethod));
            orderDTO.setPaymentMethod(convertRelationship.convertToPaymentMethodDTO(paymentMethod));
            orderDTO.setOrderDetailList(orderDetailDTOList);

            baseResponse.setData(orderDTO);
            baseResponse.setMessage(Constant.SUCCESS_ADD_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_ADD_ORDER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


    public BaseResponse<OrderDTO> updateStatus(Long orderId, JsonNode jsonNode) {
        BaseResponse<OrderDTO> baseResponse = new BaseResponse<>();
        try {
            Order order = orderRepository.getOrderByOrderId(orderId);
            if (order == null) {
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
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_UPDATE_ORDER_STATUS + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }
}
