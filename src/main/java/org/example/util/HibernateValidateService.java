package org.example.util;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.model.Product;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HibernateValidateService {
    private final CopyOnWriteArrayList<Product> list;


    public HibernateValidateService(List<Product> products) {
        this.list = new CopyOnWriteArrayList<>(products);
    }

    public List<Product> validate () {
        list.parallelStream().forEach(product -> {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            if (validator.validate(product).isEmpty()) {
                list.remove(product);
            }
        });
        return list;
    }
    public List<Product> validate2() {
        list.parallelStream().forEach(product -> {

            int x = product.getType();
            if (x > 2 || x < 0) {
                list.remove(product);
            }
        });
        return list;
    }

    /**
     * use hibernate validator
     */
    private boolean validateModel(Product product) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(product).isEmpty();
    }
}
