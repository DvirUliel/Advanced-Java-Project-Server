package main.test.integration;

import main.java.network.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.*;
import java.net.Socket;
import static org.junit.Assert.*;

/**
 * Working integration tests with single-line JSON requests.
 * The key fix: Use single-line JSON since server reads only one line.
 */
public class ServerIntegrationTest {

    private static Thread serverThread;
    private static final int TEST_PORT = 34568;
    private static final int TIMEOUT_MS = 3000;

    @BeforeClass
    public static void startServer() throws InterruptedException {
        Server server = new Server(TEST_PORT);

        serverThread = new Thread(() -> {
            try {
                server.start();
            } catch (Exception e) {
                System.err.println("Server failed to start: " + e.getMessage());
            }
        });

        serverThread.setDaemon(true);
        serverThread.start();

        // Give server time to start
        Thread.sleep(2000);
    }

    @AfterClass
    public static void stopServer() {
        if (serverThread != null) {
            serverThread.interrupt();
        }
    }

    /**
     * Test successful max profit analysis - SINGLE LINE JSON.
     */
    @Test
    public void testMaxProfitRequest() throws Exception {
        // KEY FIX: Single line JSON (no line breaks)
        String request = "{\"headers\":{\"action\":\"analysis/maxProfit\"},\"body\":{\"values\":[1.0,-2.0,3.0,-1.0,2.0],\"dataMode\":\"DAILY_CHANGES\"}}";

        String response = sendTcpRequest(request);

        assertNotNull("Response should not be null", response);
        assertTrue("Response should contain SUCCESS", response.contains("SUCCESS"));
        assertTrue("Response should contain data", response.contains("data"));
        System.out.println("✅ Max Profit Test Passed: " + response);
    }

    /**
     * Test clear operation - SINGLE LINE JSON.
     */
    @Test
    public void testClearRequest() throws Exception {
        // Single line JSON
        String request = "{\"headers\":{\"action\":\"analysis/clear\"},\"body\":{}}";

        String response = sendTcpRequest(request);

        assertNotNull("Response should not be null", response);
        assertTrue("Response should contain SUCCESS", response.contains("SUCCESS"));
        assertTrue("Response should confirm clearing", response.contains("cleared"));
        System.out.println("✅ Clear Test Passed: " + response);
    }

    /**
     * Test error handling for invalid action - SINGLE LINE JSON.
     */
    @Test
    public void testInvalidActionRequest() throws Exception {
        // Single line JSON
        String request = "{\"headers\":{\"action\":\"invalid/action\"},\"body\":{}}";

        String response = sendTcpRequest(request);

        assertNotNull("Response should not be null", response);
        assertTrue("Response should contain ERROR", response.contains("ERROR"));
        assertTrue("Response should mention unknown action", response.contains("Unknown action"));
        System.out.println("✅ Invalid Action Test Passed: " + response);
    }

    /**
     * Test another analysis type - SINGLE LINE JSON.
     */
    @Test
    public void testZeroReturnRequest() throws Exception {
        // Single line JSON
        String request = "{\"headers\":{\"action\":\"analysis/zeroReturn\"},\"body\":{\"values\":[1.0,-1.0,2.0,-2.0],\"dataMode\":\"DAILY_CHANGES\"}}";

        String response = sendTcpRequest(request);

        assertNotNull("Response should not be null", response);
        assertTrue("Response should contain SUCCESS", response.contains("SUCCESS"));
        System.out.println("✅ Zero Return Test Passed: " + response);
    }

    /**
     * Test server connectivity.
     */
    @Test
    public void testServerIsRunning() throws Exception {
        try (Socket socket = new Socket("localhost", TEST_PORT)) {
            socket.setSoTimeout(1000);
            assertTrue("Should be able to connect to server", socket.isConnected());
            System.out.println("✅ Server connectivity test passed");
        }
    }

    /**
     * Helper method to send TCP request and receive response.
     */
    private String sendTcpRequest(String request) throws Exception {
        try (Socket socket = new Socket("localhost", TEST_PORT)) {
            socket.setSoTimeout(TIMEOUT_MS);

            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // Send single-line request
                out.println(request);
                out.flush();

                // Read response
                return in.readLine();
            }
        }
    }
}