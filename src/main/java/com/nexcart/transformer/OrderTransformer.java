package com.nexcart.transformer;

import com.nexcart.dto.response.ItemResponseDto;
import com.nexcart.dto.response.OrderResponseDto;
import com.nexcart.model.Item;
import com.nexcart.model.OrderEntity;

import java.util.ArrayList;
import java.util.List;

public class OrderTransformer {
    public static OrderResponseDto OrderToOrderResponseDto(OrderEntity order){
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        for(Item item : order.getItems()){
            itemResponseDtoList.add(ItemTransformer.ItemToItemResponseDto(item));
        }

        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .orderTotal(order.getOrderTotal())
                .cardUsed(order.getCardUsed())
                .customerName(order.getCustomer().getName())
                .items(itemResponseDtoList)
                .build();
    }
}
