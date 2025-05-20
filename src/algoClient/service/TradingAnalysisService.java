package algoClient.service;

import AlgorithmModule.ISubarrayAnalyzer;
import AlgorithmModule.SubarrayResult;
import algoClient.model.request.TradingAnalysisRequest;
import algoClient.repository.IAnalysisResultDao;

/**
 * Core service class that uses a subarray analyzer strategy to process requests.
 */
public class TradingAnalysisService {
    private final ISubarrayAnalyzer analyzer;
    private final IAnalysisResultDao dao;

    public TradingAnalysisService(ISubarrayAnalyzer analyzer, IAnalysisResultDao dao) {
        this.analyzer = analyzer;
        this.dao = dao;
    }

    // Get the output from the SubarrayResult using strategy design pattern, and write it to the file.
    public SubarrayResult analyzeAndSave(TradingAnalysisRequest request) {
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