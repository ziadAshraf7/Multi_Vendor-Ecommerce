

ALTER TABLE cart_item
ADD COLUMN price DOUBLE NOT NULL,
ADD COLUMN stock INT NOT NULL,
ADD COLUMN discount DOUBLE NOT NULL;