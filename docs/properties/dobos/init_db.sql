DROP TABLE IF EXISTS recommender.options;
DROP TABLE IF EXISTS recommender.store_product;
DROP TABLE IF EXISTS recommender.product;


CREATE TABLE recommender.store_product
(
  serial_id   SERIAL UNIQUE PRIMARY KEY,
  product_id  BIGINT NOT NULL,
  category    VARCHAR(255),
  description VARCHAR(255),
  ean_code    VARCHAR(255),
  material    VARCHAR(255),
  name        VARCHAR(255)
);

CREATE TABLE recommender.options
(
  id           SERIAL UNIQUE PRIMARY KEY,
  option_value VARCHAR(255) NOT NULL,
  type         VARCHAR(255) NOT NULL,
  attribute_id INT
);

INSERT INTO recommender.options (option_value, type, attribute_id) SELECT DISTINCT
                                                                     tag,
                                                                     'tag',
                                                                     1
                                                                   FROM product_tag
                                                                   ORDER BY tag;
INSERT INTO recommender.options (option_value, type, attribute_id) SELECT DISTINCT
                                                                     name,
                                                                     'name',
                                                                     2
                                                                   FROM product
                                                                   ORDER BY name;
INSERT INTO recommender.options (option_value, type, attribute_id) SELECT DISTINCT
                                                                     material,
                                                                     'material',
                                                                     3
                                                                   FROM product
                                                                   ORDER BY material;
INSERT INTO recommender.options (option_value, type, attribute_id) SELECT DISTINCT
                                                                     description,
                                                                     'description',
                                                                     4
                                                                   FROM product
                                                                   ORDER BY description;

INSERT INTO recommender.store_product (product_id, category, description, ean_code, material, name)
  SELECT
    id AS product_id,
    category,
    description,
    ean_code,
    material,
    name
  FROM product;

CREATE TABLE recommender.product AS
  SELECT
    'unstruct'  AS type,
    product_id,
    entity_id,
    attribute_id,
    attribute_code,
    option_id,
    trim(value) AS value
  FROM
    (SELECT
       p.product_id,
       p.serial_id    AS entity_id,
       o.attribute_id AS attribute_id,
       o.type         AS attribute_code,
       o.id           AS option_id,
       o.option_value AS value
     FROM recommender.store_product AS p
       JOIN product_tag AS t ON p.product_id = t.product_id
       JOIN recommender.options AS o ON t.tag = o.option_value
     WHERE o.type = 'tag'
     UNION

     SELECT
       p.product_id,
       p.serial_id    AS entity_id,
       o.attribute_id AS attribute_id,
       o.type         AS attribute_code,
       o.id           AS option_id,
       o.option_value AS value
     FROM recommender.store_product AS p
       JOIN recommender.options AS o ON p.name = o.option_value
     WHERE o.type = 'name'

     UNION

     SELECT
       p.product_id,
       p.serial_id    AS entity_id,
       o.attribute_id AS attribute_id,
       o.type         AS attribute_code,
       o.id           AS option_id,
       o.option_value AS value
     FROM recommender.store_product AS p
       JOIN recommender.options AS o ON p.material = o.option_value
     WHERE o.type = 'material'

     UNION

     SELECT
       p.product_id,
       p.serial_id    AS entity_id,
       o.attribute_id AS attribute_id,
       o.type         AS attribute_code,
       o.id           AS option_id,
       o.option_value AS value
     FROM recommender.store_product AS p
       JOIN recommender.options AS o ON p.description = o.option_value
     WHERE o.type = 'description') AS t1;



