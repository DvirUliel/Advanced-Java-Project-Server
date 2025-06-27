package main.java.network;

/**
 * Standardized response wrapper for all server responses.
 * Structure: {"status": "SUCCESS/ERROR", "message": "...", "data": {...}}
 */
public class Response<T> {
    private String status;   // "SUCCESS" or "ERROR"
    private String message;  // Human-readable description
    private T data;         // Actual response data (null for errors)

    // Default constructor for JSON deserialization
    public Response() {}

    public Response(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Factory method for successful responses
    public static <T> Response<T> success(T data) {
        return new Response<>("SUCCESS", "Operation completed successfully", data);
    }

    // Factory method for error responses
    public static <T> Response<T> error(String message) {
        return new Response<>("ERROR", message, null);
    }

    // Standard getters and setters for JSON mapping
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}