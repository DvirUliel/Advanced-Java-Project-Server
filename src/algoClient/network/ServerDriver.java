package algoClient.network;

public class ServerDriver {
    public static void main(String[] args) {
        Server server = new Server(34567);
        server.start();  // Direct call, no threading needed
    }
}