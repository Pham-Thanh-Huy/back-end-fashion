package com.example.fashion.service;


import com.example.fashion.DTO.BaseResponse;
import com.example.fashion.DTO.ProductColorDTO;
import com.example.fashion.entity.ProductColor;
import com.example.fashion.repository.ProductColorRepository;
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
public class ProductColorService {

    private final ProductColorRepository productColorRepository;

    public BaseResponse<Page<ProductColorDTO>> getAllProductColor(Pageable pageable) {
        BaseResponse<Page<ProductColorDTO>> baseResponse = new BaseResponse<>();
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

            Page<ProductColorDTO> productColorDTOPage = new PageImpl<>(productColorDTOList, pageable, productColorPage.getTotalElements());


            baseResponse.setData(productColorDTOPage);
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

    public BaseResponse<ProductColorDTO> addProductColor(ProductColorDTO productColorDTO) {
        BaseResponse<ProductColorDTO> baseResponse = new BaseResponse<>();
        try {
          ProductColor productColor = new ProductColor();
          productColor.setColorName(productColorDTO.getColorName());

          productColorRepository.save(productColor);

          baseResponse.setData(productColorDTO);
          baseResponse.setMessage(Constant.SUCCESS_ADD_MESSAGE);
          baseResponse.setCode(Constant.SUCCESS_CODE);

        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_ADD_PRODUCT_COLOR + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

}
