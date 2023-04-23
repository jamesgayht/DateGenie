package dateGenie.server.models;

import java.util.LinkedList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class Restaurant {
    private String uuid;
    private String name;
    private String type;
    private String description;
    private String imageUUID;
    private String cuisine;
    private List<Review> reviews = new LinkedList<>();
    private Double latitude;
    private Double longitude;
    private String imageUrl;
    private String pricing; 

    public static Restaurant createRestaurantForFavourites(SqlRowSet rs) {
        Restaurant restaurant = new Restaurant(); 
        restaurant.setUuid(rs.getString("restaurant_uuid"));
        restaurant.setName(rs.getString("restaurant_name"));
        restaurant.setType(rs.getString("type"));
        restaurant.setDescription(rs.getString("description"));
        restaurant.setImageUUID(rs.getString("image_uuid"));
        restaurant.setCuisine(rs.getString("cuisine"));
        restaurant.setLatitude(rs.getDouble("latitude"));
        restaurant.setLongitude(rs.getDouble("longitude"));
        restaurant.setPricing(rs.getString("pricing"));
        restaurant.setImageUrl(rs.getString("image_url"));
        return restaurant; 
    }

    public static Restaurant createRestaurantFromClientPayload(JsonObject doc) {
        Restaurant restaurant = new Restaurant(); 

        restaurant.setUuid(doc.getString("restaurant_uuid"));
        restaurant.setName(doc.getString("name"));
        restaurant.setType(doc.getString("type"));
        restaurant.setDescription(doc.getString("description"));
        restaurant.setCuisine(doc.getString("cuisine"));
        restaurant.setPricing(doc.getString("pricing"));
        restaurant.setImageUUID(doc.getString("image_uuid"));
        restaurant.setImageUrl(doc.getString("image_url"));

        JsonArray reviewsArray = doc.getJsonArray("reviews");
        List<Review> reviews = new LinkedList<>();
        reviews = reviewsArray.stream()
                .map(v -> (JsonObject) v)
                .map(jo -> Review.createReview(jo))
                .toList();

        restaurant.setReviews(reviews);
        
        restaurant.setLatitude(doc.getJsonNumber("latitude").doubleValue());
        restaurant.setLongitude(doc.getJsonNumber("longitude").doubleValue());

        return restaurant; 
    }

    // public JsonObject toJsonFavourites() {

    //     return Json.createObjectBuilder()
    //             .add("restaurantUuid", uuid)
    //             .add("restaurantName", name)
    //             .add("restaurantType", type)
    //             .add("restaurantImageeUrl", imageUrl)
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
                .add("cuisine", cuisine)
                .add("pricing", pricing)
                .add("imageUUID", imageUUID)
                .add("imageURL", imageUrl)
                .add("reviews", arrayBuilder)
                .add("latitude", latitude)
                .add("longitude", longitude)
                .build();
    }

    public static Restaurant createRestaurant(JsonObject doc) {

        final Restaurant restaurant = new Restaurant();

        restaurant.setUuid(doc.getString("uuid"));
        restaurant.setName(doc.getString("name"));
        restaurant.setType(doc.getString("type"));
        restaurant.setDescription(
                doc.getString("description").trim().length() > 0 ? doc.getString("description") : "No description listed");

        JsonArray imagesArray = doc.getJsonArray("images");

        if(imagesArray.size() > 0) {
            System.out.println("images array >>> " + imagesArray.get(0));
            JsonObject imageObj = imagesArray.getJsonObject(0);
            restaurant.setImageUUID(imageObj.getString("uuid"));
        }
        else {
            restaurant.setImageUUID("No image listed");
        }

        restaurant.setCuisine(doc.getString("cuisine").trim().length() > 0 ? doc.getString("cuisine") : "No cuisine listed");

        JsonArray reviewsArray = doc.getJsonArray("reviews");
        List<Review> reviews = new LinkedList<>();
        reviews = reviewsArray.stream()
                .map(v -> (JsonObject) v)
                .map(jo -> Review.createReview(jo))
                .toList();

        restaurant.setReviews(reviews);

        restaurant.setPricing(
                doc.getString("pricing").trim().length() > 0 ? doc.getString("pricing") : "No pricing listed");

        JsonObject location = doc.getJsonObject("location");
        restaurant.setLatitude(location.getJsonNumber("latitude").doubleValue());
        restaurant.setLongitude(location.getJsonNumber("longitude").doubleValue());

        return restaurant;
    }

    @Override
    public String toString() {
        return "Restaurant {uuid=%s, name=%s, type=%s, description=%s, imageUUID=%s, cuisine=%s, reviews=%s, latitude=%f, longtitude=%f, pricing=%s}"
                .formatted(uuid, name, type, description, imageUUID, cuisine, reviews.toString(), latitude, longitude, pricing);
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

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
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
