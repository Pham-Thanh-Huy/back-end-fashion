package com.example.backendfruitable.utils;

import com.example.backendfruitable.DTO.*;
import com.example.backendfruitable.entity.*;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConvertRelationship {
    @Autowired
    private MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucketName;

    public List<ImageDTO> convertToImageDTOList(List<ImageProduct> imageList) {
        List<ImageDTO> imageDTOList = new ArrayList<>();
        for (ImageProduct imageProduct : imageList) {
            ImageDTO imageDTO = new ImageDTO();
            imageDTO.setImageId(imageProduct.getImageId());
            imageDTO.setImageProduct(imageProduct.getImageProduct());
            imageDTOList.add(imageDTO);
        }
        return imageDTOList;
    }

    public StockDTO convertToStockDTO(Stock stock){
        StockDTO stockDTO = new StockDTO();
        stockDTO.setStockId(stock.getStockId());
        stockDTO.setQuantity(stock.getQuantity());
        return stockDTO;
    }

    public UserDTO convertToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastname());
        userDTO.setAge(user.getAge());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
     // lấy ảnh từ minio
      try{
          String object = user.getUserImage();
          String imageUrl = minioClient.getPresignedObjectUrl(
                  GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(object).build()
          );
          userDTO.setUserImage(object);
          userDTO.setImageUrl(imageUrl);
      }catch (Exception e){
          throw new RuntimeException(e.getMessage());
      }
        return  userDTO;
    }


    public List<ImageProduct> convertToImageList(List<ImageDTO> imageDTOList){
        List<ImageProduct> imageProductList = new ArrayList<>();
        for(ImageDTO imageDTO : imageDTOList){
            ImageProduct imageProduct = new ImageProduct();
            imageProduct.setImageProduct(imageDTO.getImageProduct());
            imageProductList.add(imageProduct);
        }
        return imageProductList;
    }

    public Product convertToProduct(ProductDTO productDTO){
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setProductCode(productDTO.getProductCode());
        product.setListedPrice(productDTO.getListedPrice());
        product.setProductPrice(productDTO.getProductPrice());
        product.setProductDetail(productDTO.getProductDetail());
        product.setProductDescription(productDTO.getProductDescription());
        product.setOutstanding(productDTO.getOutstanding());
        product.setCreatedAt(productDTO.getCreatedAt());
        return  product;
    }

    public ProductDTO convertToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductName(product.getProductName());
        productDTO.setProductCode(product.getProductCode());
        productDTO.setListedPrice(product.getListedPrice());
        productDTO.setProductPrice(product.getProductPrice());
        productDTO.setProductDetail(product.getProductDetail());
        productDTO.setProductDescription(product.getProductDescription());
        productDTO.setOutstanding(product.getOutstanding());
        productDTO.setCreatedAt(product.getCreatedAt());
        return productDTO;
    }


    public Stock  convertToStock(StockDTO stockDTO){
        Stock stock = new Stock();
        stock.setQuantity(stockDTO.getQuantity());
        return stock;
    }

    public List<AuthorizeDTO> converToAuthorizeDTOList(List<Authorize> authorizeList){
        List<AuthorizeDTO> authorizeDTOList = new ArrayList<>();
        for(Authorize authorize : authorizeList){
            AuthorizeDTO authorizeDTO = new AuthorizeDTO();
            authorizeDTO.setAuthorizeId(authorize.getAuthorizeId());
            authorizeDTO.setAuthorizeName(authorize.getAuthorizeName());
            authorizeDTOList.add(authorizeDTO);
        }
        return authorizeDTOList;
    }


    public CategoryProductDTO convertToCategoryProductDTO(CategoryProduct categoryProduct){
        CategoryProductDTO categoryProductDTO = new CategoryProductDTO();
        categoryProductDTO.setCategoryId(categoryProduct.getCategoryId());
        categoryProductDTO.setCategoryName(categoryProduct.getCategoryName());
        categoryProductDTO.setParentId(categoryProduct.getParentId());
        return categoryProductDTO;
    }

    public CategoryPostDTO convertToCategoryPostDTO(CategoryPost categoryPost){
        CategoryPostDTO categoryPostDTO = new CategoryPostDTO();
        categoryPostDTO.setCategoryId(categoryPost.getCategoryId());
        categoryPostDTO.setCategoryName(categoryPost.getCategoryName());
        categoryPostDTO.setParentId(categoryPost.getParentId());
        return categoryPostDTO;
    }


}
