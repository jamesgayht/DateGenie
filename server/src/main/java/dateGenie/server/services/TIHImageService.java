package dateGenie.server.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dateGenie.server.models.Attraction;
import dateGenie.server.models.Restaurant;
import dateGenie.server.repositories.ImageRepository;

@Service
public class TIHImageService {

    @Autowired
    private ImageRepository imageRepo; 

    // static String FILEPATH = "/Users/jumo/Downloads/NUS_VTTP/dateGenie/server/src/main/resources/static/image.jpg";
    static String FILEPATH = "./src/main/resources/static/image.jpg";
    static File file = new File(FILEPATH);

    
    @Value("${TIH_API_KEY}")
    private String apiKey;

    public String searchAttractionImage(String uuid, Attraction attraction) {
        
        String mediaDownloadURL = "https://api.stb.gov.sg/media/download/v2/";
        mediaDownloadURL += uuid;

        System.out.println("uuid >>>> " + uuid);

        if(uuid.equals("") || uuid.equals(null)) return "No Image Found";

        RequestEntity<Void> req = RequestEntity.get(mediaDownloadURL)
                                    .header("X-API-KEY", apiKey)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .build();

        System.out.println("req >>> " + req);
        
        RestTemplate template = new RestTemplate();
        ResponseEntity<byte[]> resp = template.exchange(req, byte[].class);
        byte[] payload = resp.getBody(); 

        try {
            OutputStream os = new FileOutputStream(file);
            os.write(payload);
            System.out.println("file >>> " + file);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("file >>>> " + file);
        String imageUrl = imageRepo.uploadAttractionImage(attraction, file);

        return imageUrl;
    }

    public String searchRestaurantImage(String uuid, Restaurant restaurant) {
        
        String mediaDownloadURL = "https://api.stb.gov.sg/media/download/v2/";
        mediaDownloadURL += uuid;

        System.out.println("uuid >>>> " + uuid);

        if(uuid.equals("") || uuid.equals(null)) return "No Image Found";

        RequestEntity<Void> req = RequestEntity.get(mediaDownloadURL)
                                    .header("X-API-KEY", apiKey)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .build();

        System.out.println("req >>> " + req);
        
        RestTemplate template = new RestTemplate();
        ResponseEntity<byte[]> resp = template.exchange(req, byte[].class);
        byte[] payload = resp.getBody(); 

        try {
            OutputStream os = new FileOutputStream(file);
            os.write(payload);
            System.out.println("file >>> " + file);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("file >>>> " + file);
        String imageUrl = imageRepo.uploadRestaurantImage(restaurant, file);

        return imageUrl;
    }

}
