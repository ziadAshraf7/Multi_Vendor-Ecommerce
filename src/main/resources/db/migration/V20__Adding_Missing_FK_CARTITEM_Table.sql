


ALTER TABLE cart_item
ADD CONSTRAINT fk_cart_cartItem
FOREIGN KEY (cart_id)
REFERENCES cart(id);
