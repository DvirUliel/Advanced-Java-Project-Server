package algoClient.repository;

import algoClient.model.subarray.SubarrayRequest;
import AlgorithmModule.SubarrayResult;
import java.util.List;

/**
 * Interface for storing and retrieving subarray analysis results.
 */
public interface ISubarrayDao {

    // Save a subarray analysis result to the data source.
    void save(SubarrayRequest request, SubarrayResult result);

     // Load all saved entries as formatted strings.
    List<String> loadAll();

    // Clear all entries from the data source.
    void clear();
}
