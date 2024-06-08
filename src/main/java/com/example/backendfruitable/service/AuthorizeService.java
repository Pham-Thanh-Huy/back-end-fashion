package com.example.backendfruitable.service;

import com.example.backendfruitable.DTO.AuthorizeDTO;
import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.entity.Authorize;
import com.example.backendfruitable.repository.AuthorizeRepository;
import com.example.backendfruitable.repository.UserRepository;
import com.example.backendfruitable.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorizeService {

    private final AuthorizeRepository authorizeRepository;


    public BaseResponse<List<AuthorizeDTO>> getAllAuthorize(){
        BaseResponse<List<AuthorizeDTO>> baseResponse = new BaseResponse<>();
        try{
            List<Authorize> authorizeList = authorizeRepository.findAll();
            List<AuthorizeDTO> authorizeDTOList = new ArrayList<>();
            if(authorizeList.isEmpty() || authorizeList == null){
                baseResponse.setMessage(Constant.EMPTY_AUTHORIZE_LIST);
            }
            for (Authorize authorize : authorizeList) {
                AuthorizeDTO authorizeDTO = new AuthorizeDTO();
                authorizeDTO.setAuthorizeId(authorize.getAuthorizeId());
                authorizeDTO.setAuthorizeName(authorize.getAuthorizeName());
                authorizeDTOList.add(authorizeDTO);
            }
            baseResponse.setData(authorizeDTOList);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_GET_AUTHOIZE + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


    public BaseResponse<AuthorizeDTO> getAuthorizeById(Integer authorizeId){
        BaseResponse<AuthorizeDTO> baseResponse = new BaseResponse<>();
        try{
                AuthorizeDTO authorizeDTO = new AuthorizeDTO();
                Authorize authorize = authorizeRepository.getAuthorizeById(authorizeId);
                if(authorize == null){
                    baseResponse.setMessage(Constant.EMPTY_AUTHORIZE_BY_ID + authorizeId);
                    baseResponse.setCode(Constant.NOT_FOUND_CODE);
                    return baseResponse;
                }

                authorizeDTO.setAuthorizeId(authorize.getAuthorizeId());
                authorizeDTO.setAuthorizeName(authorize.getAuthorizeName());

                baseResponse.setData(authorizeDTO);
                baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
                baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
                baseResponse.setMessage(Constant.ERROR_TO_GET_AUTHOIZE + e.getMessage());
                baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }
}
