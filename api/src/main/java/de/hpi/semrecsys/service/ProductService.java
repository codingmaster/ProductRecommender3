package de.hpi.semrecsys.service;

import de.hpi.semrecsys.AttributeTable;
import de.hpi.semrecsys.OptionTable;
import de.hpi.semrecsys.ProductTable;
import de.hpi.semrecsys.Recommendation;
import de.hpi.semrecsys.dto.AttributeDto;
import de.hpi.semrecsys.dto.ProductDto;
import de.hpi.semrecsys.main.Recommender;
import de.hpi.semrecsys.model.Product;
import de.hpi.semrecsys.output.RecommendationResult;
import de.hpi.semrecsys.output.RecommendationResultsHolder;
import de.hpi.semrecsys.populator.Populator;
import de.hpi.semrecsys.repository.AttributeRepository;
import de.hpi.semrecsys.repository.OptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private PersistenceService persistenceService;

    private static final Logger LOG = LoggerFactory.getLogger(PersistenceService.class);


    public List<ProductTable> findProductTableEntries(){
        return persistenceService.findAll();
    }

    public void populateProduct(ProductDto productDto){
        List<ProductTable> productLines = createProduct(productDto);
        Product product = new Product(productDto.getId(), productLines);
        Populator.getDefault(persistenceService).populateProduct(product);
    }

    @Transactional
    public List<ProductTable> createProduct(ProductDto productDto) {
        List<ProductTable> productTableList = new ArrayList<>();
        productDto.getAttributes().forEach(attribute -> {
            ProductTable product = persistenceService.createProductTableEntry(productDto.getId(), attribute.getKey(), attribute.getValue());
            productTableList.add(product);
            LOG.info("Product {} saved", product.toString());
        });
        return productTableList;
    }

    @Transactional
    public OptionTable getOptionTable(AttributeDto attribute, AttributeTable attributeTable) {
        OptionTable optionTable;
        optionTable = new OptionTable(attribute.getValue());
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

    public List<Recommendation> recommendProduct(int productId) {
        Product product = persistenceService.getProduct(productId);
        RecommendationResultsHolder recommendationResultsHolder = Recommender.getDefault(persistenceService).recommendGenerated(product);
        return recommendationResultsHolder.getRecommendationResults().stream().map(RecommendationResult::toRecommendation).collect(Collectors.toList());
    }
}
