package org.example;

import com.sun.el.parser.AstPlus;
import org.example.store.ProductStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getSimpleName());
    private static final String PROPERTIES_FILE_NAME = "app.properties";

    public static final int TARGET_COUNT = 10_000_000;

    public static void main(String[] args) {
        var properties = loadProperties();
        var productStore = new ProductStore(properties);

    }

    private static Properties loadProperties() {
        LOGGER.info("load application");
        System.setProperty("file.encoding", "UTF-8");
        var properties = new Properties();
        File file = new File("./app.properties");
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
