package com.example.backendfruitable.service;


import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.DeliveryMethodDTO;
import com.example.backendfruitable.entity.DeliveryMethod;
import com.example.backendfruitable.repository.DeliveryMethodRepository;
import com.example.backendfruitable.utils.Constant;
import com.example.backendfruitable.utils.ConvertRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryMethodService {

    @Autowired
    private DeliveryMethodRepository deliveryMethodRepository;

    @Autowired
    private ConvertRelationship convertRelationship;

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




}
