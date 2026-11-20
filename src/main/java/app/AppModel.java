package app;

import java.util.List;
import java.util.Random;

public class AppModel {
    private static final List<String> GREETINGS = List.of(
            "Hallo!",
            "Guten Tag!",
            "Servus!",
            "Moin!",
            "Grüß Gott!",
            "Hi!",
            "Hello!",
            "Bonjour!",
            "Hola!",
            "Ciao!",
            "Hej!",
            "Merhaba!",
            "Konnichiwa!"
    );

    private final Random random = new Random();
    private String message = "Willkommen!";

    /** Wählt eine zufällige Begrüßung, die sich von der aktuellen unterscheidet. */
    public void nextGreeting() {
        String next;
        do {
            next = GREETINGS.get(random.nextInt(GREETINGS.size()));
        } while (next.equals(message));
        message = next;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
