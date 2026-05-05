package com.nexcart.service;

import com.nexcart.dto.request.CustomerRequestDto;
import com.nexcart.dto.response.CustomerResponseDto;
import com.nexcart.model.Cart;
import com.nexcart.model.Customer;
import com.nexcart.repository.CustomerRepository;
import com.nexcart.transformer.CustomerTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    public CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto) {

        Customer customer = CustomerTransformer.CustomerRequestDtoToCustomer(customerRequestDto);

        Cart cart = new Cart();
        cart.setCartTotal(0);
        cart.setCustomer(customer);
        customer.setCart(cart);

        Customer savedCustomer = customerRepository.save(customer);

        return CustomerTransformer.CustomerToCustomerResponseDto(savedCustomer);
    }
}
