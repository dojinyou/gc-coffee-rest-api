package com.dojinyou.devcourse.gccoffeerestapi.order;

import com.dojinyou.devcourse.gccoffeerestapi.order.domain.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderDefaultService implements OrderService {
    private final OrderRepository orderRepository;

    public OrderDefaultService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void create(Order order) {
        if (order.getId() != 0) {
            throw new IllegalArgumentException("생성을 위한 상품은 Id가 0 이어야 합니다.");
        }
        orderRepository.create(order);
    }
}
