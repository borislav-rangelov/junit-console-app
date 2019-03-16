package com.brangelov.junitapp.storage;

import com.brangelov.junitapp.entities.Entity;

import java.util.List;

public interface Storage<T extends Entity> {

    /**
     * Get an entity by it's id. Returns null if not found.
     *
     * @param id Id
     * @return The entity or null.
     */
    T getById(String id);

    /**
     * Get all entities.
     *
     * @return All entities. Never null.
     */
    List<T> getAll();

    /**
     * Saves the entity. If no id, generates it. Replaces an existing entity with the same id.
     *
     * @param item The entity
     */
    void save(T item);
}
