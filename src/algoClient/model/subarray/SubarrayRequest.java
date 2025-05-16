package algoClient.model.subarray;

import algoClient.model.analysis.AnalysisType;
import java.util.List;

/**
 * Represents a request to analyze a numeric sequence.
 */
public class SubarrayRequest {
    private final String requestId;
    private final List<Double> values;
    private final AnalysisType type;

    public SubarrayRequest(String requestId, List<Double> values, AnalysisType type) {
        this.requestId = requestId;
        this.values = values;
        this.type = type;
    }

    public String getRequestId() {
        return requestId;
    }

    public List<Double> getValues() {
        return values;
    }

    public AnalysisType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Request ID: " + requestId + ", Type: " + type + ", Values: " + values;
    }
}
