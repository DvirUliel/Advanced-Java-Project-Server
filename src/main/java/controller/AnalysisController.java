package main.java.controller;

import main.java.service.AnalysisService;
import main.java.model.AnalysisRequest;
import main.java.enums.AnalysisType;
import main.java.enums.DataMode;
import main.java.network.Response;
import AlgorithmModule.*;
import com.google.gson.Gson;

import java.util.List;
import java.util.UUID;

/**
 * Web layer controller that handles HTTP-like requests for financial analysis.
 * Routes actions to appropriate handlers and manages JSON conversion.
 */
public class AnalysisController {
    private final AnalysisService service; // Business logic delegation
    private final Gson gson = new Gson();  // JSON serialization

    public AnalysisController(AnalysisService service) {
        this.service = service;
    }

    /**
     * Main routing method - dispatches actions to specific handlers.
     * Returns standardized Response objects for consistent API.
     */
    public Response<?> handle(String action, String bodyJson) {
        try {
            return switch (action) {
                case "analysis/maxProfit" -> handleMaxProfit(bodyJson);
                case "analysis/maxLoss" -> handleMaxLoss(bodyJson);
                case "analysis/zeroReturn" -> handleZeroReturn(bodyJson);
                case "analysis/clear" -> handleClear();
                case "analysis/getAll" -> handleGetAll();
                default -> Response.error("Unknown action: " + action);
            };
        } catch (Exception e) {
            return Response.error("Error processing request: " + e.getMessage());
        }
    }

    /**
     * Uses default Kadane algorithm for maximum profit analysis.
     * Standard flow: parse JSON → create request → delegate to service.
     */
    private Response<SubarrayResult> handleMaxProfit(String bodyJson) {
        AnalysisRequestDto dto = gson.fromJson(bodyJson, AnalysisRequestDto.class);
        AnalysisRequest request = createRequest(dto, AnalysisType.MAX_PROFIT);
        SubarrayResult result = service.analyzeAndSave(request);
        return Response.success(result);
    }

    /**
     * Special handling for max loss using mathematical trick.
     * Flips values to negative, finds max profit, then corrects result.
     */
    private Response<SubarrayResult> handleMaxLoss(String bodyJson) {
        AnalysisRequestDto dto = gson.fromJson(bodyJson, AnalysisRequestDto.class);

        // Convert max loss problem to max profit on flipped data
        List<Double> flipped = dto.values.stream().map(v -> -v).toList();
        KadaneAnalyzer kadane = new KadaneAnalyzer();
        SubarrayResult flippedResult = kadane.analyze(flipped);

        // Correct the result: flip total back to get actual loss
        SubarrayResult corrected = new SubarrayResult(
                flippedResult.getStartIndex(),
                flippedResult.getEndIndex(),
                -flippedResult.getTotal()
        );

        AnalysisRequest request = createRequest(dto, AnalysisType.MAX_LOSS);
        service.getDao().save(request, corrected);
        return Response.success(corrected);
    }

    /**
     * Uses PrefixSumAnalyzer to find longest subarray with zero sum.
     * Different algorithm needed for target-sum problems.
     */
    private Response<SubarrayResult> handleZeroReturn(String bodyJson) {
        AnalysisRequestDto dto = gson.fromJson(bodyJson, AnalysisRequestDto.class);

        // Create specialized analyzer for zero-sum detection
        PrefixSumAnalyzer prefixAnalyzer = new PrefixSumAnalyzer(0);
        AnalysisRequest request = createRequest(dto, AnalysisType.ZERO_RETURN);
        SubarrayResult result = prefixAnalyzer.analyze(dto.values);
        service.getDao().save(request, result);
        return Response.success(result);
    }

    /**
     * Administrative function to clear all stored results.
     */
    private Response<String> handleClear() {
        service.clearAllResults();
        return Response.success("All results cleared");
    }

    /**
     * Retrieval function to get all previously saved analysis results.
     */
    private Response<List<String>> handleGetAll() {
        List<String> results = service.getDao().loadAll();
        return Response.success(results);
    }

    /**
     * Utility method to convert DTO to domain model with generated ID.
     * Handles default values for optional fields.
     */
    private AnalysisRequest createRequest(AnalysisRequestDto dto, AnalysisType type) {
        return new AnalysisRequest(
                UUID.randomUUID().toString(), // Generate unique tracking ID
                dto.values,
                type,
                dto.dataMode != null ? dto.dataMode : DataMode.DAILY_CHANGES, // Default mode
                dto.closingPrices
        );
    }

    /**
     * Simple DTO for JSON deserialization.
     * Separates JSON structure from rich domain model.
     */
    private static class AnalysisRequestDto {
        List<Double> values;
        DataMode dataMode;
        List<Double> closingPrices;
    }
}