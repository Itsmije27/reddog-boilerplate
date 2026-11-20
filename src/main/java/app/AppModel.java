package app;
//coment nummer2
//extra
public class AppModel {
    private String message = "Willkommen!";
    private String m1 = "Ihr Gewinn";
    //private String m2= "Gewinn!";

    public String getMessage() {
        return message;
    }
    
    public String getMessage2() {
        return m1;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setMessage2(String m1) {
        this.m1 = m1;
    }
}
