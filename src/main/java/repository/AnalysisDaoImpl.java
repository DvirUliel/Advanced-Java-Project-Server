package main.java.repository;

import main.java.enums.DataMode;
import main.java.model.AnalysisRequest;
import AlgorithmModule.SubarrayResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based repository implementation for persisting analysis results.
 * Uses a simple text file format with structured output for easy reading.
 */
public class AnalysisDaoImpl implements IAnalysisDao {
    private final String filePath; // Path to data storage file

    public AnalysisDaoImpl(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Appends analysis request and result to file in formatted structure.
     * Creates header if file is new, then writes all request details.
     */
    @Override
    public void save(AnalysisRequest request, SubarrayResult result) {
        File file = new File(filePath);
        // Append mode (true) preserves existing content
        try (FileWriter writer = new FileWriter(file, true)) {

            // Add header section only for new/empty files
            if (!file.exists() || file.length() == 0) {
                writer.write("----------------------------------------------------" + System.lineSeparator());
                writer.write("Profit Analyzer - Calculates profit in stock trading" + System.lineSeparator());
                writer.write("----------------------------------------------------" + System.lineSeparator());
            }

            // Write request metadata
            writer.write("Request ID: " + request.getRequestId() + System.lineSeparator());
            writer.write("Analysis Type: " + request.getType() + System.lineSeparator());

            // Write data mode and conditionally include original input
            DataMode mode = request.getDataMode();
            writer.write("Data Mode: " + mode + System.lineSeparator());

            // Show original closing prices only when relevant
            List<Double> rawInput = request.getClosingPrices();
            if (mode == DataMode.CLOSING_PRICES && rawInput != null) {
                writer.write("User Input: " + rawInput + System.lineSeparator());
            }

            // Write processed values and analysis result
            writer.write("Values Used for Analysis: " + request.getValues() + System.lineSeparator());
            writer.write("Result: " + result + System.lineSeparator());
            writer.write("----------------------------------------------------" + System.lineSeparator());

        } catch (IOException e) {
            System.err.println("[ERROR] Failed to save result: " + e.getMessage());
        }
    }

    /**
     * Reads entire file content and returns as list of strings.
     * Each line becomes one list element for easy display.
     */
    @Override
    public List<String> loadAll() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read all lines until end of file
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load data: " + e.getMessage());
        }
        return lines;
    }

    /**
     * Empties the file by opening it in write mode without writing content.
     * FileWriter in write mode (not append) truncates the file.
     */
    @Override
    public void clear() {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Opening in write mode clears file, writing nothing keeps it empty
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to clear file: " + e.getMessage());
        }
    }
}