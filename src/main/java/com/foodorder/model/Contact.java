package com.foodorder.model;

import jakarta.persistence.Embeddable;

import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Table(name = "contacts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contact {
    String email;
    String mobile;
    String x;
    String instagram;
}
