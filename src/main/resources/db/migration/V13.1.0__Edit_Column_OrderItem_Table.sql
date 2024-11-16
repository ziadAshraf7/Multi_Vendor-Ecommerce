

ALTER TABLE order_item
DROP INDEX idx_customer_price_quantity;

ALTER TABLE order_item
DROP COLUMN customer_id;
