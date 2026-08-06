CREATE TABLE products
(
    id         BIGINT         NOT NULL AUTO_INCREMENT,
    name       VARCHAR(100)   NOT NULL,
    price      DECIMAL(12, 2) NOT NULL,
    stock      INT            NOT NULL,
    status     VARCHAR(20)    NOT NULL,
    created_at DATETIME(6)    NOT NULL,

    CONSTRAINT pk_products PRIMARY KEY (id),
    CONSTRAINT ck_products_price CHECK (price >= 0),
    CONSTRAINT ck_products_stock CHECK (stock >= 0)
);