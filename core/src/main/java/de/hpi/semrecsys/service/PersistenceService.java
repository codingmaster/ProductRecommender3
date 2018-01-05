package de.hpi.semrecsys.service;

import de.hpi.semrecsys.AttributeTable;
import de.hpi.semrecsys.GeneratedRecommendation;
import de.hpi.semrecsys.OptionTable;
import de.hpi.semrecsys.ProductId;
import de.hpi.semrecsys.ProductTable;
import de.hpi.semrecsys.model.Product;
import de.hpi.semrecsys.output.RecommendationResult;
import de.hpi.semrecsys.repository.AttributeRepository;
import de.hpi.semrecsys.repository.OptionRepository;
import de.hpi.semrecsys.repository.ProductRepository;
import de.hpi.semrecsys.repository.RecommendationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersistenceService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    private static final Logger LOG = LoggerFactory.getLogger(PersistenceService.class);


    public List<ProductTable> findAll(){
        return productRepository.findAll();
    }

    public Product getProduct(String productId){
        List<ProductTable> productTableList = productRepository.findByIdEntityId(productId);
        return new Product(productId, productTableList);
    }

    @Transactional
    public ProductTable createProductTableEntry(String entityId, String attributeCode, String attributeValue) {
        AttributeTable attributeTable = getAttributeTable(attributeCode);
        OptionTable optionTable = optionRepository.findByAttributeTableIdAndValue(attributeTable.getId(), attributeValue);
        if(optionTable == null){
            optionTable = getOptionTable(attributeValue, attributeTable);
            attributeTable.getOptions().add(optionTable);
            attributeTable = attributeRepository.save(attributeTable);
        }

        ProductTable productTable = new ProductTable();
        productTable.setValue(attributeValue);
        productTable.setType("unstruct");
        productTable.setAttributeId(attributeTable.getId());
        ProductId productId = new ProductId(entityId, attributeTable.getAttributeCode(), (long)optionTable.getId());
        productTable.setId(productId);

        return productRepository.save(productTable);
    }

    @Transactional
    public OptionTable getOptionTable(String attributeValue, AttributeTable attributeTable) {
        OptionTable optionTable;
        optionTable = new OptionTable(attributeValue);
        optionTable.setAttributeTable(attributeTable);
        optionTable = optionRepository.save(optionTable);
        return optionTable;
    }

    @Transactional
    public AttributeTable getAttributeTable(String attributeCode) {
        AttributeTable attributeTable = attributeRepository.findByAttributeCode(attributeCode);
        if(attributeTable == null){
            attributeTable = new AttributeTable(attributeCode, "unstruct");
            attributeTable = attributeRepository.save(attributeTable);
        }
        return attributeTable;
    }

    public long count() {
        return productRepository.count();
    }


    @Transactional
    public void saveRecommendations(List<RecommendationResult> recommendationResults) {
        for (RecommendationResult recommendationResult : recommendationResults) {
            GeneratedRecommendation recommendation = recommendationResult.toRecommendation();
            recommendationRepository.save(recommendation);
        }
    }
}
