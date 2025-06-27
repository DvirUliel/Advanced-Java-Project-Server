package main.java.model;

import main.java.enums.AnalysisType;
import main.java.enums.DataMode;
import java.util.List;

/**
 * Domain model representing a financial analysis request.
 * Contains all data needed to perform subarray analysis on stock data.
 */
public class AnalysisRequest {
    private final String requestId;        // Unique identifier for tracking
    private final List<Double> values;     // Daily changes or computed deltas
    private final AnalysisType type;       // MAX_PROFIT, MAX_LOSS, ZERO_RETURN
    private final DataMode dataMode;       // DAILY_CHANGES or CLOSING_PRICES
    private final List<Double> closingPrices; // Original prices (optional)

    /**
     * Full constructor for requests with both data modes.
     * Used when client provides closing prices that need conversion to daily changes.
     */
    public AnalysisRequest(String requestId, List<Double> values, AnalysisType type, DataMode dataMode, List<Double> closingPrices) {
        this.requestId = requestId;
        this.values = values;
        this.type = type;
        this.dataMode = dataMode;
        this.closingPrices = closingPrices;
    }

    /**
     * Convenience constructor for direct daily changes input.
     * Most common use case - client provides daily changes directly.
     */
    public AnalysisRequest(String requestId, List<Double> values, AnalysisType type) {
        this(requestId, values, type, DataMode.DAILY_CHANGES, null);
    }

    // Immutable getters - all fields are final
    public String getRequestId() { return requestId; }
    public List<Double> getValues() { return values; }
    public AnalysisType getType() { return type; }
    public DataMode getDataMode() { return dataMode; }
    public List<Double> getClosingPrices() { return closingPrices; }

    /**
     * Custom toString that conditionally includes closing prices.
     * Only shows closing prices when dataMode is CLOSING_PRICES.
     */
    @Override
    public String toString() {
        String base = "AnalysisRequest[requestId=" + requestId + ", type=" + type +
                ", dataMode=" + dataMode + ", values=" + values;
        if (dataMode == DataMode.CLOSING_PRICES) {
            base += ", closingPrices=" + closingPrices;
        }
        return base + "]";
    }
}