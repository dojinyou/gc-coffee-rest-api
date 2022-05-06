package com.dojinyou.devcourse.gccoffeerestapi.product;

import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Category;
import com.dojinyou.devcourse.gccoffeerestapi.product.domain.Product;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dojinyou.devcourse.gccoffeerestapi.utils.JdbcUtil.toLocalDateTime;

@Repository
public class ProductJdbcRepository implements ProductRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Product> productRowMapper = (resultSet, i) -> {
        final var id = resultSet.getLong("id");
        final var name = resultSet.getString("name");
        final var category = Category.valueOf(resultSet.getString("category"));
        final var price = resultSet.getLong("price");
        final var description = resultSet.getString("description");
        final var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        final var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        final var isDeleted = resultSet.getBoolean("is_deleted");

        return new Product(id, name, category, price, description, createdAt, updatedAt, isDeleted);
    };

    @Override
    public void insert(Product product) {
        try {
            int updatedRows = jdbcTemplate.update("""
                                    INSERT INTO products(name, category, price, description)
                                    VALUES(:name, :category, :price, :description)""",
                                              getParameterSource(product));

            if (updatedRows != 1) {
                throw new RuntimeException("Not Insert Product");
            }
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException(product.name()+"이라는 상품 이름은 이미 사용 중입니다.");
        }
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM products", productRowMapper);
    }

    @Override
    public Optional<Product> findById(long id) {
        List<Product> foundProduct = jdbcTemplate.query("SELECT * FROM products WHERE id = :id",
                                                        new MapSqlParameterSource().addValue("id", id),
                                                        productRowMapper);

        if (2 < foundProduct.size()) {
            throw new RuntimeException("id 값이 " + id + "인 상품이 여러 개 존재합니다.");
        }
        if (foundProduct.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(foundProduct.get(0));
    }

    @Override
    public Optional<Product> findByName(String name) {
        List<Product> foundProduct = jdbcTemplate.query("SELECT * FROM products WHERE name = :name",
                                                        new MapSqlParameterSource().addValue("name", name),
                                                        productRowMapper);

        if (2 < foundProduct.size()) {
            throw new RuntimeException("상품의 이름이 " + name + "인 상품이 여러 개 존재합니다.");
        }
        if (foundProduct.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(foundProduct.get(0));
    }

    @Override
    public List<Product> findAllByCategory(Category category) {
        return findAll().stream().filter(product -> product.category() == category).collect(Collectors.toList());
    }

    @Override
    public void update(Product product) {
        try {
            int updatedRows = jdbcTemplate.update("""
                                        UPDATE products
                                        SET name = :name, category = :category, price = :price, description = :description  
                                        WHERE id = :id""",
                                                  getParameterSource(product));

            if (updatedRows == 0) {
                throw new IllegalArgumentException("id값이 "+product.id()+" 인 상품을 찾을 수 없습니다.");
            }
            if (updatedRows != 1) {
                throw new RuntimeException("update가 정상적으로 처리되지 않았습니다.");
            }
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException(product.name()+"이라는 상품 이름은 이미 사용 중입니다.");
        }
    }

    @Override
    public void deleteById(long id) {
            int updatedRows = jdbcTemplate.update("DELETE FROM products WHERE id = :id",
                                new MapSqlParameterSource().addValue("id", id));

            if (updatedRows == 0) {
                throw new IllegalArgumentException("id값이 "+id+" 인 상품을 찾을 수 없습니다.");
            }
            if (updatedRows != 1) {
                throw new RuntimeException("update가 정상적으로 처리되지 않았습니다.");
            }
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM products", new MapSqlParameterSource());
    }

    private SqlParameterSource getParameterSource(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
        return new MapSqlParameterSource().addValue("id", product.id())
                                          .addValue("name", product.name())
                                          .addValue("category", product.category().toString())
                                          .addValue("price", product.price())
                                          .addValue("description", product.description())
                                          .addValue("createdAt", product.createdAt())
                                          .addValue("updatedAt", product.updatedAt())
                                          .addValue("isDeleted", product.isDeleted());
    }
}
