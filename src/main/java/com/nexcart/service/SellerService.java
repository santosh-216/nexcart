package com.nexcart.service;

import com.nexcart.dto.request.SellerRequestDto;
import com.nexcart.dto.response.SellerResponseDto;
import com.nexcart.model.Seller;
import com.nexcart.repository.SellerRepository;
import com.nexcart.transformer.SellerTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerService {
    @Autowired
    SellerRepository sellerRepository;
    public SellerResponseDto addSeller(SellerRequestDto sellerRequestDto) {
        Seller seller = SellerTransformer.SellerRequestDtoToSeller(sellerRequestDto);
        Seller savedSeller = sellerRepository.save(seller);
        return SellerTransformer.SellerToSellerResponseDto(savedSeller);
    }
}
