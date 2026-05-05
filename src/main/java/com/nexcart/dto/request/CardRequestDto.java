package com.nexcart.dto.request;
import com.nexcart.Enum.CardType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder

public class CardRequestDto {

    String customerEmailId;

    String cardNo;

    int cvv;

    Date validTill;

    CardType cardType;
}
