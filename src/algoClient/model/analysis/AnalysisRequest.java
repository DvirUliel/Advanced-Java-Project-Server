package algoClient.model.analysis;
import java.util.List;
import java.util.UUID;

/**
 * Represents a full analysis request including:
 * - a unique ID
 * - a list of numeric values (either price changes or calculated from prices)
 * - a type of analysis (e.g., MAX_PROFIT, MAX_LOSS, ZERO_RETURN)
 */
public class AnalysisRequest {
    private final String requestId;
    private final List<Double> values;
    private final AnalysisType type;

    //Constructs an analysis request with a random UUID.
    public AnalysisRequest(List<Double> values, AnalysisType type) {
        this.requestId = UUID.randomUUID().toString();
        this.values = values;
        this.type = type;
    }

    // Return unique ID of the request
    public String getRequestId() {
        return requestId;
    }

    // Return list of numeric values to analyze
    public List<Double> getValues() {
        return values;
    }


     // Return the type of analysis requested
    public AnalysisType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "AnalysisRequest{" +
                "id='" + requestId + '\'' +
                ", type=" + type +
                ", values=" + values +
                '}';
    }
}
