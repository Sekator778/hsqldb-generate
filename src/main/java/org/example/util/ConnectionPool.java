package org.example.util;//package org.example.util;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//
//public class ConnectionPool {
//    private ConnectionPool() {
//
//    }
//    /*
//     * Expects a config in the following format
//     *
//     * poolName = "test pool"
//     * jdbcUrl = ""
//     * maximumPoolSize = 10
//     * minimumIdle = 2
//     * username = ""
//     * password = ""
//     * cachePrepStmts = true
//     * prepStmtCacheSize = 256
//     * prepStmtCacheSqlLimit = 2048
//     * useServerPrepStmts = true
//     *
//     * Let HikariCP bleed out here on purpose
//     */
//    public static HikariDataSource getDataSourceFromConfig() {
//
//        HikariDataSource actualDataSource = new HikariDataSource();
//        actualDataSource.setDriverClassName("org.h2.Driver");
//        actualDataSource.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;MODE=MySQL");
//        actualDataSource.setUsername("root");
//        actualDataSource.setPassword("root");
////
////        jdbcConfig.addDataSourceProperty("cachePrepStmts", true);
////        jdbcConfig.addDataSourceProperty("prepStmtCacheSize", 256);
////        jdbcConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
////        jdbcConfig.addDataSourceProperty("useServerPrepStmts", true);
//
//        return new HikariDataSource(actualDataSource);
//    }
//}