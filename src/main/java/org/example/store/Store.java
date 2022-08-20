package org.example.store;

import java.util.List;

public interface Store<T> {
    T save(T model, int shopId);
    List<T> findAll();


}
