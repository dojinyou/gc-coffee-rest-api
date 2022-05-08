package com.dojinyou.devcourse.gccoffeerestapi.order;

import com.dojinyou.devcourse.gccoffeerestapi.order.domain.Order;
import com.dojinyou.devcourse.gccoffeerestapi.order.domain.OrderProduct;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Repository
public class OrderJdbcRepository implements OrderRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void create(Order order) {
        jdbcTemplate.update("""
                                    INSERT INTO orders(email, address, postcode, order_status)
                                    VAlUES (:email, :address, :postcode, :orderStatus)""",
                            getParameterSource(order));
        long newOrderId = jdbcTemplate.query("SELECT MAX(id) FROM orders", new MapSqlParameterSource(), (ResultSet, i) -> ResultSet.getLong("MAX(id)")).get(0);

        order.getOrderProducts().forEach(orderProduct -> {
            orderProduct.setOrderId(newOrderId);
            jdbcTemplate.update("""
                                        INSERT INTO order_products(order_id, product_id, quantity)
                                        VALUES (:orderId, :productId, :quantity)""",
                                getParameterSource(orderProduct));
        });
    }


    private SqlParameterSource getParameterSource(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
        return new MapSqlParameterSource().addValue("id", order.getId())
                                          .addValue("email", order.getEmail().address())
                                          .addValue("address", order.getAddress())
                                          .addValue("postcode", order.getPostcode().value())
                                          .addValue("orderStatus", order.getOrderStatus().toString())
                                          .addValue("createdAt", order.getCreatedAt())
                                          .addValue("updatedAt", order.getUpdatedAt())
                                          .addValue("orderProducts", Arrays.toString(order.getOrderProducts().toArray()));
    }

    private SqlParameterSource getParameterSource(OrderProduct orderProduct) {
        if (orderProduct == null) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
        return new MapSqlParameterSource().addValue("id", orderProduct.getId())
                                          .addValue("orderId", orderProduct.getOrderId())
                                          .addValue("productId", orderProduct.getProductId())
                                          .addValue("quantity", orderProduct.getQuantity())
                                          .addValue("createdAt", orderProduct.getCreatedAt())
                                          .addValue("updatedAt", orderProduct.getUpdatedAt());
    }
}
