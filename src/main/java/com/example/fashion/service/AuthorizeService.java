package com.example.fashion.service;

import com.example.fashion.DTO.AuthorizeDTO;
import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.entity.Authorize;
import com.example.fashion.repository.AuthorizeRepository;
import com.example.fashion.utils.Constant;
import lombok.RequiredArgsConstructor;
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
