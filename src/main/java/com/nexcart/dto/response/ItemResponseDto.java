package com.nexcart.dto.response;

import com.nexcart.Enum.ProductCategory;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class ItemResponseDto {
    String itemName;
    int itemPrice;
    int quantityAdded;
    ProductCategory category;
}
