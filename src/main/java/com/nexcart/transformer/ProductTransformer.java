package com.nexcart.transformer;

import com.nexcart.Enum.ProductStatus;
import com.nexcart.dto.request.ProductRequestDto;
import com.nexcart.dto.response.ProductResponseDto;
import com.nexcart.model.Product;

public class ProductTransformer {

    public static Product ProductRequestDtoToProduct(ProductRequestDto productRequestDto){
        return Product.builder()
                .productName(productRequestDto.getProductName())
                .price(productRequestDto.getPrice())
                .productStatus(ProductStatus.AVAILABLE)
                .category(productRequestDto.getCategory())
                .availableQuantity(productRequestDto.getAvailableQuantity())
                .build();
    }

    public static ProductResponseDto ProductToProductResponse(Product product){
        return ProductResponseDto.builder()
                .sellerName(product.getSeller().getName())
                .productName(product.getProductName())
                .price(product.getPrice())
                .availableQuantity(product.getAvailableQuantity())
                .category(product.getCategory())
                .productStatus(product.getProductStatus())
                .build();
    }
}
