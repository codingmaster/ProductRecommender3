package de.hpi.semrecsys.api;

import de.hpi.semrecsys.ProductTable;
import de.hpi.semrecsys.dto.ProductDto;
import de.hpi.semrecsys.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/v1/products")
public class ProductApi {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET)
    public List<ProductTable> get() {
        return productRepository.findAll();
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ProductDto create(@RequestBody ProductDto productDto) {
        ProductTable productTable = new ProductTable();
        return productDto;
    }
}