CREATE TABLE cart_item (
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (product_id, cart_id),
    CONSTRAINT fk_cart_item_cart_id FOREIGN KEY (cart_id) REFERENCES cart(id),
    CONSTRAINT fk_cart_item_product_id FOREIGN KEY (product_id) REFERENCES product(id)
);
