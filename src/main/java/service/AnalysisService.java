package main.java.service;

import AlgorithmModule.ISubarrayAnalyzer;
import AlgorithmModule.SubarrayResult;
import main.java.model.AnalysisRequest;
import main.java.repository.IAnalysisDao;

/**
 * Business logic layer that coordinates analysis algorithms with data persistence.
 * Uses Strategy pattern for algorithm selection and Repository pattern for data access.
 */
public class AnalysisService {
    private final ISubarrayAnalyzer analyzer; // Strategy for analysis algorithm
    private final IAnalysisDao dao;          // Repository for data persistence

    /**
     * Constructor with dependency injection.
     * Follows Open/Closed principle - can swap algorithms without changing code.
     */
    public AnalysisService(ISubarrayAnalyzer analyzer, IAnalysisDao dao) {
        this.analyzer = analyzer;
        this.dao = dao;
    }

    /**
     * Main business operation: analyze data and persist results.
     * Delegates algorithm execution to injected strategy, then saves.
     */
    public SubarrayResult analyzeAndSave(AnalysisRequest request) {
        SubarrayResult result = analyzer.analyze(request.getValues());
        dao.save(request, result);
        return result;
    }

    /**
     * Administrative operation - removes all stored analysis data.
     */
    public void clearAllResults() {
        dao.clear();
    }

    /**
     * Utility method for displaying all saved results to console.
     */
    public void printAllSavedResults() {
        dao.loadAll().forEach(System.out::println);
    }

    /**
     * Returns the name of currently injected algorithm.
     * Useful for debugging and logging which strategy is active.
     */
    public String getAlgorithmName() {
        return analyzer.getName();
    }

    /**
     * Exposes DAO for direct access when needed by controllers.
     * Used when controller needs special DAO operations.
     */
    public IAnalysisDao getDao() {
        return dao;
    }
}