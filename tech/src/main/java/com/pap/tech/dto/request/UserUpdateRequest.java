package com.pap.tech.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String id;
    String username;
    String email;
    String phone;
    String address;
    String fullname;
    LocalDate birthday;
    String role;
    String cartId;
    Boolean active;
}
