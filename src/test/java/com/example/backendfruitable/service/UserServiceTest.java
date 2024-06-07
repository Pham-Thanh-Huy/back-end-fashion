package com.example.backendfruitable.service;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.UserDTO;
import com.example.backendfruitable.entity.User;

import com.example.backendfruitable.repository.UserRepository;
import com.example.backendfruitable.utils.Constant;
import com.example.backendfruitable.utils.ConvertRelationship;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ConvertRelationship convertRelationship;

    @MockBean
    private MinioClient minioClient;

    private String minioBucketName = "fashion";

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAllUser_ReturnsUserList() throws Exception {
//         Mock data
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastname("User");
        user.setAge(30);
        user.setAddress("123 Test St");
        user.setPhoneNumber("1234567890");
        user.setUserImage("image.jpg");
        user.setSex('M');
        user.setIsActive(true);
        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        Mockito.when(minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(minioBucketName).object(user.getUserImage()).build()
        )).thenReturn("http://example.com/image.jpg");
        Mockito.when(convertRelationship.converToAuthorizeDTOList(anyList())).thenReturn(Arrays.asList());

        // Execute service call
        BaseResponse<List<UserDTO>> response = userService.getAllUser();
        // Assertions
        assertEquals(Constant.SUCCESS_CODE, response.getCode());
        assertEquals(Constant.SUCCESS_MESSAGE, response.getMessage());

    }
}