package org.example.store;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.model.Product;
import org.example.service.generator.RandomProductGenerate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ProductStoreService {
    private final int maxSize;
    private final int batchSize;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductStoreService.class);


    private static final HikariConfig config = new HikariConfig();
    private   HikariDataSource hikariDataSource;


    public ProductStoreService(int maxSize, int batchSize) {
        this.maxSize = maxSize;
        this.batchSize = batchSize;
        hikariDataSource = new HikariDataSource(hikariConfig());
    }

    private static HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        config.setUsername("sa");
        config.setPassword("");
        return config;
    }

    public void saveAllJdbcBatchCallable1() {
        System.out.println("insert using jdbc batch, threading");
        System.out.print("cp size " + hikariDataSource.getMaximumPoolSize());
        System.out.println(" batch size " + batchSize);
        List<List<Product>> listOfBookSub = RandomProductGenerate.createSubList(maxSize, batchSize);
        ExecutorService executorService = Executors.newFixedThreadPool(hikariDataSource.getMaximumPoolSize());
        List<Callable<Integer>> callables = listOfBookSub.stream().map(sublist ->
                (Callable<Integer>) () -> {
//                System.out.println("Inserting " + sublist.size() + " using callable from thread" + Thread.currentThread().getName());
                    saveAllJdbcBatch(sublist);
                    return sublist.size();
                }).collect(Collectors.toList());
        try {
            List<Future<Integer>> futures = executorService.invokeAll(callables);
            int count = 0;
            for (Future<Integer> future : futures) {
                count += future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void saveAllJdbcBatchCallable() {
        System.out.println("insert using jdbc batch, threading");
        ExecutorService executorService = Executors.newFixedThreadPool(hikariDataSource.getMaximumPoolSize());
        List<List<Product>> listOfBookSub = RandomProductGenerate.createSubList(maxSize, batchSize);
        List<Callable<Void>> callables = listOfBookSub.stream().map(sublist ->
                (Callable<Void>) () -> {
                    saveAllJdbcBatch(sublist);
                    return null;
                }).collect(Collectors.toList());
        try {
            LOGGER.info("before invoke");
            executorService.invokeAll(callables);
            LOGGER.info("after invoke");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void saveAllJdbcBatch(List<Product> productData) {
        LOGGER.info("jdbc batch use");
        var sql = "INSERT INTO product(product_id, name, article, type_id, shop_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = hikariDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            int counter = 0;
            for (Product model : productData) {
                statement.clearParameters();
                statement.setInt(1, model.getId());
                statement.setString(2, model.getName());
                statement.setString(3, model.getArticle());
                statement.setInt(4, model.getType());
                statement.setInt(5, model.getShop_id());
                statement.addBatch();
                if ((counter + 1) % batchSize == 0 || (counter + 1) == productData.size()) {
                    statement.executeBatch();
                    statement.clearBatch();
                }
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("jdbc batch finish");

    }
}
