package com.pap.tech.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiscountRequest {
    String code;
    Integer discountpercent;
    BigDecimal maxdiscount;
    BigDecimal minorder;
    LocalDateTime expiredat;
}
