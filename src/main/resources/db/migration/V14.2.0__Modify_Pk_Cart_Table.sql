ALTER TABLE cart DROP PRIMARY KEY;
ALTER TABLE cart MODIFY COLUMN id BIGINT AUTO_INCREMENT PRIMARY KEY;