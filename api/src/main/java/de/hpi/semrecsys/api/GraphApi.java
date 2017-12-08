package de.hpi.semrecsys.api;


import de.hpi.semrecsys.RecommendationImpl;
import de.hpi.semrecsys.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/graphs")
public class GraphApi {

    @Autowired
    GraphService graphService;

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
}
