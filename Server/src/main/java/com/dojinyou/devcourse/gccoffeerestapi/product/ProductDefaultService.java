package com.dojinyou.devcourse.gccoffeerestapi.product;

import com.dojinyou.devcourse.gccoffeerestapi.common.exception.NotFoundException;
import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Category;
import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDefaultService implements ProductService {

    private final ProductRepository productRepository;

    public ProductDefaultService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void insert(Product product) {
        if (product.getId() != 0) {
            throw new IllegalArgumentException("생성을 위한 상품은 Id가 0 이어야 합니다.");
        }
        productRepository.insert(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("상품의 이름이 없습니다.");
        }
        return productRepository.findByName(name)
                                .orElseThrow(() -> new NotFoundException(name + "(이)라는 이름을 가진 상품을 찾을 수 없습니다."));
    }

    @Override
    public List<Product> findAllByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("입력된 Category가 없습니다.");
        }
        return productRepository.findAllByCategory(category);
    }

    @Override
    public Product findById(long id) {
        if (id <= 0) {
            throw new NotFoundException(id + "의 id를 가진 상품을 찾을 수 없습니다.");
        }
        return productRepository.findById(id).orElseThrow(NotFoundException::new);

    }

    @Override
    public void update(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("입력된 상품이 없습니다.");
        }

        if (product.getId() < 1) {
            throw new IllegalArgumentException("id가 양의 정수가 아닌 상품은 update 할 수 없습니다.");
        }

        productRepository.update(product);
    }

    @Override
    public void deleteById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id는 양의 정수로만 찾을 수 있습니다.");
        }
        productRepository.deleteById(id);

    }

    @Override
    public void deleteAll() {
        productRepository.deleteAll();
    }
}
