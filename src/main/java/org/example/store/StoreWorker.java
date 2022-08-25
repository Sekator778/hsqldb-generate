package org.example.store;

import org.example.Application;
import org.example.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class StoreWorker {
    private final Product[] products;
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreWorker.class);

    public StoreWorker(Product[] products) {
        this.products = products;
    }

    public void exex(Properties properties) {
        LOGGER.info("Operation Batch started");
        var sql = "INSERT INTO products(product_id, name, article, type_id, shop_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password"));
             var statement = connection.prepareStatement(sql)) {
            for (Product product : products) {
                statement.setInt(1, product.getId());
                statement.setString(2, product.getName());
                statement.setString(3, product.getArticle());
                statement.setInt(4, product.getType());
                statement.setInt(5, product.getShop_id());
                statement.addBatch();
            }
            statement.executeBatch();
            statement.clearBatch();
            LOGGER.info("Operation Batch finished");
        } catch (SQLException e) {
            LOGGER.error("Operation Batch Fail: {}", e.getMessage());
        }
        LOGGER.info("product last was insert {}", products[Integer.parseInt(properties.getProperty("batch")) - 1]);
    }
}
