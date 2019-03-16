package com.brangelov.junitapp.services;

import com.brangelov.junitapp.entities.Entity;

import java.util.List;

public interface BaseService<T extends Entity> {

    /**
     * Get all available entities.
     *
     * @return All available entities. Never null.
     */
    List<T> getAll();

    /**
     * Get entity by id.
     *
     * @param id Entity id
     * @return The entity or null.
     */
    T getById(String id);

    /**
     * Save the entity. If no id, generates the id.
     * Replaces the existing entity if id matches.
     *
     * @param entity Entity to save
     */
    void save(T entity);
}
