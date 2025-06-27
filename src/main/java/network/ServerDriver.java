package main.java.network;

/**
 * Entry point for the analysis server application.
 * Starts the server on port 34567 as required by the project specification.
 */
public class ServerDriver {
    public static void main(String[] args) {
        // Create server instance with specified port
        Server server = new Server(34567);

        // Start server - this will block and run indefinitely
        server.start();
    }
}