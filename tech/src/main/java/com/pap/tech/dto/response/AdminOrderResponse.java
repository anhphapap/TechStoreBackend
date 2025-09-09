package com.pap.tech.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminOrderResponse {
    String id;
    String status;
    BigDecimal totalamount;
    String paymentmethod;
    Boolean paymentstatus;
    String fullname;
    LocalDateTime orderdate;
}
