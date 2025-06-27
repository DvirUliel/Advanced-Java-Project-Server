package algoClient.network;

public class Response<T> {
    private String status;
    private String message;
    private T data;

    public Response() {}

    public Response(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>("SUCCESS", "Operation completed successfully", data);
    }

    public static <T> Response<T> error(String message) {
        return new Response<>("ERROR", message, null);
    }

    // Getters and setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}