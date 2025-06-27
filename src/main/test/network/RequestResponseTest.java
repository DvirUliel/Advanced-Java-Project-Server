package main.test.network;

import main.java.network.Request;
import main.java.network.Response;
import com.google.gson.Gson;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for JSON serialization/deserialization of Request and Response objects.
 */
public class RequestResponseTest {

    private final Gson gson = new Gson();

    /**
     * Test Request object serialization to JSON.
     */
    @Test
    public void testRequestSerialization() {
        Request.Headers headers = new Request.Headers("analysis/maxProfit");
        String bodyData = "test data";
        Request<String> request = new Request<>(headers, bodyData);

        String json = gson.toJson(request);

        assertTrue("JSON should contain action", json.contains("analysis/maxProfit"));
        assertTrue("JSON should contain body data", json.contains("test data"));
        assertTrue("JSON should have headers structure", json.contains("headers"));
    }

    /**
     * Test Request object deserialization from JSON.
     */
    @Test
    public void testRequestDeserialization() {
        String json = """
            {
                "headers": {"action": "analysis/maxProfit"},
                "body": {"values": [1.0, 2.0], "dataMode": "DAILY_CHANGES"}
            }
            """;

        Request<Object> request = gson.fromJson(json, Request.class);

        assertNotNull("Request should not be null", request);
        assertNotNull("Headers should not be null", request.getHeaders());
        assertEquals("Action should match", "analysis/maxProfit",
                request.getHeaders().getAction());
        assertNotNull("Body should not be null", request.getBody());
    }

    /**
     * Test Response success factory method.
     */
    @Test
    public void testResponseSuccess() {
        String testData = "test result";
        Response<String> response = Response.success(testData);

        assertEquals("Status should be SUCCESS", "SUCCESS", response.getStatus());
        assertEquals("Data should match", testData, response.getData());
        assertTrue("Message should indicate success",
                response.getMessage().contains("successfully"));
    }

    /**
     * Test Response error factory method.
     */
    @Test
    public void testResponseError() {
        String errorMessage = "Test error message";
        Response<Object> response = Response.error(errorMessage);

        assertEquals("Status should be ERROR", "ERROR", response.getStatus());
        assertEquals("Message should match", errorMessage, response.getMessage());
        assertNull("Data should be null for errors", response.getData());
    }

    /**
     * Test Response JSON serialization.
     */
    @Test
    public void testResponseSerialization() {
        Response<String> response = Response.success("test data");
        String json = gson.toJson(response);

        assertTrue("JSON should contain status", json.contains("SUCCESS"));
        assertTrue("JSON should contain message", json.contains("successfully"));
        assertTrue("JSON should contain data", json.contains("test data"));
    }

    /**
     * Test complete request-response JSON cycle.
     */
    @Test
    public void testCompleteJsonCycle() {
        // Create request
        Request.Headers headers = new Request.Headers("analysis/clear");
        Request<String> originalRequest = new Request<>(headers, "{}");

        // Serialize to JSON
        String requestJson = gson.toJson(originalRequest);

        // Deserialize back
        Request<Object> deserializedRequest = gson.fromJson(requestJson, Request.class);

        // Verify round-trip
        assertEquals("Action should survive round-trip",
                originalRequest.getHeaders().getAction(),
                deserializedRequest.getHeaders().getAction());
    }
}