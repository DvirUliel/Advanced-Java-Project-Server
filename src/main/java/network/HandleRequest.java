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
                // Read FROM client
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // Write TO client (autoFlush=true sends data immediately)
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {

            // Step 1: Read JSON request from client
            String jsonRequest = reader.readLine();
            System.out.println("Received request: " + jsonRequest);

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
            System.out.println("Sent response: " + responseJson);

        } catch (Exception e) {
            e.printStackTrace();
            // Try to send error response to client
            try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
                Response<?> errorResponse = Response.error("Server error: " + e.getMessage());
                writer.println(gson.toJson(errorResponse));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            // Always close socket to prevent resource leak
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}