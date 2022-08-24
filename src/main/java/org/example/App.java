package org.example;

import org.example.model.Product;
import org.example.service.generator.RandomProductGenerate;
import org.example.store.ProductStore;
import org.example.store.ProductStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws SQLException {
        long timeStart = System.currentTimeMillis();
        ProductStore store = null;
        Properties properties = Application.loadProperties();
        int number_of_inserts = Integer.parseInt(properties.getProperty("max"));
        int batch_size = Integer.parseInt(properties.getProperty("batch"));
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(properties.getProperty("url"), "sa", "");
            if (connection != null) {
                LOGGER.info("Connection created successfully");
                store = new ProductStore(properties, connection);
            } else {
                LOGGER.info("Problem with creating connection");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        RandomProductGenerate generate = new RandomProductGenerate();
        LOGGER.info("RandomProductGenerate created");
        generate.generateForThread(number_of_inserts, batch_size, properties);
        LOGGER.info("RandomProductGenerate.generateForThread finished");
        assert store != null;
        List<Product> all = store.findAll();
//        all.parallelStream().forEach(System.out::println);
        connection.close();
        LOGGER.info("========================= {}", all.size());
        long timeFinish = System.currentTimeMillis();
        long timerWorks = (timeFinish - timeStart);
        LOGGER.info("Time: ----------------> {}", timerWorks);
    }
}
