package algoClient.repository;

import algoClient.enums.DataMode;
import algoClient.model.AnalysisRequest;
import AlgorithmModule.SubarrayResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based implementation of ISubarrayDao using a text file.
 */
public class AnalysisDaoImpl implements IAnalysisDao {
    private final String filePath;

    public AnalysisDaoImpl(String filePath) {
        this.filePath = filePath;
    }

    // Write to the file all the information about the request.
    @Override
    public void save(AnalysisRequest request, SubarrayResult result) {
        File file = new File(filePath);
        try (FileWriter writer = new FileWriter(file, true)) {
            // Write header title if file is new or empty
            if (!file.exists() || file.length() == 0) {
                writer.write("----------------------------------------------------" + System.lineSeparator());
                writer.write("Profit Analyzer - Calculates profit in stock trading" + System.lineSeparator());
                writer.write("----------------------------------------------------" + System.lineSeparator());
            }

            // Unique request ID
            writer.write("Request ID: " + request.getRequestId() + System.lineSeparator());

            // Analysis type (MAX_PROFIT, MAX_LOSS, ZERO_RETURN)
            writer.write("Analysis Type: " + request.getType() + System.lineSeparator());

            // Selected data mode
            DataMode mode = request.getDataMode();
            writer.write("Data Mode: " + mode + System.lineSeparator());

            // Original user input
            List<Double> rawInput = request.getClosingPrices();
            if (mode == DataMode.CLOSING_PRICES && rawInput != null) {
                writer.write("User Input: " + rawInput + System.lineSeparator());
            }

            // Values used for analysis
            writer.write("Values Used for Analysis: " + request.getValues() + System.lineSeparator());

            // Result of analysis
            writer.write("Result: " + result + System.lineSeparator());
            writer.write("----------------------------------------------------" + System.lineSeparator());

        } catch (IOException e) {
            System.err.println("[ERROR] Failed to save result: " + e.getMessage());
        }
    }

    // Return list of all the lines inside the file.
    @Override
    public List<String> loadAll() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load data: " + e.getMessage());
        }
        return lines;
    }

    // Clear the file to be empty
    @Override
    public void clear() {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Writing nothing to clear the file
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to clear file: " + e.getMessage());
        }
    }
}