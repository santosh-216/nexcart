package com.nexcart.dto.response;

import com.nexcart.Enum.CardType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class CardResponseDto {

    String customerName;

    String cardNo;//masked

    Date validTill;

    CardType cardType;
}
