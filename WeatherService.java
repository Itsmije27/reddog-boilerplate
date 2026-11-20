import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class WeatherService {

    public String getWeather() {

        try {

            String apiUrl = "https://wttr.in/Koeln?format=3";

            URL url = new URL(apiUrl);

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    url.openStream()));

            String result = reader.readLine();

            reader.close();

            return result;

        } catch (Exception e) {
            return "Fehler beim Abrufen der Wetterdaten";
        }
    }
}