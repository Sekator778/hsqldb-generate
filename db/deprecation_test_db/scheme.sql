create table if not exists type
(
    id   serial primary key,
    name varchar(30) not null
);

create table if not exists products
(
    id         serial primary key,
    name       varchar(30) not null,
    article    varchar(50),
    expiration date    not null,
    type_id  integer,
    foreign key (type_id) references type (id)
);

create table if not exists stores
(
    id      serial primary key,
    name    varchar not null,
    address varchar(50)
);

create table if not exists stores_products
(
    store_id   integer not null,
    product_id integer not null,
    foreign key (store_id) references stores (id),
    foreign key (product_id) references products (id)
);