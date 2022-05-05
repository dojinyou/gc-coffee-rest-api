CREATE TABLE products
(
    id          BIGINT          PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(20)     NOT NULL UNIQUE,
    category    VARCHAR(50)     NOT NULL,
    price       BIGINT          NOT NULL,
    description VARCHAR(500)    DEFAULT NULL,
    created_at  TIMESTAMP(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at  TIMESTAMP(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    is_deleted  TINYINT         NOT NULL DEFAULT 0
    );

CREATE TABLE orders
(
    id              BIGINT          PRIMARY KEY AUTO_INCREMENT,
    email           VARCHAR(50)     NOT NULL,
    address         VARCHAR(200)    NOT NULL,
    postcode        VARCHAR(200)    NOT NULL,
    order_status    VARCHAR(50)     NOT NULL,
    created_at      TIMESTAMP(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at      TIMESTAMP(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3)
);

CREATE TABLE order_products
(
    id              BIGINT          PRIMARY KEY AUTO_INCREMENT,
    order_id        BIGINT          NOT NULL,
    product_id      BIGINT          NOT NULL,
    quantity        INT             NOT NULL,
    total_price     BIGINT          NOT NULL,
    created_at      TIMESTAMP(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at      TIMESTAMP(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    CONSTRAINT fk_order_product_to_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_product_to_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_order_products_order_id ON order_products(order_id);