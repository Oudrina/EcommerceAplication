package com.Audrina.ecommerce.controller;

import com.Audrina.ecommerce.dto.CartItemRequest;
import com.Audrina.ecommerce.dto.CartItemResponse;
import com.Audrina.ecommerce.model.CartItem;
import com.Audrina.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addProductToCart(@RequestHeader(name = "X-USER-ID") String userId, @RequestBody CartItemRequest cartItemRequest) {

        if (cartService.addProductToCart(userId, cartItemRequest)) {
            return ResponseEntity.ok("Product added to cart successfully");
        }
        return ResponseEntity.badRequest().body("Product not found or out of stock");
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItem(@RequestHeader(name = "X-USER-ID") String userId) {
        if (cartService.getCartItems(userId) == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cartService.getCartItems(userId));

    }

    @DeleteMapping("{productId}")
    public ResponseEntity<Void> deleteCartItem(@RequestHeader(name = "X-USER-ID") String userId, @PathVariable Long productId) {
        boolean deleteProduct = cartService.deleteFromCart(userId, productId);

        if (deleteProduct) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


    }
}
