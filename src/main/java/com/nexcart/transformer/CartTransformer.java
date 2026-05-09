package com.nexcart.transformer;

import com.nexcart.dto.response.CartResponseDto;
import com.nexcart.dto.response.ItemResponseDto;
import com.nexcart.model.Cart;
import com.nexcart.model.Item;

import java.util.ArrayList;
import java.util.List;

public class CartTransformer {
    public static CartResponseDto CartToCartResponseDto(Cart cart){

        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        for(Item item: cart.getItems()){
            itemResponseDtoList.add(ItemTransformer.ItemToItemResponseDto(item));
        }

        return CartResponseDto.builder()
                .customerName(cart.getCustomer().getName())
                .cartTotal(cart.getCartTotal())
                .itemList(itemResponseDtoList)
                .build();
    }
}
