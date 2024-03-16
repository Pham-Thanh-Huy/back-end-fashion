package com.example.backendfruitable.service;

import com.example.backendfruitable.DTO.AuthorizeDTO;
import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.UserDTO;
import com.example.backendfruitable.Repository.AuthorizeRepository;
import com.example.backendfruitable.Repository.UserRepository;
import com.example.backendfruitable.email.EmailConfig;
import com.example.backendfruitable.entity.Authorize;
import com.example.backendfruitable.entity.User;
import com.example.backendfruitable.utils.Constant;
import com.example.backendfruitable.utils.ConvertRelationship;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

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

    @Autowired
    private EmailConfig emailConfig;

    @Autowired
    private MinioClient minioClient;

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
                // Xử lý lấy phần ảnh
                String objectName = user.getUserImage();
                String imageUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket("fashion").object(objectName).build()
                );
                userDTO.setUserImage(objectName);
                userDTO.setImageUrl(imageUrl);
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
            userDTO.setSex(user.getSex());
            userDTO.setIsActive(user.getIsActive());
            userDTO.setAuthorizeList(convertRelationship.converToAuthorizeDTOList(user.getAuthorizeList()));

            // Lấy tên đối tượng của ảnh từ User và chuyển đổi thành đường link
            String objectName = user.getUserImage();
            String imageUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket("fashion")
                            .object(objectName)
                            .build()
            );
            userDTO.setUserImage(objectName);
            userDTO.setImageUrl(imageUrl);

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
            if(userDTO.getDataImage() == null || userDTO.getDataImage().length == 0){
                baseResponse.setMessage(Constant.EMPTY_BASE64_IMAGE);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }
            User checkUserEmailExits = userRepository.getUserByEmail(userDTO.getEmail());
            User checkUserUsernameExits = userRepository.getUserByUsername(userDTO.getUsername());
            if (checkUserUsernameExits != null) {
                baseResponse.setMessage(Constant.EXISTS_USER_USERNAME + userDTO.getUsername());
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }
            if (checkUserEmailExits != null) {
                baseResponse.setMessage(Constant.EXISTS_USER_EMAIL + userDTO.getEmail());
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }

            if(userDTO.getPassword() == null || userDTO.getPassword().isEmpty()){
                baseResponse.setMessage(Constant.EMPTY_PASSWORD);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }

            User user = new User();
            user.setUserId(userDTO.getUserId());
            user.setUsername(userDTO.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
            user.setEmail(userDTO.getEmail());
            user.setFirstName(userDTO.getFirstName());
            user.setLastname(userDTO.getLastName());
            user.setAge(userDTO.getAge());
            user.setAddress(userDTO.getAddress());
            user.setSex(userDTO.getSex());
            String randomTokenActive = codeActive();
            user.setToken_active(randomTokenActive);
            user.setIsActive(false);

            //xử lý hình ảnh
            try {
                byte[] imageByte = Base64.getDecoder().decode(Base64.getEncoder().encode(userDTO.getDataImage()));
                InputStream inputStream = new ByteArrayInputStream(imageByte);
                String objectName = "user_" + System.currentTimeMillis() + ".jpg";
                // config minio
                minioClient.putObject(
                        PutObjectArgs.builder().bucket("fashion")
                                .object(objectName)
                                .stream(inputStream, imageByte.length, -1)
                                .contentType("image/jpeg").build()
                );

                // lấy link url sau khi put
                user.setUserImage(objectName);
            } catch (Exception e) {
                baseResponse.setMessage(Constant.ERORR_TO_ADD_USER + e.getMessage());
                baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
            }

            // Lấy 1 list mối quan hệ bên bảng authorize  qua id khi truyền vào từ api mapping của userDTO
            List<AuthorizeDTO> authorizeDTOList = userDTO.getAuthorizeList();
            List<Authorize> authorizeList = new ArrayList<>();
            for (AuthorizeDTO authorizeDTO : authorizeDTOList) {
                Authorize authorize = authorizeRepository.getAuthorizeById(authorizeDTO.getAuthorizeId());
                if (authorize != null) {
                    authorizeList.add(authorize);
                } else {
                    baseResponse.setMessage(Constant.EMPTY_AUTHORIZE_BY_ID + authorizeDTO.getAuthorizeId());
                    baseResponse.setCode(Constant.NOT_FOUND_CODE);
                    return baseResponse;
                }
            }
            user.setAuthorizeList(authorizeList);

            userRepository.save(user);

            //cấu hình mail
            String subject = "Active tài khoản tài Fashion";
            String textSendMailActive = "Bạn vừa đăng kí tại khoản ở fashion để tài khoản có thể sử dụng bạn cần xác thực" +
                    "<Br>Mã xác thực của bạn là:  " + randomTokenActive +
                    "<Br>Bạn có thể xác thực theo đường link sau: " +
                    "http://localhost:3000/user-active?email=" + user.getEmail() + "&codeActive=" + randomTokenActive;

            emailConfig.sendMail("phamthanhhuy3062k3@gmail.com", user.getEmail(), subject, textSendMailActive);

            baseResponse.setData(userDTO);
            baseResponse.setMessage(Constant.SUCCESS_ADD_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);


        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERORR_TO_ADD_USER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


    public BaseResponse<UserDTO> updateUser(Long userId, UserDTO userDTO) {
        BaseResponse<UserDTO> baseResponse = new BaseResponse<>();
        try {
            User user = userRepository.getUserById(userId);
            User checkUserEmailExits = userRepository.getUserByEmail(userDTO.getEmail());
            User checkUserUsernameExits = userRepository.getUserByUsername(userDTO.getUsername());

            if (user == null) {
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + userId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            } else {
                //nếu email khác với email đã lưu mới check xem nó đã tồn tại trong hệ thống chưa
                if (!user.getUsername().equals(userDTO.getUsername())) {
                    if (checkUserUsernameExits != null) {
                        baseResponse.setMessage(Constant.EXISTS_USER_USERNAME + userDTO.getUsername());
                        baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                        return baseResponse;
                    }
                }
                //nếu username khác với username đã lưu mới check xem nó đã tồn tại trong hệ thống chưa
                if (!user.getEmail().equals(userDTO.getEmail())) {
                    if (checkUserEmailExits != null) {
                        baseResponse.setMessage(Constant.EXISTS_USER_EMAIL + userDTO.getEmail());
                        baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                        return baseResponse;
                    }
                }
            }

            // cập nhật
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setFirstName(userDTO.getFirstName());
            user.setLastname(userDTO.getLastName());
            user.setAge(userDTO.getAge());
            user.setAddress(userDTO.getAddress());
            //update Hình ảnh nếu có update không thì sẽ không update
            if(userDTO.getDataImage() != null && userDTO.getDataImage().length > 0){
                byte[] newImage = Base64.getDecoder().decode(Base64.getEncoder().encode(userDTO.getDataImage()));
                InputStream inputStream = new ByteArrayInputStream(newImage);
                    // lấy object hiện tại và delete
                String object = userDTO.getUserImage();
                // so sánh tiếp xem object của ảnh hiện tại có đúng với ảnh của người đấy hiện tại không
                if(!object.equals(user.getUserImage())){
                    baseResponse.setMessage(Constant.ERROR_USER_IMAGE_FOR_USER);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return baseResponse;
                }
                minioClient.removeObject(
                        RemoveObjectArgs.builder().bucket("fashion").object(object).build()
                );

                // tiếp tục thêm lại ảnh mới vẫn là tên object đấy
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket("fashion")
                                .stream(inputStream, newImage.length, -1)
                                .object(object)
                                .contentType("image/jpeg")
                                .build()
                );

                user.setUserImage(object);

            }
            user.setSex(userDTO.getSex());

            // làm mới lại authorize
            user.getAuthorizeList().clear();

            List<AuthorizeDTO> authorizeDTOList = userDTO.getAuthorizeList();
            List<Authorize> authorizeList = new ArrayList<>();
            for (AuthorizeDTO authorizeDTO : authorizeDTOList) {
                Authorize authorize = authorizeRepository.getAuthorizeById(authorizeDTO.getAuthorizeId());
                if (authorize != null) {
                    authorizeList.add(authorize);
                } else {
                    baseResponse.setMessage(Constant.EMPTY_AUTHORIZE_BY_ID + authorizeDTO.getAuthorizeId());
                    baseResponse.setCode(Constant.NOT_FOUND_CODE);
                    return baseResponse;
                }
            }
            user.setAuthorizeList(authorizeList);

            userRepository.save(user);

            baseResponse.setData(userDTO);
            baseResponse.setMessage(Constant.SUCCESS_UPDATE_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERORR_TO_UPDATE_USER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


    public BaseResponse<UserDTO> deleteUserById(Long id) {
        BaseResponse<UserDTO> baseResponse = new BaseResponse<>();
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (!optionalUser.isPresent()) {
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + id);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            User user = optionalUser.get();

            // xoá ảnh trong minio
            String object = user.getUserImage();

            minioClient.removeObject(RemoveObjectArgs.builder().bucket("fashion").object(object).build());

            // Xoá tất cả các mối quan hệ nhiều-nhiều với Authorize
            user.getAuthorizeList().clear();

            // Xoá người dùng và lưu thay đổi vào cơ sở dữ liệuxs
            userRepository.deleteAllInBatch(List.of(user));

            baseResponse.setMessage(Constant.DELETE_SUCCESS_USER_BY_ID + id);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERORR_TO_DELETE_USER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


    // random 1 chuoi code Active
    public String codeActive() {
        return UUID.randomUUID().toString();
    }

}
