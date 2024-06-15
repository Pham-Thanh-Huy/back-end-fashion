package com.example.fashion.service;

import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.ProductSizeDTO;
import com.example.fashion.entity.ProductSize;
import com.example.fashion.repository.ProductSizeRepository;
import com.example.fashion.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSizeService {
    private final ProductSizeRepository productSizeRepository;

    public BaseResponse<Page<ProductSizeDTO>> getAllProductSizes(Pageable pageable) {
        BaseResponse<Page<ProductSizeDTO>> baseResponse = new BaseResponse<>();
        try {
            Page<ProductSize> productSizePage = productSizeRepository.findAll(pageable);
            if (ObjectUtils.isEmpty(productSizePage)) {
                baseResponse.setMessage(Constant.EMPTY_ALL_PRODUCT_SIZE);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            List<ProductSizeDTO> productSizeDTOList = productSizePage.stream()
                    .map(productSize -> {
                                ProductSizeDTO productSizeDTO = new ProductSizeDTO();
                                productSizeDTO.setProductSizeId(productSize.getProductSizeId());
                                productSizeDTO.setSizeName(productSize.getSizeName());
                                return productSizeDTO;
                            }
                    )
                    .collect(Collectors.toList());

            Page<ProductSizeDTO> productSizeDTOPage = new PageImpl<>(productSizeDTOList, pageable, productSizePage.getTotalElements());

            baseResponse.setData(productSizeDTOPage);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_PRODUCT_SIZE);
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<ProductSizeDTO> getProductSizeById(Integer productSizeId) {
        BaseResponse<ProductSizeDTO> baseResponse = new BaseResponse<>();
        try {
            Optional<ProductSize> productSizeOptional = productSizeRepository.findById(productSizeId);
            if (!productSizeOptional.isPresent()) {
                baseResponse.setMessage(Constant.ERROR_TO_GET_PRODUCT_SIZE);
                baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
                return baseResponse;
            }
            ProductSizeDTO productSizeDTO = productSizeOptional.map(productSize -> {
                ProductSizeDTO dto = new ProductSizeDTO();
                dto.setProductSizeId(productSize.getProductSizeId());
                dto.setSizeName(productSize.getSizeName());
                return dto;
            }).orElse(null);

            baseResponse.setData(productSizeDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_PRODUCT_SIZE);
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<ProductSizeDTO> createProductSize(ProductSizeDTO productSizeDTO) {
        BaseResponse<ProductSizeDTO> baseResponse = new BaseResponse<>();
        try {
            ProductSize productSize = new ProductSize();
            productSize.setSizeName(productSizeDTO.getSizeName());
            productSizeRepository.save(productSize);

            baseResponse.setData(productSizeDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_ADD_PRODUCT_SIZE + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


}
