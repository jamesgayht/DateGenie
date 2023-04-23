package dateGenie.server.models;

import java.util.LinkedList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class Attraction {
    private String uuid;
    private String name;
    private String type;
    private String description;
    private String imageUUID;
    private List<Review> reviews = new LinkedList<>();
    private Double latitude;
    private Double longitude;
    private String imageUrl;
    private String pricing;

    public static Attraction createAttractionForFavourites(SqlRowSet rs) {
        Attraction attraction = new Attraction(); 
        attraction.setUuid(rs.getString("attraction_uuid"));
        attraction.setName(rs.getString("attraction_name"));
        attraction.setType(rs.getString("type"));
        attraction.setDescription(rs.getString("description"));
        attraction.setImageUUID(rs.getString("image_uuid"));
        attraction.setLatitude(rs.getDouble("latitude"));
        attraction.setLongitude(rs.getDouble("longitude"));
        attraction.setPricing(rs.getString("pricing"));
        attraction.setImageUrl(rs.getString("image_url"));
        return attraction; 
    }

    public static Attraction createAttractionFromClientPayload(JsonObject doc) {
        Attraction attraction = new Attraction(); 

        attraction.setUuid(doc.getString("attraction_uuid"));
        attraction.setName(doc.getString("name"));
        attraction.setType(doc.getString("type"));
        attraction.setDescription(doc.getString("description"));
        attraction.setPricing(doc.getString("pricing"));
        attraction.setImageUUID(doc.getString("image_uuid"));
        attraction.setImageUrl(doc.getString("image_url"));

        JsonArray reviewsArray = doc.getJsonArray("reviews");
        List<Review> reviews = new LinkedList<>();
        reviews = reviewsArray.stream()
                .map(v -> (JsonObject) v)
                .map(jo -> Review.createReview(jo))
                .toList();

        attraction.setReviews(reviews);
        
        attraction.setLatitude(doc.getJsonNumber("latitude").doubleValue());
        attraction.setLongitude(doc.getJsonNumber("longitude").doubleValue());

        return attraction; 
    }

    // public JsonObject toJsonFavourites() {
    //     return Json.createObjectBuilder()
    //             .add("attractionUuid", uuid)
    //             .add("attractionName", name)
    //             .add("attractionType", type)
    //             .add("attractionImageUrl", imageUrl)
    //             .build();
    // }

    public JsonObject toJson() {

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (Review review : reviews) {
            arrayBuilder.add(review.toJson());
        }

        return Json.createObjectBuilder()
                .add("uuid", uuid)
                .add("name", name)
                .add("type", type)
                .add("description", description)
                .add("pricing", pricing)
                .add("imageUUID", imageUUID)
                .add("imageURL", imageUrl)
                .add("reviews", arrayBuilder)
                .add("latitude", latitude)
                .add("longitude", longitude)
                .build();
    }

    public static Attraction createAttraction(JsonObject doc) {

        final Attraction attraction = new Attraction();

        attraction.setUuid(doc.getString("uuid"));
        attraction.setName(doc.getString("name"));
        attraction.setType(doc.getString("type"));
        attraction.setDescription(
                doc.getString("description").trim().length() > 0 ? doc.getString("description")
                        : "No description listed");

        JsonArray imagesArray = doc.getJsonArray("images");

        if (imagesArray.size() > 0) {
            System.out.println("images array >>> " + imagesArray.get(0));
            JsonObject imageObj = imagesArray.getJsonObject(0);
            attraction.setImageUUID(imageObj.getString("uuid"));
        } else {
            attraction.setImageUUID("no image listed");
        }

        JsonObject pricesObj = doc.getJsonObject("pricing");

        attraction.setPricing(
                pricesObj.getString("others").trim().length() > 0 ? pricesObj.getString("others") : "No pricing listed");

        JsonArray reviewsArray = doc.getJsonArray("reviews");
        List<Review> reviews = new LinkedList<>();
        reviews = reviewsArray.stream()
                .map(v -> (JsonObject) v)
                .map(jo -> Review.createReview(jo))
                .toList();

        attraction.setReviews(reviews);

        JsonObject location = doc.getJsonObject("location");
        attraction.setLatitude(location.getJsonNumber("latitude").doubleValue());
        attraction.setLongitude(location.getJsonNumber("longitude").doubleValue());

        return attraction;
    }

    @Override
    public String toString() {
        return "Attraction {uuid=%s, name=%s, type=%s, description=%s, imageUUID=%s, pricing=%s, reviews=%s, latitude=%f, longtitude=%f}"
                .formatted(uuid, name, type, description, imageUUID, pricing, reviews.toString(), latitude, longitude);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUUID() {
        return imageUUID;
    }

    public void setImageUUID(String imageUUID) {
        this.imageUUID = imageUUID;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPricing() {
        return pricing;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }
}
