package com.example.fashion.service;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.PaymentMethodDTO;
import com.example.fashion.entity.PaymentMethod;
import com.example.fashion.entity.User;
import com.example.fashion.repository.PaymentMethodRepository;
import com.example.fashion.repository.UserRepository;
import com.example.fashion.utils.Constant;
import com.example.fashion.utils.ConvertRelationship;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    private final ConvertRelationship convertRelationship;

    private final UserRepository userRepository;

    public BaseResponse<List<PaymentMethodDTO>> getAllPaymentMethod() {
        BaseResponse<List<PaymentMethodDTO>> baseResponse = new BaseResponse<>();
        List<PaymentMethodDTO> paymentMethodDTOList = new ArrayList<>();
        try{
            List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
            if(paymentMethodList == null || paymentMethodList.isEmpty()){
                baseResponse.setMessage(Constant.EMPTY_ALL_PAYMENT_METHOD);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return  baseResponse;
            }
            for(PaymentMethod paymentMethod : paymentMethodList){
                PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
                paymentMethodDTO.setPaymentId(paymentMethod.getPaymentId());
                paymentMethodDTO.setPaymentName(paymentMethod.getPaymentName());
                paymentMethodDTO.setDescription(paymentMethod.getDescription());
                paymentMethodDTO.setPaymentCost(paymentMethod.getPaymentCost());
                paymentMethodDTO.setUser(convertRelationship.convertToUserDTO(paymentMethod.getUser()));
                paymentMethodDTOList.add(paymentMethodDTO);
            }

            baseResponse.setData(paymentMethodDTOList);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_GET_PAYMENT_METHOD + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<PaymentMethodDTO> getPaymentMethodById(Long paymentId){
        BaseResponse<PaymentMethodDTO> baseResponse = new BaseResponse<>();
        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
        try {
            PaymentMethod paymentMethod = paymentMethodRepository.findPaymentMethodById(paymentId);
            if(paymentMethod == null){
                baseResponse.setMessage(Constant.EMPTY_PAYMENT_METHOD_BY_ID + paymentId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            paymentMethodDTO.setPaymentId(paymentMethod.getPaymentId());
            paymentMethodDTO.setPaymentName(paymentMethod.getPaymentName());
            paymentMethodDTO.setPaymentCost(paymentMethod.getPaymentCost());
            paymentMethodDTO.setDescription(paymentMethod.getDescription());
            paymentMethodDTO.setUser(convertRelationship.convertToUserDTO(paymentMethod.getUser()));

            baseResponse.setData(paymentMethodDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_GET_PAYMENT_METHOD + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<PaymentMethodDTO>addPaymentMethod(PaymentMethodDTO paymentMethodDTO, Long userId){
        BaseResponse<PaymentMethodDTO> baseResponse = new BaseResponse<>();
        PaymentMethod paymentMethod = new PaymentMethod();
        try {
            User user  = userRepository.getUserById(userId);
            if(user == null){
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + userId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            paymentMethod.setPaymentName(paymentMethodDTO.getPaymentName());
            paymentMethod.setPaymentCost(paymentMethodDTO.getPaymentCost());
            paymentMethod.setDescription(paymentMethodDTO.getDescription());
            paymentMethod.setUser(user);
            paymentMethodRepository.save(paymentMethod);

            baseResponse.setData(paymentMethodDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_ADD_PAYMENT_METHOD + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<PaymentMethodDTO> updatePaymentMethod(PaymentMethodDTO paymentMethodDTO, Long paymentId){
        BaseResponse<PaymentMethodDTO> baseResponse = new BaseResponse<>();
        try {
            PaymentMethod paymentMethod = paymentMethodRepository.findPaymentMethodById(paymentId);
            if(paymentMethod == null){
                baseResponse.setMessage(Constant.EMPTY_PAYMENT_METHOD_BY_ID + paymentId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            paymentMethod.setPaymentName(paymentMethodDTO.getPaymentName());
            paymentMethod.setPaymentCost(paymentMethodDTO.getPaymentCost());
            paymentMethod.setDescription(paymentMethodDTO.getDescription());
            paymentMethodRepository.save(paymentMethod);

            baseResponse.setData(paymentMethodDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_UPDATE_PAYMENT_METHOD + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<PaymentMethodDTO> deletePaymentMethod(Long paymentId){
        BaseResponse<PaymentMethodDTO> baseResponse = new BaseResponse<>();
        try {
            PaymentMethod paymentMethod = paymentMethodRepository.findPaymentMethodById(paymentId);
            if(paymentMethod == null){
                baseResponse.setMessage(Constant.EMPTY_PAYMENT_METHOD_BY_ID + paymentId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            paymentMethodRepository.delete(paymentMethod);

            baseResponse.setMessage(Constant.DELETE_SUCCESS_PAYMENT_METHOD + paymentId);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_DELETE_PAYMENT_METHOD + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }
}
