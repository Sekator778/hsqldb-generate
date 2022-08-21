package org.example.service.generator;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.model.Product;

import java.nio.CharBuffer;
import java.util.*;

public class RandomProductGenerate implements ProductGenerator {
    public static Set<String> randomWord() {
        Set<String> wordSet = new HashSet<>(10_000_000);
        while (wordSet.size() < 10_000_000) {
            wordSet.add(RandomStringUtils.randomAlphanumeric(8));
        }
        return wordSet;
    }

    @Override
    public Product[] generate(int count) {
        Product[] products = new Product[count];
        Random random = new Random();
        int i = 0;
        Set<String> wordSet = new HashSet<>(count);
        while (wordSet.size() < count) {
            String s = RandomStringUtils.randomAlphanumeric(8);
            boolean add = wordSet.add(s);
            if (add) {
                Product product = new Product(s, s, random.nextInt(3) + 1);
                products[i++] = product;
            }

        }
        return products;
    }
}
