package com.pap.tech.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListProductResponse {
    String id;
    String name;
    Long price;
    String image;
    double avgRating;
    int ratingCount;
}
