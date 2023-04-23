package dateGenie.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import dateGenie.server.models.Attraction;
import dateGenie.server.models.Restaurant;
import dateGenie.server.models.Review;
import dateGenie.server.models.User;

import static dateGenie.server.repositories.Queries.*;

import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void deleteRestaurantByUsernameAndUuid(String username, String uuid) {
        jdbcTemplate.update(SQL_DELETE_FROM_RESTAURANTS, username, uuid);
    }

    public void deleteRestaurantReviewsByUsernameAndUuid(String username, String uuid) {
        jdbcTemplate.update(SQL_DELETE_FROM_RESTAURANT_REVIEWS, username, uuid);
    }

    public void deleteAttractionByUsernameAndUuid(String username, String uuid) {
        jdbcTemplate.update(SQL_DELETE_FROM_ATTRACTIONS, username, uuid);
    }

    public void deleteAttractionReviewsByUsernameAndUuid(String username, String uuid) {
        jdbcTemplate.update(SQL_DELETE_FROM_ATTRACTION_REVIEWS, username, uuid);
    }

    public Optional<List<Attraction>> findAttractionsByUsername(String username) {
        final SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_ATTRACTIONS_BY_USERNAME, username);

        List<Attraction> attractions = new LinkedList<>();
        List<Review> attractionsReviews = new LinkedList<>();

        if (rs.wasNull()) {
            System.out.println("returning null in userrepo attr");
            return Optional.empty();
        }

        while (rs.next()) {
            Attraction attraction = (Attraction.createAttractionForFavourites(rs));
            final SqlRowSet rsReviews = jdbcTemplate.queryForRowSet(SQL_GET_ATTRACTIONS_REVIEWS_BY_UUID,
                    attraction.getUuid());

            if (rsReviews.wasNull()) {
                System.out.println("userrepo attr null");
                attraction.setReviews(attractionsReviews);
            }
            while (rsReviews.next()) {
                attractionsReviews.add(Review.createReviewFromSql(rsReviews));
            }
            attraction.setReviews(attractionsReviews);
            attractions.add(attraction);
        }

        return Optional.of(attractions);
    }

    public Optional<List<Restaurant>> findRestaurantsByUsername(String username) {
        final SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_RESTAURANTS_BY_USERNAME, username);

        List<Restaurant> restaurants = new LinkedList<>();
        List<Review> restaurantsReviews = new LinkedList<>();

        if (rs.wasNull()) {
            return Optional.empty();
        }

        while (rs.next()) {
            Restaurant restaurant = (Restaurant.createRestaurantForFavourites(rs));
            final SqlRowSet rsReviews = jdbcTemplate.queryForRowSet(SQL_GET_RESTAURANTS_REVIEWS_BY_UUID,
                    restaurant.getUuid());

            if (rsReviews.wasNull()) {
                restaurant.setReviews(restaurantsReviews);
            }
            while (rsReviews.next()) {
                restaurantsReviews.add(Review.createReviewFromSql(rsReviews));
            }

            restaurant.setReviews(restaurantsReviews);
            restaurants.add(restaurant);

        }

        return Optional.of(restaurants);
    }

    public Optional<User> findUserByUsernameAndPassword(String username, String password) {

        final SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_USER_BY_USERNAME_AND_PASSWORD, username, password);

        User user = new User();

        if (!rs.next()) {
            return Optional.empty();
        } else {
            user = User.createUserFromSql(rs);
            return Optional.of(user);
        }
    }

    public boolean insertNewUser(User user) {
        String hash = "";
        try {
            // Message digest = md5, sha1, sha512
            // Get an instance of MD5
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // Calculate our hash
            // Update our message digest
            md5.update(user.getPassword().getBytes());
            // Get the MD5 digest
            byte[] h = md5.digest();
            // Stringify the MD5 digest
            hash = HexFormat.of().formatHex(h);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("password >>> " + user.getPassword());
        System.out.println("hash >>> " + hash);

        return jdbcTemplate.update(SQL_INSERT_INTO_USERS, user.getUsername(), user.getEmail(), hash) > 0;
    }

    public Optional<User> findUserByUsername(String username) {

        final SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_USER_BY_USERNAME, username);

        User user = new User();

        if (!rs.next()) {
            return Optional.empty();
        } else {
            user = User.createUserFromSql(rs);
            return Optional.of(user);
        }
    }

    public Optional<User> findUserByEmail(String email) {

        final SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_USER_BY_EMAIL, email);

        User user = new User();

        if (!rs.next()) {
            return Optional.empty();
        } else {
            user = User.createUserFromSql(rs);
        }
        return Optional.of(user);
    }

}
