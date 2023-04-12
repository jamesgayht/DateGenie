package dateGenie.server.models;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;


import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class RestaurantResults {
    
    private List<Restaurant> restaurants = new LinkedList<>(); 
    private LocalDateTime timestamp;

    public JsonObject toJson() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder(); 

        for(Restaurant res: restaurants) {
            arrayBuilder.add(res.toJson()); 
        }

        return Json.createObjectBuilder()
        .add("restaurants", arrayBuilder)
        .add("timestamp", timestamp.toString())
        .build();
    }

    public static RestaurantResults createRestaurantResults(List<Restaurant> restaurants) {
        
        RestaurantResults restaurantResults = new RestaurantResults(); 
        restaurantResults.setRestaurants(restaurants);
        restaurantResults.setTimestamp(LocalDateTime.now());

        return restaurantResults; 
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }
    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    } 


}
