DROP SCHEMA IF EXISTS customer;
CREATE SCHEMA IF NOT EXISTS customer;
SET SEARCH_PATH TO customer;

-- auto-generated definition
CREATE TABLE product
(
  id          BIGINT NOT NULL
    PRIMARY KEY,
  version     INTEGER DEFAULT 0,
  change_date TIMESTAMP,
  create_date TIMESTAMP,
  category    VARCHAR(255),
  description VARCHAR(255),
  ean_code    VARCHAR(255),
  material    VARCHAR(255),
  name        VARCHAR(255),
  brand_id    BIGINT,
  store_id    BIGINT
);

INSERT INTO product (id, version, change_date, create_date, category, description, ean_code, material, name, brand_id, store_id) VALUES (1, 1, '2017-12-05 22:59:51.000000', '2017-12-05 22:59:51.000000', 'CLOTHING', ' Cleavage and side cuts are finished with lace ribbon.', '1000000000001', 'material', ' Velour Dress', 62378, 1);
INSERT INTO product (id, version, change_date, create_date, category, description, ean_code, material, name, brand_id, store_id) VALUES (2, 1, '2017-12-05 22:59:51.000000', '2017-12-05 22:59:51.000000', 'CLOTHING', ' Belt is made of elastic band, which is partially sewed with velour.', '1000000000002', 'material', ' Black Velour Pants with crease', 62378, 1);
INSERT INTO product (id, version, change_date, create_date, category, description, ean_code, material, name, brand_id, store_id) VALUES (3, 1, '2017-12-05 22:59:51.000000', '2017-12-05 22:59:51.000000', 'CLOTHING', ' It owes its name to heart shaped, wide sleeves. Wide neckline can be worn on the back as well as on the front.', '1000000000003', 'material', ' Fluffy Oversize Sweater', 62378, 1);
INSERT INTO product (id, version, change_date, create_date, category, description, ean_code, material, name, brand_id, store_id) VALUES (4, 1, '2017-12-05 22:59:51.000000', '2017-12-05 22:59:51.000000', 'CLOTHING', ' Cape has an asymmetrical end and can be fastened in the waist. Cape has a wide hoodie with mesh lining.', '1000000000004', 'material', ' Waterproof Unisex Cape', 62378, 1);
INSERT INTO product (id, version, change_date, create_date, category, description, ean_code, material, name, brand_id, store_id) VALUES (5, 1, '2017-12-05 22:59:51.000000', '2017-12-05 22:59:51.000000', 'CLOTHING', ' Straight, narrow sleeves finished with welt.', '1000000000005', 'material', ' Black Velour Loose Shirt', 62378, 1);
INSERT INTO product (id, version, change_date, create_date, category, description, ean_code, material, name, brand_id, store_id) VALUES (6, 1, '2017-12-05 22:59:51.000000', '2017-12-05 22:59:51.000000', 'CLOTHING', ' Inspired by sportswear jerseys, unisex, elongated unique with raw edges.', '1000000000006', 'material', ' Raw Edge Tunique', 24994, 1);
INSERT INTO product (id, version, change_date, create_date, category, description, ean_code, material, name, brand_id, store_id) VALUES (7, 1, '2017-12-05 22:59:51.000000', '2017-12-05 22:59:51.000000', 'CLOTHING', ' Inspired by the famous MA-1 flight jacket.', '1000000000007', 'material', ' Wool Bomber Jacket', 24994, 1);
INSERT INTO product (id, version, change_date, create_date, category, description, ean_code, material, name, brand_id, store_id) VALUES (8, 1, '2017-12-05 22:59:51.000000', '2017-12-05 22:59:51.000000', 'CLOTHING', ' Loose fit bomber jacket made of high-quality denim, inspired by the `90s. It has two large pockets and a belt with round eyelets. Fastened with metal pins.', '1000000000008', 'material', ' Unisex Bomber Jacket Danger', 51749, 1);
INSERT INTO product (id, version, change_date, create_date, category, description, ean_code, material, name, brand_id, store_id) VALUES (9, 1, '2017-12-05 22:59:51.000000', '2017-12-05 22:59:51.000000', 'ACCESSOIRES', ' Feel the city and enjoy the freedom. With this capacious bag made from luxurious lamb leather, you can easily carry all your personal belongings and become the real Urban Nomad!', '1000000000009', 'material', ' Lambs Leather Basic Bag', 32953, 1);
INSERT INTO product (id, version, change_date, create_date, category, description, ean_code, material, name, brand_id, store_id) VALUES (10, 1, '2017-12-05 22:59:51.000000', '2017-12-05 22:59:51.000000', 'ACCESSOIRES', ' For the Tromso collection we used lamb leather combined with cold proof filling and rubber elements for the reinforcement. Do not mind the latitude. Just grab the Tromso Bag and start your Arctic adventure.', '1000000000010', 'material', ' Lambs Leather Trismo Bag', 32953, 1);
INSERT INTO product (id, version, change_date, create_date, category, description, ean_code, material, name, brand_id, store_id) VALUES (11, 1, '2017-12-05 22:59:51.000000', '2017-12-05 22:59:51.000000', 'ACCESSOIRES', ' Harmonized precision and elegance, nature along with technology. This collection is based on a combination of 100% pure Merino Wool felt, naturally tanned leather and heavy duty rubber.', '1000000000011', 'material', ' Zuerich', 32953, 1);

INSERT INTO public.attribute(code, type) VALUES ('tag', 'unstruct'), ('name', 'unstruct'), ('material', 'unstruct'), ('description', 'unstruct');

INSERT INTO public.option (value,attribute_id) SELECT DISTINCT
                                                                     tag,
                                                 (SELECT id from public.attribute WHERE code = 'tag')
                                                                   FROM product_tag
                                                                   ORDER BY tag;
INSERT INTO public.option (value,attribute_id) SELECT DISTINCT
                                                                     name,
                                                 (SELECT id from public.attribute WHERE code = 'name')
                                                                   FROM product
                                                                   ORDER BY name;
INSERT INTO public.option (value,attribute_id) SELECT DISTINCT
                                                                     material,
                                                 (SELECT id from public.attribute WHERE code = 'material')
                                                                   FROM product
                                                                   ORDER BY material;
INSERT INTO public.option (value,attribute_id) SELECT DISTINCT
                                                                     description,
                                                 (SELECT id from public.attribute WHERE code = 'description')
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
       o.value AS value
     FROM recommender.store_product AS p
       JOIN product_tag AS t ON p.product_id = t.product_id
       JOIN public.option AS o ON t.tag = o.value
     WHERE o.type = 'tag'
     UNION

     SELECT
       p.product_id,
       p.serial_id    AS entity_id,
       o.attribute_id AS attribute_id,
       o.type         AS attribute_code,
       o.id           AS option_id,
       o.value AS value
     FROM recommender.store_product AS p
       JOIN public.option AS o ON p.name = o.value
     WHERE o.type = 'name'

     UNION

     SELECT
       p.product_id,
       p.serial_id    AS entity_id,
       o.attribute_id AS attribute_id,
       o.type         AS attribute_code,
       o.id           AS option_id,
       o.value AS value
     FROM recommender.store_product AS p
       JOIN public.option AS o ON p.material = o.value
     WHERE o.type = 'material'

     UNION

     SELECT
       p.product_id,
       p.serial_id    AS entity_id,
       o.attribute_id AS attribute_id,
       o.type         AS attribute_code,
       o.id           AS option_id,
       o.value AS value
     FROM recommender.store_product AS p
       JOIN public.option AS o ON p.description = o.value
     WHERE o.type = 'description') AS t1;



