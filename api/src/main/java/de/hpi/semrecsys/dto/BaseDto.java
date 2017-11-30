package de.hpi.semrecsys.dto;

import java.io.Serializable;

public abstract class BaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    public BaseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
