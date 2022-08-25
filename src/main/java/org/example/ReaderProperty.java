package org.example;

import org.example.model.Product;
import org.example.store.ProductStore;
import org.example.store.ProductStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class ReaderProperty {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderProperty.class);
    private static final String PROPERTIES_FILE_NAME = "application.properties";
//    private static final String PROPERTIES_FILE_NAME = "application-hsqldb.properties";

    public static void main(String[] args) {
        long timeStart = System.currentTimeMillis();
        ProductStore store = null;
        Properties properties = loadProperties();
        int number_of_inserts = Integer.parseInt(properties.getProperty("max"));
        int butch_size = Integer.parseInt(properties.getProperty("batch"));
        Connection con = null;
        try {
            //Registering the HSQLDB JDBC driver
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            //Creating the connection with HSQLDB
            con = DriverManager.getConnection(properties.getProperty("url"), "sa", "");
            if (con != null) {
                System.out.println("Connection created successfully");
                store = new ProductStore(properties, con);
            } else {
                System.out.println("Problem with creating connection");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        ProductStoreService service = new ProductStoreService(number_of_inserts, butch_size);
        service.saveAllJdbcBatchCallable();
        assert store != null;
        List<Product> all = store.findAll();
        LOGGER.info("========================= {}", all.size());
        long timeFinish = System.currentTimeMillis();
        long   timerWorks = (timeFinish - timeStart);
        LOGGER.info("Time: ----------------> {}", timerWorks);
    }

    public static Properties loadProperties() {
        LOGGER.info("load application");
        System.setProperty("file.encoding", "UTF-8");
        var properties = new Properties();
        File file = new File(PROPERTIES_FILE_NAME);
        if (file.exists()) {
            try (InputStreamReader in = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
                properties.load(in);
                LOGGER.info("properties loading from user file");
            } catch (IOException e) {
                LOGGER.warn("No 'app.property' file {}", e.getMessage());
            }
        } else {
            try {
                properties.load(new InputStreamReader(
                        Objects.requireNonNull(
                                ReaderProperty.class.getClassLoader()
                                        .getResourceAsStream(PROPERTIES_FILE_NAME)),
                        StandardCharsets.UTF_8));
                LOGGER.info("default properties loading");
            } catch (IOException e) {
                LOGGER.warn("No 'app.properties' near jar. So using default file {}", e.getMessage());
            }
        }
        return properties;
    }
}
