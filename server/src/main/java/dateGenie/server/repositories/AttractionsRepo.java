package dateGenie.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dateGenie.server.models.Attraction;
import dateGenie.server.models.Review;

import static dateGenie.server.repositories.Queries.*;

import java.util.List;

@Repository
public class AttractionsRepo {
    
    @Autowired
    private JdbcTemplate jdbcTemplate; 

    public boolean insertFavouriteAttraction(Attraction attraction, String username) {

        return jdbcTemplate.update(SQL_INSERT_INTO_ATTRACTIONS, attraction.getUuid(), attraction.getName(),
                attraction.getType(), attraction.getDescription(), attraction.getImageUUID(),
                attraction.getLatitude(), attraction.getLongitude(), attraction.getImageUrl(),
                attraction.getPricing(), username) > 0;

    }

    public void insertFavouriteAttractionReview(List<Review> reviews, String attractionUuid, String username) {
        List<Object[]> data = reviews.stream()
                .map(review -> {
                    Object[] l = new Object[6];
                    l[0] = review.getAuthorName();
                    l[1] = review.getRating();
                    l[2] = review.getText();
                    l[3] = review.getDate();
                    l[4] = attractionUuid;
                    l[5] = username;
                    System.out.println("attr l >>> " + l[5]);
                    return l;
                }).toList();

        // batch update
        jdbcTemplate.batchUpdate(SQL_INSERT_INTO_ATTRACTION_REVIEWS, data);

    }

}
