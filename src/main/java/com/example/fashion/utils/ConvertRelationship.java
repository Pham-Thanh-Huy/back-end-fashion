package com.example.fashion.utils;

import com.example.fashion.DTO.*;
import com.example.fashion.entity.*;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<InventoryDTO> convertToInventoryDTOList(List<Inventory> inventoryList) {
        List<InventoryDTO> inventoryDTOList = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            InventoryDTO inventoryDTO = new InventoryDTO();
            inventoryDTO.setInventoryId(inventory.getInventoryId());
            inventoryDTO.setQuantity(inventory.getQuantity());
            inventoryDTO.setProductColor(convertToProductColorDTO(inventory.getProductColor()));
            inventoryDTO.setProductSize(convertToProductSizeDTO(inventory.getProductSize()));
            inventoryDTOList.add(inventoryDTO);
        }
        return inventoryDTOList;
    }

    public List<Inventory> convertToInventoryList(List<InventoryDTO> inventoryDTOList) {
        List<Inventory> inventoryList = new ArrayList<>();
        for (InventoryDTO inventoryDTO : inventoryDTOList) {
            Inventory inventory = new Inventory();
            inventory.setInventoryId(inventoryDTO.getInventoryId());
            inventory.setQuantity(inventoryDTO.getQuantity());
            inventory.setQuantity(inventoryDTO.getQuantity());
            inventory.setProductColor(convertToProductColor(inventoryDTO.getProductColor()));
            inventory.setProductSize(convertToProductSize(inventoryDTO.getProductSize()));
            inventoryList.add(inventory);
        }
        return inventoryList;
    }

    public ProductColorDTO convertToProductColorDTO(ProductColor productColor) {
        ProductColorDTO productColorDTO = new ProductColorDTO();
        productColorDTO.setProductColorId(productColor.getProductColorId());
        productColorDTO.setColorName(productColor.getColorName());
        return productColorDTO;
    }

    public ProductSizeDTO convertToProductSizeDTO(ProductSize productSize) {
        ProductSizeDTO productSizeDTO = new ProductSizeDTO();
        productSizeDTO.setProductSizeId(productSize.getProductSizeId());
        productSizeDTO.setSizeName(productSize.getSizeName());
        return productSizeDTO;
    }

    public ProductColor convertToProductColor(ProductColorDTO productColorDTO) {
        ProductColor productColor = new ProductColor();
        productColor.setProductColorId(productColorDTO.getProductColorId());
        productColor.setColorName(productColorDTO.getColorName());
        return productColor;
    }

    public ProductSize convertToProductSize(ProductSizeDTO productSizeDTO) {
        ProductSize productSize = new ProductSize();
        productSize.setProductSizeId(productSizeDTO.getProductSizeId());
        productSize.setSizeName(productSizeDTO.getSizeName());
        return productSize;
    }

    public UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastname());
        userDTO.setAge(user.getAge());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        // lấy ảnh từ minio
        try {
            String object = user.getUserImage();
            String imageUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(object).build()
            );
            userDTO.setUserImage(object);
            userDTO.setImageUrl(imageUrl);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return userDTO;
    }


    public List<ImageProduct> convertToImageList(List<ImageDTO> imageDTOList) {
        List<ImageProduct> imageProductList = new ArrayList<>();
        for (ImageDTO imageDTO : imageDTOList) {
            ImageProduct imageProduct = new ImageProduct();
            imageProduct.setImageProduct(imageDTO.getImageProduct());
            imageProductList.add(imageProduct);
        }
        return imageProductList;
    }

    public Product convertToProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductId(productDTO.getProductId());
        product.setProductName(productDTO.getProductName());
        product.setProductCode(productDTO.getProductCode());
        product.setListedPrice(productDTO.getListedPrice());
        product.setProductPrice(productDTO.getProductPrice());
        product.setProductDetail(productDTO.getProductDetail());
        product.setProductDescription(productDTO.getProductDescription());
        product.setOutstanding(productDTO.getOutstanding());
        product.setCreatedAt(productDTO.getCreatedAt());
        return product;
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
        List<ImageDTO> imageList = product.getImageList()
                .stream()
                .map(imageProduct -> {
                    ImageDTO imageDTO = new ImageDTO();
                    try {
                        String imageUrl = minioClient.getPresignedObjectUrl(
                                GetPresignedObjectUrlArgs
                                        .builder()
                                        .method(Method.GET)
                                        .bucket(bucketName)
                                        .object(imageProduct.getImageProduct())
                                        .build()

                        );
                        imageDTO.setImageProduct(imageProduct.getImageProduct());
                        imageDTO.setImageUrl(imageUrl);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                    return imageDTO;
                })
                .collect(Collectors.toList());
        productDTO.setImageList(imageList);


        return productDTO;
    }


    public List<AuthorizeDTO> converToAuthorizeDTOList(List<Authorize> authorizeList) {
        List<AuthorizeDTO> authorizeDTOList = new ArrayList<>();
        for (Authorize authorize : authorizeList) {
            AuthorizeDTO authorizeDTO = new AuthorizeDTO();
            authorizeDTO.setAuthorizeId(authorize.getAuthorizeId());
            authorizeDTO.setAuthorizeName(authorize.getAuthorizeName());
            authorizeDTOList.add(authorizeDTO);
        }
        return authorizeDTOList;
    }


    public CategoryProductDTO convertToCategoryProductDTO(CategoryProduct categoryProduct) {
        CategoryProductDTO categoryProductDTO = new CategoryProductDTO();
        categoryProductDTO.setCategoryId(categoryProduct.getCategoryId());
        categoryProductDTO.setCategoryName(categoryProduct.getCategoryName());
        categoryProductDTO.setParentId(categoryProduct.getParentId());
        return categoryProductDTO;
    }

    public CategoryPostDTO convertToCategoryPostDTO(CategoryPost categoryPost) {
        CategoryPostDTO categoryPostDTO = new CategoryPostDTO();
        categoryPostDTO.setCategoryId(categoryPost.getCategoryId());
        categoryPostDTO.setCategoryName(categoryPost.getCategoryName());
        categoryPostDTO.setParentId(categoryPost.getParentId());
        return categoryPostDTO;
    }

    public DeliveryMethodDTO convertToDeliveryMethodDTO(DeliveryMethod deliveryMethod) {
        DeliveryMethodDTO deliveryMethodDTO = new DeliveryMethodDTO();
        deliveryMethodDTO.setDeliveryId(deliveryMethod.getDeliveryId());
        deliveryMethodDTO.setName(deliveryMethod.getName());
        deliveryMethodDTO.setDeliveryCost(deliveryMethod.getDeliveryCost());
        deliveryMethodDTO.setDescription(deliveryMethod.getDescription());
        return deliveryMethodDTO;
    }

    public PaymentMethodDTO convertToPaymentMethodDTO(PaymentMethod paymentMethod) {
        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
        paymentMethodDTO.setPaymentId(paymentMethod.getPaymentId());
        paymentMethodDTO.setPaymentName(paymentMethod.getPaymentName());
        paymentMethodDTO.setPaymentCost(paymentMethod.getPaymentCost());
        paymentMethodDTO.setDescription(paymentMethod.getDescription());
        return paymentMethodDTO;
    }

}
