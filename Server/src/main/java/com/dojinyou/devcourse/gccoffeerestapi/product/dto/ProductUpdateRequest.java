package com.dojinyou.devcourse.gccoffeerestapi.product.dto;

import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Category;

public record ProductUpdateRequest(String name, Category category, long price, String description) {
}
