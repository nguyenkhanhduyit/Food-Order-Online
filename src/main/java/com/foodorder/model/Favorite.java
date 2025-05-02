package com.foodorder.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_favorite")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Favorite {
    Long restaurantId;
    Long foodId;
}
