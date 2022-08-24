# CREATE OR REPLACE TABLE type (
#     id   serial primary key,
#     name varchar(30) not null
# );
# CREATE OR REPLACE TABLE stores
# (
#     id      serial primary key,
#     name    varchar not null,
#     address varchar(50)
# );
# --
-- CREATE OR REPLACE TABLE products
-- (
--     id         serial primary key,
--     name       varchar(30) not null,
--     article    varchar(50),
--     expiration date    not null,
--     type_id  integer,
--     foreign key (type_id) references type (id)
-- );
--
--
--
-- CREATE OR REPLACE TABLE stores_products
-- (
--     store_id   integer not null,
--     product_id integer not null,
--     quantity int,
--     foreign key (store_id) references stores (id),
--     foreign key (product_id) references products (id)
-- );
--
-- # drop table if exists products;
-- # drop table if exists type_of_product;
-- # drop table if exists store_addresses;
-- #
-- # create table type_of_product
-- # (
-- #     id   integer auto_increment primary key,
-- #     name varchar(50) not null
-- # );
-- #
-- # create table store_addresses
-- # (
-- #     id   integer auto_increment primary key,
-- #     name varchar(200) not null
-- # );
-- #
-- # create table products
-- # (
-- #     id         bigint auto_increment primary key,
-- #     type_id    integer     not null,
-- #     name       varchar(50) not null,
-- #     quantity   integer default 0,
-- #     address_id integer     not null,
-- #     constraint fk_type
-- #         foreign key (type_id) references type_of_product (id)
-- #             on delete cascade on update cascade,
-- #     constraint fk_address
-- #         foreign key (address_id) references store_addresses (id)
-- #             on delete cascade on update cascade
-- # );

