ALTER TABLE cart_item DROP FOREIGN KEY fk_cart_item_cart_id;

ALTER TABLE cart
DROP COLUMN id;

ALTER TABLE cart
ADD PRIMARY KEY (customer_id);
