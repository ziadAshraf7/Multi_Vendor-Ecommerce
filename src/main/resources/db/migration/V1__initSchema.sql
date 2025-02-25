


-- BaseEntity structure added where applicable
CREATE TABLE brand (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    image LONGBLOB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE category (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    image LONGBLOB NOT NULL,
    description TEXT NOT NULL,
    parent_category_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (parent_category_id) REFERENCES category(id)
);

CREATE TABLE product (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    title VARCHAR(100) NOT NULL,
    rating_count INT NOT NULL,
    rating INT CHECK (rating >= 0 AND rating <= 5),
    brand_id BIGINT,
    category_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (brand_id) REFERENCES brand(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);


CREATE TABLE user (
    id BIGINT AUTO_INCREMENT,
    user_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(100) UNIQUE NOT NULL,
    user_role ENUM('ADMIN', 'VENDOR', 'CUSTOMER') NOT NULL,
    address VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE product_review (
    id BIGINT AUTO_INCREMENT,
    rate DOUBLE CHECK (rate >= 0.5 AND rate <= 5.0),
    description TEXT NOT NULL,
    image LONGBLOB,
    customer_id BIGINT,
    product_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (customer_id) REFERENCES user(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);


CREATE TABLE sub_category_attribute (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE product_attribute_value  (
    id BIGINT,
    sub_category_attribute_id BIGINT ,
    value VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (sub_category_attribute_id) REFERENCES sub_category_attribute(id)
);

CREATE TABLE sub_category_attributes (
    sub_category_attribute_id BIGINT,
    sub_category_id BIGINT,
    PRIMARY KEY (sub_category_attribute_id, sub_category_id),
    FOREIGN KEY (sub_category_attribute_id) REFERENCES sub_category_attribute(id),
    FOREIGN KEY (sub_category_id) REFERENCES category(id)
);


CREATE TABLE Vendor_Product (
    product_id BIGINT,
    vendor_id BIGINT,
    description VARCHAR(100) NOT NULL,
    stock INT NOT NULL CHECK (stock >= 0),
    price DOUBLE CHECK (price >= 0.1),
    discount DOUBLE CHECK (discount >= 0.0 AND discount <= 100.0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (product_id, vendor_id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product(id),
    CONSTRAINT fk_vendor FOREIGN KEY (vendor_id) REFERENCES user(id)
);

CREATE TABLE vendor_product_image (
    id BIGINT AUTO_INCREMENT,
    image LONGBLOB NOT NULL,
    product_id BIGINT,
    vendor_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (vendor_id) REFERENCES user(id)
);
