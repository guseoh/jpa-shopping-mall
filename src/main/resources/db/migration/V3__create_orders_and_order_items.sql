CREATE TABLE orders
(
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    member_id  BIGINT      NOT NULL,
    status     VARCHAR(20) NOT NULL,
    ordered_at DATETIME(6) NOT NULL,

    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT fk_orders_member
        FOREIGN KEY (member_id)
            REFERENCES members (id)
);

CREATE TABLE order_items
(
    id          BIGINT         NOT NULL AUTO_INCREMENT,
    order_id    BIGINT         NOT NULL,
    product_id  BIGINT         NOT NULL,
    order_price DECIMAL(12, 2) NOT NULL,
    quantity    INT            NOT NULL,

    CONSTRAINT pk_order_items PRIMARY KEY (id),

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
            REFERENCES orders (id),

    CONSTRAINT fk_order_items_product
        FOREIGN KEY (product_id)
            REFERENCES products (id),

    CONSTRAINT ck_order_items_price
        CHECK (order_price >= 0),

    CONSTRAINT ck_order_items_quantity
        CHECK (quantity > 0)
);