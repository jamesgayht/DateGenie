package dateGenie.server.models;

import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class Favourites {

    private List<Restaurant> restaurants = new LinkedList<>(); 
    private List<Attraction> attractions = new LinkedList<>();
    
    public JsonObject toJson() {
        JsonArrayBuilder resBuilder = Json.createArrayBuilder();
        JsonArrayBuilder attrBuilder = Json.createArrayBuilder();
        if(restaurants.size() > 0) {
            for(Restaurant res: restaurants) {
                resBuilder.add(res.toJson());
            }
        }

        if(attractions.size() > 0) {
            for(Attraction attr: attractions) {
                attrBuilder.add(attr.toJson());
            }
        }

        return Json.createObjectBuilder()
        .add("restaurants", resBuilder)
        .add("attractions", attrBuilder)
        .build();

    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }
    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
    public List<Attraction> getAttractions() {
        return attractions;
    }
    public void setAttractions(List<Attraction> attractions) {
        this.attractions = attractions;
    } 

}
