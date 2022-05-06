package com.dojinyou.devcourse.gccoffeerestapi.product.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Product {
    private final long id;
    private final String name;
    private final Category category;
    private final long price;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final boolean isDeleted;

    private Product(long id,
                   String name,
                   Category category,
                   long price,
                   String description,
                   LocalDateTime createdAt,
                   LocalDateTime updatedAt,
                   boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    private Product(String name, Category category, long price, String description) {
        this(0, name, category, price, description, null, null, false);
    }

    public static Product of(String name, Category category, long price, String description) {
        valid(0, name, category, price);
        return new Product(name, category, price, description);

    }

    public static Product of(long id,
                             String name,
                             Category category,
                             long price,
                             String description,
                             LocalDateTime createdAt,
                             LocalDateTime updatedAt,
                             boolean isDeleted) {
        valid(id, name, category, price);
        return new Product(id, name, category, price, description, createdAt, updatedAt, isDeleted);
    }

    private static void valid(long id, String name, Category category, long price) {
        if (id < 0 || name == null || category == null || price < 0) {
            throw new IllegalArgumentException("상품 생성에 잘못된 입력값이 존재합니다.");
        }
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public long getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    @Override
    public String toString() {
        return "Product[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "category=" + category + ", " +
                "price=" + price + ", " +
                "description=" + description + ", " +
                "createdAt=" + createdAt + ", " +
                "updatedAt=" + updatedAt + ", " +
                "isDeleted=" + isDeleted + ']';
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Product otherEntity = (Product) other;
        return id == otherEntity.id && price == otherEntity.price && name.equals(otherEntity.name)
                && category == otherEntity.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, price);
    }

}
