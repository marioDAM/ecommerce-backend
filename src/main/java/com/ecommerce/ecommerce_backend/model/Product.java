package com.ecommerce.ecommerce_backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {
    private int id;
    private String description;
    private double price;

    public Product(int id, String description, double price) {
        this.id = id;
        this.description = description;
        this.price = price;
    }
}
