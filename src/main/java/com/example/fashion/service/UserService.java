package com.example.fashion.service;

import com.example.fashion.DTO.*;
import com.example.fashion.repository.AuthorizeRepository;
import com.example.fashion.repository.UserRepository;
import com.example.fashion.security.JWT.JWTUtils;
import com.example.fashion.email.EmailConfig;
import com.example.fashion.entity.Authorize;
import com.example.fashion.entity.User;
import com.example.fashion.utils.Constant;
import com.example.fashion.utils.ConvertRelationship;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    private final ConvertRelationship convertRelationship;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AuthorizeRepository authorizeRepository;

    private final AuthenticationManager authenticationManager;

    private final JWTUtils jwtUtils;

    private final EmailConfig emailConfig;

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String minioBucketName;

    public BaseResponse<List<UserDTO>> getAllUser() {
        BaseResponse<List<UserDTO>> baseResponse = new BaseResponse<>();
        List<UserDTO> userDTOList = new ArrayList<>();
        try {
            List<User> userList = userRepository.findAll();
            if (userList.isEmpty()){
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
                userDTO.setPhoneNumber(user.getPhoneNumber());
                // Xử lý lấy phần ảnh
                String objectName = user.getUserImage();
                String imageUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(minioBucketName).object(objectName).build()
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
            baseResponse.setMessage(Constant.ERROR_TO_GET_USER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<Page<UserDTO>> getListUserByAuthorizeName(Pageable pageable, String authorizeName) {
        BaseResponse<Page<UserDTO>> baseResponse = new BaseResponse<>();
        List<UserDTO> userDTOList = new ArrayList<>();
        try {
            Page<User> userPage = userRepository.getUserByAuthorizeName(authorizeName, pageable);
            if (userPage == null || userPage.isEmpty()) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_LIST_USER_BY_AUTHORIZE_NAME + authorizeName);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            for (User user : userPage) {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(user.getUserId());
                userDTO.setUsername(user.getUsername());
                userDTO.setEmail(user.getEmail());
                userDTO.setFirstName(user.getFirstName());
                userDTO.setLastName(user.getLastname());
                userDTO.setAge(user.getAge());
                userDTO.setAddress(user.getAddress());
                userDTO.setPhoneNumber(user.getPhoneNumber());
                // Xử lý lấy phần ảnh
                String objectName = user.getUserImage();
                String imageUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(minioBucketName).object(objectName).build()
                );
                userDTO.setUserImage(objectName);
                userDTO.setImageUrl(imageUrl);
                userDTO.setSex(user.getSex());
                userDTO.setIsActive(user.getIsActive());
                userDTO.setAuthorizeList(convertRelationship.converToAuthorizeDTOList(user.getAuthorizeList()));
                userDTOList.add(userDTO);
            }

            Page<UserDTO> userDTOPage = new PageImpl<>(userDTOList, pageable, userPage.getTotalElements());

            baseResponse.setData(userDTOPage);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_USER + e.getMessage());
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
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setSex(user.getSex());
            userDTO.setIsActive(user.getIsActive());
            userDTO.setAuthorizeList(convertRelationship.converToAuthorizeDTOList(user.getAuthorizeList()));

            // Lấy tên đối tượng của ảnh từ User và chuyển đổi thành đường link
            String objectName = user.getUserImage();
            String imageUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioBucketName)
                            .object(objectName)
                            .build()
            );
            userDTO.setUserImage(objectName);
            userDTO.setImageUrl(imageUrl);

            baseResponse.setData(userDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_USER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<UserDTO> getByUsername(String username) {
        BaseResponse<UserDTO> baseResponse = new BaseResponse<>();
        try {
            User user = userRepository.getUserByUsername(username);
            if (user == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_USER_BY_USERNAME + username);
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
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setSex(user.getSex());
            userDTO.setIsActive(user.getIsActive());
            userDTO.setAuthorizeList(convertRelationship.converToAuthorizeDTOList(user.getAuthorizeList()));

            // Lấy tên đối tượng của ảnh từ User và chuyển đổi thành đường link
            String objectName = user.getUserImage();
            String imageUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioBucketName)
                            .object(objectName)
                            .build()
            );
            userDTO.setUserImage(objectName);
            userDTO.setImageUrl(imageUrl);

            baseResponse.setData(userDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_USER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


    public BaseResponse<UserDTO> addUser(UserDTO userDTO){
        BaseResponse<UserDTO> baseResponse = new BaseResponse<>();
        log.info("Service : Add User");
        try{
            if(userDTO.getDataImage() == null){
                baseResponse.setMessage(Constant.EMPTY_BASE64_IMAGE);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
            }

            User checkUserUsernameExists = userRepository.getUserByUsername(userDTO.getUsername());
            User checkUserEmailExists = userRepository.getUserByEmail(userDTO.getEmail());
            if(checkUserUsernameExists != null){
                baseResponse.setMessage(Constant.EXISTS_USER_USERNAME + userDTO.getUsername());
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }

            if(checkUserEmailExists != null){
                baseResponse.setMessage(Constant.EXISTS_USER_EMAIL + userDTO.getEmail());
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }

            if(userDTO.getPassword() == null || userDTO.getPassword().isEmpty()){
                baseResponse.setMessage(Constant.EMPTY_PASSWORD);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return  baseResponse;
            }
            if(userDTO.getAge() > 100){
                baseResponse.setMessage(Constant.USER_AGE_SMALL_THAN_100);
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
            user.setPhoneNumber(userDTO.getPhoneNumber());
            String randomTokenActive = codeActive();
            user.setToken_active(randomTokenActive);
            user.setIsActive(true);
            byte[] imageData = Base64.getDecoder().decode((userDTO.getDataImage()));

            InputStream inputStream = new ByteArrayInputStream(imageData);
            String objectName = "user_" + System.currentTimeMillis() + ".jpg";

            minioClient.putObject(PutObjectArgs.builder().bucket(minioBucketName)
                    .object(objectName)
                    .stream(inputStream, imageData.length, -1)
                    .contentType("image/jpeg")
                    .build());
            user.setUserImage(objectName);

            List<AuthorizeDTO> authorizeDTOList = userDTO.getAuthorizeList();
            if(authorizeDTOList == null || authorizeDTOList.isEmpty()){
                baseResponse.setMessage(Constant.EMPTY_AUTHORIZE_LIST);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }
            List<Authorize> authorizeList = new ArrayList<>();

            for(AuthorizeDTO authorizeDTO : authorizeDTOList){
                Authorize authorize = authorizeRepository.getAuthorizeById(authorizeDTO.getAuthorizeId());
                if(authorize != null){
                    authorizeList.add(authorize);
                }
            }
            user.setAuthorizeList(authorizeList);
            userRepository.save(user);

            baseResponse.setData(userDTO);
            baseResponse.setMessage(Constant.SUCCESS_ADD_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_ADD_USER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<UserDTO> registerUser(UserDTO userDTO) {
        BaseResponse<UserDTO> baseResponse = new BaseResponse<>();
        try {
            if(userDTO.getDataImage() == null ){
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
            user.setPhoneNumber(userDTO.getPhoneNumber());
            String randomTokenActive = codeActive();
            user.setToken_active(randomTokenActive);
            user.setIsActive(false);

            //xử lý hình ảnh
            byte[] imageByte = Base64.getDecoder().decode(userDTO.getDataImage());
            InputStream inputStream = new ByteArrayInputStream(imageByte);
            String objectName = "user_" + System.currentTimeMillis() + ".jpg";
            // config minio
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(minioBucketName)
                            .object(objectName)
                            .stream(inputStream, imageByte.length, -1)
                            .contentType("image/jpeg").build()
            );

            // lấy link url sau khi put
            user.setUserImage(objectName);


            List<Authorize> authorizeList = new ArrayList<>();
            Authorize userAuthorize =  authorizeRepository.getAuthorizeByName("USER");
//            System.out.println(userAuthorize);
            if(userAuthorize == null){
                baseResponse.setMessage(Constant.NOT_EXISTS_AUTHORIZE_USER_TO_REGISTER);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            authorizeList.add(userAuthorize);
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
            baseResponse.setMessage(Constant.ERROR_TO_REGISTER_USER + e.getMessage());
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
            user.setPhoneNumber(userDTO.getPhoneNumber());
            //update Hình ảnh nếu có update không thì sẽ không update
            if(userDTO.getDataImage() != null){
                byte[] newImage = Base64.getDecoder().decode(userDTO.getDataImage());
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
                        RemoveObjectArgs.builder().bucket(minioBucketName).object(object).build()
                );

                // tiếp tục thêm lại ảnh mới vẫn là tên object đấy
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(minioBucketName)
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
            baseResponse.setMessage(Constant.ERROR_TO_UPDATE_USER + e.getMessage());
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

            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioBucketName).object(object).build());

            // Xoá tất cả các mối quan hệ nhiều-nhiều với Authorize
            user.getAuthorizeList().clear();

            // Xoá người dùng và lưu thay đổi vào cơ sở dữ liệu
            userRepository.deleteAllInBatch(List.of(user));

            baseResponse.setMessage(Constant.DELETE_SUCCESS_USER_BY_ID + id);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_DELETE_USER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


    // random 1 chuoi code Active
    public String codeActive() {
        return UUID.randomUUID().toString();
    }


    // Login
    public BaseResponse<JWTResponse> loginUser(LoginRequest loginRequest){
        BaseResponse<JWTResponse> baseResponse = new BaseResponse<>();
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            // nếu login  thành công
            if(authentication.isAuthenticated()){
                User user = userRepository.getUserByUsername(loginRequest.getUsername());
                if(user.getIsActive() == false){
                    baseResponse.setMessage(Constant.USER_IS_NOT_ACTIVE);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return  baseResponse;
                }
                JWTResponse jwtResponse = new JWTResponse();
                jwtResponse.setJwt(jwtUtils.generateToken(loginRequest.getUsername()));


                baseResponse.setData(jwtResponse);
                baseResponse.setMessage(Constant.LOGIN_SUCCESS);
                baseResponse.setCode(Constant.SUCCESS_CODE);
                return  baseResponse;
            }

            baseResponse.setMessage(Constant.LOGIN_FAILED);
            baseResponse.setCode(Constant.BAD_REQUEST_CODE);
        }catch (Exception e){
            log.error(Constant.AUTHENTICATION_FAILED + e.getMessage());
            baseResponse.setMessage(Constant.LOGIN_FAILED);
            baseResponse.setCode(Constant.BAD_REQUEST_CODE);
        }
        return baseResponse;
    }

    //active user;
    public BaseResponse<UserDTO> activeUser(Long userId, String codeActive){
        BaseResponse<UserDTO> baseResponse = new BaseResponse<>();
        try{
            User user = userRepository.getUserById(userId);
            if(user == null){
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + userId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return  baseResponse;
            }

            if(user.getIsActive() == true){
                baseResponse.setMessage(Constant.USER_HAS_BEEN_AUTHENTICATED);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return  baseResponse;
            }

            if(!user.getToken_active().equals(codeActive)){
                baseResponse.setMessage(Constant.AUTHENTICATION_CODE_IS_INCORRECT);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return  baseResponse;
            }

            // nếu tất cả đều đúng thì xác thực
            user.setIsActive(true);
            userRepository.save(user);

            baseResponse.setMessage(Constant.AUTHENTICATION_SUCCESS);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_ACTIVE_USER + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }
}

