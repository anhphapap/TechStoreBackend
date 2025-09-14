package com.pap.tech.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminUserRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;
    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;
    @Email(message = "EMAIL_INVALID")
    String email;
    String phone;
    String fullname;
    LocalDate birthday;
    String role;
    Boolean active;
}
