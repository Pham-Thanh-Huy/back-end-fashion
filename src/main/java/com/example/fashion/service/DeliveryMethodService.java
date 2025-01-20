package com.example.fashion.service;


import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.DeliveryMethodDTO;
import com.example.fashion.entity.DeliveryMethod;
import com.example.fashion.entity.User;
import com.example.fashion.repository.DeliveryMethodRepository;
import com.example.fashion.repository.UserRepository;
import com.example.fashion.utils.Constant;
import com.example.fashion.utils.ConvertRelationship;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryMethodService {

    private final DeliveryMethodRepository deliveryMethodRepository;
    private final ConvertRelationship convertRelationship;
    private final UserRepository userRepository;

    public BaseResponse<List<DeliveryMethodDTO>> getAllDeliveryMethod(){
        BaseResponse<List<DeliveryMethodDTO>> baseResponse = new BaseResponse<>();
        List<DeliveryMethodDTO> deliveryMethodDTOList = new ArrayList<>();
        try{
            List<DeliveryMethod> deliveryMethodList = deliveryMethodRepository.findAll();
            if(deliveryMethodList == null || deliveryMethodList.isEmpty()){
                baseResponse.setMessage(Constant.EMPTY_ALL_DELIVERY_METHOD);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return  baseResponse;
            }

            for(DeliveryMethod deliveryMethod : deliveryMethodList){
                DeliveryMethodDTO deliveryMethodDTO = new DeliveryMethodDTO();
                deliveryMethodDTO.setDeliveryId(deliveryMethod.getDeliveryId());
                deliveryMethodDTO.setName(deliveryMethod.getName());
                deliveryMethodDTO.setDescription(deliveryMethod.getDescription());
                deliveryMethodDTO.setDeliveryCost(deliveryMethod.getDeliveryCost());
                deliveryMethodDTO.setUser(convertRelationship.convertToUserDTO(deliveryMethod.getUser()));
                deliveryMethodDTOList.add(deliveryMethodDTO);
            }

            baseResponse.setData(deliveryMethodDTOList);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_GET_DELIVERY_METHOD + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<DeliveryMethodDTO> getDeliveryMethodById(Long id){
        BaseResponse<DeliveryMethodDTO> baseResponse = new BaseResponse<>();
        try{
            DeliveryMethod deliveryMethod = deliveryMethodRepository.findDeliveryMethodByDeliveryId(id);
            if(deliveryMethod == null){
                baseResponse.setMessage(Constant.EMPTY_DELIVERY_METHOD_BY_ID + id);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            DeliveryMethodDTO deliveryMethodDTO = new DeliveryMethodDTO();
            deliveryMethodDTO.setDeliveryId(deliveryMethod.getDeliveryId());
            deliveryMethodDTO.setName(deliveryMethod.getName());
            deliveryMethodDTO.setDescription(deliveryMethod.getDescription());
            deliveryMethodDTO.setDeliveryCost(deliveryMethod.getDeliveryCost());
            deliveryMethodDTO.setUser(convertRelationship.convertToUserDTO(deliveryMethod.getUser()));

            baseResponse.setData(deliveryMethodDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_GET_DELIVERY_METHOD + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


    public BaseResponse<DeliveryMethodDTO> addDeliveryMethod(Long userId, DeliveryMethodDTO deliveryMethodDTO){
        BaseResponse<DeliveryMethodDTO> baseResponse = new BaseResponse<>();
        DeliveryMethod deliveryMethod = new DeliveryMethod();
        try{
            User user =  userRepository.getUserById(userId);
            if(user == null){
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + userId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            deliveryMethod.setName(deliveryMethodDTO.getName());
            deliveryMethod.setDescription(deliveryMethodDTO.getDescription());
            deliveryMethod.setDeliveryCost(deliveryMethodDTO.getDeliveryCost());
            deliveryMethod.setUser(user);
            deliveryMethodRepository.save(deliveryMethod);

            baseResponse.setData(deliveryMethodDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_ADD_DELIVERY_METHOD + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<DeliveryMethodDTO> updateDeliveryMethod(Long deliveryMethodId, Long userId,DeliveryMethodDTO deliveryMethodDTO){
        BaseResponse<DeliveryMethodDTO> baseResponse = new BaseResponse<>();
        try{
            DeliveryMethod deliveryMethod = deliveryMethodRepository.findDeliveryMethodByDeliveryId(deliveryMethodId);
            if(deliveryMethod == null){
                baseResponse.setMessage(Constant.EMPTY_DELIVERY_METHOD_BY_ID + deliveryMethodId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            User user = userRepository.getUserById(userId);
            if(user == null){
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + userId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            deliveryMethod.setName(deliveryMethodDTO.getName());
            deliveryMethod.setDescription(deliveryMethodDTO.getDescription());
            deliveryMethod.setDeliveryCost(deliveryMethodDTO.getDeliveryCost());
            deliveryMethod.setUser(user);
            deliveryMethodRepository.save(deliveryMethod);

            baseResponse.setData(deliveryMethodDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_UPDATE_DELIVERY_METHOD + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


    public BaseResponse<DeliveryMethodDTO> deleteDeliveryMethod(Long deliveryMethodId){
        BaseResponse<DeliveryMethodDTO> baseResponse = new BaseResponse<>();
        try{
            DeliveryMethod deliveryMethod = deliveryMethodRepository.findDeliveryMethodByDeliveryId(deliveryMethodId);
            if(deliveryMethod == null){
                baseResponse.setMessage(Constant.EMPTY_DELIVERY_METHOD_BY_ID + deliveryMethodId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            deliveryMethodRepository.delete(deliveryMethod);
            baseResponse.setMessage(Constant.DELETE_SUCCESS_DELIVERY_METHOD + deliveryMethodId);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_DELETE_DELIVERY_METHOD + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


}
