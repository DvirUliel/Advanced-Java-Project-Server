package algoClient.repository;

import algoClient.model.subarray.SubarrayRequest;
import AlgorithmModule.SubarrayResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based implementation of ISubarrayDao using a text file.
 */
public class SubarrayFileDaoImpl implements ISubarrayDao {
    private final String filePath;

    public SubarrayFileDaoImpl(String filePath) {
        this.filePath = filePath;
    }

    // Write to the file all the information about the request.
    @Override
    public void save(SubarrayRequest request, SubarrayResult result) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write("Request ID: " + request.getRequestId() + "\n");
            writer.write("Analysis Type: " + request.getType() + "\n");
            writer.write("Input: " + request.getValues() + "\n");
            writer.write("Result: " + result + "\n");
            writer.write("-----\n");
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
