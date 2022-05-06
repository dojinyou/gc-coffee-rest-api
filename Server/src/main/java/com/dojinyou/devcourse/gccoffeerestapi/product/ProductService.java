package com.dojinyou.devcourse.gccoffeerestapi.product;

import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Category;
import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Product;

import java.util.List;

public interface ProductService {
    void insert(Product product);

    List<Product> findAll();

    Product findById(long id);

    Product findByName(String name);

    List<Product> findAllByCategory(Category category);

    void update(Product product);

    void deleteById(long id);

    void deleteAll();

}
