package org.example;

import org.example.model.Product;
import org.example.service.generator.RandomProductGenerate;
import org.example.store.ProductStore;
import org.example.util.ReaderProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws SQLException {
        long timeStart = System.currentTimeMillis();
        ProductStore store = null;
        Properties properties = ReaderProperty.loadProperties();
        int number_of_inserts = Integer.parseInt(properties.getProperty("max"));
        int batch_size = Integer.parseInt(properties.getProperty("batch"));
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password"))) {
            if (connection != null) {
                LOGGER.info("Connection created successfully");
                store = new ProductStore(connection);
                RandomProductGenerate generate = new RandomProductGenerate();
                LOGGER.info("RandomProductGenerate created");
                generate.generateForThread(number_of_inserts, batch_size, properties);
                LOGGER.info("RandomProductGenerate.generateForThread finished");
                store.distributionProducts(connection);
                List<Product> all = store.findAll();
                LOGGER.info("Now dataBase include {} records", all.size());
                String type = "Food";
                String addressWhereMoreTypePresent = store.findAddressWhereMoreTypePresent(type);
                LOGGER.info("Result address: {}", addressWhereMoreTypePresent);
            } else {
                LOGGER.info("Problem with creating connection");
            }
        } catch (SQLException e) {
            LOGGER.error("main Application class was error with {}", e.getMessage());
        }
        long timeFinish = System.currentTimeMillis();
        long timerWorks = (timeFinish - timeStart);
        LOGGER.info("Time: ----------------> {}", timerWorks);
    }
}
