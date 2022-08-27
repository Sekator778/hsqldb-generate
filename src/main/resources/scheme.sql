drop table if exists stores_products cascade;
drop table if exists products cascade;
drop table if exists stores cascade;
drop table if exists type cascade;
create table type
(
    type_id INT auto_increment primary key,
    name    varchar(30)
);
create table if not exists stores
(
    id      INT auto_increment primary key,
    address varchar(50),
    UNIQUE (address)
);
create table if not exists products
(
    product_id INT,
    name       varchar(50),
    article    varchar(12),
    type_id    INT,
    PRIMARY KEY (product_id),
    FOREIGN KEY (type_id) REFERENCES type (type_id)
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