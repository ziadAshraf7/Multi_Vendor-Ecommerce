

CREATE TABLE recently_viewed (
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_recently_viewed FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_product_recently_viewed FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    UNIQUE (user_id, product_id),
    PRIMARY KEY (user_id , product_id)
);

