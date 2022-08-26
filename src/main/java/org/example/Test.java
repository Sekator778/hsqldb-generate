package org.example;

import org.apache.commons.lang3.RandomUtils;

public class Test {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            int i1 = RandomUtils.nextInt(1, 7);
            System.out.println(i1);
        }
    }
}
