package com.example.backendfruitable.security;

import org.springframework.stereotype.Component;

@Component
public class SecurityEndpoints {

    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "/api/product/get/**",
            "/api/category-product/get/**",
            "/api/category-post/get/**",
            "/api/post/get/**",
            "/active",
            "/api/delivery-method/get/**",

    };

    public static final String[] ADMIN_GET_ENDPOINTS = {
            "/api/user/get/**",
    };

    public static final String[] PUBLIC_POST_ENDPOINTS = {
            "/api/user/add",
            "/login",
            "/register"
    };

    public static final String[] ADMIN_POST_ENDPOINTS = {
            "/api/product/add",
            "/api/category-product/add",
            "/api/category-post/add",
            "/api/post/add"

    };

    public static final String[] PUBLIC_PUT_ENDPOINTS = {
        "/api/user/update"
    };


    public static final String[] ADMIN_PUT_ENDPOINTS = {
            "/api/user/update/**",
            "/api/product/update/**",
            "/api/category-product/update/**",
            "/api/category-post/update/**",
            "api/post/update/**"
    };


//    public String [] PUBLIC_DELETE_ENDPOINTS = {
//
//    };

    public static final String[] ADMIN_DELETE_ENPOINTS = {
            "/api/user/delete/**",
            "/api/product/delete/**",
            "/api/category-product/delete/**",
            "/api/category-post/delete/**",
            "/api/post/delete/**"
    };

}
