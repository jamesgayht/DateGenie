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

import dateGenie.server.models.Attraction;
import dateGenie.server.models.AttractionsResult;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class TIHAttractionsService {
    
    public static final String URL_RESTAURANT = "https://api.stb.gov.sg/content/attractions/v2/search";

    // Inject into the service TIH private key
    @Value("${TIH_API_KEY}")
    private String apiKey;

    public AttractionsResult searchAttractions(String keyword, Integer offset) {
        return searchAttractions(keyword, 8, offset);
    }

    public AttractionsResult searchAttractions(String searchValues, Integer limit, Integer offset) {

        // https://api.stb.gov.sg/content/attractions/v2/search
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
        Integer totalRecords = result.getInt("totalRecords"); 
        System.out.println("totalRecords >>> " + totalRecords);

        List<Attraction> attractions = new LinkedList<>();
        attractions = data.stream()
                        .map(v -> (JsonObject) v)
                        .map(jo -> Attraction.createAttraction(jo))
                        .toList();

        AttractionsResult attractionsResult = AttractionsResult.createAttractionsResult(attractions);
        attractionsResult.setTotalRecords(totalRecords);
        
        return attractionsResult;
    }


}
