package de.hpi.semrecsys.dto;

import java.util.ArrayList;
import java.util.List;

public class ProductDto extends BaseDto{
    private String imagePath;
    private List<AttributeDto> attributes = new ArrayList<>();

    public List<AttributeDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDto> attributes) {
        this.attributes = attributes;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
