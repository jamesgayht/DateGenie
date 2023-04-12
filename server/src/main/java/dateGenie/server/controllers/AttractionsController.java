package dateGenie.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dateGenie.server.models.Attraction;
import dateGenie.server.models.AttractionsResult;
import dateGenie.server.services.TIHAttractionsService;
import dateGenie.server.services.TIHImageService;
import jakarta.json.JsonObject;

@Controller
@RequestMapping(path = "/api/attractions")
public class AttractionsController {
    
    @Autowired
    private TIHImageService tihImageService;

    @Autowired
    private TIHAttractionsService tihAttractionsService;

    @GetMapping
    public ResponseEntity<String> searchAttractions (@RequestParam String searchValues) {
        List<Attraction> attractions = tihAttractionsService.searchAttractions(searchValues, 10);

        for (Attraction attraction : attractions) {
            attraction.setImageUrl(tihImageService.searchAttractionImage(attraction.getImageUUID(), attraction));
        }

        AttractionsResult attractionResults = AttractionsResult.createAttractionsResult(attractions);

        JsonObject results = attractionResults.toJson();
        
        return ResponseEntity.ok(results.toString());
    }
}
