package dateGenie.server.services;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import dateGenie.server.models.Restaurant;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class tihRestaurantService {
    public static final String URL_RESTAURANT = "https://api.stb.gov.sg/content/food-beverages/v2/search";

    // Inject into the service TIH private key
    @Value("${TIH_API_KEY}")
    private String apiKey;

    public List<Restaurant> searchRestaurants(String keyword, Integer offset) {
        return searchRestaurants(keyword, 9, offset);
    }

    public List<Restaurant> searchRestaurants(String searchValues, Integer limit, Integer offset) {

        // https://api.stb.gov.sg/content/food-beverages/v2/search
        // searchType="keyword" searchValues="???" limit=10 offset=???
        String url = UriComponentsBuilder.fromUriString(URL_RESTAURANT)
                .queryParam("searchType", "keyword")
                .queryParam("searchValues", searchValues)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .toUriString();

        // System.out.printf("url = %s\n", url);

        // Use the url to make a call
        RequestEntity<Void> req = RequestEntity.get(url)
                .header("X-API-KEY", apiKey)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        String payload = resp.getBody();

        // Parse the String to JsonObject
        JsonReader reader = Json.createReader(new StringReader(payload));
        // { data: [ { } ] }
        JsonObject result = reader.readObject();
        JsonArray data = result.getJsonArray("data");

        List<Restaurant> restaurants = new LinkedList<>();
        restaurants = data.stream()
                        .map(v -> (JsonObject) v)
                        .map(jo -> Restaurant.createRestaurant(jo))
                        .toList();

        return restaurants;
    }
}
