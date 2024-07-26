package com.example.fashion.security;

import org.springframework.stereotype.Component;

@Component
public class SecurityEndpoints {

    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "/api/product/get/**",
            "/api/category-product/get/all",
            "/api/category-product/get/*",
            "/api/category-post/get/**",
            "/api/post/get/**",
            "/active",
            "/api/user/get/by/username",
            "/api/delivery-method/get/**",
            "/api/payment-method/get/*",
            "/api/order/get/*",
            "/api/product-size/get/*",
            "/api/product-color/get/*",
            "/api/product/get/outstanding"
    };

    public static final String[] ADMIN_STAFF_GET_ENDPOINTS = {
            "/api/category-product/get/all/pagination",
            "/api/user/get/*",
            "/api/user/get/by/authorize-name",
            "/api/authorize/get/*",
            "/api/order/get/all/pagination",
            "/api/order/get/by/status",
            "/api/order/get/totalprice/by/status",
            "/api/order/get/totalprice/expected"
    };

    public static final String[] PUBLIC_POST_ENDPOINTS = {
            "/login",
            "/register",
            "/api/order/add"
    };


    public static final String[] ADMIN_POST_ENDPOINTS = {
            "/api/user/add",

    };

    public static final String[] ADMIN_STAFF_POST_ENDPOINTS = {
            "/api/product/add",
            "/api/category-product/add",
            "/api/category-post/add",
            "/api/post/add",
            "/api/delivery-method/add",
            "/api/payment-method/add",
            "/api/product/add/inventory",
            "/api/product-size/add",
            "/api/product-color/add"

    };

    public static final String[] PUBLIC_PUT_ENDPOINTS = {
            "/api/user/update"
    };


    public static final String[] ADMIN_STAFF_PUT_ENDPOINTS = {
            "/api/product/update/**",
            "/api/category-product/update/**",
            "/api/category-post/update/**",
            "api/post/update/**",
            "/api/delivery-method/update/*",
            "/api/payment-method/update/*",
            "/api/order/update/*",
            "/api/product/update/inventory/*"
    };
    public static final String[] ADMIN_PUT_ENDPOINTS = {
            "/api/user/update/**",
    };


//    public String [] PUBLIC_DELETE_ENDPOINTS = {
//
//    };

    public static final String[] ADMIN_STAFF_DELETE_ENPOINTS = {
            "/api/product/delete/**",
            "/api/category-product/delete/**",
            "/api/category-post/delete/**",
            "/api/post/delete/**",
            "/api/delivery-method/delete/*",
            "/api/payment-method/delete/*",
    };

    public static final String [] ADMIN_DELETE_ENDPOINTS = {
            "/api/user/delete/**",
    };

    public static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

}
