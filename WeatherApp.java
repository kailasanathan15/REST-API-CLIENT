import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class WeatherApp {

    private static final String API_KEY = "b8909560e65f972f2803fdd8d489974a";  // MyAPI key
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) throws IOException {
        String city = "Puducherry"; 
        String url = String.format("%s?q=%s&appid=%s&units=metric", BASE_URL, city, API_KEY);

        // 1. Create an HttpClient
        HttpClient client = HttpClientBuilder.create().build();

        // 2. Create an HttpGet request
        HttpGet request = new HttpGet(url);

        // 3. Execute the request
        try {
            org.apache.http.HttpResponse response = client.execute(request);
            String jsonResponse = EntityUtils.toString(response.getEntity());

            // 4. Parse the JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            // 5. Extract and display data
            if (root.has("cod") && root.get("cod").asInt() != 200) {
    String message = root.has("message") ? root.get("message").asText() : "Unknown error";
    System.out.println("API Error: " + message);
    return;
    }
            String cityName = root.get("name").asText();
            double temperature = root.get("main").get("temp").asDouble();
            int humidity = root.get("main").get("humidity").asInt();
            double windSpeed = root.get("wind").get("speed").asDouble();
            String description = root.get("weather").get(0).get("description").asText();

            System.out.println("Weather in " + cityName + ":");
            System.out.println("Temperature: " + temperature + "°C");
            System.out.println("Humidity: " + humidity + "%");
            System.out.println("Wind Speed: " + windSpeed + " m/s");
            System.out.println("Description: " + description);

        } catch (IOException e) {
            System.err.println("Error fetching weather data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
