DROP TABLE IF EXISTS stores CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS type CASCADE;
DROP TABLE IF EXISTS stores_products CASCADE;
create table if not exists type
(
    type_id INT GENERATED ALWAYS AS IDENTITY,
    name    varchar(30),
    PRIMARY KEY (type_id)
);
create table if not exists stores
(
    id      INT GENERATED ALWAYS AS IDENTITY,
    address varchar(50),
    PRIMARY KEY (id),
    UNIQUE (address)
);
create table if not exists products
(
    product_id INT,
    name       varchar(50),
    article    varchar(12),
    type_id    INT,
    PRIMARY KEY (product_id),
    FOREIGN KEY (type_id) REFERENCES type (type_id),
    UNIQUE (name)
);
create table if not exists stores_products
(
    store_id   INT,
    product_id INT,
    foreign key (store_id) references stores (id),
    foreign key (product_id) references products (product_id)
);
INSERT INTO type (name)
VALUES ('Drink');
INSERT INTO type (name)
VALUES ('Food');
INSERT INTO type (name)
VALUES ('Electronic');
INSERT INTO stores (address)
VALUES ('Kyiv');
INSERT INTO stores (address)
VALUES ('Kharkiv');
INSERT INTO stores (address)
VALUES ('Dnipro');