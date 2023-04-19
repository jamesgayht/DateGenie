package dateGenie.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dateGenie.server.models.Restaurant;
import dateGenie.server.models.RestaurantResults;
import dateGenie.server.services.TIHImageService;
import dateGenie.server.services.tihRestaurantService;
import jakarta.json.JsonObject;

@Controller
@RequestMapping(path = "/api/restaurants")
@CrossOrigin(origins = "*")
public class RestaurantController {

    @Autowired
    private tihRestaurantService tihRestaurantService;

    @Autowired
    private TIHImageService tihImageService;

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

    // @GetMapping(path = "/images/{uuid}")
    // public ResponseEntity<String> searchRestaurantImage(@PathVariable(name = "uuid") String uuid) {

    //     // tihImageService.searchImage(uuid);

    //     return ResponseEntity.ok("ok");
    // }
}
