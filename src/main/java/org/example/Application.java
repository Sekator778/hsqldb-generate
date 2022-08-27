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

    public static void main(String[] args) {
        long timeStart = System.currentTimeMillis();
        ProductStore store;
        Properties properties = ReaderProperty.loadProperties();
        int number_of_inserts = Integer.parseInt(properties.getProperty("max"));
        int batch_size = Integer.parseInt(properties.getProperty("batch"));
       try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.error("driver register fail {}", e.getMessage());
        }
        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password"))) {
            if (connection != null) {
                LOGGER.info("Connection created successfully");
                store = new ProductStore(connection);
                if(store.initScheme()) {
                    RandomProductGenerate generate = new RandomProductGenerate();
                    LOGGER.info("RandomProductGenerate created");
                    generate.generate(number_of_inserts, batch_size, properties);
                    LOGGER.info("RandomProductGenerate.generateForThread finished");
                    List<Product> all = store.findAll();
                    int sizeListProducts = all.size();
                    store.distributionProducts(connection,
                                                sizeListProducts,
                                                batch_size);
                    LOGGER.info("Now dataBase include {} records", sizeListProducts);
//                    String type = properties.getProperty("type");
                    String type = System.getProperty("type");
                    String addressWhereMoreTypePresent = store.findAddressWhereMoreTypePresent(type);
                    LOGGER.info("Result address: {}", addressWhereMoreTypePresent);
                } else {
                    LOGGER.info("Problem with creating tables");
                }
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
