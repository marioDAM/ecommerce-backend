package com.ecommerce.ecommerce_backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Cart {
    private String id;
    private List<Product> products = new ArrayList<>();

    public Cart(String id) {
        this.id = id;
    }
}
