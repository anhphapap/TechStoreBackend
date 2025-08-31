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
@Table(name = "orderdetail")
public class OrderDetail  {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    Long price;
    int quantity;
    @ManyToOne
    @JoinColumn(name = "productid", referencedColumnName = "id")
    Product product;
    @ManyToOne
    @JoinColumn(name = "orderid", referencedColumnName = "id")
    Order order;
}
