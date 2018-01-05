package de.hpi.semrecsys.service;

import de.hpi.semrecsys.AttributeTable;
import de.hpi.semrecsys.OptionTable;
import de.hpi.semrecsys.ProductTable;
import de.hpi.semrecsys.dto.AttributeDto;
import de.hpi.semrecsys.dto.ProductDto;
import de.hpi.semrecsys.model.Attribute;
import de.hpi.semrecsys.model.Product;
import de.hpi.semrecsys.populator.Populator;
import de.hpi.semrecsys.repository.AttributeRepository;
import de.hpi.semrecsys.repository.OptionRepository;
import de.hpi.semrecsys.repository.ProductRepository;
import de.hpi.semrecsys.repository.RecommendationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GraphService graphService;

    private static final Logger LOG = LoggerFactory.getLogger(PersistenceService.class);


    public List<ProductTable> findProductTableEntries(){
        return persistenceService.findAll();
    }

    public void populateProduct(ProductDto productDto){
        List<ProductTable> productLines = createProduct(productDto);
        Product product = new Product(productDto.getId(), productLines);
        Populator populator = Populator.getDefault(persistenceService);
//        populator.populateMeta(false);
        product = populator.populateProduct(product);
        updateProduct(product, productLines);
//        populator.populateEntitySimilarity(false);
    }

    @Transactional
    public void updateProduct(Product product, List<ProductTable> productLines) {
        for(Attribute attribute :  product.getFlatAttributes()){
            for(ProductTable productTable : productLines){
                if(productTable.getId().getAttributeCode().equals(attribute.getAttributeCode())
                        && productTable.getId().getOptionId() == attribute.getOptionId()){
                    productTable.setValueWithEntities(attribute.getValueWithEntities());
                }
            }
        }
        productRepository.save(productLines);
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

    @Transactional
    public Map<String, Long> deleteAllProducts(String customer, boolean withGraphs){
        Map<String, Long> sizes = new HashMap<>();
        productRepository.deleteAll();
        attributeRepository.deleteAll();
        optionRepository.deleteAll();
        recommendationRepository.deleteAll();

        if(withGraphs){
            graphService.getGraphNames().stream().filter(s -> s.contains(customer)).forEach(graphService::cleanGraph);
        }

        sizes.put("products", productRepository.count());
        sizes.put("attributes", attributeRepository.count());
        sizes.put("options", optionRepository.count());
        sizes.put("recommendations", recommendationRepository.count());
        return sizes;
    }
}
