package com.nexcart.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class OrderRequestDto {
    String cardNo;
    int productId;
    String customerEmail;
    int requiredQuantity;
    int cvv;
}
