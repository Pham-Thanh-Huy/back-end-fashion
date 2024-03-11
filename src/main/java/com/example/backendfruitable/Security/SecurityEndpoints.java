package com.example.backendfruitable.Security;

import org.springframework.stereotype.Component;

@Component
public class SecurityEndpoints {

    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "/api/product/get/**",
            "/api/category-product/get/**"
    };

    public static final String[] ADMIN_GET_ENDPOINTS = {
            "/api/user/get/**",
    };

//    public String[] PUBLIC_POST_ENDPOINTS = {
//
//    };

    public static final String[] ADMIN_POST_ENDPOINTS = {
            "/api/user/add",
            "/api/product/add",
            "/api/category-product/add",

    };

//    public String[] PUBLIC_PUT_ENDPOINTS = {
//
//    };


    public static final String[] ADMIN_PUT_ENDPOINTS = {
            "/api/user/update/**",
            "/api/product/update/**",
            "api/category-product/update/**"

    };


//    public String [] PUBLIC_DELETE_ENDPOINTS = {
//
//    };

    public static final String[] ADMIN_DELETE_ENPOINTS = {
            "/api/user/delete/**",
            "/api/product/delete/**",
            "/api/category-product/delete/**"
    };

}
