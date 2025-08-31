package com.pap.tech.entity;

import com.pap.tech.enums.PaymentMethod;
import com.pap.tech.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    LocalDateTime orderdate;
    @Enumerated(EnumType.STRING)
    OrderStatus status;
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentmethod;
    @Column(precision = 18, scale = 2, nullable = false)
    BigDecimal totalamount;
    String shippingaddress;
    String vnpTxnref;
    LocalDateTime vnpPaydate;
    LocalDateTime shippeddate;
    LocalDateTime delivereddate;
    LocalDateTime cancelleddate;
    Boolean paymentstatus;
    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "id")
    User user;
    @ManyToOne
    @JoinColumn(name = "addressid", referencedColumnName = "id")
    Address address;
}
