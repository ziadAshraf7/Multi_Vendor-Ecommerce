


ALTER TABLE product_review
DROP COLUMN id;

ALTER TABLE product_review
ADD CONSTRAINT PK_Product_Customer_Review PRIMARY KEY (product_id , customer_id);

