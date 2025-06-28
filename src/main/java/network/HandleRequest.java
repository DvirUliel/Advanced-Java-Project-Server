package main.java.network;

import main.java.controller.AnalysisController;
import main.java.controller.FactoryController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.*;
import java.net.Socket;

/**
 * Handles individual client requests in separate threads.
 * Each instance processes one client connection from start to finish.
 * FIXED VERSION - Robust JSON reading
 */
public class HandleRequest implements Runnable {

    private final Socket socket;        // Connection to specific client
    private final Gson gson = new Gson(); // JSON converter (thread-safe)

    public HandleRequest(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // Try-with-resources ensures streams auto-close
        try (
                // Read FROM client - using InputStreamReader directly for better control
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // Write TO client (autoFlush=true sends data immediately)
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {

            // Step 1: Read JSON request from client - IMPROVED VERSION
            String jsonRequest = readCompleteJson(reader);
            System.out.println("Received request: " + jsonRequest);

            // Validate that we received actual JSON
            if (jsonRequest == null || jsonRequest.trim().isEmpty()) {
                throw new IOException("Received empty or null request");
            }

            // Step 2: Parse JSON to Request object
            // TypeToken needed because Request<Object> is generic type
            Type requestType = new TypeToken<Request<Object>>(){}.getType();
            Request<Object> request = gson.fromJson(jsonRequest, requestType);

            // Step 3: Extract action and body data
            String action = request.getHeaders().getAction(); // "analysis/maxProfit"
            String bodyJson = gson.toJson(request.getBody()); // Convert back to JSON for controller

            // Step 4: Get controller and process request
            AnalysisController controller = FactoryController.getAnalysisController();
            Response<?> response = controller.handle(action, bodyJson);

            // Step 5: Send response back to client
            String responseJson = gson.toJson(response);
            writer.println(responseJson);
            writer.flush(); // Ensure data is sent immediately
            System.out.println("Sent response: " + responseJson);

        } catch (Exception e) {
            System.err.println("Error handling request: " + e.getMessage());
            e.printStackTrace();

            // Try to send error response to client
            try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
                Response<?> errorResponse = Response.error("Server error: " + e.getMessage());
                String errorJson = gson.toJson(errorResponse);
                writer.println(errorJson);
                writer.flush();
                System.out.println("Sent error response: " + errorJson);
            } catch (Exception ex) {
                System.err.println("Failed to send error response: " + ex.getMessage());
                ex.printStackTrace();
            }
        } finally {
            // Always close socket to prevent resource leak
            try {
                socket.close();
                System.out.println("Client connection closed");
            } catch (Exception e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    /**
     * Read complete JSON from client - handles multi-line JSON and ensures complete transmission
     */
    private String readCompleteJson(BufferedReader reader) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        int braceCount = 0;
        boolean jsonStarted = false;
        boolean inString = false;
        boolean escaped = false;

        // Read character by character to properly handle JSON structure
        int ch;
        while ((ch = reader.read()) != -1) {
            char c = (char) ch;
            jsonBuilder.append(c);

            // Handle string escaping
            if (escaped) {
                escaped = false;
                continue;
            }

            if (c == '\\') {
                escaped = true;
                continue;
            }

            // Handle strings (ignore braces inside strings)
            if (c == '"') {
                inString = !inString;
                continue;
            }

            if (inString) {
                continue; // Skip brace counting inside strings
            }

            // Count braces to determine when JSON is complete
            if (c == '{') {
                braceCount++;
                jsonStarted = true;
            } else if (c == '}') {
                braceCount--;

                // If we've closed all braces, JSON is complete
                if (jsonStarted && braceCount == 0) {
                    break;
                }
            }
        }

        String result = jsonBuilder.toString().trim();

        // Fallback: if the above method doesn't work, try readLine
        if (result.isEmpty() || (!result.startsWith("{") && !result.startsWith("["))) {
            System.out.println("Trying fallback readLine method...");
            result = reader.readLine();
        }

        return result;
    }
}