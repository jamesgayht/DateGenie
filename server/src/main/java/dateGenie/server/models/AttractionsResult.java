package dateGenie.server.models;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class AttractionsResult {

    private List<Attraction> attractions = new LinkedList<>();
    private LocalDateTime timestamp;

    public JsonObject toJson() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (Attraction attraction : attractions) {
            arrayBuilder.add(attraction.toJson());
        }

        return Json.createObjectBuilder()
                .add("attractions", arrayBuilder)
                .add("timestamp", timestamp.toString())
                .build();
    }

    public static AttractionsResult createAttractionsResult(List<Attraction> attractions) {

        AttractionsResult attractionsResult = new AttractionsResult();
        attractionsResult.setAttractions(attractions);
        attractionsResult.setTimestamp(LocalDateTime.now());

        return attractionsResult;
    }

    public List<Attraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<Attraction> attractions) {
        this.attractions = attractions;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
