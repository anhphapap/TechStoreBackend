package com.pap.tech.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListOrderResponse {
    String id;
    String status;
    BigDecimal totalamount;
    String vnpTxnref;
    Boolean paymentstatus;
    BigDecimal discountamount;
    String discountcode;
    List<OrderDetailResponse> orderDetails;
}
