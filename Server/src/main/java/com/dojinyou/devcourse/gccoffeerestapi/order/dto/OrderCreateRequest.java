package com.dojinyou.devcourse.gccoffeerestapi.order.dto;

import java.util.List;

public record OrderCreateRequest(String email, String address, String postcode,
                                 List<OrderProductCreateRequest> orderProducts) {
}
