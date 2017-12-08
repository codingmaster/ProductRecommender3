package de.hpi.semrecsys.dto;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import de.hpi.semrecsys.model.AttributeEntity;

import java.util.ArrayList;
import java.util.List;

public class AttributeEntityDto {
    private ListMultimap<AttributeDto, EntityDto> attributeEntitiesMap = LinkedListMultimap.create();

    public AttributeEntityDto(List<AttributeEntity> attributeEntities) {
        attributeEntities.forEach(attributeEntity -> {
            AttributeDto attributeDto = new AttributeDto();
            attributeDto.setKey(attributeEntity.getAttribute().getAttributeCode());
            attributeDto.setValue(attributeEntity.getAttribute().getValue());

            EntityDto entityDto = new EntityDto(attributeEntity.getEntity().getUri());
            attributeEntitiesMap.put(attributeDto, entityDto);
        });
    }

    public ListMultimap<AttributeDto, EntityDto> getAttributeEntitiesMap() {
        return attributeEntitiesMap;
    }
}
