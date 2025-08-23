package com.pap.tech.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressRequest {
    String fullname;
    String phone;
    String provincecode;
    String provincename;
    String districtcode;
    String districtname;
    String wardcode;
    String wardname;
    String detail;
    Boolean isdefault;
}
