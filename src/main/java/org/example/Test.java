package org.example;

import org.apache.commons.dbcp2.BasicDataSource;
import org.example.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws SQLException {
        Product model = new Product(1, "laptop", "very nice", 1, 1);

        BasicDataSource pool = new BasicDataSource();
        Properties properties = Application.loadProperties();
        LOGGER.info("before each setUp running");
        pool.setDriverClassName(properties.getProperty("driver"));
        pool.setUrl(properties.getProperty("url"));
        pool.setUsername(properties.getProperty("username"));
        pool.setPassword(properties.getProperty("password"));
        pool.setMaxTotal(2);
        Connection connection = pool.getConnection();
        LOGGER.info("save product");
        var sql = "INSERT INTO product(product_id, name, article, type_id, shop_id) VALUES (?, ?, ?, ?, ?)";
        try (var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, model.getId());
            statement.setString(2, model.getName());
            statement.setString(3, model.getArticle());
            statement.setInt(4, model.getType());
            statement.setInt(5, model.getShop_id());
            statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("Operation Fail: { }", e.getCause());
            throw new IllegalStateException();
        }
    }
}
