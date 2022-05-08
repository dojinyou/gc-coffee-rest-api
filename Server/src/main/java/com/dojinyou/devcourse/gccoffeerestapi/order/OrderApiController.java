package com.dojinyou.devcourse.gccoffeerestapi.order;


import com.dojinyou.devcourse.gccoffeerestapi.order.domain.Order;
import com.dojinyou.devcourse.gccoffeerestapi.order.dto.OrderCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/orders")
public class OrderApiController {
    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity create(@RequestBody OrderCreateRequest request) {
        orderService.create(Order.from(request));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
