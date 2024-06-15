package com.example.fashion.controller;

import com.example.fashion.DTO.AuthorizeDTO;
import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.UserDTO;
import com.example.fashion.repository.UserRepository;
import com.example.fashion.security.JWT.JWTUtils;
import com.example.fashion.service.UserService;
import com.example.fashion.utils.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private JWTUtils jwtUtils;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserDTO userDTO;

    private BaseResponse<UserDTO> baseResponse;

    @BeforeEach
    void initData() {
        userDTO = UserDTO.builder()
                .username("exampleUser")
                .password("examplePass")
                .email("example@example.com")
                .firstName("John")
                .lastName("Doe")
                .address("123 Main St")
                .age(102)
                .sex('M')
                .phoneNumber("0812345678")
                .authorizeList(Collections.singletonList(AuthorizeDTO.builder().authorizeId(2).build()))
                .build();

        baseResponse = BaseResponse.<UserDTO>builder().code(Constant.SUCCESS_CODE).message(Constant.SUCCESS_MESSAGE).build();

    }

    @Test
    void createUser() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Mockito.when(userService.addUser(userDTO)).thenReturn(baseResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/user/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(userDTO))
                        .header("Authorization", "Bearer " + jwtUtils.generateToken("Phamthanhhuy3062k3")))
                .andExpect(MockMvcResultMatchers.status().is(Constant.SUCCESS_CODE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(Constant.SUCCESS_MESSAGE))
                .andDo(print());


    }

}
