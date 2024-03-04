package com.example.backendfruitable.service;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.CategoryProductDTO;
import com.example.backendfruitable.Repository.CategoryProductRepository;
import com.example.backendfruitable.entity.CategoryProduct;
import com.example.backendfruitable.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryProductService {
    @Autowired
    private CategoryProductRepository categoryProductRepository;

    public BaseResponse<List<CategoryProductDTO>> getAllCategoryProduct() {
        BaseResponse<List<CategoryProductDTO>> baseResponse = new BaseResponse<>();
        List<CategoryProductDTO> categoryProductDTOList = new ArrayList<>();
        try {
            List<CategoryProduct> categoryProductList = categoryProductRepository.findAll();
            if (categoryProductList == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_ALL_CATEGORY_PRODUCT);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            for (CategoryProduct categoryProduct : categoryProductList) {
                CategoryProductDTO categoryProductDTO = new CategoryProductDTO();
                categoryProductDTO.setCategoryId(categoryProduct.getCategoryId());
                categoryProductDTO.setCategoryName(categoryProduct.getCategoryName());
                categoryProductDTOList.add(categoryProductDTO);
            }

            baseResponse.setData(categoryProductDTOList);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);


        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_CATEGORY_PRODUCT + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<CategoryProductDTO> getCategoryProductById(Long id) {
        BaseResponse<CategoryProductDTO> baseResponse = new BaseResponse<>();
        CategoryProductDTO categoryProductDTO = new CategoryProductDTO();
        try {
            CategoryProduct categoryProduct = categoryProductRepository.getCategoryProductById(id);
            if (categoryProduct == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_CATEGORY_PRODUCT_BY_ID + id);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            categoryProductDTO.setCategoryId(categoryProduct.getCategoryId());
            categoryProductDTO.setCategoryName(categoryProduct.getCategoryName());

            baseResponse.setData(categoryProductDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_CATEGORY_PRODUCT + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<CategoryProductDTO> addCategoryProduct(CategoryProductDTO categoryProductDTO) {
        BaseResponse<CategoryProductDTO> baseResponse = new BaseResponse<>();
        try {
            CategoryProduct categoryProduct = new CategoryProduct();
            categoryProduct.setCategoryName(categoryProductDTO.getCategoryName());
            categoryProductRepository.save(categoryProduct);

            baseResponse.setData(categoryProductDTO);
            baseResponse.setMessage(Constant.SUCCESS_ADD_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_ADD_CATEGORY_PRODUCT + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<CategoryProductDTO> updateCategoryProduct(Long id, CategoryProductDTO categoryProductDTO) {
        BaseResponse<CategoryProductDTO> baseResponse = new BaseResponse<>();
        try {
            CategoryProduct categoryProduct = categoryProductRepository.getCategoryProductById(id);
            if (categoryProduct == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_PRODUCT_BY_ID + id + "nên không thể sửa");
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            categoryProduct.setCategoryName(categoryProductDTO.getCategoryName());
            categoryProductRepository.save(categoryProduct);

            baseResponse.setData(categoryProductDTO);
            baseResponse.setMessage(Constant.SUCCESS_UPDATE_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_UPDATE_CATEGORY_PRODUCT);
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<CategoryProductDTO> deleteCategoryProductById(Long id) {
        BaseResponse<CategoryProductDTO> baseResponse = new BaseResponse<>();
        try {
            CategoryProduct categoryProduct = categoryProductRepository.getCategoryProductById(id);
            if (categoryProduct == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_PRODUCT_BY_ID + id + " nên không thể xoá");
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            baseResponse.setMessage(Constant.SUCCESS_DELETE_MESSAGE + " với danh mục có id là: " + id);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_DELETE_CATEGORY_PRODUCT);
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

}
