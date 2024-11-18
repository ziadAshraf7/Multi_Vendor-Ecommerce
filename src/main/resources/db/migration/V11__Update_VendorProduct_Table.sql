

ALTER TABLE vendor_product
ADD COLUMN id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY;


ALTER TABLE vendor_product
ADD CONSTRAINT Idx_Unique_product_vendor_constraint UNIQUE (product_id,vendor_id );

CREATE INDEX IDX_VendorId_VendorProduct
ON vendor_product (vendor_id , product_id ,  price , stock , discount , created_at);

CREATE INDEX IDX_ProductId_VendorProduct
ON vendor_product (product_id , vendor_id , price , stock , discount , created_at);


ALTER TABLE Vendor_Product
ADD CONSTRAINT fk_product
FOREIGN KEY (product_id)
REFERENCES product(id);

ALTER TABLE Vendor_Product
ADD CONSTRAINT fk_vendor
FOREIGN KEY (vendor_id)
REFERENCES user(id);