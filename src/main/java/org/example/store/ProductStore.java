package org.example.store;

import org.example.model.Product;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductStore implements Store<Product>, AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductStore.class);

    private final Properties properties;

    private Connection connection;

    public ProductStore(Properties properties) {
        this.properties = properties;
    }
    private void initConnection() {
        LOGGER.info("Connect to DataBase");

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Product save(Product model) {
        return null;
    }

    @Override
    public List<Product> findAll() {
        return null;
    }
}
