package algoClient.controller;

import algoClient.service.AnalysisService;
import algoClient.model.AnalysisRequest;
import algoClient.enums.AnalysisType;
import algoClient.enums.DataMode;
import algoClient.network.Response;
import AlgorithmModule.*;
import com.google.gson.Gson;

import java.util.List;
import java.util.UUID;

public class AnalysisController {
    private final AnalysisService service;
    private final Gson gson = new Gson();

    public AnalysisController(AnalysisService service) {
        this.service = service;
    }

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

    private Response<SubarrayResult> handleMaxProfit(String bodyJson) {
        AnalysisRequestDto dto = gson.fromJson(bodyJson, AnalysisRequestDto.class);
        AnalysisRequest request = createRequest(dto, AnalysisType.MAX_PROFIT);
        SubarrayResult result = service.analyzeAndSave(request);
        return Response.success(result);
    }

    private Response<SubarrayResult> handleMaxLoss(String bodyJson) {
        AnalysisRequestDto dto = gson.fromJson(bodyJson, AnalysisRequestDto.class);

        // Handle max loss with flipped values (as in your App.java)
        List<Double> flipped = dto.values.stream().map(v -> -v).toList();
        KadaneAnalyzer kadane = new KadaneAnalyzer();
        SubarrayResult flippedResult = kadane.analyze(flipped);
        SubarrayResult corrected = new SubarrayResult(
                flippedResult.getStartIndex(),
                flippedResult.getEndIndex(),
                -flippedResult.getTotal()
        );

        AnalysisRequest request = createRequest(dto, AnalysisType.MAX_LOSS);
        service.getDao().save(request, corrected);
        return Response.success(corrected);
    }

    private Response<SubarrayResult> handleZeroReturn(String bodyJson) {
        AnalysisRequestDto dto = gson.fromJson(bodyJson, AnalysisRequestDto.class);

        // Use PrefixSumAnalyzer for zero return
        PrefixSumAnalyzer prefixAnalyzer = new PrefixSumAnalyzer(0);
        AnalysisRequest request = createRequest(dto, AnalysisType.ZERO_RETURN);
        SubarrayResult result = prefixAnalyzer.analyze(dto.values);
        service.getDao().save(request, result);
        return Response.success(result);
    }

    private Response<String> handleClear() {
        service.clearAllResults();
        return Response.success("All results cleared");
    }

    private Response<List<String>> handleGetAll() {
        List<String> results = service.getDao().loadAll();
        return Response.success(results);
    }

    private AnalysisRequest createRequest(AnalysisRequestDto dto, AnalysisType type) {
        return new AnalysisRequest(
                UUID.randomUUID().toString(),
                dto.values,
                type,
                dto.dataMode != null ? dto.dataMode : DataMode.DAILY_CHANGES,
                dto.closingPrices
        );
    }

    // DTO for JSON deserialization
    private static class AnalysisRequestDto {
        List<Double> values;
        DataMode dataMode;
        List<Double> closingPrices;
    }
}