package dateGenie.server.controllers;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dateGenie.server.models.Restaurant;
import dateGenie.server.models.RestaurantResults;
import dateGenie.server.models.Review;
import dateGenie.server.services.FavouriteService;
import dateGenie.server.services.TIHImageService;
import dateGenie.server.services.tihRestaurantService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Controller
@RequestMapping(path = "/api/restaurants")
@CrossOrigin(origins = "*")
public class RestaurantController {

    @Autowired
    private tihRestaurantService tihRestaurantService;

    @Autowired
    private TIHImageService tihImageService;

    @Autowired
    private FavouriteService favouriteService;

    @GetMapping(path = "/{keyword}")
    public ResponseEntity<String> searchRestaurants(@RequestParam String keyword, int offset) {

        RestaurantResults restaurantResults = tihRestaurantService.searchRestaurants(keyword, offset);

        for (Restaurant res : restaurantResults.getRestaurants()) {
            res.setImageUrl(tihImageService.searchRestaurantImage(res.getImageUUID(), res));
        }

        JsonObject results = restaurantResults.toJson();

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(results.toString());
    }

    @PostMapping(path = "/favourites", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postFavouriteRestaurants(@RequestBody String body) {

        JsonReader reader = Json.createReader(new StringReader(body));
        JsonObject json = reader.readObject();

        System.out.println("json for favourite restaurant >>> " + json.toString());

        Restaurant restaurant = Restaurant.createRestaurantFromClientPayload(json);
        JsonArray reviewsJson = json.getJsonArray("reviews");
        List<Review> reviews = new LinkedList<>();
        reviews = reviewsJson.stream()
                .map(v -> (JsonObject) v)
                .map(v -> Review.createReview(v))
                .toList();

        System.out.println("restaurant >>> " + restaurant);
        System.out.println("reviews >>> " + reviews);

        try {
            favouriteService.createFavouriteRestaurant(restaurant);
        } catch (Exception e) {
            String restaurantSaveError = "{\"error\": \"%s\"}".formatted(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                    .body(restaurantSaveError);
        }

        String restaurantPostCompletion = "{\"posted to favourites\": \"%s\"}".formatted(restaurant.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(restaurantPostCompletion.toString());
    }
}
