package com.example.backendfruitable.service;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.CategoryPostDTO;
import com.example.backendfruitable.Repository.CategoryPostRepository;
import com.example.backendfruitable.entity.CategoryPost;
import com.example.backendfruitable.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryPostService {
    @Autowired
    private CategoryPostRepository categoryPostRepository;

    public BaseResponse<List<CategoryPostDTO>> getAllCategoryPost() {
        BaseResponse<List<CategoryPostDTO>> baseResponse = new BaseResponse<>();
        List<CategoryPostDTO> categoryPostDTOList = new ArrayList<>();
        try {
            List<CategoryPost> categoryPostList = categoryPostRepository.findAll();
            if (categoryPostList.isEmpty() || categoryPostList == null) {
                baseResponse.setMessage(Constant.EMPTY_ALL_CATEGORY_POST);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return  baseResponse;
            }

            for(CategoryPost categoryPost : categoryPostList){
                CategoryPostDTO categoryPostDTO = new CategoryPostDTO();
                categoryPostDTO.setCategoryId(categoryPost.getCategoryId());
                categoryPostDTO.setCategoryName(categoryPost.getCategoryName());
                categoryPostDTO.setParentId(categoryPost.getParentId());
                categoryPostDTOList.add(categoryPostDTO);
            }
            baseResponse.setData(categoryPostDTOList);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_CATEGORY_POST + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }

        return baseResponse;
    }

    public BaseResponse<CategoryPostDTO> getCategoryPostById(Long categoryId) {
        BaseResponse<CategoryPostDTO>  baseResponse = new BaseResponse<>();
        try {
            CategoryPost categoryPost = categoryPostRepository.findCategoryPostByCategoryId(categoryId);
            if (categoryPost == null) {
                baseResponse.setMessage(Constant.EMPTY_CATEGORY_POST_BY_ID + categoryId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return  baseResponse;
            }

                CategoryPostDTO categoryPostDTO = new CategoryPostDTO();
                categoryPostDTO.setCategoryId(categoryPost.getCategoryId());
                categoryPostDTO.setCategoryName(categoryPost.getCategoryName());
                categoryPostDTO.setParentId(categoryPost.getParentId());


            baseResponse.setData(categoryPostDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_CATEGORY_POST + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
   }

   public BaseResponse<CategoryPostDTO> addCategoryPost(CategoryPostDTO categoryPostDTO){
        BaseResponse<CategoryPostDTO> baseResponse = new BaseResponse<>();
        try{
            CategoryPost categoryPost = new CategoryPost();
            categoryPost.setCategoryName(categoryPostDTO.getCategoryName());
            categoryPost.setParentId(categoryPostDTO.getParentId());

            categoryPostRepository.save(categoryPost);

            baseResponse.setData(categoryPostDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_ADD_CATEGORY_POST + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
   }

   public BaseResponse<CategoryPostDTO> updateCategoryPost(Long categoryId, CategoryPostDTO categoryPostDTO){

       BaseResponse<CategoryPostDTO> baseResponse = new BaseResponse<>();
       try{

           CategoryPost categoryPost = categoryPostRepository.findCategoryPostByCategoryId(categoryId);
           if(categoryPost == null){
               baseResponse.setMessage(Constant.EMPTY_CATEGORY_POST_BY_ID + categoryId);
               baseResponse.setCode(Constant.NOT_FOUND_CODE);
               return baseResponse;
           }
           categoryPost.setCategoryName(categoryPostDTO.getCategoryName());
           categoryPost.setParentId(categoryPostDTO.getParentId());

           categoryPostRepository.save(categoryPost);

           baseResponse.setData(categoryPostDTO);
           baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
           baseResponse.setCode(Constant.SUCCESS_CODE);
       }catch (Exception e){
           baseResponse.setMessage(Constant.ERROR_TO_UPDATE_CATEGORY_POST + e.getMessage());
           baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
       }
       return baseResponse;
   }

   public BaseResponse<CategoryPostDTO> deleteCategoryPostById(Long categoryId){
        BaseResponse<CategoryPostDTO> baseResponse = new BaseResponse<>();
        try{
            CategoryPost categoryPost = categoryPostRepository.findCategoryPostByCategoryId(categoryId);
            if(categoryPost == null){
                baseResponse.setMessage(Constant.EMPTY_CATEGORY_POST_BY_ID + categoryId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            categoryPostRepository.delete(categoryPost);
            baseResponse.setMessage(Constant.SUCCESS_DELETE_MESSAGE + " với danh mục bài viết có id là: " + categoryId);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        }catch (Exception e){
            baseResponse.setMessage(Constant.ERROR_TO_DELETE_CATEGORY_POST + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
   }
}
