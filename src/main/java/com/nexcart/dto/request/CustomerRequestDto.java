package com.nexcart.dto.request;

import com.nexcart.Enum.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class CustomerRequestDto {
    String name;

    String emailId;

    String mobileNo;

    Gender gender;
}
