package algoClient.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Alternative Server implementation that doesn't implement Runnable.
 * Uses a simple start() method instead.
 */
public class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    /**
     * Starts the server and listens for client connections.
     * This version doesn't implement Runnable.
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getRemoteSocketAddress());

                // Create handler for this client
                HandleRequest requestHandler = new HandleRequest(clientSocket);

                // Start new thread for this client
                new Thread(requestHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}