package com.pap.tech.entity;

import com.pap.tech.enums.PaymentMethod;
import com.pap.tech.enums.PaymentStatus;
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
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    LocalDateTime orderdate;
    PaymentStatus status;
    PaymentMethod paymentmethod;
    Long totalAmount;
    String shippingaddress;
    String vnpTxnref;
    LocalDateTime vnpPaydate;
    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "id")
    User user;
    @ManyToOne
    @JoinColumn(name = "addressid", referencedColumnName = "id")
    Address address;
}
