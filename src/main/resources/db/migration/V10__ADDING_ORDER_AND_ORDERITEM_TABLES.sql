CREATE TABLE `order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,  -- Set id to auto-increment
    `customer_id` BIGINT NOT NULL,
    `status` ENUM('PENDING', 'COMPLETED', 'CANCELLED') NOT NULL,
    `total_amount` DOUBLE NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_order_customer`
        FOREIGN KEY (`customer_id`)
        REFERENCES `user` (`id`)
        ON DELETE CASCADE
);

CREATE TABLE `order_item` (
    `customer_id` BIGINT NOT NULL,
    `order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `quantity` INT NOT NULL,
    `price` DOUBLE NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`order_id`, `product_id`),
    CONSTRAINT `fk_orderItem_order`
        FOREIGN KEY (`order_id`)
        REFERENCES `order` (`id`)
        ON DELETE CASCADE,
    CONSTRAINT `fk_orderItem_product`
        FOREIGN KEY (`product_id`)
        REFERENCES `product` (`id`)
        ON DELETE CASCADE
);

CREATE INDEX idx_customer_created_status ON `order` (customer_id, total_amount, status, created_at, updated_at);

CREATE INDEX idx_customer_price_quantity ON `order_item` (customer_id, price, quantity, created_at, updated_at);

CREATE UNIQUE INDEX idx_unique_customer_order
ON order_item (customer_id, order_id);