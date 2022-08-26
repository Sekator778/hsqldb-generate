package org.example.util;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.model.Product;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * правила валідації:
 * 1) всі назви від 3х до 50 символів
 * 2) у назві продукту має бути мінімум 3 голосні
 */
public class HibernateValidateService {
    private final CopyOnWriteArrayList<Product> list;


    public HibernateValidateService(List<Product> products) {
        this.list = new CopyOnWriteArrayList<>(products);
    }

    public List<Product> validate () {
        list.parallelStream().forEach(product -> {
            Validator validator;
            try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
                validator = factory.getValidator();
            }
            if (!validator.validate(product).isEmpty()) {
                list.remove(product);
            }
        });
        return list;
    }
}
