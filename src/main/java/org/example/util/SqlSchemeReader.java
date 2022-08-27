package org.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

public class SqlSchemeReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlSchemeReader.class);
    private final Connection connection;

    public SqlSchemeReader(Connection connection) {
        this.connection = connection;
    }

    public boolean readSqlFileAndRunSqlCommand() {
        boolean result = false;
        LOGGER.info("****** Maria *******");
        LOGGER.info("--- Create tables, also feel tables type and stores ---");
        try (var statement = connection.createStatement()) {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("scheme.sql");
            assert inputStream != null;
            String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String[] split = sql.split(";");
            for (String s : split) {
                statement.execute(s);
            }
            LOGGER.info("--- created MariaDB tables done ---");
            result = true;
        } catch (Exception e) {
            LOGGER.error("Operation initScheme fail: {}", e.getMessage());
        }
        return result;
    }
}
