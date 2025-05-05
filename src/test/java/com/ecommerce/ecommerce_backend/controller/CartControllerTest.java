package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Cart;
import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

public class CartControllerTest {
    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCart_Ok() {
        String expectedCartId = "12345";
        when(cartService.createCart()).thenReturn(expectedCartId);

        ResponseEntity<String> response = cartController.createCart();

        assertEquals(OK, response.getStatusCode());
        assertEquals(expectedCartId, response.getBody());
    }

    @Test
    void createCart_Exception() {
        when(cartService.createCart()).thenThrow(new RuntimeException("Simulated exception"));

        ResponseEntity<String> response = cartController.createCart();

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Error al crear el carrito.", response.getBody());
        verify(cartService, times(1)).createCart();
    }

    @Test
    void getCart_Ok() {
        String cartId = "12345";
        Cart expectedCart = new Cart(cartId);
        when(cartService.getCart(cartId)).thenReturn(expectedCart);

        ResponseEntity<Cart> response = cartController.getCart(cartId);

        assertEquals(OK, response.getStatusCode());
        assertEquals(expectedCart, response.getBody());
    }

    @Test
    void getCart_NotFound() {
        String cartId = "12345";
        when(cartService.getCart(cartId)).thenReturn(null);

        ResponseEntity<Cart> response = cartController.getCart(cartId);

        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetCart_Exception() {
        String cartId = "test-cart-id";
        when(cartService.getCart(cartId)).thenThrow(new RuntimeException("Simulated exception"));

        ResponseEntity<Cart> response = cartController.getCart(cartId);

        assertEquals(500, response.getStatusCode().value());
        verify(cartService, times(1)).getCart(cartId);
    }

    @Test
    void addProductsToCart_Ok() {
        String cartId = "12345";
        Product product = new Product(1, "Producto de prueba", 10.99);
        List<Product> products = List.of(product);
        when(cartService.addProductsToCart(cartId, products)).thenReturn(products);

        ResponseEntity<List<Product>> response = cartController.addProductsToCart(cartId, products);

        assertEquals(200, response.getStatusCode().value(), "El estado HTTP debería ser 200 OK");
        assertNotNull(response.getBody(), "El cuerpo de la respuesta no debería ser nulo");
        assertIterableEquals(products, response.getBody(), "La lista de productos añadidos debería coincidir");
    }

    @Test
    void addProductsToCart_CartNotExistsException() {
        String invalidCartId = "nonexistent";
        Product product = new Product(1, "Producto de prueba", 10.99);
        List<Product> products = List.of(product);
        when(cartService.addProductsToCart(invalidCartId, products)).thenReturn(null);

        ResponseEntity<List<Product>> response = cartController.addProductsToCart(invalidCartId, products);

        assertEquals(400, response.getStatusCode().value(), "El estado HTTP debería ser 400 Bad Request");
        assertNull(response.getBody(), "El cuerpo de la respuesta debería ser nulo");
    }

    @Test
    void addProductsToCart_whenUnexpectedErrorOccurs_shouldReturnServerError() {
        String cartId = "12345";
        Product product = new Product(1, "Producto de prueba", 10.99);
        List<Product> products = List.of(product);
        when(cartService.addProductsToCart(cartId, products)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<List<Product>> response = cartController.addProductsToCart(cartId, products);

        assertEquals(500, response.getStatusCode().value(), "El estado HTTP debería ser 500 Internal Server Error");
        assertNull(response.getBody(), "El cuerpo de la respuesta debería ser nulo");
    }

    @Test
    void deleteCart_shouldReturnNoContent() {
        String cartId = "12345";
        when(cartService.deleteCart(cartId)).thenReturn("Carrito eliminado correctamente");

        ResponseEntity<String> response = cartController.deleteCart(cartId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Carrito eliminado correctamente", response.getBody());
        verify(cartService, times(1)).deleteCart(cartId);
    }

    @Test
    void deleteCart_WhenServiceThrowsException_ShouldReturn500() {
        String cartId = "test-cart-id";
        doThrow(new RuntimeException("Simulated exception")).when(cartService).deleteCart(cartId);

        ResponseEntity<String> response = cartController.deleteCart(cartId);

        assertEquals(500, response.getStatusCode().value());
        verify(cartService, times(1)).deleteCart(cartId);
    }
}
