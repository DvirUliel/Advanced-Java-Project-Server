import AlgorithmModule.KadaneAnalyzer;
import AlgorithmModule.PrefixSumAnalyzer;
import AlgorithmModule.ISubarrayAnalyzer;
import AlgorithmModule.SubarrayResult;
import algoClient.model.analysis.AnalysisType;
import algoClient.model.subarray.SubarrayRequest;
import algoClient.repository.SubarrayFileDaoImpl;
import algoClient.service.SubarrayService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Entry point for simulating subarray analysis based on stock data.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SubarrayFileDaoImpl dao = new SubarrayFileDaoImpl("datasource.txt");

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

        System.out.println("Enter stock data type: [1] Daily changes  [2] Closing prices");
        int inputType = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter comma-separated numbers (e.g. 100.0, 102.5, 99.8):");
        String inputLine = scanner.nextLine();
        List<Double> rawValues;
        try {
            rawValues = Arrays.stream(inputLine.split(","))
                    .map(String::trim)
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid decimal numbers.");
            return;
        }

        List<Double> values;
        if (inputType == 2 && rawValues.size() >= 2) {
            values = new ArrayList<>();
            for (int i = 1; i < rawValues.size(); i++) {
                values.add(rawValues.get(i) - rawValues.get(i - 1));
            }
        } else {
            values = rawValues;
        }

        System.out.println("Choose analysis type:");
        System.out.println("[1] Max profit");
        System.out.println("[2] Max loss");
        System.out.println("[3] Zero return");
        int analysisChoice = scanner.nextInt();

        AnalysisType type;
        if (analysisChoice == 1) {
            type = AnalysisType.MAX_PROFIT;
        } else if (analysisChoice == 2) {
            type = AnalysisType.MAX_LOSS;
        } else if (analysisChoice == 3) {
            type = AnalysisType.ZERO_RETURN;
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        SubarrayService service;
        SubarrayRequest request = new SubarrayRequest(UUID.randomUUID().toString(), values, type);

        if (type == AnalysisType.MAX_LOSS) {
            List<Double> flipped = values.stream().map(v -> -v).toList();
            KadaneAnalyzer analyzer = new KadaneAnalyzer();
            SubarrayResult flippedResult = analyzer.analyze(flipped);

            SubarrayResult corrected = new SubarrayResult(
                    flippedResult.getStartIndex(),
                    flippedResult.getEndIndex(),
                    -flippedResult.getTotal()
            );

            dao.save(request, corrected);
            System.out.println("Analysis type: " + type);
            System.out.println("Using algorithm: " + analyzer.getName());
            System.out.println("Result: " + corrected);
            System.out.println("Saved results in datasource.txt");
            return;
        }

        ISubarrayAnalyzer analyzer = (type == AnalysisType.ZERO_RETURN)
                ? new PrefixSumAnalyzer(0)
                : new KadaneAnalyzer();

        service = new SubarrayService(analyzer, dao);
        SubarrayResult result = service.analyzeAndSave(request);

        System.out.println("Analysis type: " + type);
        System.out.println("Using algorithm: " + analyzer.getName());
        System.out.println("Result: " + result);
        System.out.println("Saved results in datasource.txt");
    }
}
