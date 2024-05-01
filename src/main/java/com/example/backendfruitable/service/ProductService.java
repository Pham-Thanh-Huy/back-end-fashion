package com.example.backendfruitable.service;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.ImageDTO;
import com.example.backendfruitable.DTO.ProductDTO;
import com.example.backendfruitable.DTO.StockDTO;
import com.example.backendfruitable.repository.*;
import com.example.backendfruitable.entity.*;
import com.example.backendfruitable.utils.Constant;
import com.example.backendfruitable.utils.ConvertRelationship;
import com.example.backendfruitable.utils.Recursive;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryProductRepository categoryProductRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private Recursive recursive;

    @Autowired
    private ConvertRelationship convertRelationship;

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String minioBucketName;

    public BaseResponse<Page<ProductDTO>> getAllProduct(Pageable pageable) {
        BaseResponse<Page<ProductDTO>> baseResponse = new BaseResponse<>();
        try {
            Page<Product> productPage = productRepository.findAll(pageable);
            if (productPage.isEmpty() || productPage == null) {
                baseResponse.setMessage(Constant.EMPTY_ALL_PRODUCT);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            List<ProductDTO> productDTOList = new ArrayList<>();
            for (Product product : productPage) {
                ProductDTO productDTO = new ProductDTO();
                // Gán thông tin cơ bản của sản phẩm
                productDTO.setProductId(product.getProductId());
                productDTO.setListedPrice(product.getListedPrice());
                productDTO.setOutstanding(product.getOutstanding());
                productDTO.setProductCode(product.getProductCode());
                productDTO.setProductDescription(product.getProductDescription());
                productDTO.setProductDetail(product.getProductDetail());
                productDTO.setProductName(product.getProductName());
                productDTO.setProductPrice(product.getProductPrice());
                productDTO.setCreatedAt(product.getCreatedAt());
                productDTO.setStock(convertRelationship.convertToStockDTO(product.getStock()));
                productDTO.setUser(convertRelationship.convertToUserDTO(product.getUser()));
                productDTO.setCategoryProduct(convertRelationship.convertToCategoryProductDTO(product.getCategoryProduct()));

                // Xử lý hình ảnh sản phẩm
                List<ImageDTO> imageDTOList = new ArrayList<>();
                for (ImageProduct imageProduct : product.getImageList()) {
                    ImageDTO imageDTO = new ImageDTO();
                    String object = imageProduct.getImageProduct();
                    String imageUrl = minioClient.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(minioBucketName).object(object).build()
                    );
                    imageDTO.setImageUrl(imageUrl);
                    imageDTO.setImageProduct(object);
                    imageDTOList.add(imageDTO);
                }
                productDTO.setImageList(imageDTOList);

                productDTOList.add(productDTO);
            }
            Page<ProductDTO> productDTOPage = new PageImpl<>(productDTOList, pageable, productPage.getTotalElements());
            baseResponse.setData(productDTOPage);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_PRODUCT + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }



    public BaseResponse<ProductDTO> getProductById(Long id) {
        BaseResponse<ProductDTO> baseResponse = new BaseResponse<>();
        try {
            Product product = productRepository.getProductById(id);
            if (product == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_PRODUCT_BY_ID + id);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductId(product.getProductId());
            productDTO.setListedPrice(product.getListedPrice());
            productDTO.setOutstanding(product.getOutstanding());
            productDTO.setProductCode(product.getProductCode());
            productDTO.setProductDescription(product.getProductDescription());
            productDTO.setProductDetail(product.getProductDetail());
            productDTO.setProductName(product.getProductName());
            productDTO.setProductPrice(product.getProductPrice());
            productDTO.setCreatedAt(product.getCreatedAt());
            productDTO.setImageList(convertRelationship.convertToImageDTOList(product.getImageList()));
            productDTO.setStock(convertRelationship.convertToStockDTO(product.getStock()));
            productDTO.setUser(convertRelationship.convertToUserDTO(product.getUser()));
            productDTO.setCategoryProduct(convertRelationship.convertToCategoryProductDTO(product.getCategoryProduct()));

            //xử lý hình ảnh
            List<ImageProduct> imageProductList = product.getImageList();
            List<ImageDTO> imageDTOList = new ArrayList<>();
            for (ImageProduct imageProduct : imageProductList) {
                ImageDTO imageDTO = new ImageDTO();
                // lấy url hình ảnh
                String object = imageProduct.getImageProduct();
                String imageUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(minioBucketName).object(object).build()
                );
                imageDTO.setImageUrl(imageUrl);
                imageDTO.setImageProduct(object);
                imageDTOList.add(imageDTO);
            }
            productDTO.setImageList(imageDTOList);

            baseResponse.setData(productDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_PRODUCT + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


    public BaseResponse<List<ProductDTO>> getProductByCategoryProductId(Long categoryId){
            BaseResponse<List<ProductDTO>> baseResponse = new BaseResponse<>();
            List<ProductDTO> productDTOList = new ArrayList<>();
            try{
                // lấy list categoryProduct nếu đó là danh mục cha và có nhiều danh mục con
                List<CategoryProduct> categoryProductList = recursive.getAllChildrenCategoryProduct(categoryId);

                if(categoryProductList == null || categoryProductList.isEmpty()){
                    baseResponse.setMessage(Constant.EMPTY_CATEGORY_PRODUCT_BY_ID + categoryId);
                    baseResponse.setCode(Constant.NOT_FOUND_CODE);
                    return baseResponse;
                }

                //sau khi lấy được tất cả các menu con của menu cha nếu có
                List<Long>  listCategoryId = new ArrayList<>();
                for(CategoryProduct categoryProduct : categoryProductList){
                    listCategoryId.add(categoryProduct.getCategoryId());
                }
                // query tất cả sản phẩm thuộc tất cả danh mục
                List<Product> productList = productRepository.getListProductByCategoryProductId(listCategoryId);
                if(productList == null || productList.isEmpty()){
                    baseResponse.setMessage(Constant.EMPTY_ALL_PRODUCT + Constant.WITH_CATEGORY + categoryId);
                    baseResponse.setCode(Constant.NOT_FOUND_CODE);
                    return baseResponse;
                }

                for(Product product : productList){
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setProductId(product.getProductId());
                    productDTO.setListedPrice(product.getListedPrice());
                    productDTO.setOutstanding(product.getOutstanding());
                    productDTO.setProductCode(product.getProductCode());
                    productDTO.setProductDescription(product.getProductDescription());
                    productDTO.setProductDetail(product.getProductDetail());
                    productDTO.setProductName(product.getProductName());
                    productDTO.setProductPrice(product.getProductPrice());
                    productDTO.setCreatedAt(product.getCreatedAt());
                    productDTO.setImageList(convertRelationship.convertToImageDTOList(product.getImageList()));
                    productDTO.setStock(convertRelationship.convertToStockDTO(product.getStock()));
                    productDTO.setUser(convertRelationship.convertToUserDTO(product.getUser()));
                    productDTO.setCategoryProduct(convertRelationship.convertToCategoryProductDTO(product.getCategoryProduct()));

                    productDTOList.add(productDTO);
                }

                baseResponse.setData(productDTOList);
                baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
                baseResponse.setCode(Constant.SUCCESS_CODE);
            }catch (Exception e){
                baseResponse.setMessage(Constant.ERROR_TO_GET_PRODUCT + e.getMessage());
                baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
            }
            return baseResponse;
    }

    public BaseResponse<ProductDTO> addProduct(ProductDTO productDTO, Long categoryProductId, Long userId) {
        BaseResponse<ProductDTO> baseResponse = new BaseResponse<>();

        try {
            CategoryProduct categoryProduct = categoryProductRepository.getCategoryProductById(categoryProductId);
            User user = userRepository.getUserById(userId);
            if (categoryProduct == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_CATEGORY_PRODUCT_BY_ID + categoryProductId + " " + "Nên không thể thêm sản phẩm");
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            if (user == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + userId + " " + "Nên không thể thêm sản phẩm");
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            Product product = new Product();
            product.setProductName(productDTO.getProductName());
            product.setProductCode(productDTO.getProductCode());
            product.setListedPrice(productDTO.getListedPrice());
            product.setProductPrice(productDTO.getProductPrice());
            product.setProductDetail(productDTO.getProductDetail());
            product.setProductDescription(productDTO.getProductDescription());
            product.setOutstanding(productDTO.getOutstanding());
            product.setCreatedAt(LocalDate.now());


            //add mối quan hệ
            product.setCategoryProduct(categoryProduct);
            product.setUser(user);


            //add bên image
            List<ImageDTO> imageDTOList = productDTO.getImageList();
            List<ImageProduct> imageProductList = new ArrayList<>();
            for (ImageDTO imageDTO : imageDTOList) {
                byte[] imageBytes = java.util.Base64.getDecoder().decode(Base64.getEncoder().encodeToString(imageDTO.getData()));
                InputStream inputStream = new ByteArrayInputStream(imageBytes);
                String objectName = "product_" + System.currentTimeMillis() + ".jpg"; // Tên của file ảnh trong MinIO

                // Lưu ảnh vào MinIO
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(minioBucketName)
                                .object(objectName)
                                .stream(inputStream, imageBytes.length, -1)
                                .contentType("image/jpeg")
                                .build()
                );
                ImageProduct imageProduct = new ImageProduct();
                imageProduct.setImageProduct(objectName);
                imageProduct.setProduct(product);
                imageProductList.add(imageProduct);
            }

            //add bên stock
            Stock stock = convertRelationship.convertToStock(productDTO.getStock());
            stock.setProduct(product);

            //add vào csdl
            productRepository.save(product);
            imageRepository.saveAll(imageProductList);
            stockRepository.save(stock);


            baseResponse.setData(productDTO);
            baseResponse.setMessage(Constant.SUCCESS_ADD_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_ADD_PRODUCT + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


    public BaseResponse<ProductDTO> updateProduct(Long productId, ProductDTO productDTO, Long categoryProductId, Long userId) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            Product product = productRepository.getProductById(productId);
            CategoryProduct categoryProduct = categoryProductRepository.getCategoryProductById(categoryProductId);
            User user = userRepository.getUserById(userId);

            if (product == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_PRODUCT_BY_ID + productId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            if (categoryProduct == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_CATEGORY_PRODUCT_BY_ID + categoryProductId + " " + "Nên không thể sửa sản phẩm");
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            if (user == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + userId + " " + "Nên không thể sửa sản phẩm");
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            if (product == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_PRODUCT_BY_ID + productId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            product.setProductName(productDTO.getProductName());
            product.setProductCode(productDTO.getProductCode());
            product.setListedPrice(productDTO.getListedPrice());
            product.setProductPrice(productDTO.getProductPrice());
            product.setProductDetail(productDTO.getProductDetail());
            product.setProductDescription(productDTO.getProductDescription());
            product.setOutstanding(productDTO.getOutstanding());

            // set mối quan hệ bên product
            product.setCategoryProduct(categoryProduct);
            product.setUser(user);


            //update ảnh
            List<ImageProduct> imageProductExits = new ArrayList<>(product.getImageList());
            List<ImageDTO> imageDTOList = new ArrayList<>(productDTO.getImageList());
            for (ImageProduct imageProduct : imageProductExits) {
                for (ImageDTO imageDTO : imageDTOList) {
                    if (imageProduct.getImageProduct().equals(imageDTO.getImageProduct())) {
                        if ((imageDTO.getData().length > 0) || imageDTO.getData() != null) {
                            byte[] newImage = Base64.getDecoder().decode(Base64.getEncoder().encode(imageDTO.getData()));
                            InputStream inputStream = new ByteArrayInputStream(newImage);
                            String objectName = imageDTO.getImageProduct();

//                            xoá ảnh cũ đi
                            minioClient.removeObject(
                                    RemoveObjectArgs.builder().bucket(minioBucketName).object(objectName).build()
                            );

                            // thêm lại với ảnh mới
                            minioClient.putObject(
                                    PutObjectArgs.builder().bucket(minioBucketName)
                                            .stream(inputStream, newImage.length, -1)
                                            .object(objectName)
                                            .contentType("image/jpeg")
                                            .build()
                            );
                            imageProduct.setImageProduct(objectName);
                        }
                        break;
                    }
                }
            }

            Stock stockExists = product.getStock();
            StockDTO stockDTO = productDTO.getStock();

            if (stockExists.getStockId() != stockDTO.getStockId()) {
                baseResponse.setMessage(Constant.ERROR_STOCK_ID_COMPARE_PRODUCT_GET_STOCK_ID);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return  baseResponse;
            }
            stockExists.setQuantity(stockDTO.getQuantity());

            // update vào trong csdl
            productRepository.save(product);
            imageRepository.saveAll(imageProductExits);
            stockRepository.save(stockExists);

            baseResponse.setData(productDTO);
            baseResponse.setMessage(Constant.SUCCESS_UPDATE_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_UPDATE_PRODUCT + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
            e.printStackTrace();
        }

        return baseResponse;
    }

    public BaseResponse<ProductDTO> deleteById(Long productId) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            Product product = productRepository.getProductById(productId);
            if (product == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_PRODUCT_BY_ID + productId + " nên không thể xoá");
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            // xoá hình ảnh trong minio
            List<ImageProduct> imageProductList = product.getImageList();
            for(ImageProduct imageProduct : imageProductList){
                String object = imageProduct.getImageProduct();
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioBucketName).object(object).build());
            }

            productRepository.delete(product);
            baseResponse.setData(null);
            baseResponse.setMessage(Constant.DELETE_SUCCESS_PRODUCT_BY_ID + productId);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_DELETE_PRODUCT + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
            e.printStackTrace();
        }
        return baseResponse;
    }

}
