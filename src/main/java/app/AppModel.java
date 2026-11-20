package app;

public class AppModel {
    private String message = "Willkommen!";
    private int clickCount = 0;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int incrementClickCount() {
        return ++clickCount;
    }
}
