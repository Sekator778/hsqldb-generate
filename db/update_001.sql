CREATE TABLE IF NOT EXISTS orders
(
    id          serial,
    name        VARCHAR(50),
    description VARCHAR(50),
    created     timestamp,
    PRIMARY KEY (id)
);
