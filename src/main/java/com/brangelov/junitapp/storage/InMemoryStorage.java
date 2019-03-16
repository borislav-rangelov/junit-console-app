package com.brangelov.junitapp.storage;

import com.brangelov.junitapp.entities.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryStorage<T extends Entity> implements Storage<T> {

    private Map<String, T> map = new HashMap<>();

    @Override
    public T getById(String id) {
        assert id != null;
        return map.get(id);
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public void save(T item) {
        if (item.getId() == null) {
            item.generateId();
        }

        map.put(item.getId(), item);
    }
}
