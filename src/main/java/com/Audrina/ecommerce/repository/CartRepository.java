package com.Audrina.ecommerce.repository;

import com.Audrina.ecommerce.dto.CartItemResponse;
import com.Audrina.ecommerce.model.CartItem;
import com.Audrina.ecommerce.model.Product;
import com.Audrina.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    CartItem findByUserAndProduct(User user, Product product);

    List<CartItem> findByUser(User user);

    void deleteByUserAndProduct(User user, Product product);

    void deleteByUser(User user);
}
