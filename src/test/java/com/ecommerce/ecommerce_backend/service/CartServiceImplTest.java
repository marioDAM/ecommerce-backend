package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.Cart;
import com.ecommerce.ecommerce_backend.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CartServiceImplTest {
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartServiceImpl();
    }

    @Test
    void getCart_whenCartDoesNotExist_shouldReturnNull() {
        Cart cart = cartService.getCart("nonexistent");

        assertNull(cart);
    }

    @Test
    void deleteCart_shouldRemoveCart() {
        String cartId = cartService.createCart();

        cartService.deleteCart(cartId);

        assertNull(cartService.getCart(cartId));
    }

}
