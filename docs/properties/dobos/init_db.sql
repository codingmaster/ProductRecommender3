drop TABLE IF EXISTS recommender.options;
drop TABLE IF EXISTS recommender.store_product;
drop TABLE IF EXISTS recommender.product;
drop TABLE IF EXISTS recommender.recommendation;



create table recommender.store_product
(
  serial_id SERIAL UNIQUE PRIMARY KEY ,
  product_id bigint not null,
  category varchar(255),
  description varchar(255),
  ean_code varchar(255),
  material varchar(255),
  name varchar(255)
);

create table recommender.options
(
  id SERIAL UNIQUE PRIMARY KEY ,
  option_value varchar(255) not null,
  type  varchar(255) not null,
  attribute_id int
);

create table recommender.recommendation
(
  product_id int default '0' not null,
  linked_product_id int default '0' not null,
  type varchar(32) not null,
  position int default '0' not null,
  DTYPE varchar(255) default '' not null,
  score FLOAT null,
  relative_score FLOAT null,
  primary key (product_id, type, position)
);


INSERT INTO recommender.options(option_value, type, attribute_id) SELECT distinct tag, 'tag', 1 FROM product_tag ORDER BY tag;
INSERT INTO recommender.options(option_value, type, attribute_id) SELECT distinct name, 'name',2  FROM product ORDER BY name;
INSERT INTO recommender.options(option_value, type, attribute_id) SELECT distinct material, 'material',3  FROM product ORDER BY material;
INSERT INTO recommender.options(option_value, type, attribute_id) SELECT distinct description, 'description',4  FROM product ORDER BY description;

INSERT INTO recommender.store_product(product_id, category, description, ean_code, material, name)
  SELECT id as product_id, category, description, ean_code, material, name FROM product;

create table recommender.product as
  SELECT  'unstruct' as type, entity_id, attribute_id, attribute_code, option_id, trim(value) as option_value FROM
    (SELECT p.serial_id as entity_id, o.attribute_id as attribute_id, o.type as attribute_code, o.id as option_id,
            o.option_value as value
     from recommender.store_product as p
       JOIN product_tag as t ON p.product_id = t.product_id
       JOIN recommender.options as o ON t.tag = o.option_value
     WHERE o.type = 'tag'
     UNION

     SELECT p.serial_id as entity_id, o.attribute_id as attribute_id, o.type as attribute_code, o.id as option_id,
            o.option_value as value
     from recommender.store_product as p
       JOIN recommender.options as o ON p.name = o.option_value
     WHERE o.type = 'name'

     UNION

     SELECT p.serial_id as entity_id, o.attribute_id as attribute_id, o.type as attribute_code, o.id as option_id,
            o.option_value as value
     from recommender.store_product as p
       JOIN recommender.options as o ON p.material = o.option_value
     WHERE o.type = 'material'

     UNION

     SELECT p.serial_id as entity_id, o.attribute_id as attribute_id, o.type as attribute_code, o.id as option_id,
            o.option_value as value
     from recommender.store_product as p
       JOIN recommender.options as o ON p.description = o.option_value
     WHERE o.type = 'description') as t1;



