package com.dojinyou.devcourse.gccoffeerestapi.order;

import com.dojinyou.devcourse.gccoffeerestapi.order.domain.Order;

public interface OrderRepository {
    void create(Order order);
}
