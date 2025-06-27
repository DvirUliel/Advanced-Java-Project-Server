package main.test.controller;

import main.java.controller.AnalysisController;
import main.java.controller.FactoryController;
import main.java.network.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static org.junit.Assert.*;

/**
 * Tests for AnalysisController routing and response handling.
 */
public class AnalysisControllerTest {

    private final String testFile = "controller_test_datasource.txt";
    private AnalysisController controller;

    @Before
    public void setUp() {
        controller = FactoryController.getAnalysisController();
        // Clear any existing test data
        controller.handle("analysis/clear", "{}");
    }

    @After
    public void tearDown() {
        controller.handle("analysis/clear", "{}");
        new File(testFile).delete();
    }

    /**
     * Test successful max profit analysis routing.
     */
    @Test
    public void testMaxProfitAction() {
        String bodyJson = """
            {
                "values": [1.0, -2.0, 3.0, -1.0, 2.0],
                "dataMode": "DAILY_CHANGES"
            }
            """;

        Response<?> response = controller.handle("analysis/maxProfit", bodyJson);

        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getData());
        assertTrue(response.getMessage().contains("successfully"));
    }

    /**
     * Test max loss analysis with algorithm switching.
     */
    @Test
    public void testMaxLossAction() {
        String bodyJson = """
            {
                "values": [2.0, -3.0, -1.0, 4.0],
                "dataMode": "DAILY_CHANGES"
            }
            """;

        Response<?> response = controller.handle("analysis/maxLoss", bodyJson);

        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getData());
    }

    /**
     * Test zero return analysis with PrefixSum algorithm.
     */
    @Test
    public void testZeroReturnAction() {
        String bodyJson = """
            {
                "values": [1.0, -1.0, 2.0, -2.0],
                "dataMode": "DAILY_CHANGES"
            }
            """;

        Response<?> response = controller.handle("analysis/zeroReturn", bodyJson);

        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getData());
    }

    /**
     * Test unknown action returns error response.
     */
    @Test
    public void testUnknownAction() {
        Response<?> response = controller.handle("unknown/action", "{}");

        assertEquals("ERROR", response.getStatus());
        assertTrue(response.getMessage().contains("Unknown action"));
        assertNull(response.getData());
    }

    /**
     * Test clear functionality.
     */
    @Test
    public void testClearAction() {
        // First add some data
        testMaxProfitAction();

        // Then clear
        Response<?> response = controller.handle("analysis/clear", "{}");

        assertEquals("SUCCESS", response.getStatus());
        assertEquals("All results cleared", response.getData());
    }

    /**
     * Test invalid JSON handling.
     */
    @Test
    public void testInvalidJson() {
        String invalidJson = "{ invalid json }";

        Response<?> response = controller.handle("analysis/maxProfit", invalidJson);

        assertEquals("ERROR", response.getStatus());
        assertTrue(response.getMessage().contains("Error processing"));
    }
}