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
    shop_id    INT,
    PRIMARY KEY (product_id),
    FOREIGN KEY (type_id) REFERENCES type (type_id),
    FOREIGN KEY (shop_id) REFERENCES stores (id),
    UNIQUE (name)
);
create table if not exists stores_products
(
    store_id   INT,
    product_id INT,
    foreign key (store_id) references stores (id),
    foreign key (product_id) references products (product_id)
);