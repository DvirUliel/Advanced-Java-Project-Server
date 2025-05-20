package algoClient.repository;

import algoClient.model.request.TradingAnalysisRequest;
import AlgorithmModule.SubarrayResult;
import java.util.List;

/**
 * Interface for storing and retrieving subarray analysis results.
 */
public interface IAnalysisResultDao {

    // Save a subarray analysis result to the data source.
    void save(TradingAnalysisRequest request, SubarrayResult result);

    // Load all saved entries as formatted strings.
    List<String> loadAll();

    // Clear all entries from the data source.
    void clear();
}