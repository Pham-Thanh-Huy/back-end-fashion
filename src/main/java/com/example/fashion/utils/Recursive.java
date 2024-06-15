package com.example.fashion.utils;

import com.example.fashion.entity.CategoryPost;
import com.example.fashion.repository.CategoryPostRepository;
import com.example.fashion.repository.CategoryProductRepository;
import com.example.fashion.entity.CategoryProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Recursive {
    @Autowired
    private CategoryProductRepository categoryProductRepository;

    @Autowired
    private CategoryPostRepository categoryPostRepository;

    public List<CategoryProduct> getAllChildrenCategoryProduct(Long parentId) {
        List<CategoryProduct> result = new ArrayList<>();
        List<CategoryProduct> categoryProductList = categoryProductRepository.getCategoryProductByParentId(parentId);
        CategoryProduct parentCategory = categoryProductRepository.getCategoryProductById(parentId);
        if (parentCategory != null) {
            result.add(parentCategory);
        }

        if (categoryProductList != null && !categoryProductList.isEmpty()) {
            for (CategoryProduct categoryProduct : categoryProductList) {
                List<CategoryProduct> categoryProductListChildren = getAllChildrenCategoryProduct(categoryProduct.getCategoryId());
                result.addAll(categoryProductListChildren);
            }
        }
        return result;
    }
//             lấy danh mục sản phẩm đệ quy
        public List<CategoryPost> getAllChildrenCategoryPost(Long parenId){
            List<CategoryPost> result = new ArrayList<>();
            // lấy toàn bộ danh mục cha
            List<CategoryPost> categoryPostList = categoryPostRepository.getListCategoryPostByParentId(parenId);
            // lấy danh mục sản phẩm
            CategoryPost parentCategory = categoryPostRepository.findCategoryPostByCategoryId(parenId);
            if(parentCategory != null){
                result.add(parentCategory);
            }

            if(!categoryPostList.isEmpty() || categoryPostList != null){
                 for(CategoryPost categoryPost : categoryPostList){
                     List<CategoryPost> childrenCategory =  getAllChildrenCategoryPost(categoryPost.getCategoryId());
                     result.addAll(childrenCategory);
                 }
            }
            return result;
        }


}
