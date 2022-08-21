create table if not exists type
(
    type_id INT GENERATED ALWAYS AS IDENTITY,
    name    varchar(30),
    PRIMARY KEY (type_id)
);
create table if not exists product
(
    product_id INT GENERATED ALWAYS AS IDENTITY,
    name       varchar(10),
    article    varchar(50),
    type_id    INT,
    PRIMARY KEY (product_id),
    FOREIGN KEY (type_id) REFERENCES type (type_id),
    UNIQUE (name)
);

create table if not exists shops
(
    shop_id INT GENERATED ALWAYS AS IDENTITY,
    address varchar(50),
    PRIMARY KEY (shop_id),
    UNIQUE (address)
);

create table if not exists product_shop
(
    id         INT GENERATED ALWAYS AS IDENTITY,
    product_id INT,
    shop_id    INT,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES product (product_id),
    FOREIGN KEY (shop_id) REFERENCES shops (shop_id)
);