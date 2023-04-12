package dateGenie.server.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import dateGenie.server.models.Restaurant;

@Repository
public class RestaurantsRepo {
    
    public Optional<List<Restaurant>> getRestaurants (String name) {
        
        // JsonReader jsonReader = Json.createReader(new StringReader())
        
        return null; 
    }

}
