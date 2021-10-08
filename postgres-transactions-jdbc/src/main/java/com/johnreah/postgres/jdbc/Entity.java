package com.johnreah.postgres.jdbc;

public class Entity {

    private Long id;

    public Entity(Long id) {
        this.setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
