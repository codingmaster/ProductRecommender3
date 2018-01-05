CREATE SEQUENCE IF NOT EXISTS hibernate_sequence;
CREATE TABLE IF NOT EXISTS category
(
  id             SERIAL PRIMARY KEY NOT NULL,
  category_id    INTEGER      NOT NULL,
  children_count INTEGER      NOT NULL,
  level          INTEGER      NOT NULL,
  parent_id      INTEGER      NOT NULL,
  path           VARCHAR(255) NOT NULL,
  value          VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS product_category
(
  category_id INTEGER NOT NULL,
  product_id  VARCHAR(255) NOT NULL,
  CONSTRAINT product_category_pkey
  PRIMARY KEY (category_id, product_id)
);

CREATE TABLE IF NOT EXISTS product
(
  type           VARCHAR(20),
  product_id     VARCHAR(255),
  entity_id      VARCHAR(255),
  attribute_id   INTEGER,
  attribute_code VARCHAR(255),
  option_id      INTEGER,
  value          TEXT,
  value_with_entities TEXT,
  PRIMARY KEY (attribute_code, entity_id, option_id)
);

CREATE TABLE IF NOT EXISTS recommendation
(
  product_id        VARCHAR(255),
  linked_product_id VARCHAR(255),
  type              VARCHAR(255),
  position          INTEGER,
  dtype             VARCHAR(255),
  score             DOUBLE PRECISION,
  relative_score    DOUBLE PRECISION,
  PRIMARY KEY (product_id, type, position)
);

CREATE TABLE IF NOT EXISTS attribute
(
  id BIGINT PRIMARY KEY NOT NULL,
  code VARCHAR(255),
  type           VARCHAR(8) NOT NULL
);

CREATE TABLE IF NOT EXISTS option
(
  id BIGINT PRIMARY KEY NOT NULL,
  value        TEXT,
  attribute_id BIGINT
);

