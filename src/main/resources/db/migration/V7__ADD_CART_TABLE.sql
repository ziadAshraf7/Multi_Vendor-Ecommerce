

CREATE TABLE cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total_price DOUBLE NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    customer_id BIGINT,
    FOREIGN KEY (customer_id) REFERENCES user(id)
);