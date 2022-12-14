package org.example.store;

import org.apache.commons.lang3.RandomUtils;
import org.example.model.Product;
import org.example.util.SqlSchemeReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * its created tables and fill it
 */
public class ProductStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductStore.class);
    /**
     * connection one for more jobs
     */
    private static Connection connection;

    public ProductStore(Connection connection) {
        ProductStore.connection = connection;
    }

    /**
     * drop and crate tables
     */
    public boolean initScheme() {
        boolean result;
        SqlSchemeReader reader = new SqlSchemeReader(connection);
        result = reader.readSqlFileAndRunSqlCommand();
        LOGGER.info("MariaDB config use !");
        return result;
    }

    /**
     * select all data from product table
     *
     * @return - list with all record
     */
    public List<Product> findAll() {
        LOGGER.info("Load All Products");
        var sql = "SELECT * FROM products";
        var products = new ArrayList<Product>();
        try (var statement = connection.prepareStatement(sql)) {
            var selection = statement.executeQuery();
            while (selection.next()) {
                products.add(new Product(
                        selection.getInt("product_id"),
                        selection.getString("name"),
                        selection.getString("article"),
                        selection.getInt("type_id")
                ));
            }
        } catch (Exception e) {
            LOGGER.error("Operation fail: { }", e.getCause());
            throw new IllegalStateException();
        }
        return products;
    }

    /**
     * select address store
     * where the specified type of product is the most
     *
     * @return - address
     */
    public String findAddressWhereMoreTypePresent(String type) {
        String result = "";
        LOGGER.info("Find store");
        var QUERY = "SELECT S.address, count(*) as count\n" +
                "FROM stores S join stores_products SP on S.id = SP.store_id\n" +
                "              join products P on SP.product_id = P.product_id\n" +
                "              join type T on P.type_id = T.type_id\n" +
                "where T.name = " +
                "'" +
                type + "' " +
                "group by S.id\n" +
                "LIMIT 1;";
        try (var statement = connection.prepareStatement(QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result = resultSet.getString("address");
            }
            if (result.length() != 0) {
                LOGGER.info("Store found successfully !!!");
            } else {
                return "check param type please";
            }
        } catch (Exception e) {
            LOGGER.error("Operation fail: { }", e.getCause());
            throw new IllegalStateException();
        }
        return result;
    }

    public void distributionProducts(Connection connection, int sizeListProducts, int sizeBatch) {
        int countShops = 3 + 1;
        int i = 0;
        LOGGER.info("--- insert into table stores_products ---");
        var sql = "INSERT INTO stores_products(store_id, product_id) VALUES (?, ?)";
        try (var statement = connection.prepareStatement(sql)) {
            while (sizeListProducts != 0) {
                statement.setInt(1, RandomUtils.nextInt(1, countShops));
                statement.setInt(2, sizeListProducts--);
                statement.addBatch();
                if (i == sizeBatch) {
                    statement.executeBatch();
                    statement.clearBatch();
                    i = 0;
                }
                i++;
            }
            statement.executeBatch();
            LOGGER.info("Operation Batch finished");
        } catch (SQLException e) {
            LOGGER.error("Operation distributionProducts fail: {}", e.getMessage());
        }
    }
}

