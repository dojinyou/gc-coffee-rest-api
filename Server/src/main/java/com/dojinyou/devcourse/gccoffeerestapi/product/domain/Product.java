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

    public Product(long id,
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

    public Product(String name, Category category, long price, String description) {
        this(0, name, category, price, description, null, null, false);
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Category category() {
        return category;
    }

    public long price() {
        return price;
    }

    public String description() {
        return description;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public boolean isDeleted() {
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
