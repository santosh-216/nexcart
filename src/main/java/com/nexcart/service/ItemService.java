package com.nexcart.service;

import com.nexcart.dto.request.ItemRequestDto;
import com.nexcart.exception.CustomerNotFoundException;
import com.nexcart.exception.InsufficientQuantityException;
import com.nexcart.exception.ProductNotFoundException;
import com.nexcart.model.Customer;
import com.nexcart.model.Item;
import com.nexcart.model.Product;
import com.nexcart.repository.CustomerRepository;
import com.nexcart.repository.ProductRepository;
import com.nexcart.transformer.ItemTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    public Item createItem(ItemRequestDto itemRequestDto) {
        Customer customer = customerRepository.findByEmailId(itemRequestDto.getCustomerEmail());
        if(customer == null){
            throw new CustomerNotFoundException("customer doesn't exist");
        }

        Optional<Product> optionalProduct = productRepository.findById(itemRequestDto.getProductId());
        if(optionalProduct.isEmpty()){
            throw new ProductNotFoundException("product doesn't exist");
        }

        Product product = optionalProduct.get();
        if(product.getAvailableQuantity() < itemRequestDto.getRequiredQuantity()){
            throw new InsufficientQuantityException("insufficient quantity available");
        }

        Item item = ItemTransformer.ItemRequestDtoToItem(itemRequestDto.getRequiredQuantity());
        return item;
    }
}
