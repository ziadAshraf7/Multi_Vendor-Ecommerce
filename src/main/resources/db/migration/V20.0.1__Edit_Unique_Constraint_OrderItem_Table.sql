

ALTER TABLE order_item DROP FOREIGN KEY fk_orderItem_order;

ALTER TABLE order_item DROP INDEX idx_unique_customer_order;


ALTER TABLE order_item
ADD CONSTRAINT fk_orderItem_order
FOREIGN KEY (order_id)
REFERENCES `order`(id);

ALTER TABLE order_item
ADD CONSTRAINT idx_unique_cart_order UNIQUE (product_id, order_id);