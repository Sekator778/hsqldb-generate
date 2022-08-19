package org.example.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


 class OrdersStoreTest {
    private final BasicDataSource pool = new BasicDataSource();

    @BeforeEach
    public void setUp() throws SQLException {
        System.out.println("before each");
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/update_001.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Connection connection = pool.getConnection();
        String truncate = "DROP TABLE IF EXISTS orders;";
        Statement statement = connection.createStatement();
        statement.executeUpdate(truncate);
        connection.commit();
        connection.prepareStatement(builder.toString()).executeUpdate();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrdersStore store = new OrdersStore(pool);

        store.save(Order.of("name1", "description1"));

        List<Order> all = (List<Order>) store.findAll();
        assertEquals(all.size(),(1));
        assertEquals(all.get(0).getDescription(),("description1"));
        assertEquals(all.get(0).getId(),(1));
    }

    @Test
    public void whenUpdateOrderAndFindAllTwoRowThanChangeOneRow() {
        OrdersStore store = new OrdersStore(pool);

        store.save(Order.of("name1", "description1"));
        store.update(1, "newName");

        List<Order> all = (List<Order>) store.findAll();
        assertEquals(all.get(0).getName(),("newName"));
    }

    @Test
    public void testFindByName() {
        OrdersStore store = new OrdersStore(pool);

        store.save(Order.of("name1", "description1"));
        store.save(Order.of("name2", "description2"));
        store.save(Order.of("name3", "description3"));

        Order name1 = store.findByName("name1");
        assertEquals(name1.getDescription(),("description1"));
        assertEquals(store.findByName("name2").getDescription(),("description2"));
        assertEquals(store.findByName("name3").getDescription(),("description3"));
    }

    @Test
    public void findById() {
        OrdersStore store = new OrdersStore(pool);

        store.save(Order.of("name1", "description1"));
        store.save(Order.of("name2", "description2"));
        store.save(Order.of("name3", "description3"));

        assertEquals(store.findById(1).getDescription(),("description1"));
        assertEquals(store.findById(2).getDescription(),("description2"));
        assertEquals(store.findById(3).getDescription(),("description3"));
    }
}