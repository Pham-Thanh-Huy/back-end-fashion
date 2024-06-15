package com.example.fashion.service;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.CategoryProductDTO;
import com.example.fashion.repository.CategoryProductRepository;
import com.example.fashion.repository.UserRepository;
import com.example.fashion.entity.CategoryProduct;
import com.example.fashion.entity.User;
import com.example.fashion.utils.Constant;
import com.example.fashion.utils.ConvertRelationship;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryProductService {

    private final CategoryProductRepository categoryProductRepository;
    private final UserRepository userRepository;
    private final ConvertRelationship convertRelationship;



    public BaseResponse<List<CategoryProductDTO>> getAllCategoryProduct() {
        BaseResponse<List<CategoryProductDTO>> baseResponse = new BaseResponse<>();
        List<CategoryProductDTO> categoryProductDTOList = new ArrayList<>();
        try {
            List<CategoryProduct> categoryProductList = categoryProductRepository.getAllCategoryProduct();
            if (categoryProductList == null || categoryProductList.isEmpty()) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_ALL_CATEGORY_PRODUCT);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            for (CategoryProduct categoryProduct : categoryProductList) {
                CategoryProductDTO categoryProductDTO = new CategoryProductDTO();
                categoryProductDTO.setCategoryId(categoryProduct.getCategoryId());
                categoryProductDTO.setCategoryName(categoryProduct.getCategoryName());
                categoryProductDTO.setParentId(categoryProduct.getParentId());
                categoryProductDTO.setCreatedAt(categoryProduct.getCreatedAt());
                categoryProductDTO.setUser(convertRelationship.convertToUserDTO(categoryProduct.getUser()));
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


    public BaseResponse<Page<CategoryProductDTO>> getListCategoryProductPagination(Pageable pageable){
        BaseResponse<Page<CategoryProductDTO>> baseResponse = new BaseResponse<>();
        List<CategoryProductDTO> categoryProductDTOList = new ArrayList<>();
         try{
            Page<CategoryProduct> categoryProductPage = categoryProductRepository.findAll(pageable);

            if(categoryProductPage.isEmpty()){
                baseResponse.setMessage(Constant.EMPTY_ALL_CATEGORY_PRODUCT);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return  baseResponse;
            }

            for(CategoryProduct categoryProduct : categoryProductPage.getContent()){
                CategoryProductDTO categoryProductDTO = new CategoryProductDTO();
                categoryProductDTO.setCategoryId(categoryProduct.getCategoryId());
                categoryProductDTO.setCategoryName(categoryProduct.getCategoryName());
                categoryProductDTO.setParentId(categoryProduct.getParentId());
                categoryProductDTO.setCreatedAt(categoryProduct.getCreatedAt());
                categoryProductDTO.setUser(convertRelationship.convertToUserDTO(categoryProduct.getUser()));
                categoryProductDTOList.add(categoryProductDTO);
            }

            Page<CategoryProductDTO> categoryProductDTOPage = new PageImpl<>(categoryProductDTOList,
                    pageable, categoryProductPage.getTotalElements());
            baseResponse.setData(categoryProductDTOPage);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
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
            categoryProductDTO.setParentId(categoryProduct.getParentId());
            categoryProductDTO.setCreatedAt(categoryProduct.getCreatedAt());
            categoryProductDTO.setUser(convertRelationship.convertToUserDTO(categoryProduct.getUser()));


            baseResponse.setData(categoryProductDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_CATEGORY_PRODUCT + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    // lấy theo tên
    public BaseResponse<List<CategoryProductDTO>> getCategoryProductByCategoryName(String categoryName){
        BaseResponse<List<CategoryProductDTO>> baseResponse = new BaseResponse<>();
      List<CategoryProductDTO> categoryProductDTOList = new ArrayList<>();
        try{
            List<CategoryProduct> categoryProductList = categoryProductRepository.getCategoryProductByCategoryName(categoryName);
            if(categoryProductList == null || categoryProductList.isEmpty()){
                baseResponse.setMessage(Constant.EMPTY_CATEGORY_PRODUCT_BY_CATEGORY_NAME + categoryName);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return  baseResponse;
            }

            // convert sang DTO
            for(CategoryProduct categoryProduct : categoryProductList) {
                CategoryProductDTO categoryProductDTO = new CategoryProductDTO();
                categoryProductDTO.setCategoryId(categoryProduct.getCategoryId());
                categoryProductDTO.setCategoryName(categoryProduct.getCategoryName());
                categoryProductDTO.setParentId(categoryProduct.getParentId());
                categoryProductDTO.setCreatedAt(categoryProduct.getCreatedAt());
                categoryProductDTO.setUser(convertRelationship.convertToUserDTO(categoryProduct.getUser()));
                categoryProductDTOList .add(categoryProductDTO);
            }
            baseResponse.setData(categoryProductDTOList);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_GET_CATEGORY_PRODUCT + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<CategoryProductDTO> addCategoryProduct(CategoryProductDTO categoryProductDTO,  Long userId) {
        BaseResponse<CategoryProductDTO> baseResponse = new BaseResponse<>();
        try {
            User user =  userRepository.getUserById(userId);

            if(user == null){
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + userId + "nên không thể sửa");
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            CategoryProduct categoryProduct = new CategoryProduct();
            categoryProduct.setCategoryName(categoryProductDTO.getCategoryName());
            categoryProduct.setParentId(categoryProductDTO.getParentId());
            categoryProduct.setCreatedAt(LocalDate.now());
            categoryProduct.setUser(user);
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

    public BaseResponse<CategoryProductDTO> updateCategoryProduct(Long id, CategoryProductDTO categoryProductDTO, Long userId) {
        BaseResponse<CategoryProductDTO> baseResponse = new BaseResponse<>();
        try {
            CategoryProduct categoryProduct = categoryProductRepository.getCategoryProductById(id);
            User user =  userRepository.getUserById(userId);

            if(user == null){
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + userId + " nên không thể sửa");
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            if (categoryProduct == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_PRODUCT_BY_ID + id + "nên không thể sửa");
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            categoryProduct.setCategoryName(categoryProductDTO.getCategoryName());
            categoryProduct.setParentId(categoryProductDTO.getParentId());
            categoryProduct.setUser(user);
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
            categoryProductRepository.delete(categoryProduct);
            baseResponse.setMessage(Constant.SUCCESS_DELETE_MESSAGE + " với danh mục có id là: " + id);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_DELETE_CATEGORY_PRODUCT);
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

}
