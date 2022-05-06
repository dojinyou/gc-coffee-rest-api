package com.dojinyou.devcourse.gccoffeerestapi.product;

import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Category;
import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductDefaultService implements ProductService {

    private final ProductRepository productRepository;

    public ProductDefaultService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void insert(Product product) {
        if (product.id() != 0) {
            throw new IllegalArgumentException("생성되는 상품은 Id가 0이어야 합니다.");
        }
        productRepository.insert(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id값은 양의 정수로만 찾을 수 있습니다.");
        }
        return productRepository.findById(id);
    }

    @Override
    public Optional<Product> findByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("상품의 이름이 없습니다.");
        }
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> findAllByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("입력된 Category가 없습니다.");
        }
        return productRepository.findAllByCategory(category);
    }

    @Override
    public void update(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("입력된 상품이 없습니다.");
        }

        if (product.id() == 0) {
            throw new IllegalArgumentException("id값이 0인 상품은 update 할 수 없습니다.");
        }

        productRepository.update(product);
    }

    @Override
    public void deleteById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id값은 양의 정수로만 찾을 수 있습니다.");
        }
        productRepository.deleteById(id);

    }

    @Override
    public void deleteAll() {
        productRepository.deleteAll();
    }
}
