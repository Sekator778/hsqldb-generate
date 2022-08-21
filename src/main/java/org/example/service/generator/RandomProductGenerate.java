package org.example.service.generator;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.model.Product;
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
    private static void viewList(List<List<Product>> data){
        System.out.println("length " + data.size());
        for (List<Product> list : data             ) {
            System.out.println("==============");
            System.out.println("inner list size " + list.size());
            System.out.println(list);
            System.out.println("==============");
        }
    }
}
