package org.example.store;

import org.example.model.Product;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.example.service.generator.RandomProductGenerate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductStore implements Store<Product>, AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductStore.class);
    private final String PACK = "db/script";

    private final Properties properties;

    private final Connection connection;

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

    @Override
    public Product save(Product model, int idShop) {
        LOGGER.info("save product");
        var sql = "INSERT INTO product(name, article, type_id) VALUES (?, ?, ?)";
        try (var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getName());
            statement.setString(2, model.getArticle());
            statement.setInt(3, idShop);
            statement.executeUpdate();
            var key = statement.getGeneratedKeys();
            while (key.next()) {
                model.setId(key.getInt(1));
            }
        } catch (Exception e) {
            LOGGER.error("Operation Fail: { }", e.getCause());
            throw new IllegalStateException();
        }
        return model;
    }

    public static boolean saveAll() {
        RandomProductGenerate productGenerate = new RandomProductGenerate();
        Product[] generate = productGenerate.generate(10_000_000);
        Arrays.stream(generate).forEach(System.out::println);

        return true;
    }

    @Override
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
                        selection.getInt("type_id")
                ));
            }
        } catch (Exception e) {
            LOGGER.error("Operation fail: { }", e.getCause());
            throw new IllegalStateException();
        }
        return products;
    }

    public static void main(String[] args) {
        saveAll();
    }
}
