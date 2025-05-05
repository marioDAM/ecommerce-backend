package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.Cart;
import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CartServiceImplTest {
    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private Map<String, Cart> cartStorage;

    @Mock
    private Map<String, LocalDateTime> cartActivity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

    @Test
    void getCart_NotFound() {
        String cartId = "1";
        when(cartStorage.containsKey(cartId)).thenReturn(false);

        Cart result = cartService.getCart(cartId);

        assertNull(result);
        verify(cartActivity, never()).put(anyString(), any(LocalDateTime.class));
    }

    @Test
    void addProductsToCart_CartNotFound() {
        String cartId = "nonExistentCartId";
        when(cartStorage.get(cartId)).thenReturn(null);

        List<Product> result = cartService.addProductsToCart(cartId, List.of(new Product(1, "Product1", 2.1)));

        assertNull(result);
        verify(cartActivity, never()).put(anyString(), any(LocalDateTime.class));
    }

    @Test
    void deleteCart_NotFound() {
        String cartId = "nonExistentCartId";
        when(cartStorage.containsKey(cartId)).thenReturn(false);

        cartService.deleteCart(cartId);

        verify(cartStorage, never()).remove(cartId);
        verify(cartActivity, never()).remove(cartId);
    }
}
