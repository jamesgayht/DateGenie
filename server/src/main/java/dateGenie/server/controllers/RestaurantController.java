package dateGenie.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dateGenie.server.models.Restaurant;
import dateGenie.server.models.RestaurantResults;
import dateGenie.server.services.TIHImageService;
import dateGenie.server.services.tihRestaurantService;
import jakarta.json.JsonObject;

@Controller
@RequestMapping(path = "/api/restaurants")
public class RestaurantController {

    @Autowired
    private tihRestaurantService tihRestaurantService;

    @Autowired
    private TIHImageService tihImageService;

    @GetMapping
    public ResponseEntity<String> searchRestaurants(@RequestParam String searchValues) {

        List<Restaurant> restaurants = tihRestaurantService.searchRestaurants(searchValues, 10);

        for (Restaurant res : restaurants) {
            res.setImageUrl(tihImageService.searchRestaurantImage(res.getImageUUID(), res));
        }

        RestaurantResults restaurantResults = RestaurantResults.createRestaurantResults(restaurants);

        JsonObject results = restaurantResults.toJson();

        return ResponseEntity.ok(results.toString());
    }

    // @GetMapping(path = "/images/{uuid}")
    // public ResponseEntity<String> searchRestaurantImage(@PathVariable(name = "uuid") String uuid) {

    //     // tihImageService.searchImage(uuid);

    //     return ResponseEntity.ok("ok");
    // }
}
