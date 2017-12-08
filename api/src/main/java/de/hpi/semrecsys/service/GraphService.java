package de.hpi.semrecsys.service;

import de.hpi.semrecsys.populator.Populator;
import org.springframework.stereotype.Service;

@Service
public class GraphService {

    private Populator populator;

    public GraphService(PersistenceService persistenceService){
        this.populator = Populator.getDefault(persistenceService);
    }

    public void cleanGraph(String graphName){
        populator.cleanGraph(graphName);
    }

    public int getGraphSize(String graphName){
        return populator.getGraphSize(graphName);
    }
}
