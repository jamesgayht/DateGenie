package dateGenie.server.repositories;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import dateGenie.server.models.Attraction;
import dateGenie.server.models.Restaurant;

@Repository
public class ImageRepository {
    
    private Logger logger = Logger.getLogger(ImageRepository.class.getName());

	@Value("${spaces.bucket}")
	private String spacesBucket;

	@Value("${spaces.endpoint.url}")
	private String spacesEndpointUrl;

    @Autowired
    private AmazonS3 s3; 

	public String uploadAttractionImage(Attraction attraction, File image) {

		try {
			PutObjectRequest putReq = new PutObjectRequest(spacesBucket
					, attraction.getName(), image);
			putReq.withCannedAcl(CannedAccessControlList.PublicRead);
			s3.putObject(putReq);
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Put S3", ex);
			// return "Upload Error";
		}

		String imageUrl = "https://%s.%s/%s"
				.formatted(spacesBucket, spacesEndpointUrl, attraction.getName());
		attraction.setImageUrl(imageUrl);

        System.out.println("image URL >>> " + imageUrl);

		return imageUrl;
	}

	public String uploadRestaurantImage(Restaurant restaurant, File image) {

		try {
			PutObjectRequest putReq = new PutObjectRequest(spacesBucket
					, restaurant.getName(), image);
			putReq.withCannedAcl(CannedAccessControlList.PublicRead);
			s3.putObject(putReq);
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Put S3", ex);
			// return "Upload Error";
		}

		String imageUrl = "https://%s.%s/%s"
				.formatted(spacesBucket, spacesEndpointUrl, restaurant.getName());
		restaurant.setImageUrl(imageUrl);

        System.out.println("image URL >>> " + imageUrl);

		return imageUrl;
	}

}
