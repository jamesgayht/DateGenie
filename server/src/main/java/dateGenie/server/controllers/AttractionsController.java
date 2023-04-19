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

import dateGenie.server.models.Attraction;
import dateGenie.server.models.AttractionsResult;
import dateGenie.server.services.TIHAttractionsService;
import dateGenie.server.services.TIHImageService;
import jakarta.json.JsonObject;

@Controller
@RequestMapping(path = "/api/attractions")
@CrossOrigin(origins = "*")
public class AttractionsController {

    @Autowired
    private TIHImageService tihImageService;

    @Autowired
    private TIHAttractionsService tihAttractionsService;

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
}
