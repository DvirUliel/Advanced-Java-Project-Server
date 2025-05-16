package algoClient.service;

import AlgorithmModule.ISubarrayAnalyzer;
import AlgorithmModule.SubarrayResult;
import algoClient.model.subarray.SubarrayRequest;
import algoClient.repository.ISubarrayDao;

/**
 * Core service class that uses a subarray analyzer strategy to process requests.
 */
public class SubarrayService {
    private final ISubarrayAnalyzer analyzer;
    private final ISubarrayDao dao;

    public SubarrayService(ISubarrayAnalyzer analyzer, ISubarrayDao dao) {
        this.analyzer = analyzer;
        this.dao = dao;
    }

    // Get the output from the SubarrayResult using strategy design pattern, and write it to the file.
    public SubarrayResult analyzeAndSave(SubarrayRequest request) {
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
