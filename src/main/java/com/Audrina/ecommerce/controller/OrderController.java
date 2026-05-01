package com.Audrina.ecommerce.controller;

import com.Audrina.ecommerce.dto.OrderResponse;
import com.Audrina.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestHeader(name = "X-USER-ID") String userId) {
      return  orderService.creatOrder(userId).map
                      (ResponseEntity::ok)
              .orElseGet(() -> ResponseEntity.notFound().build());

    }
}
