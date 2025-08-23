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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
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
    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "id")
    User user;
}
