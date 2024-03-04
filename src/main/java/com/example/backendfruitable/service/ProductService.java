package com.example.backendfruitable.service;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.ImageDTO;
import com.example.backendfruitable.DTO.ProductDTO;
import com.example.backendfruitable.DTO.StockDTO;
import com.example.backendfruitable.Repository.*;
import com.example.backendfruitable.entity.*;
import com.example.backendfruitable.utils.Constant;
import com.example.backendfruitable.utils.ConvertRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
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
    private ConvertRelationship convertRelationship;


    public BaseResponse<List<ProductDTO>> getAllProduct() {
        BaseResponse<List<ProductDTO>> baseResponse = new BaseResponse<>();
        try {
            List<Product> productList = productRepository.findAll();
            List<ProductDTO> productDTOList = new ArrayList<>();

            if (productList == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_ALL_PRODUCT);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            for (Product product : productList) {
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

                //add vào list
                productDTOList.add(productDTO);
            }

            baseResponse.setData(productDTOList);
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

            baseResponse.setData(productDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
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
                baseResponse.setMessage(Constant.EMPTY_CATEGORY_PRODUCT_BY_ID + categoryProductId + "Nên không thể thêm sản phẩm");
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            if (user == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + userId + "Nên không thể thêm sản phẩm");
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
            //add csdl
            Product newProduct = productRepository.save(product);

            // add bên image
            List<ImageProduct> imageProductList = convertRelationship.convertToImageList(productDTO.getImageList());

            for (ImageProduct imageProduct : imageProductList) {
                imageProduct.setProduct(newProduct);
            }
            imageRepository.saveAll(imageProductList);

            //add bên stock
            Stock stock = convertRelationship.convertToStock(productDTO.getStock());
            stock.setProduct(newProduct);
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
                baseResponse.setMessage(Constant.EMPTY_CATEGORY_PRODUCT_BY_ID + categoryProductId + "Nên không thể thêm sản phẩm");
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            if (user == null) {
                baseResponse.setData(null);
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + userId + "Nên không thể thêm sản phẩm");
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

            productRepository.save(product);
            //set mối quan hệ bên image
            List<ImageProduct> imageProductExits = new ArrayList<>(product.getImageList());
            List<ImageDTO> imageDTOList = new ArrayList<>(productDTO.getImageList());

            for (ImageProduct image : imageProductExits) {
                for (ImageDTO imageDtoNow : imageDTOList) {
                    if (image.getImageId() == imageDtoNow.getImageId()) {
                        image.setData(imageDtoNow.getData());
                        break;
                    }
                }
            }

            imageRepository.saveAll(imageProductExits);


            Stock stockExists = product.getStock();
            StockDTO stockDTO = productDTO.getStock();

            if (stockExists.getStockId() == stockDTO.getStockId()) {
                stockExists.setQuantity(stockDTO.getQuantity());
            }
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
