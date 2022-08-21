package org.example.store;

import jakarta.validation.Validator;
import org.apache.commons.dbcp2.BasicDataSource;
import org.example.Application;
import org.example.model.Product;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductStoreTest {
    private final Logger LOGGER = LoggerFactory.getLogger(ProductStoreTest.class);
    private final BasicDataSource pool = new BasicDataSource();
    private ProductStore store;
    private static Validator validator;

//    @BeforeAll
//    public static void setUpValidator() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }

    @BeforeEach
    void setUp() throws SQLException {
        Properties properties = Application.loadProperties();
        LOGGER.info("before each setUp running");
        pool.setDriverClassName(properties.getProperty("driver"));
        pool.setUrl(properties.getProperty("url"));
        pool.setUsername(properties.getProperty("username"));
        pool.setPassword(properties.getProperty("password"));
        pool.setMaxTotal(2);
        store = new ProductStore(properties, pool.getConnection());
    }

    @BeforeEach
    void fillTypeAndShop() {
    }

    @Order(1)
    @Test
    void save() {
        Product product = new Product(1, "laptop", "very nice", 1, 1);
        ProductStore.save(product);
        List<Product> all = store.findAll();
        System.out.println(all);
        assertEquals(1, all.get(0).getType());
    }

//    @Order(2)
//    @Test
//    void whenTestSaveTypeThenOneViolation() {
//        Product product = new Product("server", "very nice", 8);
//        Set<ConstraintViolation<Product>> constraintViolations =
//                validator.validate(product);
//
//        assertEquals(1, constraintViolations.size());
//        assertEquals("Size of state must be from 1 to 3", constraintViolations.iterator().next().getMessage());
//    }

    @Order(3)
    @Test
    void saveManyProductWithId() {
       store.saveAll();
        List<Product> all = store.findAll();
        assertEquals(100, all.size());
    }

//    @Order(4)
//    @Test
//    void saveManyProductWithId4() {
//        Product product = new Product(1, "laptop1", "very nice", 1);
//        Product product2 = new Product(2, "laptop2", "very nice", 2);
//        Product product3 = new Product(3, "laptop3", "very nice", 0);
//        Product product4 = new Product(4, "laptop4", "very nice", 2);
//        ProductStore.save(product);
//        ProductStore.save(product2);
//        ProductStore.save(product3);
//        ProductStore.save(product4);
//        List<Product> all = store.findAll();
//        System.out.println(all);
//    }

    @Order(5)
    @Test
    void saveManyProductWithButchUseStatement() {
        store.saveAllWithBatchUseStatement(100_000);
        List<Product> all = store.findAll();
        assertEquals(100_000, all.size());
    }

    @Order(6)
    @Test
    void saveManyProductWithButch() {
        store.saveAllWithBatch(1_000_000);
        List<Product> all = store.findAll();
        assertEquals(1_000_000, all.size());
    }

}