package algoClient.repository;

import algoClient.model.AnalysisRequest;
import AlgorithmModule.SubarrayResult;
import java.util.List;

/**
 * Interface for storing and retrieving subarray analysis results.
 */
public interface IAnalysisDao {

    // Save a subarray analysis result to the data source.
    void save(AnalysisRequest request, SubarrayResult result);

    // Load all saved entries as formatted strings.
    List<String> loadAll();

    // Clear all entries from the data source.
    void clear();
}