package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.Cart;
import com.ecommerce.ecommerce_backend.model.Product;

import java.util.List;

public interface CartService {
    String createCart();

    Cart getCart(String id);

    List<Product> addProductsToCart(String id, List<Product> products);

    String deleteCart(String id);

    void deleteInactiveCarts();
}
