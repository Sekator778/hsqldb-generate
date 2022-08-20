package org.example.service.generator;

import org.example.model.Product;

import java.util.List;

public interface ProductGenerator {
    Product[] generate(int count);
}
