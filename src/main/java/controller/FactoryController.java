package main.java.controller;

import main.java.service.AnalysisService;
import main.java.repository.AnalysisDaoImpl;
import AlgorithmModule.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory pattern implementation for controller creation and dependency injection.
 * Pre-configures controllers with all required dependencies at application startup.
 */
public class FactoryController {
    private static final Map<String, Object> controllers = new HashMap<>(); // Controller registry

    /**
     * Static initialization block - runs once when class is first loaded.
     * Sets up complete dependency chain: Algorithm → Service → Controller.
     */
    static {
        // Create data access layer with file path
        AnalysisDaoImpl dao = new AnalysisDaoImpl("src/main/resources/datasource.txt");

        // Create default algorithm strategy (Kadane handles most use cases)
        KadaneAnalyzer kadaneAnalyzer = new KadaneAnalyzer();

        // Wire dependencies: inject algorithm and DAO into service
        AnalysisService kadaneService = new AnalysisService(kadaneAnalyzer, dao);

        // Create controller with configured service and store in registry
        controllers.put("analysis", new AnalysisController(kadaneService));
    }

    /**
     * Returns pre-configured analysis controller with all dependencies injected.
     * Used by HandleRequest to get ready-to-use controller.
     */
    public static AnalysisController getAnalysisController() {
        return (AnalysisController) controllers.get("analysis");
    }

    /**
     * Generic controller getter for future extensibility.
     * Allows adding new controller types without changing interface.
     */
    public static Object getController(String name) {
        return controllers.get(name);
    }
}