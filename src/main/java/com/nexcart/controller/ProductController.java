package com.nexcart.controller;

import com.nexcart.Enum.ProductCategory;
import com.nexcart.dto.request.ProductRequestDto;
import com.nexcart.dto.response.ProductResponseDto;
import com.nexcart.exception.SellerNotFoundException;
import com.nexcart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/add")
    public ResponseEntity addProduct(@RequestBody ProductRequestDto productRequestDto){
        try{
            ProductResponseDto response = productService.addProduct(productRequestDto);
            return new ResponseEntity(response,HttpStatus.CREATED);
        }
        catch (SellerNotFoundException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get_by_category_and_price_greater_than")
    public ResponseEntity getProdByCategoryAndPriceGreaterThan(@RequestParam("category") ProductCategory productCategory,
                                                               @RequestParam("price") int price){
        List<ProductResponseDto> productResponseDtoList =
                productService.getProdByCategoryAndPriceGreaterThan(price,productCategory);
        return new ResponseEntity(productResponseDtoList,HttpStatus.FOUND);
    }

}
