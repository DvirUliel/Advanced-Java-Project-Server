package main.java.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP Server that accepts client connections and handles them concurrently.
 * Each client gets its own thread for parallel processing.
 */
public class Server {
    private final int port;  // Port number to listen on

    public Server(int port) {
        this.port = port;
    }

    /**
     * Main server loop - accepts clients and creates handler threads.
     * Runs indefinitely until IOException occurs.
     */
    public void start() {
        // Try-with-resources ensures ServerSocket is closed properly
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("✅ Server started successfully!");
            System.out.println("📡 Listening on port: " + port);
            System.out.println("🔄 Ready to accept client connections...");

            // Infinite loop - accept clients continuously
            while (true) {
                // Block here until a client connects
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getRemoteSocketAddress());

                // Create handler for this specific client
                HandleRequest requestHandler = new HandleRequest(clientSocket);

                // Start new thread - allows concurrent client handling
                new Thread(requestHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}