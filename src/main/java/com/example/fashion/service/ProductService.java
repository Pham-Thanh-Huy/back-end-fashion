package com.example.fashion.service;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.ImageDTO;
import com.example.fashion.DTO.InventoryDTO;
import com.example.fashion.DTO.ProductDTO;
import com.example.fashion.entity.*;
import com.example.fashion.repository.*;
import com.example.fashion.utils.Constant;
import com.example.fashion.utils.ConvertRelationship;
import com.example.fashion.utils.Recursive;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ImageRepository imageRepository;

    private final UserRepository userRepository;

    private final CategoryProductRepository categoryProductRepository;

    private final InventoryRepository inventoryRepository;

    private final ProductSizeRepository productSizeRepository;

    private final ProductColorRepository productColorRepository;

    private final Recursive recursive;

    private final ConvertRelationship convertRelationship;

    private final MinioClient minioClient;
    @Value("${minio.bucket}")
    private String minioBucketName;


    public BaseResponse<Page<ProductDTO>> getAllProduct(Pageable pageable) {
        BaseResponse<Page<ProductDTO>> baseResponse = new BaseResponse<>();
        try {
            Page<Product> productPage = productRepository.findAll(pageable);
            if (productPage.isEmpty()) {
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
                productDTO.setInventoryList(convertRelationship.convertToInventoryDTOList(product.getInventoryList()));
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
            productDTO.setInventoryList(convertRelationship.convertToInventoryDTOList(product.getInventoryList()));
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


    public BaseResponse<List<ProductDTO>> getProductByCategoryProductId(Long categoryId) {
        BaseResponse<List<ProductDTO>> baseResponse = new BaseResponse<>();
        List<ProductDTO> productDTOList = new ArrayList<>();
        try {
            // lấy list categoryProduct nếu đó là danh mục cha và có nhiều danh mục con
            List<CategoryProduct> categoryProductList = recursive.getAllChildrenCategoryProduct(categoryId);

            if (categoryProductList == null || categoryProductList.isEmpty()) {
                baseResponse.setMessage(Constant.EMPTY_CATEGORY_PRODUCT_BY_ID + categoryId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            //sau khi lấy được tất cả các menu con của menu cha nếu có
            List<Long> listCategoryId = new ArrayList<>();
            for (CategoryProduct categoryProduct : categoryProductList) {
                listCategoryId.add(categoryProduct.getCategoryId());
            }
            // query tất cả sản phẩm thuộc tất cả danh mục
            List<Product> productList = productRepository.getListProductByCategoryProductId(listCategoryId);
            if (productList == null || productList.isEmpty()) {
                baseResponse.setMessage(Constant.EMPTY_ALL_PRODUCT + Constant.WITH_CATEGORY + categoryId);
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
                productDTO.setInventoryList(convertRelationship.convertToInventoryDTOList(product.getInventoryList()));
                productDTO.setUser(convertRelationship.convertToUserDTO(product.getUser()));
                productDTO.setCategoryProduct(convertRelationship.convertToCategoryProductDTO(product.getCategoryProduct()));

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

            //add bên inventory

            if (productDTO.getInventoryList() == null || productDTO.getInventoryList().isEmpty()) {
                baseResponse.setMessage(Constant.INVENTORY_LIST_REQUIRED);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }

            List<Inventory> inventoryList = new ArrayList<>();

            for (InventoryDTO inventoryDTO : productDTO.getInventoryList()) {
                if (inventoryDTO.getQuantity() == null || inventoryDTO.getQuantity() < 0) {
                    baseResponse.setMessage(Constant.INVENTORY_QUANTITY_REQUIRED);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return baseResponse;
                }

                if (inventoryDTO.getProductSize() == null) {
                    baseResponse.setMessage(Constant.INVENTORY_PRODUCT_SIZE_REQUIRED);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return baseResponse;
                }

                if (inventoryDTO.getProductSize().getProductSizeId() == null || inventoryDTO.getProductSize().getProductSizeId() <= 0) {
                    baseResponse.setMessage(Constant.INVENTORY_PRODUCT_SIZE_ID_REQUIRED);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return baseResponse;
                }

                if (inventoryDTO.getProductColor() == null) {
                    baseResponse.setMessage(Constant.INVENTORY_PRODUCT_COLOR_REQUIRED);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return baseResponse;
                }

                if (inventoryDTO.getProductColor().getProductColorId() == null || inventoryDTO.getProductColor().getProductColorId() <= 0) {
                    baseResponse.setMessage(Constant.INVENTORY_PRODUCT_COLOR_ID_REQUIRED);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return baseResponse;
                }

                int productSizeId = inventoryDTO.getProductSize().getProductSizeId();
                int productColorId = inventoryDTO.getProductColor().getProductColorId();
                ProductSize productSize = productSizeRepository.findProductSizeByProductSizeId(productSizeId);
                ProductColor productColor = productColorRepository.findProductColorByProductColorId(productColorId);
                if (productSize == null) {
                    baseResponse.setMessage(Constant.EMPTY_PRODUCT_SIZE_BY_ID + productSizeId);
                    baseResponse.setCode(Constant.NOT_FOUND_CODE);
                    return baseResponse;
                }
                if (productColor == null) {
                    baseResponse.setMessage(Constant.EMPTY_PRODUCT_COLOR_BY_ID + productColorId);
                    baseResponse.setCode(Constant.NOT_FOUND_CODE);
                    return baseResponse;
                }
//                nếu qua tất cả điều kiện validate thì bắt đầu chuyển sang entity từ dto
                Inventory inventory = new Inventory();
                inventory.setInventoryId(inventoryDTO.getInventoryId());
                inventory.setQuantity(inventoryDTO.getQuantity());
                inventory.setQuantity(inventoryDTO.getQuantity());
                inventory.setProductSize(productSize);
                inventory.setProductColor(productColor);
                inventory.setProduct(product);
                inventoryList.add(inventory);
                // chỉ cho add 1 lần nên ta sẽ break luôn
                break;
            }


            //add vào csdl
            productRepository.save(product);
            imageRepository.saveAll(imageProductList);
            inventoryRepository.saveAll(inventoryList);


            baseResponse.setData(productDTO);
            baseResponse.setMessage(Constant.SUCCESS_ADD_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_ADD_PRODUCT + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<Page<ProductDTO>> getProductOutstanding(Boolean outstanding, Pageable pageable) {
        BaseResponse<Page<ProductDTO>> baseResponse = new BaseResponse<>();
        try {
            Page<Product> productPage = productRepository.getListProductByOutstanding(outstanding, pageable);
            if (ObjectUtils.isEmpty(productPage)) {
                baseResponse.setMessage(Constant.EMPTY_ALL_PRODUCT);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            List<ProductDTO> productDTOList = productPage.stream()
                    .map(product -> {
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
                        productDTO.setInventoryList(convertRelationship.convertToInventoryDTOList(product.getInventoryList()));
                        productDTO.setUser(convertRelationship.convertToUserDTO(product.getUser()));
                        productDTO.setCategoryProduct(convertRelationship.convertToCategoryProductDTO(product.getCategoryProduct()));
                        return productDTO;
                    }).collect(Collectors.toList());
            Page<ProductDTO> productDTOPage = new PageImpl<>(productDTOList, pageable, productPage.getTotalPages());
                baseResponse.setData(productDTOPage);
                baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
                baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_PRODUCT + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<ObjectNode> addInventoryProduct(Long productId, JsonNode jsonNode) {
        BaseResponse<ObjectNode> baseResponse = new BaseResponse<>();
        try {
            Product product = productRepository.getProductById(productId);
            if (product == null) {
                baseResponse.setMessage(Constant.EMPTY_PRODUCT_BY_ID + productId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            Long quantity;
            JsonNode quantityJsonNode = jsonNode.get("quantity");
            if (ObjectUtils.isEmpty(quantityJsonNode)) {
                baseResponse.setMessage(Constant.INVENTORY_QUANTITY_REQUIRED);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            } else {
                quantity = quantityJsonNode.asLong();
            }
            Integer productSizeId;
            Integer productColorId;
            JsonNode productSizeIdJsonNode = jsonNode.get("productSizeId");
            JsonNode productColorIdJsonNode = jsonNode.get("productColorId");
            if (ObjectUtils.isEmpty(productSizeIdJsonNode)) {
                baseResponse.setMessage(Constant.INVENTORY_PRODUCT_SIZE_ID_REQUIRED);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            } else {
                productSizeId = productSizeIdJsonNode.asInt();
            }

            if (ObjectUtils.isEmpty(productColorIdJsonNode)) {
                baseResponse.setMessage(Constant.INVENTORY_PRODUCT_COLOR_ID_REQUIRED);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            } else {
                productColorId = productColorIdJsonNode.asInt();
            }

            // check xem  màu + size + số lượng sản phẩm đã tồn tại chưa nếu tồn tại rồi thì không cho thêm;
            for (Inventory inventory : product.getInventoryList()) {
                if (inventory.getProductColor().getProductColorId().equals(productColorId) && inventory.getProductSize().getProductSizeId().equals(productSizeId)) {
                    baseResponse.setMessage(Constant.PRODUCT_COLOR_ID_AND_PRODUCT_SIZE_ID_EXISTS_QUANTITY_IN_PRODUCT);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return baseResponse;
                }
            }
            ProductSize productSize = productSizeRepository.findProductSizeByProductSizeId(productSizeId);
            ProductColor productColor = productColorRepository.findProductColorByProductColorId(productColorId);
            if (productSize == null) {
                baseResponse.setMessage(Constant.EMPTY_PRODUCT_SIZE_BY_ID + productSizeId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            if (productColor == null) {
                baseResponse.setMessage(Constant.EMPTY_PRODUCT_COLOR_BY_ID + productColorId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            List<Inventory> inventoryList = new ArrayList<>(product.getInventoryList());
            Inventory inventory = new Inventory();
            inventory.setQuantity(quantity);
            inventory.setProductColor(productColor);
            inventory.setProductSize(productSize);
            inventory.setProduct(product);
            inventoryList.add(inventory);

            inventoryRepository.saveAll(inventoryList);

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("productName", product.getProductName());
            objectNode.put("quantity", quantity);
            objectNode.put("colorName", productColor.getColorName());
            objectNode.put("sizeName", productSize.getSizeName());

            baseResponse.setData(objectNode);
            baseResponse.setMessage(Constant.SUCCESS_ADD_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_ADD_INVENTORY_IN_PRODUCT + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


    public BaseResponse<ProductDTO> updateProduct(Long productId, ProductDTO productDTO, Long categoryProductId, Long userId) {
        BaseResponse<ProductDTO> baseResponse = new BaseResponse<>();
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
                        if (imageDTO.getData() != null && imageDTO.getData().length > 0) {
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


            // update vào trong csdl
            productRepository.save(product);
            imageRepository.saveAll(imageProductExits);


            baseResponse.setData(productDTO);
            baseResponse.setMessage(Constant.SUCCESS_UPDATE_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_UPDATE_PRODUCT + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }

        return baseResponse;
    }

    public BaseResponse<ObjectNode> updateQuantityInventoryProduct(Long productId, JsonNode jsonNode) {
        BaseResponse<ObjectNode> baseResponse = new BaseResponse<>();
        try {
            Product product = productRepository.getProductById(productId);
            if (product == null) {
                baseResponse.setMessage(Constant.EMPTY_PRODUCT_BY_ID + productId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            JsonNode inventoryListJsonNode = jsonNode.get("inventoryList");
            if (ObjectUtils.isEmpty(inventoryListJsonNode)) {
                baseResponse.setMessage(Constant.INVENTORY_LIST_REQUIRED);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }
            List<Inventory> inventoryList = new ArrayList<>(product.getInventoryList());
            for (JsonNode inventoryJsonNode : inventoryListJsonNode) {
                Long quantity;
                Integer productSizeId;
                Integer productColorId;
                JsonNode quantityJsonNode = inventoryJsonNode.get("quantity");
                JsonNode productSizeIdJsonNode = inventoryJsonNode.get("productSizeId");
                JsonNode productColorIdJsonNode = inventoryJsonNode.get("productColorId");
                if (ObjectUtils.isEmpty(quantityJsonNode)) {
                    baseResponse.setMessage(Constant.INVENTORY_QUANTITY_REQUIRED);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return baseResponse;
                } else {
                    quantity = quantityJsonNode.asLong();
                }

                if (ObjectUtils.isEmpty(productSizeIdJsonNode)) {
                    baseResponse.setMessage(Constant.INVENTORY_PRODUCT_SIZE_ID_REQUIRED);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return baseResponse;
                } else {
                    productSizeId = productSizeIdJsonNode.asInt();
                }

                if (ObjectUtils.isEmpty(productColorIdJsonNode)) {
                    baseResponse.setMessage(Constant.INVENTORY_PRODUCT_COLOR_ID_REQUIRED);
                    baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                    return baseResponse;
                } else {
                    productColorId = productColorIdJsonNode.asInt();
                }

                // tiếp tục kiểm tra
                for (Inventory inventory : inventoryList) {
                    if (inventory.getProductSize().getProductSizeId().equals(productSizeId) && inventory.getProductColor().getProductColorId().equals(productColorId)) {
                        inventory.setQuantity(quantity);
                    }
                }

            }
            List<Inventory> inventoryAddList = inventoryRepository.saveAll(inventoryList);
            List<InventoryDTO> inventoryDTOList = convertRelationship.convertToInventoryDTOList(inventoryAddList);

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("productName", product.getProductName());
            objectNode.putPOJO("inventoryList", inventoryDTOList);

            baseResponse.setData(objectNode);
            baseResponse.setMessage(Constant.SUCCESS_UPDATE_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_UPDATE_INVENTORY_IN_PRODUCT);
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<ProductDTO> deleteById(Long productId) {
        BaseResponse<ProductDTO> baseResponse = new BaseResponse<>();
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
            for (ImageProduct imageProduct : imageProductList) {
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
        }
        return baseResponse;
    }

}
