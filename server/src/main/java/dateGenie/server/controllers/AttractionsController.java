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

import dateGenie.server.models.Attraction;
import dateGenie.server.models.AttractionsResult;
import dateGenie.server.models.Review;
import dateGenie.server.services.FavouriteService;
import dateGenie.server.services.TIHAttractionsService;
import dateGenie.server.services.TIHImageService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Controller
@RequestMapping(path = "/api/attractions")
@CrossOrigin(origins = "*")
public class AttractionsController {

    @Autowired
    private TIHImageService tihImageService;

    @Autowired
    private TIHAttractionsService tihAttractionsService;

    @Autowired
    private FavouriteService favouriteService;

    @GetMapping(path = "/{keyword}")
    public ResponseEntity<String> searchAttractions(@RequestParam String keyword, int offset) {
        AttractionsResult attractionResults = tihAttractionsService.searchAttractions(keyword, offset);

        for (Attraction attraction : attractionResults.getAttractions()) {
            attraction.setImageUrl(tihImageService.searchAttractionImage(attraction.getImageUUID(), attraction));
        }

        JsonObject results = attractionResults.toJson();

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(results.toString());
    }

    @PostMapping(path = "/favourites", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postFavouriteAttractions(@RequestBody String body) {
        JsonReader reader = Json.createReader(new StringReader(body));
        JsonObject json = reader.readObject();

        System.out.println("json for favourite attraction >>> " + json.toString());

        Attraction attraction = Attraction.createAttractionFromClientPayload(json);
        JsonArray reviewsJson = json.getJsonArray("reviews");
        List<Review> reviews = new LinkedList<>();
        reviews = reviewsJson.stream()
                .map(v -> (JsonObject) v)
                .map(v -> Review.createReview(v))
                .toList();

        System.out.println("attraction >>> " + attraction);
        System.out.println("reviews >>> " + reviews);

        try {
            favouriteService.createFavouriteAttraction(attraction);
        } catch (Exception e) {
            String attractionSaveError = "{\"error\": \"%s\"}".formatted(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                    .body(attractionSaveError);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(json.toString());
    }
}
