package com.nexcart.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class OrderResponseDto {
    String orderId;//UUID
    Date orderDate;
    int orderTotal;
    String cardUsed;//masked
    String customerName;
    List<ItemResponseDto> items;
}
