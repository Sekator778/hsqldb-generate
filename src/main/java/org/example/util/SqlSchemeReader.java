package org.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;

public class SqlSchemeReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlSchemeReader.class);
    private final Connection connection;
    private final String PACK = "db/script";

    public SqlSchemeReader(Connection connection) {
        this.connection = connection;
    }

    public boolean readSqlFileAndRunSqlCommand() {
        boolean result;
        LOGGER.info("****** Maria *******");
        LOGGER.info("--- Create tables, also feel tables type and stores ---");
        try (var statement = connection.createStatement()) {
            var sql = Files.readString(Path.of(PACK, "scheme.sql"));
            String[] split = sql.split(";");
            for (String s : split) {
                statement.execute(s);
            }
            LOGGER.info("--- created MariaDB tables done ---");
            result = true;
        } catch (Exception e) {
            LOGGER.error("Operation initScheme fail: {}", e.getMessage());
            throw new IllegalStateException();
        }
        return result;
    }
}
