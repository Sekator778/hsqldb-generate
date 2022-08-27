package org.example.service.generator;

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
    public boolean generate(int count, int batchSize, Properties properties) {
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
                        (random.nextInt(3) + 1));
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
                    new StoreWorker(products).insertWithBatch(properties);
                    products = new Product[batchSize];
                    i = 0;
                }
            }
        }
        LOGGER.info("generated random data finished");
        return products.length != 0;
    }
}
