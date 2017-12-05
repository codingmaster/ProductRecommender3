package de.hpi.semrecsys.repository;

import de.hpi.semrecsys.AttributeTable;

public interface AttributeRepository extends BaseRepository<AttributeTable>{

    AttributeTable findByAttributeCode(String attributeCode);
}
