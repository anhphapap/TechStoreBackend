package com.pap.tech.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    @Id
    @Column(length = 100)
    String id;

    @Column(length = 100)
    String name;

    @Column(name = "parentid", length = 100)
    String parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentid", insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Category parent;
}


