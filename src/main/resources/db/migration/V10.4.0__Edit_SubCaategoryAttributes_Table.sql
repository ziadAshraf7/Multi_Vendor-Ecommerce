ALTER TABLE sub_category_attributes
DROP FOREIGN KEY FK70rhk2mdu6timaiaoobn4qxs7;

ALTER TABLE sub_category_attributes
 Rename Column sub_category_attribute_id To attribute_id;

ALTER TABLE sub_category_attributes
ADD CONSTRAINT fk_SubCategoryAttributes_Attribute
FOREIGN KEY (attribute_id) REFERENCES attribute(id);
