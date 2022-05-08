package com.dojinyou.devcourse.gccoffeerestapi.order.domain;

import com.dojinyou.devcourse.gccoffeerestapi.order.dto.OrderProductCreateRequest;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderProduct {
    private final long id;
    private long orderId;
    private final long productId;
    private final int quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public OrderProduct(long id,
                        long orderId,
                        long productId,
                        int quantity,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private OrderProduct(long productId, int quantity) {
        this(0, 0, productId, quantity, null, null);
    }

    public static OrderProduct from(OrderProductCreateRequest orderProductCreateRequest) {
        valid(orderProductCreateRequest);
        return new OrderProduct(orderProductCreateRequest.productId(),
                                orderProductCreateRequest.quantity());
    }

    private static void valid(OrderProductCreateRequest orderProductCreateRequest) {
        if (orderProductCreateRequest.productId() <= 0 || orderProductCreateRequest.quantity() <= 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        OrderProduct that = (OrderProduct) other;
        return id == that.id
                && orderId == that.orderId
                && productId == that.productId
                && quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, productId, quantity);
    }

    public long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "OrderProduct[" +
                "id=" + id + ", " +
                "orderId=" + orderId + ", " +
                "productId=" + productId + ", " +
                "quantity=" + quantity + ", " +
                "createdAt=" + createdAt + ", " +
                "updatedAt=" + updatedAt + ']';
    }

}
