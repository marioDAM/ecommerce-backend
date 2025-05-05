package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.controller.api.CartApi;
import com.ecommerce.ecommerce_backend.model.Cart;
import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class CartController implements CartApi {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public ResponseEntity<String> createCart() {
        try {
            String cartId = cartService.createCart();
            return ResponseEntity.ok(cartId);
        } catch (Exception e) {
            log.error("Error al crear el carrito: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al crear el carrito.");
        }
    }

    @Override
    public ResponseEntity<Cart> getCart(String id) {
        try {
            Cart cart = cartService.getCart(id);
            if (cart == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            log.error("Error al obtener el carrito con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    public ResponseEntity<List<Product>> addProductsToCart(String id, List<Product> products) {
        try {
            List<Product> addedProducts = cartService.addProductsToCart(id, products);

            if (addedProducts == null || addedProducts.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(addedProducts);
        } catch (Exception e) {
            log.error("Error al añadir productos al carrito con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    public ResponseEntity<String> deleteCart(String id) {
        try {
            cartService.deleteCart(id);
            return ResponseEntity.ok("Carrito eliminado correctamente");
        } catch (Exception e) {
            log.error("Error al eliminar el carrito con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al eliminar el carrito.");
        }
    }
}
