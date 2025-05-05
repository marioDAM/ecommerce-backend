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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
    void addProducts_NonExistsCartException() {
        String cartId = "nonExistentCart";
        List<Product> products = List.of(new Product(1, "Product1", 2.1));

        when(cartStorage.get(cartId)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.addProductsToCart(cartId, products);
        });
        assertEquals("Error inesperado al añadir productos al carrito", exception.getMessage());
    }

    @Test
    public void addProductsToCart_OK() {
        Map<String, Cart> cartStorage = new ConcurrentHashMap<>();
        Map<String, LocalDateTime> cartActivity = new ConcurrentHashMap<>();

        CartServiceImpl cartService = new CartServiceImpl(cartStorage, cartActivity);

        String cartId = UUID.randomUUID().toString();
        Cart cart = new Cart(cartId);
        cart.setProducts(new ArrayList<>());
        cartStorage.put(cartId, cart);
        cartActivity.put(cartId, LocalDateTime.now());

        List<Product> products = List.of(
                new Product(1, "Product1", 2.1),
                new Product(2, "Product2", 2.1)
        );

        List<Product> result = cartService.addProductsToCart(cartId, products);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2, cart.getProducts().size());
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
    void deleteCart_NotFound() {
        String cartId = "nonExistentCartId";
        when(cartStorage.containsKey(cartId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.deleteCart(cartId);
        });

        assertEquals("Se produjo un error crítico al eliminar el carrito con ID " + cartId, exception.getMessage());

        verify(cartStorage, never()).remove(cartId);
        verify(cartActivity, never()).remove(cartId);
    }

    @Test
    void getCart_whenCartDoesNotExist() {
        Cart cart = cartService.getCart("nonexistent");

        assertNull(cart);
    }

    @Test
    void getCart_CartDoesNotExistException() {
        String cartId = "nonExistentCartId";
        when(cartStorage.containsKey(cartId)).thenReturn(false);

        Cart result = cartService.getCart(cartId);

        assertNull(result);
        verify(cartStorage, never()).get(cartId);
        verify(cartActivity, never()).put(eq(cartId), any(LocalDateTime.class));
    }
}
