package co.unicauca.dto;

public class NotificationResponse {
    private boolean sent;
    private String message;

    public NotificationResponse() {
    }

    public boolean isSent() { return sent; }
    public void setSent(boolean sent) { this.sent = sent; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
