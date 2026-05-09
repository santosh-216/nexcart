package com.nexcart.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class CartResponseDto {
    String customerName;
    int cartTotal;
    List<ItemResponseDto> itemList;
}
