package de.hpi.semrecsys.dto;

import java.io.Serializable;

public abstract class BaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    public BaseDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
