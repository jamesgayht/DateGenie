package dateGenie.server.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dateGenie.server.models.Restaurant;
import dateGenie.server.models.Review;

import static dateGenie.server.repositories.Queries.*;

@Repository
public class RestaurantsRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean insertFavouriteRestaurant(Restaurant restaurant, String username) {

        return jdbcTemplate.update(SQL_INSERT_INTO_RESTAURANTS, restaurant.getUuid(), restaurant.getName(),
                restaurant.getType(), restaurant.getDescription(), restaurant.getImageUUID(), restaurant.getCuisine(),
                restaurant.getLatitude(), restaurant.getLongitude(), restaurant.getImageUrl(),
                restaurant.getPricing(), username) > 0;

    }

    public void insertFavouriteRestaurantReview(List<Review> reviews, String restaurantUuid, String username) {
        List<Object[]> data = reviews.stream()
                .map(review -> {
                    Object[] l = new Object[6];
                    l[0] = review.getAuthorName();
                    l[1] = review.getRating();
                    l[2] = review.getText();
                    l[3] = review.getDate();
                    l[4] = restaurantUuid;
                    l[5] = username;
                    return l;
                }).toList();

        // batch update
        jdbcTemplate.batchUpdate(SQL_INSERT_INTO_RESTAURANT_REVIEWS, data);

    }

}
