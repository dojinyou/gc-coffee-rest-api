package com.dojinyou.devcourse.gccoffeerestapi.order.domain;

import com.dojinyou.devcourse.gccoffeerestapi.order.dto.OrderCreateRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private final long id;
    private final Email email;
    private final String address;
    private final Postcode postcode;
    private final OrderStatus orderStatus;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<OrderProduct> orderProducts;

    private Order(long id, Email email, String address, Postcode postcode, OrderStatus orderStatus,
                  LocalDateTime createdAt, LocalDateTime updatedAt, List<OrderProduct> orderProducts) {
        this.id = id;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderProducts = orderProducts;
    }

    private Order(Email email, String address, Postcode postcode, List<OrderProduct> orderProducts) {
        this(0, email, address, postcode, OrderStatus.ACCEPTED, null, null, orderProducts);
    }

    public static Order from(OrderCreateRequest orderCreateRequest) {
        valid(orderCreateRequest);
        return new Order(new Email(orderCreateRequest.email().strip()), orderCreateRequest.address().strip(),
                         new Postcode(orderCreateRequest.postcode().strip()),
                         orderCreateRequest.orderProducts().stream().map(OrderProduct::from).toList());
    }

    private static void valid(OrderCreateRequest orderCreateRequest) {
        if (orderCreateRequest.email() == null || orderCreateRequest.email().strip().length() == 0 ||
            orderCreateRequest.address() == null || orderCreateRequest.address().strip().length() == 0 ||
            orderCreateRequest.postcode() == null || orderCreateRequest.postcode().strip().length() == 0 ||
            orderCreateRequest.orderProducts() == null || orderCreateRequest.orderProducts().size() == 0) {
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
        Order otherEntity = (Order) other;
        return id == otherEntity.id
                && Objects.equals(email, otherEntity.email)
                && Objects.equals(address, otherEntity.address)
                && Objects.equals(postcode, otherEntity.postcode)
                && orderStatus == otherEntity.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, address, postcode, orderStatus);
    }

    public long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public Postcode getPostcode() {
        return postcode;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", email=" + email +
                ", address='" + address + '\'' +
                ", postcode=" + postcode +
                ", orderStatus=" + orderStatus +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", orderProducts=" + orderProducts +
                '}';
    }
}
