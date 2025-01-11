package com.example.fashion.service;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.CategoryPostDTO;
import com.example.fashion.repository.CategoryPostRepository;
import com.example.fashion.repository.UserRepository;
import com.example.fashion.entity.CategoryPost;
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
public class CategoryPostService {

    private final CategoryPostRepository categoryPostRepository;

    private final ConvertRelationship convertRelationship;

    private final UserRepository userRepository;

    public BaseResponse<List<CategoryPostDTO>> getAllCategoryPost()  {
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
                categoryPostDTO.setCreatedAt(categoryPost.getCreatedAt());
                categoryPostDTO.setUser(convertRelationship.convertToUserDTO(categoryPost.getUser()));
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

    public BaseResponse<Page<CategoryPostDTO>>  getListCategoryPostPagination(Pageable pageable){
        BaseResponse<Page<CategoryPostDTO>> baseResponse = new BaseResponse<>();
        List<CategoryPostDTO> categoryPostDTOList = new ArrayList<>();
        try{
            Page<CategoryPost> categoryPostPage = categoryPostRepository.findAll(pageable);
            if(categoryPostPage == null || categoryPostPage.isEmpty()){
                baseResponse.setMessage(Constant.EMPTY_ALL_CATEGORY_POST);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return  baseResponse;
            }
            for(CategoryPost categoryPost : categoryPostPage){
                CategoryPostDTO categoryPostDTO = new CategoryPostDTO();
                categoryPostDTO.setCategoryId(categoryPost.getCategoryId());
                categoryPostDTO.setCategoryName(categoryPost.getCategoryName());
                categoryPostDTO.setParentId(categoryPost.getParentId());
                categoryPostDTO.setCreatedAt(categoryPost.getCreatedAt());
                categoryPostDTO.setUser(convertRelationship.convertToUserDTO(categoryPost.getUser()));
                categoryPostDTOList.add(categoryPostDTO);
            }

            Page<CategoryPostDTO> categoryPostDTOPage = new PageImpl<>(categoryPostDTOList, pageable, categoryPostPage.getTotalElements());
            baseResponse.setData(categoryPostDTOPage);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        }catch (Exception e){
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
            categoryPostDTO.setCreatedAt(categoryPost.getCreatedAt());
            categoryPostDTO.setUser(convertRelationship.convertToUserDTO(categoryPost.getUser()));


            baseResponse.setData(categoryPostDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_CATEGORY_POST + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
   }

   public BaseResponse<CategoryPostDTO> addCategoryPost(CategoryPostDTO categoryPostDTO, Long userId){
        BaseResponse<CategoryPostDTO> baseResponse = new BaseResponse<>();
        try{
            User user = userRepository.getUserById(userId);
            if(user == null){
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + userId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return  baseResponse;
            }
            CategoryPost categoryPost = new CategoryPost();
            categoryPost.setCategoryName(categoryPostDTO.getCategoryName());
            categoryPost.setParentId(categoryPostDTO.getParentId());
            categoryPost.setCreatedAt(LocalDate.now());
            categoryPost.setUser(user);

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
