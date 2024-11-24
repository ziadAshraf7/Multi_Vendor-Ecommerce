



ALTER TABLE order_item
ADD COLUMN vendor_id BIGINT NOT NULL;


ALTER TABLE order_item add CONSTRAINT FK_OrderItem_Vendor FOREIGN KEY (vendor_id) REFERENCES user(id);






