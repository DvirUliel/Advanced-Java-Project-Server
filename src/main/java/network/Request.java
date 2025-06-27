package main.java.network;

/**
 * Generic request wrapper for client-server communication.
 * Structure: {"headers": {"action": "..."}, "body": {...}}
 */
public class Request<T> {
    private Headers headers;  // Contains routing information (action)
    private T body;          // Contains actual data (generic type for flexibility)

    // Default constructor for JSON deserialization
    public Request() {}

    public Request(Headers headers, T body) {
        this.headers = headers;
        this.body = body;
    }

    // Standard getters/setters for JSON mapping
    public Headers getHeaders() { return headers; }
    public void setHeaders(Headers headers) { this.headers = headers; }
    public T getBody() { return body; }
    public void setBody(T body) { this.body = body; }

    /**
     * Inner class for request headers.
     * Contains metadata about what action to perform.
     */
    public static class Headers {
        private String action;  // Route identifier: "analysis/maxProfit", "analysis/clear", etc.

        public Headers() {}
        public Headers(String action) { this.action = action; }

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
    }
}