package com.brangelov.junitapp.services;

import com.brangelov.junitapp.entities.Entity;
import com.brangelov.junitapp.storage.Storage;

import java.util.List;

public class BaseServiceImpl<T extends Entity> implements BaseService<T> {

    protected final Storage<T> storage;

    public BaseServiceImpl(Storage<T> storage) {
        assert storage != null;
        this.storage = storage;
    }

    @Override
    public List<T> getAll() {
        return storage.getAll();
    }

    @Override
    public T getById(String id) {
        return storage.getById(id);
    }

    @Override
    public void save(T entity) {
        storage.save(entity);
    }
}
