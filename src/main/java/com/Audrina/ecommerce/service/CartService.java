package com.Audrina.ecommerce.service;
import com.Audrina.ecommerce.dto.CartItemRequest;
import com.Audrina.ecommerce.dto.CartItemResponse;
import com.Audrina.ecommerce.model.CartItem;
import com.Audrina.ecommerce.model.Product;
import com.Audrina.ecommerce.model.User;
import com.Audrina.ecommerce.repository.CartRepository;
import com.Audrina.ecommerce.repository.ProductRepository;
import com.Audrina.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;


    public Boolean addProductToCart(String userId, CartItemRequest cartItemRequest) {

        Product product = productRepository.findById(Long.valueOf(cartItemRequest.getProductId())).get();

        if (product.getStockQuantity() < cartItemRequest.getQuantity()) {
           return false;
        }

        User user = userRepository.findById(Long.valueOf(userId)).get();

        CartItem existingCartItem = cartRepository.findByUserAndProduct(user, product);
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(product.getPrice());
            cartRepository.save(existingCartItem);
        } else {


            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(product.getPrice());
            cartRepository.save(cartItem);
        }


        return true;
    }

    public List<CartItemResponse> getCartItems(String userId) {
        User user = userRepository.findById(Long.valueOf(userId)).get();
        List<CartItem> cartItemList = cartRepository.findByUser(user);

        return cartItemList.stream().map(
                this::toCartItemResponse
        ).toList();


    }

    public boolean deleteFromCart(String userId, Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
           return false;
        }
        Product foundProduct = product.get();

        Optional<User> user = userRepository.findById(Long.valueOf(userId));
        if (user.isEmpty()) {
          return false;
        }
        User foundUser = user.get();

        cartRepository.deleteByUserAndProduct(foundUser, foundProduct);


        return true;
    }

    private CartItemResponse toCartItemResponse(CartItem cartItem) {
        CartItemResponse response = new CartItemResponse();
        BigDecimal subTotal = cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity()));

        response.setProductId(String.valueOf(cartItem.getProduct().getId()));
        response.setQuantity(cartItem.getQuantity());
        response.setUnitPrice(cartItem.getPrice());
        response.setSubtotal(subTotal);

        return response;

    }
}
