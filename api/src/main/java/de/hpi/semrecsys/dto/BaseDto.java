package de.hpi.semrecsys.dto;

import java.io.Serializable;

public abstract class BaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    public BaseDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
