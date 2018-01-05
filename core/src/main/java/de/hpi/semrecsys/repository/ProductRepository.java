package de.hpi.semrecsys.repository;

import de.hpi.semrecsys.ProductTable;

import java.util.List;

public interface ProductRepository extends BaseRepository<ProductTable>{
    List<ProductTable> findByIdEntityId(String entityId);
}
