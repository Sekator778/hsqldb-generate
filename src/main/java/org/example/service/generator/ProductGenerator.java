package org.example.service.generator;

import java.util.Properties;

public interface ProductGenerator {
    boolean generate(int count, int batchSize, Properties properties);
}
