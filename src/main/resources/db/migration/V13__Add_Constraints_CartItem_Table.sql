

ALTER TABLE cart_item
ADD CONSTRAINT fk_cart_item_product_id
FOREIGN KEY (product_id)
REFERENCES product(id);


 ALTER TABLE cart_item
 ADD PRIMARY KEY (cart_id , vendor_product_id , product_id);
