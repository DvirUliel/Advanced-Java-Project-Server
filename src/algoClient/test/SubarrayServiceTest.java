package algoClient.test;

import AlgorithmModule.KadaneAnalyzer;
import AlgorithmModule.SubarrayResult;
import algoClient.model.analysis.AnalysisType;
import algoClient.model.subarray.SubarrayRequest;
import algoClient.repository.ISubarrayDao;
import algoClient.repository.SubarrayFileDaoImpl;
import algoClient.service.SubarrayService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Unit tests for SubarrayService using KadaneAnalyzer.
 */
public class SubarrayServiceTest {

    private final String testFile = "test_datasource.txt";
    private ISubarrayDao dao;

    @Before
    public void setUp() {
        dao = new SubarrayFileDaoImpl(testFile);
        dao.clear();
    }

    @After
    public void tearDown() {
        dao.clear();
        new File(testFile).delete();
    }

    private SubarrayRequest buildRequest(List<Double> values) {
        return new SubarrayRequest(UUID.randomUUID().toString(), values, AnalysisType.MAX_PROFIT);
    }

    /**
     * Mixed values.
     * Kadane expected to return max sum: 6.0 (subarray: [3.0, -1.0, 4.0])
     */
    @Test
    public void testAnalyzeMixedValues() {
        List<Double> values = Arrays.asList(-2.0, 3.0, -1.0, 4.0, -5.0);
        SubarrayRequest request = buildRequest(values);
        SubarrayService service = new SubarrayService(new KadaneAnalyzer(), dao);
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
        SubarrayRequest request = buildRequest(values);
        SubarrayService service = new SubarrayService(new KadaneAnalyzer(), dao);
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
        SubarrayRequest request = buildRequest(values);
        SubarrayService service = new SubarrayService(new KadaneAnalyzer(), dao);
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
        SubarrayRequest request = buildRequest(values);
        SubarrayService service = new SubarrayService(new KadaneAnalyzer(), dao);
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
        SubarrayService service = new SubarrayService(new KadaneAnalyzer(), dao);
        assertEquals("Kadane", service.getAlgorithmName());
    }

    /**
     * Checks clearAllResults() clears the file.
     */
    @Test
    public void testClearAllResults() {
        SubarrayService service = new SubarrayService(new KadaneAnalyzer(), dao);
        service.analyzeAndSave(buildRequest(Arrays.asList(1.0, 2.0, 3.0)));
        service.clearAllResults();
        assertTrue(dao.loadAll().isEmpty());
    }

    /**
     * Checks saved content is not empty after analysis.
     */
    @Test
    public void testSavedContentExists() {
        SubarrayService service = new SubarrayService(new KadaneAnalyzer(), dao);
        service.analyzeAndSave(buildRequest(Arrays.asList(1.0, 2.0, -1.0)));
        List<String> content = dao.loadAll();
        assertFalse(content.isEmpty());
        assertTrue(content.stream().anyMatch(line -> line.contains("Result")));
    }
}
