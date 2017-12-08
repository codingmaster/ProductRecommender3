package de.hpi.semrecsys.api;

import de.hpi.semrecsys.ProductTable;
import de.hpi.semrecsys.dto.ProductDto;
import de.hpi.semrecsys.service.GraphService;
import de.hpi.semrecsys.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/v1/products")
public class ProductApi {

    @Autowired
    private ProductService productService;

    @Autowired
    GraphService graphService;

    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET)
    public List<ProductTable> get() {
        return productService.findProductTableEntries();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ProductDto create(@RequestBody ProductDto productDto) {
        productService.populateProduct(productDto);
        return productDto;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, Long> delete(@RequestParam String customer, @RequestParam boolean withGraphs){
        return productService.deleteAllProducts(customer, withGraphs);
    }
}