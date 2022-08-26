package org.example.service.generator;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.model.Product;
import org.example.store.StoreWorker;
import org.example.util.HibernateValidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RandomProductGenerate implements ProductGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomProductGenerate.class);

    @Override
    public Product[] generate(int count) {
        LOGGER.info("start generate random data with count {}", count);
        Product[] products = new Product[count];
        Random random = new Random();
        int i = 0;
        Set<String> wordSet = new HashSet<>(count);
        while (wordSet.size() < count) {
            String s = RandomStringUtils.randomAlphanumeric(8);
            boolean add = wordSet.add(s);
            if (add) {
                Product product = new Product(i, s, s, random.nextInt(3), random.nextInt(3));
                products[i++] = product;
            }
        }
        return products;
    }

    /**
     * use hibernate validator
     */
    private boolean validateModel(Product product) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(product).isEmpty();
    }

    /**
     * generate part - of list
     * for multithreading
     */
    public static List<List<Product>> createSubList(int count, int butchSize) {
        int size = count / butchSize;
        List<List<Product>> productsLists = new ArrayList<>(size);
        List<Product> subListProducts = new ArrayList<>(butchSize);
        Random random = new Random();
        int i = 0;
        int indexProduct = 0;
        Set<String> wordSet = new HashSet<>(count);
        while (wordSet.size() < count) {
            String s = RandomStringUtils.randomAlphanumeric(8);
            boolean add = wordSet.add(s);
            if (add) {
                Product product = new Product(indexProduct++, s, s, random.nextInt(3), random.nextInt(3));
                if (i == (butchSize)) {
                    productsLists.add(subListProducts);
                    subListProducts = new ArrayList<>(butchSize);
                    i = 0;
                    subListProducts.add(product);
                    continue;
                }
                subListProducts.add(product);
                i++;
            }
        }
        productsLists.add(subListProducts);
//        viewList(productsLists);
        return productsLists;
    }

    /**
     * output list to console
     * with its length
     */
    private static void viewList(List<List<Product>> data) {
        System.out.println("length " + data.size());
        for (List<Product> list : data) {
            System.out.println("==============");
            System.out.println("inner list size " + list.size());
            System.out.println(list);
            System.out.println("==============");
        }
    }

    public boolean generateForThread(int count, int batchSize, Properties properties) {
        LOGGER.info("start generate random data with count {}", count);
        Product[] products = new Product[batchSize];
        Random random = new Random();
        int minCountCharsForNameProduct = Integer.parseInt(
                properties.getProperty("minCountCharsForNameProduct"));
        int maxCountCharsForNameProduct = Integer.parseInt(
                properties.getProperty("maxCountCharsForNameArticle"));
        int i = 0;
        int index_product = 1;
        int differenceSize;
        Set<String> wordSet = new HashSet<>(count);
        while (wordSet.size() < count) {
            String name = RandomStringUtils.randomAlphanumeric(
                    minCountCharsForNameProduct, maxCountCharsForNameProduct);
            String article = RandomStringUtils.randomAlphanumeric(12);
            boolean add = wordSet.add(name);
            if (add) {
                Product product = new Product(index_product++,
                        name,
                        article,
                        (random.nextInt(3)+1),
                        (random.nextInt(3)+1));
                products[i++] = product;
                if (i == batchSize) {
                    LOGGER.info("start validate Copy On Write Use");
                    List<Product> productList = Arrays.asList(products);
                    HibernateValidateService validateService = new HibernateValidateService(productList);
                    List<Product> validate = validateService.validate();
                    LOGGER.info("size after validate {}", validate.size());
                    differenceSize = batchSize - validate.size();
                    if (differenceSize > 0) {
                        LOGGER.error("do something immediately !!!!! ");
                    }
                    LOGGER.info("arr for batch done make insert");
                    new StoreWorker(products).exex(properties);
                    products = new Product[batchSize];
                    i = 0;
                }
            }
        }
        LOGGER.info("generated random data finished");
        return products.length != 0;
    }
}
