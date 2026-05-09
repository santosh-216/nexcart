package com.nexcart.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder

public class CheckoutCartRequestDto {
    String customerEmail;
    String cardNo;
    int cvv;
}
