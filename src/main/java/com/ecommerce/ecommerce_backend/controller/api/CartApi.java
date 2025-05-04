package com.ecommerce.ecommerce_backend.controller.api;

import com.ecommerce.ecommerce_backend.model.Cart;
import com.ecommerce.ecommerce_backend.model.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
public interface CartApi {
    @Operation(summary = "Crear un carrito", description = "Crea un nuevo carrito y devuelve su identificador único", operationId = "createUser", tags = {"Carts"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping("/cart")
    ResponseEntity<String> createCart();

    @Operation(summary = "Obtener un carrito", description = "Obtiene la información de un carrito dado su identificador único", operationId = "getCartById", tags = {"Carts"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/cart/{id}")
    ResponseEntity<Cart> getCart(@PathVariable String id);

    @Operation(summary = "Agregar productos al carrito", description = "Aggrega uno o más productos a un carrito dado su identificador único", operationId = "addProductsToCart", tags = {"Carts"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping("/cart/{id}/products")
    ResponseEntity<List<Product>> addProductsToCart(@PathVariable String id, @RequestBody List<Product> products);

    @Operation(summary = "Eliminar un  carrito", description = "Elimina un carrito dado su identificador único", operationId = "addProductsToCart", tags = {"Carts"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @DeleteMapping("/cart/{id}")
    ResponseEntity<Void> deleteCart(@PathVariable String id);
}
