
ALTER TABLE vendor_product
DROP FOREIGN KEY fk_vendor;

ALTER TABLE vendor_product
DROP FOREIGN KEY fk_product;


ALTER TABLE vendor_product
DROP PRIMARY KEY;