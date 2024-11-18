
Alter table cart Drop CONSTRAINT fk_cart_customer;
ALTER TABLE cart DROP PRIMARY KEY;


ALTER TABLE cart ADD COLUMN id BIGINT NOT NULL;
ALTER TABLE cart ADD PRIMARY KEY (id);

ALTER TABLE cart
ADD CONSTRAINT fk_cart_customer
FOREIGN KEY (customer_id)
REFERENCES user(id);
