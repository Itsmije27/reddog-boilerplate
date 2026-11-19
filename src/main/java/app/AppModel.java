package app;

import java.time.LocalTime;

public class AppModel {
    private static final String DEFAULT_MESSAGE = "Willkommen!";
    private static final String DEFAULT_SUBTITLE = "Tippe deinen Namen ein und sag Hallo!";

    private static final String[] CUTE_SUBTITLES = {
            "Wuff! Dein kleiner Chibi-Hund wedelt vor Freude!",
            "Sterne und Konfetti nur für dich!",
            "Du machst den Tag gleich doppelt so süß!",
            "Pfötchen hoch — schön, dass du da bist!"
    };

    private static final String[] WAG_SUBTITLES = {
            "Wedel, wedel! Der kleine Hund freut sich riesig!",
            "Wusch, wusch! So ein fröhliches Schwänzchen!",
            "Glöckchen klingelt — beste Laune!"
    };

    private String message = DEFAULT_MESSAGE;
    private String subtitle = DEFAULT_SUBTITLE;
    private String name = "";
    private int greetingCount = 0;
    private int wagCount = 0;
    private boolean darkMode = false;
    private boolean excited = false;

    public String getMessage() {
        return message;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name.trim();
    }

    public int getGreetingCount() {
        return greetingCount;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public boolean isExcited() {
        return excited;
    }

    public void greet() {
        greet(LocalTime.now());
    }

    public void greet(LocalTime time) {
        String greetingPrefix = switch (greetingCount % 4) {
            case 0 -> timeOfDayGreeting(time);
            case 1 -> "Huhu";
            case 2 -> "Juhu, Hallo";
            default -> "Schön, dich zu sehen";
        };

        message = name.isEmpty()
                ? greetingPrefix + "!"
                : greetingPrefix + ", " + name + "!";
        subtitle = CUTE_SUBTITLES[greetingCount % CUTE_SUBTITLES.length];
        greetingCount++;
        excited = true;
    }

    public void wagTail() {
        subtitle = WAG_SUBTITLES[wagCount % WAG_SUBTITLES.length];
        wagCount++;
        excited = true;
    }

    public void reset() {
        message = DEFAULT_MESSAGE;
        subtitle = DEFAULT_SUBTITLE;
        name = "";
        greetingCount = 0;
        wagCount = 0;
        excited = false;
    }

    public void toggleDarkMode() {
        darkMode = !darkMode;
    }

    private static String timeOfDayGreeting(LocalTime time) {
        int hour = time.getHour();
        if (hour >= 5 && hour < 11) {
            return "Guten Morgen";
        }
        if (hour >= 11 && hour < 17) {
            return "Guten Tag";
        }
        if (hour >= 17 && hour < 22) {
            return "Guten Abend";
        }
        return "Gute Nacht";
    }
}
