package algoClient.test;

import AlgorithmModule.KadaneAnalyzer;
import AlgorithmModule.SubarrayResult;
import algoClient.enums.AnalysisType;
import algoClient.model.AnalysisRequest;
import algoClient.repository.IAnalysisDao;
import algoClient.repository.AnalysisDaoImpl;
import algoClient.service.AnalysisService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Unit tests for SubarrayService using KadaneAnalyzer.
 */
public class AnalysisServiceTest {

    private final String testFile = "test_datasource.txt";
    private IAnalysisDao dao;

    @Before
    public void setUp() {
        dao = new AnalysisDaoImpl(testFile);
        dao.clear();
    }

    @After
    public void tearDown() {
        dao.clear();
        new File(testFile).delete();
    }

    private AnalysisRequest buildRequest(List<Double> values) {
        return new AnalysisRequest(UUID.randomUUID().toString(), values, AnalysisType.MAX_PROFIT);
    }

    /**
     * Mixed values.
     * Kadane expected to return max sum: 6.0 (subarray: [3.0, -1.0, 4.0])
     */
    @Test
    public void testAnalyzeMixedValues() {
        List<Double> values = Arrays.asList(-2.0, 3.0, -1.0, 4.0, -5.0);
        AnalysisRequest request = buildRequest(values);
        AnalysisService service = new AnalysisService(new KadaneAnalyzer(), dao);
        SubarrayResult result = service.analyzeAndSave(request);

        assertEquals(6.0, result.getTotal(), 0.001);
        assertEquals(1, result.getStartIndex());
        assertEquals(3, result.getEndIndex());
    }

    /**
     * All negative values.
     * Kadane should return the least negative value.
     */
    @Test
    public void testAnalyzeAllNegative() {
        List<Double> values = Arrays.asList(-4.0, -2.0, -7.0);
        AnalysisRequest request = buildRequest(values);
        AnalysisService service = new AnalysisService(new KadaneAnalyzer(), dao);
        SubarrayResult result = service.analyzeAndSave(request);

        assertEquals(-2.0, result.getTotal(), 0.001);
        assertEquals(1, result.getStartIndex());
        assertEquals(1, result.getEndIndex());
    }

    /**
     * All values are zero.
     */
    @Test
    public void testAnalyzeAllZeros() {
        List<Double> values = Arrays.asList(0.0, 0.0, 0.0);
        AnalysisRequest request = buildRequest(values);
        AnalysisService service = new AnalysisService(new KadaneAnalyzer(), dao);
        SubarrayResult result = service.analyzeAndSave(request);

        assertEquals(0.0, result.getTotal(), 0.001);
        assertEquals(0, result.getStartIndex());
        assertEquals(0, result.getEndIndex());
    }

    /**
     * Empty list input. Should return -1, -1.
     */
    @Test
    public void testAnalyzeEmptyList() {
        List<Double> values = Collections.emptyList();
        AnalysisRequest request = buildRequest(values);
        AnalysisService service = new AnalysisService(new KadaneAnalyzer(), dao);
        SubarrayResult result = service.analyzeAndSave(request);

        assertEquals(-1, result.getStartIndex());
        assertEquals(-1, result.getEndIndex());
        assertEquals(0.0, result.getTotal(), 0.001);
    }

    /**
     * Checks getAlgorithmName() returns correct name.
     */
    @Test
    public void testGetAlgorithmName() {
        AnalysisService service = new AnalysisService(new KadaneAnalyzer(), dao);
        assertEquals("Kadane", service.getAlgorithmName());
    }

    /**
     * Checks clearAllResults() clears the file.
     */
    @Test
    public void testClearAllResults() {
        AnalysisService service = new AnalysisService(new KadaneAnalyzer(), dao);
        service.analyzeAndSave(buildRequest(Arrays.asList(1.0, 2.0, 3.0)));
        service.clearAllResults();
        assertTrue(dao.loadAll().isEmpty());
    }

    /**
     * Checks saved content is not empty after analysis.
     */
    @Test
    public void testSavedContentExists() {
        AnalysisService service = new AnalysisService(new KadaneAnalyzer(), dao);
        service.analyzeAndSave(buildRequest(Arrays.asList(1.0, 2.0, -1.0)));
        List<String> content = dao.loadAll();
        assertFalse(content.isEmpty());
        assertTrue(content.stream().anyMatch(line -> line.contains("Result")));
    }
}