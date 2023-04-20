package dateGenie.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dateGenie.server.models.Attraction;
import dateGenie.server.models.Restaurant;
import dateGenie.server.repositories.AttractionsRepo;
import dateGenie.server.repositories.RestaurantsRepo;

@Service
public class FavouriteService {
    
    @Autowired
    private RestaurantsRepo restaurantsRepo; 

    @Autowired
    private AttractionsRepo attractionsRepo; 

    @Transactional(rollbackFor = FavouriteException.class)
    public void createFavouriteRestaurant(Restaurant restaurant) {
        restaurantsRepo.insertFavouriteRestaurant(restaurant);
        restaurantsRepo.insertFavouriteRestaurantReview(restaurant.getReviews(), restaurant.getUuid());
    }

    @Transactional(rollbackFor = FavouriteException.class)
    public void createFavouriteAttraction(Attraction attraction) {
        attractionsRepo.insertFavouriteAttraction(attraction);
        attractionsRepo.insertFavouriteAttractionReview(attraction.getReviews(), attraction.getUuid());
    }
}
