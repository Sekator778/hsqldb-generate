package org.example.test;

import java.util.Random;

public class Rantest3 {
    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int t = (random.nextInt(3)+1);
            System.out.println(t);

        }
    }
}
