package de.hpi.semrecsys.service;

import de.hpi.semrecsys.model.AttributeEntity;
import de.hpi.semrecsys.model.Product;
import de.hpi.semrecsys.populator.Populator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphService {

    private Populator populator;

    public GraphService(PersistenceService persistenceService){
        this.populator = Populator.getDefault(persistenceService);
    }

    public void cleanGraph(String graphName){
        populator.cleanGraph(graphName);
    }

    public int getGraphSize(String graphName){
        return populator.getGraphSize(graphName);
    }

    public String getGraphSizes(){
        return populator.getGraphSizes();
    }

    public List<String> getGraphNames(){
        return populator.getGraphNames();
    }

    public String getEntitySimilarity(String customer){
        return populator.getEntitySimilarity(customer);
    }

    public List<AttributeEntity> getEntities(Product product){
        return populator.getEntitiesForProduct(product).getAttributeEntities();
    }

    public void populateEntities(){
        populator.populateMeta(false);
        populator.populateEntitySimilarity(false);
    }

    public void populateAttributes(){
        populator.populateAttributeSimilarity(false);
    }
}
