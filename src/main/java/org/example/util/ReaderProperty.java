package org.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class ReaderProperty {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderProperty.class);
//    private static final String PROPERTIES_FILE_NAME = "application-maria.properties";
    private static final String PROPERTIES_FILE_NAME = "application-postgres.properties";
//    private static final String PROPERTIES_FILE_NAME = "application-hsqldb.properties";

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
