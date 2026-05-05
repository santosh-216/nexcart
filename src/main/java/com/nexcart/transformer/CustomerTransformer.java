package com.nexcart.transformer;

import com.nexcart.dto.request.CustomerRequestDto;
import com.nexcart.dto.response.CustomerResponseDto;
import com.nexcart.model.Customer;

public class CustomerTransformer {

    public static Customer CustomerRequestDtoToCustomer(CustomerRequestDto customerRequestDto){
        return Customer.builder()
                .name(customerRequestDto.getName())
                .emailId(customerRequestDto.getEmailId())
                .mobileNo(customerRequestDto.getMobileNo())
                .gender(customerRequestDto.getGender())
                .build();
    }

    public static CustomerResponseDto CustomerToCustomerResponseDto(Customer customer){
        return CustomerResponseDto.builder()
                .name(customer.getName())
                .emailId(customer.getEmailId())
                .gender(customer.getGender())
                .mobileNo(customer.getMobileNo())
                .build();
    }


}
