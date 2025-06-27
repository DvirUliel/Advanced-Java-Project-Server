package algoClient.network;

public class Request<T> {
    private Headers headers;
    private T body;

    public Request() {}

    public Request(Headers headers, T body) {
        this.headers = headers;
        this.body = body;
    }

    public Headers getHeaders() { return headers; }
    public void setHeaders(Headers headers) { this.headers = headers; }
    public T getBody() { return body; }
    public void setBody(T body) { this.body = body; }

    public static class Headers {
        private String action;

        public Headers() {}
        public Headers(String action) { this.action = action; }

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
    }
}