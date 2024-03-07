package com.example.backendfruitable.service;

import com.example.backendfruitable.DTO.AuthorizeDTO;
import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.UserDTO;
import com.example.backendfruitable.Repository.AuthorizeRepository;
import com.example.backendfruitable.Repository.UserRepository;
import com.example.backendfruitable.entity.Authorize;
import com.example.backendfruitable.entity.User;
import com.example.backendfruitable.utils.Constant;
import com.example.backendfruitable.utils.ConvertRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service

public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConvertRelationship convertRelationship;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthorizeRepository authorizeRepository;

    public BaseResponse<List<UserDTO>> getAllUser() {
        BaseResponse<List<UserDTO>> baseResponse = new BaseResponse<>();
        List<UserDTO> userDTOList = new ArrayList<>();
        try {
            List<User> userList = userRepository.findAll();
            if (userList == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_ALL_USER);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
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
                userDTO.setUserImage(user.getUserImage());
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
            if (user == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + id);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastname());
            userDTO.setAge(user.getAge());
            userDTO.setAddress(user.getAddress());
            userDTO.setUserImage(user.getUserImage());
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

    public BaseResponse<UserDTO> addUser(UserDTO userDTO) {
        BaseResponse<UserDTO> baseResponse = new BaseResponse<>();
        try {
            User user = new User();
            user.setUserId(userDTO.getUserId());
            user.setUsername(userDTO.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
            user.setEmail(userDTO.getEmail());
            user.setFirstName(userDTO.getFirstName());
            user.setLastname(userDTO.getLastName());
            user.setAge(userDTO.getAge());
            user.setAddress(userDTO.getAddress());
            user.setUserImage(userDTO.getUserImage());
            user.setSex(userDTO.getSex());
            String randomTokenActive = codeActive();
            user.setToken_active(randomTokenActive);
            user.setIsActive(false);

            //cấu hình mail
            String textSendMailActive = "Bạn vừa đăng kí tại khoản ở fashion";

            // Lấy 1 list mối quan hệ bên bảng authorize  qua id khi truyền vào từ api mapping của userDTO
            List<AuthorizeDTO> authorizeDTOList = userDTO.getAuthorizeList();
            List<Authorize> authorizeList = new ArrayList<>();
            for (AuthorizeDTO authorizeDTO : authorizeDTOList) {
                Authorize authorize = authorizeRepository.getAuthorizeById(authorizeDTO.getAuthorizeId());
                if (authorize != null) {
                    authorizeList.add(authorize);
                }else{
                    baseResponse.setMessage(Constant.EMPTY_AUTHORIZE_BY_ID + authorizeDTO.getAuthorizeId());
                    baseResponse.setCode(Constant.NOT_FOUND_CODE);
                    return baseResponse;
                }
            }
            user.setAuthorizeList(authorizeList);

            userRepository.save(user);

            baseResponse.setData(userDTO);
            baseResponse.setMessage(Constant.SUCCESS_ADD_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);


        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERORR_TO_ADD_USER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    // random 1 chuoi code Active
    public String codeActive() {
        return UUID.randomUUID().toString();
    }

}
