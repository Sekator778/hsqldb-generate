package org.example.store;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.commons.dbcp2.BasicDataSource;
import org.example.Application;
import org.example.model.Product;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductStoreTest {
    private final Logger LOGGER = LoggerFactory.getLogger(ProductStoreTest.class);
    private final BasicDataSource pool = new BasicDataSource();
    Properties properties;
    private ProductStore store;
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() throws SQLException {
        properties = Application.loadProperties();
        LOGGER.info("before each setUp running");
        pool.setDriverClassName(properties.getProperty("driver"));
        pool.setUrl(properties.getProperty("url"));
        pool.setUsername(properties.getProperty("username"));
        pool.setPassword(properties.getProperty("password"));
        pool.setMaxTotal(2);
        store = new ProductStore(properties, pool.getConnection());

//        StringBuilder builder = new StringBuilder();
//        try (BufferedReader br = new BufferedReader(
//                new InputStreamReader(new FileInputStream("./db/script/scheme.sql")))
//        ) {
//            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @BeforeEach
    void fillTypeAndShop() {
    }
@Order(1)
    @Test
    void save() {
        Product product = new Product("laptop", "very nice", 1);
        store.save(product, 1);
        List<Product> all = store.findAll();
        System.out.println(all);
        assertEquals(1, all.get(0).getType());

    }
    @Order(2)
    @Test
    void whenTestSaveTypeThenOneViolation() {
        Product product = new Product("server", "very nice", 8);
//        store.save(product, 2);
        Set<ConstraintViolation<Product>> constraintViolations =
                validator.validate(product);

        assertEquals(1, constraintViolations.size());
        assertEquals( "Size of state must be from 1 to 3", constraintViolations.iterator().next().getMessage() );

    }

}