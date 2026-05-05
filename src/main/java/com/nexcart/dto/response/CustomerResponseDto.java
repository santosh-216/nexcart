package com.nexcart.dto.response;

import com.nexcart.Enum.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder

public class CustomerResponseDto {
    String name;

    String emailId;

    String mobileNo;

    Gender gender;
}
