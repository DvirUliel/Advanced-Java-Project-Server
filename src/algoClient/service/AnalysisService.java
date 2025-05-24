package algoClient.service;

import AlgorithmModule.ISubarrayAnalyzer;
import AlgorithmModule.SubarrayResult;
import algoClient.model.AnalysisRequest;
import algoClient.repository.IAnalysisDao;

/**
 * Core service class that uses a subarray analyzer strategy to process requests.
 */
public class AnalysisService {
    private final ISubarrayAnalyzer analyzer;
    private final IAnalysisDao dao;

    public AnalysisService(ISubarrayAnalyzer analyzer, IAnalysisDao dao) {
        this.analyzer = analyzer;
        this.dao = dao;
    }

    // Get the output from the SubarrayResult using strategy design pattern, and write it to the file.
    public SubarrayResult analyzeAndSave(AnalysisRequest request) {
        SubarrayResult result = analyzer.analyze(request.getValues());
        dao.save(request, result);
        return result;
    }

    public void clearAllResults() {
        dao.clear();
    }

    public void printAllSavedResults() {
        dao.loadAll().forEach(System.out::println);
    }

    public String getAlgorithmName() {
        System.out.println(analyzer.getName());
        return analyzer.getName();
    }
}