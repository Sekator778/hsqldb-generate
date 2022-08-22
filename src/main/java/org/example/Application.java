package org.example;

import org.apache.commons.dbcp2.BasicDataSource;
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
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final String PROPERTIES_FILE_NAME = "application.properties";
    private static final BasicDataSource pool = new BasicDataSource();
    private static ProductStore store;

    public static final int TARGET_COUNT = 10_000_000;

    public static void main(String[] args) throws SQLException {
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
    }

    public static Properties loadProperties() {
        LOGGER.info("load application");
        System.setProperty("file.encoding", "UTF-8");
        var properties = new Properties();
        File file = new File("./application.properties");
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
                                Application.class.getClassLoader()
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
