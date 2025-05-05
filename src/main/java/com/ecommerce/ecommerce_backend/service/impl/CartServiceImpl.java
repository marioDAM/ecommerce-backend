package com.ecommerce.ecommerce_backend.service.impl;

import com.ecommerce.ecommerce_backend.model.Cart;
import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    public final Map<String, Cart> cartStorage;

    public final Map<String, LocalDateTime> cartActivity;

    public CartServiceImpl() {
        this.cartStorage = new ConcurrentHashMap<>();
        this.cartActivity = new ConcurrentHashMap<>();
    }

    public CartServiceImpl(Map<String, Cart> cartStorage, Map<String, LocalDateTime> cartActivity) {
        this.cartStorage = cartStorage;
        this.cartActivity = cartActivity;
    }

    @Override
    public String createCart() {
        try {
            String cartId = UUID.randomUUID().toString();

            cartStorage.put(cartId, new Cart(cartId));

            cartActivity.put(cartId, LocalDateTime.now());

            log.info("Carrito creado correctamente con ID: {}", cartId);
            return "Carrito creado correctamente, con ID: " + cartId;

        } catch (Exception e) {
            log.error("Error al crear el carrito: {}", e.getMessage(), e);
            return "Error al crear el carrito. Por favor, inténtelo de nuevo.";
        }
    }

    @Override
    public Cart getCart(String id) {
        try {
            if (!cartStorage.containsKey(id)) {
                log.warn("El carrito con ID {} no existe.", id);
                return null;
            }
            updateCartActivity(id);
            log.info("Carrito con ID {} obtenido correctamente.", id);
            return cartStorage.get(id);
        } catch (Exception e) {
            log.error("Error al obtener el carrito con ID {}: {}", id, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Product> addProductsToCart(String id, List<Product> products) {
        try {
            Cart cart = cartStorage.get(id);

            if (cart == null) {
                log.warn("El carrito con ID {} no existe. No se pueden añadir productos.", id);
                throw new RuntimeException("El carrito no existe. No se pueden añadir productos");
            }

            if (products == null || products.isEmpty()) {
                log.warn("La lista de productos es nula o está vacía para el carrito con ID {}.", id);
                return null;
            }

            if (cart.getProducts() == null) {
                cart.setProducts(new ArrayList<>());
            }

            cart.getProducts().addAll(products);
            updateCartActivity(id);

            log.info("Se añadieron {} productos correctamente al carrito con ID {}.", products.size(), id);
            return products;
        } catch (Exception e) {
            log.error("Error inesperado al añadir productos al carrito con ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error inesperado al añadir productos al carrito", e);
        }
    }

    @Override
    public String deleteCart(String id) {
        try {
            if (!cartStorage.containsKey(id)) {
                log.warn("El carrito con ID {} no existe. No se puede eliminar.", id);
                throw new RuntimeException("El carrito con ID " + id + " no existe.");
            }

            cartStorage.remove(id);
            cartActivity.remove(id);
            log.info("El carrito con ID {} ha sido eliminado correctamente.", id);

            return "El carrito con ID " + id + " ha sido eliminado correctamente.";
        } catch (Exception e) {
            log.error("Error al eliminar el carrito con ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Se produjo un error crítico al eliminar el carrito con ID " + id, e);
        }
    }

    @Override
    public void deleteInactiveCarts() {
        try {
            LocalDateTime now = LocalDateTime.now();
            cartActivity.forEach((id, lastActivity) -> {
                if (Duration.between(lastActivity, now).toMinutes() > 10) {
                    log.info("Eliminando carrito inactivo con ID {}. Última actividad: {}", id, lastActivity);
                    deleteCart(id);
                }
            });
        } catch (Exception e) {
            log.error("Error al eliminar carritos inactivos: {}", e.getMessage(), e);
        }
    }

    public void updateCartActivity(String id) {
        cartActivity.put(id, LocalDateTime.now());
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleCartCleanup() {
        System.out.println("Ejecutando limpieza de carritos inactivos...");
        deleteInactiveCarts();
    }
}
