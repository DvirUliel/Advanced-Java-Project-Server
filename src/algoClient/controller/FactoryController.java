package algoClient.controller;

import algoClient.service.AnalysisService;
import algoClient.repository.AnalysisDaoImpl;
import AlgorithmModule.*;
import java.util.HashMap;
import java.util.Map;

public class FactoryController {
    private static final Map<String, Object> controllers = new HashMap<>();

    // This code runs when the class is first loaded
    static {
        // Initialize controllers
        AnalysisDaoImpl dao = new AnalysisDaoImpl("datasource.txt");

        // Create analysis controller with Kadane (default)
        KadaneAnalyzer kadaneAnalyzer = new KadaneAnalyzer();
        AnalysisService kadaneService = new AnalysisService(kadaneAnalyzer, dao);
        controllers.put("analysis", new AnalysisController(kadaneService));
    }

    public static AnalysisController getAnalysisController() {
        return (AnalysisController) controllers.get("analysis");
    }

    public static Object getController(String name) {
        return controllers.get(name);
    }
}