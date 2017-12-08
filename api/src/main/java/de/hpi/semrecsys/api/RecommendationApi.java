package de.hpi.semrecsys.api;

import de.hpi.semrecsys.Recommendation;
import de.hpi.semrecsys.RecommendationImpl;
import de.hpi.semrecsys.dto.RecommendationDto;
import de.hpi.semrecsys.repository.RecommendationRepository;
import de.hpi.semrecsys.service.ProductService;
import de.hpi.semrecsys.service.RecommenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;


@RestController
@RequestMapping("api/v1/recommendations")
public class RecommendationApi {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private RecommenderService recommenderService;

    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET)
    public List<RecommendationImpl> get() {
        return recommendationRepository.findAll();
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "{productId}", method = RequestMethod.GET)
    public List<RecommendationImpl> getForProduct(@PathVariable int productId){
        return recommendationRepository.findByIdProductId(productId);
    }

    @Transactional(readOnly = false)
    @RequestMapping(value="{productId}", method = RequestMethod.POST)
    public List<RecommendationDto> recommend(@PathVariable int productId) {
        List<Recommendation> recommendationResults = recommenderService.recommendProduct(productId);
        return recommendationResults.stream().map(RecommendationDto::new).collect(Collectors.toList());
    }
}