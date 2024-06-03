package com.example.backendfruitable.service;


import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.ProductColorDTO;
import com.example.backendfruitable.entity.ProductColor;
import com.example.backendfruitable.repository.ProductColorRepository;
import com.example.backendfruitable.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductColorService {

    private final ProductColorRepository productColorRepository;

    public BaseResponse<List<ProductColorDTO>> getAllProductColor(Pageable pageable) {
        BaseResponse<List<ProductColorDTO>> baseResponse = new BaseResponse<>();
        try {
            Page<ProductColor> productColorPage = productColorRepository.findAll(pageable);
            if (ObjectUtils.isEmpty(productColorPage)) {
                baseResponse.setMessage(Constant.EMPTY_ALL_PRODUCT_COLOR);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            List<ProductColorDTO> productColorDTOList = productColorPage.stream()
                    .map(productColor -> {
                        ProductColorDTO productColorDTO = new ProductColorDTO();
                        productColorDTO.setProductColorId(productColor.getProductColorId());
                        productColorDTO.setColorName(productColor.getColorName());
                        return productColorDTO;
                    })
                    .collect(Collectors.toList());

            baseResponse.setData(productColorDTOList);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_PRODUCT_COLOR + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }


    public BaseResponse<ProductColorDTO> getProductColorById(Integer productColorId) {
        BaseResponse<ProductColorDTO> baseResponse = new BaseResponse<>();
        try {
            Optional<ProductColor> productColorOptional = productColorRepository.findById(productColorId);
            if (!productColorOptional.isPresent()) {
                baseResponse.setMessage(Constant.EMPTY_PRODUCT_COLOR_BY_ID + productColorId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            ProductColorDTO productColorDTO = productColorOptional.map(productColor -> {
                ProductColorDTO productColorDTOMap = new ProductColorDTO();
                productColorDTOMap.setProductColorId(productColor.getProductColorId());
                productColorDTOMap.setColorName(productColor.getColorName());
                return productColorDTOMap;
            }).orElse(null);

            baseResponse.setData(productColorDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_PRODUCT_COLOR + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

}
