package com.pap.tech.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    int rating;
    String comment;
    LocalDateTime createdat;
    @ManyToOne
    @JoinColumn(name = "productid", referencedColumnName = "id")
    Product product;
    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "id")
    User user;
}
