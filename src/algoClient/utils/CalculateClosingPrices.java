package algoClient.utils;

import java.util.ArrayList;
import java.util.List;

public class CalculateClosingPrices {
    /**
     * Computes differences between consecutive closing prices.
     * Used when input is closing prices and we want daily changes.
     */
    public static List<Double> computeDeltas(List<Double> prices) {
        List<Double> deltas = new ArrayList<>();
        for (int i = 1; i < prices.size(); i++) {
            deltas.add(prices.get(i) - prices.get(i - 1));
        }
        return deltas;
    }
}
