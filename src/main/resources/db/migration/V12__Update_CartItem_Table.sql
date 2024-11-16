

ALTER TABLE cart_item
ADD COLUMN vendor_product_id BIGINT NOT NULL;


ALTER TABLE cart_item
ADD CONSTRAINT FK_VendorProduct_CartItem
FOREIGN KEY (vendor_product_id)
REFERENCES vendor_product (id);



 ALTER TABLE cart_item
 DROP PRIMARY KEY;


 ALTER TABLE cart_item
 ADD PRIMARY KEY (cart_id , vendor_product_id , product_id);
