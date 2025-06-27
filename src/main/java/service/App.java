package main.java.service;

import AlgorithmModule.*;
import main.java.enums.AnalysisType;
import main.java.enums.DataMode;
import main.java.model.AnalysisRequest;
import main.java.repository.AnalysisDaoImpl;
import main.java.utils.InputValidate;
import main.java.utils.CalculateClosingPrices;

import java.util.*;

public class App {
    public static void run() {
        AnalysisDaoImpl dao = new AnalysisDaoImpl("src/main/resources/datasource.txt");

        // Step 1: Main menu
        int action = InputValidate.readInt("""
                Choose action:
                [1] Run analysis
                [2] View saved results
                [3] Clear saved results
                ➜ """, 1, 3);

        if (action == 2) {
            dao.loadAll().forEach(System.out::println);
            return;
        } else if (action == 3) {
            dao.clear();
            System.out.println("All results cleared.");
            return;
        }

        // Step 2: Input data mode
        int inputType = InputValidate.readInt("""
                Enter stock data type:
                [1] Daily changes
                [2] Closing prices
                ➜ """, 1, 2);

        // Step 3: Read input values
        List<Double> rawValues = InputValidate.readDoubleList("Enter comma-separated numbers (e.g. 100.0, 102.5, 99.8):\n");

        // Step 4: Calculate deltas if needed
        List<Double> values;
        if (inputType == 2) {
            try {
                values = CalculateClosingPrices.computeDeltas(rawValues);
            } catch (IllegalArgumentException e) {
                System.out.println("❌ " + e.getMessage());
                return;
            }
        } else {
            values = rawValues;
        }

        // Step 5: Choose analysis type
        int choice = InputValidate.readInt("""
                Choose analysis type:
                [1] Max profit
                [2] Max loss
                [3] Zero return
                ➜ """, 1, 3);

        AnalysisType type = switch (choice) {
            case 1 -> AnalysisType.MAX_PROFIT;
            case 2 -> AnalysisType.MAX_LOSS;
            case 3 -> AnalysisType.ZERO_RETURN;
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };

        // Step 6: Build request
        DataMode mode = (inputType == 2) ? DataMode.CLOSING_PRICES : DataMode.DAILY_CHANGES;
        List<Double> closingPrices = (mode == DataMode.CLOSING_PRICES) ? rawValues : null;

        AnalysisRequest request = new AnalysisRequest(
                UUID.randomUUID().toString(),
                values,
                type,
                mode,
                closingPrices
        );

        // Step 7: Max loss case handled separately
        if (type == AnalysisType.MAX_LOSS) {
            List<Double> flipped = values.stream()
                    .map(v -> -v)
                    .toList();

            KadaneAnalyzer kadane = new KadaneAnalyzer();
            SubarrayResult flippedResult = kadane.analyze(flipped);
            SubarrayResult corrected = new SubarrayResult(
                    flippedResult.getStartIndex(),
                    flippedResult.getEndIndex(),
                    -flippedResult.getTotal()
            );

            dao.save(request, corrected);
            printResult(type, kadane.getName(), corrected);
            return;
        }

        // Step 8: Run regular analysis
        ISubarrayAnalyzer analyzer = (type == AnalysisType.ZERO_RETURN)
                ? new PrefixSumAnalyzer(0)
                : new KadaneAnalyzer();

        AnalysisService service = new AnalysisService(analyzer, dao);
        SubarrayResult result = service.analyzeAndSave(request);

        printResult(type, analyzer.getName(), result);
    }

    private static void printResult(AnalysisType type, String algorithm, SubarrayResult result) {
        System.out.println("✔ Analysis type: " + type);
        System.out.println("✔ Using algorithm: " + algorithm);
        System.out.println("✔ Result: " + result);
        System.out.println("✔ Saved results in datasource.txt");
    }
}
