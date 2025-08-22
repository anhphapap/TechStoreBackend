package com.pap.tech.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "productattribute")
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "productid", referencedColumnName = "id")
    Product product;

    @ManyToOne
    @JoinColumn(name = "attributeid", referencedColumnName = "id")
    Attribute attribute;

    String value;
}
