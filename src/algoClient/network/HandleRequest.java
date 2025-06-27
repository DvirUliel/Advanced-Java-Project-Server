package algoClient.network;

import algoClient.controller.AnalysisController;
import algoClient.controller.FactoryController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.*;
import java.net.Socket;

public class HandleRequest implements Runnable {
    private final Socket socket;
    private final Gson gson = new Gson();

    public HandleRequest(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String jsonRequest = reader.readLine();
            System.out.println("Received request: " + jsonRequest);

            // Parse the JSON request
            Type requestType = new TypeToken<Request<Object>>(){}.getType();
            Request<Object> request = gson.fromJson(jsonRequest, requestType);

            String action = request.getHeaders().getAction();
            String bodyJson = gson.toJson(request.getBody());

            // Get controller from factory
            AnalysisController controller = FactoryController.getAnalysisController();
            Response<?> response = controller.handle(action, bodyJson);

            // Send response back to client
            String responseJson = gson.toJson(response);
            writer.println(responseJson);
            System.out.println("Sent response: " + responseJson);

        } catch (Exception e) {
            e.printStackTrace();
            // Send error response
            try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
                Response<?> errorResponse = Response.error("Server error: " + e.getMessage());
                writer.println(gson.toJson(errorResponse));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}