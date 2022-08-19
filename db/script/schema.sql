drop table if exists shops, products, types;

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
    FOREIGN KEY (type_id) REFERENCES type (type_id)
);

create table if not exists shops
(
    id      INT GENERATED ALWAYS AS IDENTITY,
    address varchar(50),


)

CREATE TABLE customers
(
    customer_id   INT GENERATED ALWAYS AS IDENTITY,
    customer_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (customer_id)
);

CREATE TABLE contacts
(
    contact_id   INT GENERATED ALWAYS AS IDENTITY,
    customer_id  INT,
    contact_name VARCHAR(255) NOT NULL,
    phone        VARCHAR(15),
    email        VARCHAR(100),
    PRIMARY KEY (contact_id),
    CONSTRAINT fk_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers (customer_id)
);