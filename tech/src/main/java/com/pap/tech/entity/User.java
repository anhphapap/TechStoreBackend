package com.pap.tech.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String username;
    String password;
    String email;
    String phone;
    String fullname;
    LocalDate birthday;
    String role;
    Boolean active;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    Cart cart;
}
