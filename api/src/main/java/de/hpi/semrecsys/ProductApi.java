package de.hpi.semrecsys;

import de.hpi.semrecsys.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
}