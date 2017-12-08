package de.hpi.semrecsys.dto;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import de.hpi.semrecsys.model.Attribute;
import de.hpi.semrecsys.model.AttributeEntity;
import de.hpi.semrecsys.model.Product;

import java.util.ArrayList;
import java.util.List;

public class AttributeEntityDto {
    private ListMultimap<AttributeDto, EntityDto> attributeEntitiesMap = LinkedListMultimap.create();

    public AttributeEntityDto(Product product, List<AttributeEntity> attributeEntities) {
        attributeEntities.forEach(attributeEntity -> {
            String attributeCode = attributeEntity.getAttribute().getAttributeCode();

            List<Attribute> attributeValue = product.getAttributes().get(attributeCode);

            for (Attribute attribute : attributeValue) {
                AttributeDto attributeDto = new AttributeDto();
                attributeDto.setKey(attributeCode);
                attributeDto.setValue(attribute.getValue());

                EntityDto entityDto = new EntityDto(attributeEntity.getEntity().getLongUri());
                attributeEntitiesMap.put(attributeDto, entityDto);
            }
        });
    }

    public ListMultimap<AttributeDto, EntityDto> getAttributeEntitiesMap() {
        return attributeEntitiesMap;
    }
}
