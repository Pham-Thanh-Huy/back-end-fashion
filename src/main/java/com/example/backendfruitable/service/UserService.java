package com.example.backendfruitable.service;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.UserDTO;
import com.example.backendfruitable.Repository.UserRepository;
import com.example.backendfruitable.entity.User;
import com.example.backendfruitable.utils.Constant;
import com.example.backendfruitable.utils.ConvertRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConvertRelationship convertRelationship;

    public BaseResponse<List<UserDTO>> getAllUser() {
        BaseResponse<List<UserDTO>> baseResponse = new BaseResponse<>();
        List<UserDTO> userDTOList = new ArrayList<>();
        try {
            List<User> userList = userRepository.findAll();
            if (userList == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_ALL_USER);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return  baseResponse;
            }
            for (User user : userList) {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(user.getUserId());
                userDTO.setUsername(user.getUsername());
                userDTO.setEmail(user.getEmail());
                userDTO.setFirstName(user.getFirstName());
                userDTO.setLastName(user.getLastname());
                userDTO.setAge(user.getAge());
                userDTO.setAddress(user.getAddress());
                userDTO.setSex(user.getSex());
                userDTO.setIsActive(user.getIsActive());
                userDTO.setAuthorizeList(convertRelationship.converToAuthorizeDTOList(user.getAuthorizeList()));
                userDTOList.add(userDTO);
            }

            baseResponse.setData(userDTOList);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERORR_TO_GET_USER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<UserDTO> getUserById(Long id) {
        BaseResponse<UserDTO> baseResponse = new BaseResponse<>();
        try {
                User user = userRepository.getUserById(id);
                if(user == null){
                    baseResponse.setData(null);
                    baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + id);
                    baseResponse.setCode(Constant.NOT_FOUND_CODE);
                    return  baseResponse;
                }
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastname());
            userDTO.setAge(user.getAge());
            userDTO.setAddress(user.getAddress());
            userDTO.setSex(user.getSex());
            userDTO.setIsActive(user.getIsActive());
            userDTO.setAuthorizeList(convertRelationship.converToAuthorizeDTOList(user.getAuthorizeList()));


            baseResponse.setData(userDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERORR_TO_GET_USER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }
}
