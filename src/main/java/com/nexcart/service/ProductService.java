package com.nexcart.service;

import com.nexcart.Enum.ProductCategory;
import com.nexcart.dto.request.ProductRequestDto;
import com.nexcart.dto.response.ProductResponseDto;
import com.nexcart.exception.SellerNotFoundException;
import com.nexcart.model.Product;
import com.nexcart.model.Seller;
import com.nexcart.repository.ProductRepository;
import com.nexcart.repository.SellerRepository;
import com.nexcart.transformer.ProductTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SellerRepository sellerRepository;

    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {

        Seller seller = sellerRepository.findByEmailId(productRequestDto.getSellerEmail());

        if(seller == null){
            throw new SellerNotFoundException("Seller doesn't exist");
        }

        Product product = ProductTransformer.ProductRequestDtoToProduct(productRequestDto);
        product.setSeller(seller);
        seller.getProducts().add(product);

        Seller savedSeller = sellerRepository.save(seller);

        List<Product> productList = savedSeller.getProducts();
        Product latestProduct = productList.get(productList.size()-1);

        return ProductTransformer.ProductToProductResponse(latestProduct);

    }

    public List<ProductResponseDto> getProdByCategoryAndPriceGreaterThan(int price, ProductCategory productCategory) {

        List<Product> products = productRepository.getProdByCategoryAndPriceGreaterThan(price,productCategory);
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();

        for (Product product : products){
            productResponseDtos.add(ProductTransformer.ProductToProductResponse(product));
        }

        return productResponseDtos;
    }
}
