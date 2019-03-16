package com.brangelov.junitapp.entities;

import java.util.UUID;

public class Entity {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }
}
