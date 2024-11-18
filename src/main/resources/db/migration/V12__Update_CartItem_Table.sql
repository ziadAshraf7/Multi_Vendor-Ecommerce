

ALTER TABLE cart_item
ADD COLUMN vendor_product_id BIGINT NOT NULL;


ALTER TABLE cart_item
ADD CONSTRAINT FK_VendorProduct_CartItem
FOREIGN KEY (vendor_product_id)
REFERENCES vendor_product (id);



