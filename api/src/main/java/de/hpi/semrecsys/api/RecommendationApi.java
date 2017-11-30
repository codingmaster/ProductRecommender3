package de.hpi.semrecsys.api;

import de.hpi.semrecsys.RecommendationImpl;
import de.hpi.semrecsys.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/v1/recommendations")
public class RecommendationApi {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET)
    public List<RecommendationImpl> get() {
        return recommendationRepository.findAll();
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public List<RecommendationImpl> getForProduct(@PathVariable int id){
        return recommendationRepository.findByIdProductId(id);
    }
}