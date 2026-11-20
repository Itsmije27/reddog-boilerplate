package app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class WeatherService {

    public String getWeather() {

        try {

            String apiUrl = "https://wttr.in/Koeln?format=3";

            URLConnection connection = URI.create(apiUrl).toURL().openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                return reader.readLine();
            }

        } catch (Exception e) {
            return "Fehler beim Abrufen der Wetterdaten";
        }
    }
}