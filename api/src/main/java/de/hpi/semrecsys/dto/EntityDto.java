package de.hpi.semrecsys.dto;

public class EntityDto{

    private String entity;

    public EntityDto(String entity) {
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}
