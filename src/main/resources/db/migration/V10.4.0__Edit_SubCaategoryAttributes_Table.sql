
ALTER TABLE sub_category_attributes
 Rename Column sub_category_attribute_id To attribute_id;

ALTER TABLE sub_category_attributes
ADD CONSTRAINT fk_SubCategoryAttributes_Attribute
FOREIGN KEY (attribute_id) REFERENCES attribute(id);
