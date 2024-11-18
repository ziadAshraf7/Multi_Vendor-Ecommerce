

 ALTER TABLE product_attribute_value
 Rename Column sub_category_attribute_id To attribute_id;


ALTER TABLE product_attribute_value
ADD CONSTRAINT fk_product_attribute_value
FOREIGN KEY (attribute_id) REFERENCES attribute(id);


ALTER TABLE product_attribute_value
ADD product_id BIGINT,
ADD CONSTRAINT fk_attributeValue_Product FOREIGN KEY (product_id) REFERENCES product(id);


