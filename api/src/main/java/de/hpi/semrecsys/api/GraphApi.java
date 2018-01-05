package de.hpi.semrecsys.api;


import com.google.common.collect.LinkedListMultimap;
import de.hpi.semrecsys.dto.AttributeEntityDto;
import de.hpi.semrecsys.model.Product;
import de.hpi.semrecsys.service.GraphService;
import de.hpi.semrecsys.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/graphs")
public class GraphApi {

    @Autowired
    GraphService graphService;

    @Autowired
    PersistenceService persistenceService;

    @Transactional()
    @RequestMapping(value = "/clean", method = RequestMethod.DELETE)
    public void cleanGraph(@RequestParam String graphName){
        graphService.cleanGraph(graphName);
    }

    @Transactional()
    @RequestMapping(value = "/size", method = RequestMethod.GET)
    public int getGraphSize(@RequestParam(value="graph") String graphName){
        return graphService.getGraphSize(graphName);
    }

    @Transactional()
    @RequestMapping(value = "/sizes", method = RequestMethod.GET)
    public String getGraphSizes(){
        return graphService.getGraphSizes();
    }

    @Transactional()
    @RequestMapping(method = RequestMethod.GET)
    public List<String> getGraphs(){
        return graphService.getGraphNames();
    }

    @Transactional()
    @RequestMapping(value="/entities", method = RequestMethod.GET)
    public String getEntitySimilarity(String customer){
        return graphService.getEntitySimilarity(customer);
    }

    @Transactional()
    @RequestMapping(value="/entities/{productId}", method = RequestMethod.GET)
    public Map getEntitiesForProduct(@PathVariable int productId){
        Product product = persistenceService.getProduct(productId);
        AttributeEntityDto attributeEntityDto = new AttributeEntityDto(product, graphService.getEntities(product));
        return ((LinkedListMultimap) attributeEntityDto.getAttributeEntitiesMap()).asMap();
    }

    @Transactional(readOnly = false)
    @RequestMapping(value="/entities", method = RequestMethod.POST)
    public void populateEntities() {
        graphService.populateEntities();
    }

    @Transactional(readOnly = false)
    @RequestMapping(value="/attributes", method = RequestMethod.POST)
    public void populateAttributes() {
        graphService.populateAttributes();
    }



}
