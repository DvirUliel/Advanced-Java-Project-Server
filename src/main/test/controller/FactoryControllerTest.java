package main.test.controller;

import main.java.controller.AnalysisController;
import main.java.controller.FactoryController;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for FactoryController dependency injection and object creation.
 */
public class FactoryControllerTest {

    /**
     * Test that factory returns non-null controller.
     */
    @Test
    public void testGetAnalysisController() {
        AnalysisController controller = FactoryController.getAnalysisController();

        assertNotNull("Controller should not be null", controller);
    }

    /**
     * Test that factory returns same instance (singleton behavior).
     */
    @Test
    public void testSingletonBehavior() {
        AnalysisController controller1 = FactoryController.getAnalysisController();
        AnalysisController controller2 = FactoryController.getAnalysisController();

        assertSame("Factory should return same instance", controller1, controller2);
    }

    /**
     * Test generic controller getter.
     */
    @Test
    public void testGenericControllerGetter() {
        Object controller = FactoryController.getController("analysis");

        assertNotNull("Generic getter should return controller", controller);
        assertTrue("Should return AnalysisController instance",
                controller instanceof AnalysisController);
    }

    /**
     * Test non-existent controller returns null.
     */
    @Test
    public void testNonExistentController() {
        Object controller = FactoryController.getController("nonexistent");

        assertNull("Non-existent controller should return null", controller);
    }

    /**
     * Test that factory creates properly configured controller.
     */
    @Test
    public void testControllerConfiguration() {
        AnalysisController controller = FactoryController.getAnalysisController();

        // Test that controller can handle requests (dependencies are injected)
        String testJson = """
            {
                "values": [1.0, 2.0],
                "dataMode": "DAILY_CHANGES"
            }
            """;

        // Should not throw exception if properly configured
        assertNotNull(controller.handle("analysis/maxProfit", testJson));
    }
}