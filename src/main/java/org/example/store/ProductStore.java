package org.example.store;

import org.example.model.Product;
import org.example.service.generator.RandomProductGenerate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ProductStore implements AutoCloseable, Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductStore.class);
    private final String PACK = "db/script";

    private final Properties properties;

    private static Connection connection;

    public ProductStore(Properties properties, Connection connection) {
        this.properties = properties;
        this.connection = connection;
        initScheme();
        fillTypeAndShop();
    }

    private void fillTypeAndShop() {
        LOGGER.info("--- fill table type ---");
        try (var statement = connection.createStatement()) {
            var sql = Files.readString(Path.of(PACK, "insert.sql"));
            statement.execute(sql);
            LOGGER.info("--- insert into table type ---");
        } catch (Exception e) {
            LOGGER.error("Operation fail: {}", e.getMessage());
            throw new IllegalStateException();
        }
    }

    private void initScheme() {
        LOGGER.info("--- Create tables ---");
        try (var statement = connection.createStatement()) {
            var sql = Files.readString(Path.of(PACK, "dropAll.sql"));
            statement.execute(sql);
            LOGGER.info("--- drop all if exists table ---");
            sql = Files.readString(Path.of(PACK, "scheme.sql"));
            statement.execute(sql);
            LOGGER.info("--- created tables done ---");
        } catch (Exception e) {
            LOGGER.error("Operation fail: {}", e.getMessage());
            throw new IllegalStateException();
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * insert one element
     */
    public static Product save(Product model) {
        LOGGER.info("save product");
        var sql = "INSERT INTO product(product_id, name, article, type_id, shop_id) VALUES (?, ?, ?, ?, ?)";
        try (var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, model.getId());
            statement.setString(2, model.getName());
            statement.setString(3, model.getArticle());
            statement.setInt(4, model.getType());
            statement.setInt(5, model.getShop_id());
            statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("Operation Fail: { }", e.getCause());
            throw new IllegalStateException();
        }
        return model;
    }

    /**
     * insert use batch method
     *
     * @param count - max element for insert
     */
    public boolean saveAllWithBatch(int count) {
        RandomProductGenerate productGenerate = new RandomProductGenerate();
        Product[] generate = productGenerate.generate(count);
        int sizeBatch = 100_000;
        LOGGER.info("Operation Batch = {} started", sizeBatch);
        var sql = "INSERT INTO product(product_id, name, article, type_id, shop_id) VALUES (?, ?, ?, ?, ?)";
        try (var statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < count; i++) {
                statement.setInt(1, generate[i].getId());
                statement.setString(2, generate[i].getName());
                statement.setString(3, generate[i].getArticle());
                statement.setInt(4, generate[i].getType());
                statement.setInt(5, generate[i].getShop_id());
                statement.addBatch();
                if ((i % sizeBatch) == 0) {
                    statement.executeBatch();
                    statement.clearBatch();
                    LOGGER.info("batch use id {}", i);
                }
            }
            statement.executeBatch();
            statement.clearBatch();
        } catch (Exception e) {
            LOGGER.error("Operation Batch Fail: {}", e.getMessage());
            throw new IllegalStateException();
        }
        return true;
    }

    /**
     * insert one to one use prepareStatement
     */
    public boolean saveAll() {
        RandomProductGenerate productGenerate = new RandomProductGenerate();
        Product[] generate = productGenerate.generate(100);
        Arrays.stream(generate).forEach(ProductStore::save);
        return true;
    }

    /**
     * insert use no safety statement
     */
    public boolean saveAllWithBatchUseStatement(int count) {
        LOGGER.info("Operation Batch started with statement ");
        RandomProductGenerate productGenerate = new RandomProductGenerate();
        Product[] generate = productGenerate.generate(count);
        int sizeBatch = 100_000;
        try (var statement = connection.createStatement()) {
            for (int i = 0; i < count; i++) {
                var sql = "insert into product (product_id, name, article, type_id, shop_id) " +
                        "values ('" + generate[i].getId() + "','" + generate[i].getName() + "','" + generate[i].getArticle() + "','" + generate[i].getType() + "','" + generate[i].getShop_id() + "')";
                statement.addBatch(sql);
                if ((i % sizeBatch) == 0) {
                    statement.executeBatch();
                }
            }
            statement.executeBatch();
        } catch (Exception e) {
            LOGGER.error("Operation Batch Fail: { }", e.getCause());
            throw new IllegalStateException();
        }
        return true;
    }

    /**
     * select all data from product table
     *
     * @return - list with all record
     */
    public List<Product> findAll() {
        LOGGER.info("Load All Products");
        var sql = "SELECT * FROM product";
        var products = new ArrayList<Product>();
        try (var statement = connection.prepareStatement(sql)) {
            var selection = statement.executeQuery();
            while (selection.next()) {
                products.add(new Product(
                        selection.getInt("product_id"),
                        selection.getString("name"),
                        selection.getString("article"),
                        selection.getInt("type_id"),
                        selection.getInt("shop_id")
                ));
            }
        } catch (Exception e) {
            LOGGER.error("Operation fail: { }", e.getCause());
            throw new IllegalStateException();
        }
        return products;
    }

    /**
     * find address where max count defined type
     * @param type - product for looking
     * @return address shop
     */
    public String findShop(String type) {
        return "need more info";
    }

    @Override
    public void run() {
    }
}
