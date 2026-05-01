package com.Audrina.ecommerce.service;

import com.Audrina.ecommerce.dto.CartItemResponse;
import com.Audrina.ecommerce.dto.OrderItemDTO;
import com.Audrina.ecommerce.dto.OrderResponse;
import com.Audrina.ecommerce.exception.CartItemNotFound;
import com.Audrina.ecommerce.model.*;
import com.Audrina.ecommerce.repository.CartRepository;
import com.Audrina.ecommerce.repository.OrderRepository;
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
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public Optional<OrderResponse> creatOrder(String userId) {

        User user = userRepository.findUserById(Long.valueOf(userId));

        List<CartItem> cartItem = cartRepository.findByUser(user);
        if (cartItem.isEmpty()) {
            return Optional.empty();
        }

        BigDecimal totalAmount = cartItem.stream().map(
                item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        ).reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(OrderStatus.COMPLETED);
        List<OrderItem> orderItems = cartItem.stream().map(
                item-> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order

                )
        ).toList();
        order.setOrderItemList(orderItems);


        Order savedOrder =   orderRepository.save(order);
        cartRepository.deleteByUser(user);

        OrderResponse orderResponse = toOrderResponse(savedOrder);

        return Optional.of(orderResponse);
    }

    private OrderResponse toOrderResponse(Order savedOrder) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(savedOrder.getId());
        orderResponse.setOrderStatus(savedOrder.getOrderStatus());
        orderResponse.setTotalAmount(savedOrder.getTotalAmount());
        orderResponse.setOrderItems(savedOrder.getOrderItemList().stream().map(
                orderItem -> {
                    return new OrderItemDTO(
                            orderItem.getId(),
                            orderItem.getQuantity(),
                            orderItem.getProduct().getId(),
                            orderItem.getPrice()

                            );
                }
        ).toList());

    orderResponse.setOrderDate(savedOrder.getCreatedDate());
        return orderResponse;
    }
}
