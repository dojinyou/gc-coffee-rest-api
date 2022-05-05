package com.dojinyou.devcourse.gccoffeerestapi.product.repository;

import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Category;
import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void insert(Product product);

    List<Product> findAll();

    Optional<Product> findById(long id);

    Optional<Product> findByName(String name);

    List<Product> findAllByCategory(Category category);

    void update(Product product);

    void deleteById(long id);

    void deleteAll();
}
