import AlgorithmModule.KadaneAnalyzer;
import AlgorithmModule.PrefixSumAnalyzer;
import AlgorithmModule.ISubarrayAnalyzer;
import AlgorithmModule.SubarrayResult;
import algoClient.model.enums.AnalysisType;
import algoClient.model.enums.DataMode;
import algoClient.model.request.TradingAnalysisRequest;
import algoClient.repository.AnalysisResultDaoImpl;
import algoClient.service.TradingAnalysisService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Entry point for simulating subarray analysis based on stock data.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AnalysisResultDaoImpl dao = new AnalysisResultDaoImpl("datasource.txt");

        System.out.println("Choose action:");
        System.out.println("[1] Run analysis");
        System.out.println("[2] View saved results");
        System.out.println("[3] Clear saved results");
        int action = scanner.nextInt();
        scanner.nextLine();

        if (action == 2) {
            dao.loadAll().forEach(System.out::println);
            return;
        } else if (action == 3) {
            dao.clear();
            System.out.println("All results cleared.");
            return;
        }

        // Read input mode
        System.out.println("Enter stock data type: [1] Daily changes  [2] Closing prices");
        int inputType = scanner.nextInt();
        scanner.nextLine();

        // Read raw numbers
        System.out.println("Enter comma-separated numbers (e.g. 100.0, 102.5, 99.8):");
        String line = scanner.nextLine();
        List<Double> rawValues;
        try {
            rawValues = Arrays.stream(line.split(","))
                    .map(String::trim)
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid decimal numbers.");
            return;
        }

        // Compute the actual values array (deltas if closing-prices)
        List<Double> values;
        if (inputType == 2 && rawValues.size() >= 2) {
            values = new ArrayList<>();
            for (int i = 1; i < rawValues.size(); i++) {
                values.add(rawValues.get(i) - rawValues.get(i - 1));
            }
        } else {
            values = rawValues;
        }

        // Read analysis type
        System.out.println("Choose analysis type:");
        System.out.println("[1] Max profit");
        System.out.println("[2] Max loss");
        System.out.println("[3] Zero return");
        int choice = scanner.nextInt();

        AnalysisType type;
        if (choice == 1) {
            type = AnalysisType.MAX_PROFIT;
        } else if (choice == 2) {
            type = AnalysisType.MAX_LOSS;
        } else if (choice == 3) {
            type = AnalysisType.ZERO_RETURN;
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        // Determine DataMode and optional closingPrices
        DataMode mode = (inputType == 2)
                ? DataMode.CLOSING_PRICES
                : DataMode.DAILY_CHANGES;
        List<Double> closingPrices = (mode == DataMode.CLOSING_PRICES)
                ? rawValues
                : null;

        // Build request with full constructor
        TradingAnalysisRequest request = new TradingAnalysisRequest(
                UUID.randomUUID().toString(),
                values,
                type,
                mode,
                closingPrices
        );

        // Handle max-loss separately by flipping
        if (type == AnalysisType.MAX_LOSS) {
            List<Double> flipped = values.stream()
                    .map(v -> -v)
                    .collect(Collectors.toList());
            KadaneAnalyzer kadane = new KadaneAnalyzer();
            SubarrayResult flippedResult = kadane.analyze(flipped);

            SubarrayResult corrected = new SubarrayResult(
                    flippedResult.getStartIndex(),
                    flippedResult.getEndIndex(),
                    -flippedResult.getTotal()
            );

            dao.save(request, corrected);
            System.out.println("Analysis type: " + type);
            System.out.println("Using algorithm: " + kadane.getName());
            System.out.println("Result: " + corrected);
            System.out.println("Saved results in datasource.txt");
            return;
        }

        // Choose analyzer for profit or zero-return
        ISubarrayAnalyzer analyzer = (type == AnalysisType.ZERO_RETURN)
                ? new PrefixSumAnalyzer(0)
                : new KadaneAnalyzer();

        TradingAnalysisService service = new TradingAnalysisService(analyzer, dao);
        SubarrayResult result = service.analyzeAndSave(request);

        System.out.println("Analysis type: " + type);
        System.out.println("Using algorithm: " + analyzer.getName());
        System.out.println("Result: " + result);
        System.out.println("Saved results in datasource.txt");
    }
}
