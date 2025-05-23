// src/algoClient/model/subarray/SubarrayRequest.java
package algoClient.model.request;

import algoClient.model.enums.AnalysisType;
import algoClient.model.enums.DataMode;
import java.util.List;

/**
 * Represents a request to analyze a numeric sequence, with an optional closing prices array.
 */
public class TradingAnalysisRequest {
    private final String requestId;
    private final List<Double> values;
    private final AnalysisType type;
    private final DataMode dataMode;
    private final List<Double> closingPrices;

    /**
     * Constructs a new SubarrayRequest with a specific data mode.
     *
     * @param requestId - unique identifier for this request
     * @param values - list of numeric values (e.g., daily changes)
     * @param type - analysis type (e.g., MAX_PROFIT)
     * @param dataMode - mode indicating DAILY_CHANGES or CLOSING_PRICES
     * @param closingPrices - list of closing prices; may be null if mode is DAILY_CHANGES
     */
    public TradingAnalysisRequest(String requestId, List<Double> values, AnalysisType type, DataMode dataMode, List<Double> closingPrices) {
        this.requestId = requestId;
        this.values = values;
        this.type = type;
        this.dataMode = dataMode;
        this.closingPrices = closingPrices;
    }

    /**
     * Convenience constructor for DAILY_CHANGES mode without closing prices.
     *
     * @param requestId - unique identifier for this request
     * @param values - list of numeric values (daily changes)
     * @param type - analysis type
     */
    public TradingAnalysisRequest(String requestId, List<Double> values, AnalysisType type) {
        this(requestId, values, type, DataMode.DAILY_CHANGES, null);
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

    public DataMode getDataMode() {
        return dataMode;
    }

    public List<Double> getClosingPrices() {
        return closingPrices;
    }

    @Override
    public String toString() {
        String base = "SubarrayRequest[requestId=" + requestId + ", type=" + type +
                ", dataMode=" + dataMode + ", values=" + values;
        if (dataMode == DataMode.CLOSING_PRICES) {
            base += ", closingPrices=" + closingPrices;
        }
        return base + "]";
    }
}
