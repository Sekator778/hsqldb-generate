package org.example.service.generator;

import org.apache.commons.lang3.RandomStringUtils;

import java.nio.*;
import java.util.*;

public class RandomWords implements Readable {
    private static Random rand = new Random(new Date().getTime());
    private static final char[] capitals =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] lowers =
            "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] vowels =
            "aeiou".toCharArray();
    private int count;

    public RandomWords(int count) {
        this.count = count;
    }

    public int read(CharBuffer cb) {
        if (count-- == 0)
            return -1;
        cb.append(capitals[rand.nextInt(capitals.length)]);
        for (int i = 0; i < 4; i++) {
            cb.append(capitals[rand.nextInt(vowels.length)]);
            cb.append(capitals[rand.nextInt(lowers.length)]);
        }
        cb.append(" ");
        return 10;
    }

    public static Set<String> randomWord() {
        Set<String> wordSet = new HashSet<>(10_000_000);
        while (wordSet.size() < 10_000_000) {
            wordSet.add(RandomStringUtils.randomAlphanumeric(8));
        }
        return wordSet;
    }

    public static void main(String[] args) {
        Set<String> strings = randomWord();
        for (String s : strings
             ) {
            System.out.println(s);
        }

    }
}