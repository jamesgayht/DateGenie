package dateGenie.server.repositories;

public class Queries {

    public static String SQL_INSERT_INTO_RESTAURANTS = 
    "insert into restaurants (restaurant_uuid, restaurant_name, type, description, image_uuid, cuisine, latitude, longitude, image_url, pricing) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";    

    public static String SQL_INSERT_INTO_RESTAURANT_REVIEWS = "insert into restaurant_reviews (author_name, rating, review, review_date, restaurant_uuid) values (?, ?, ?, ?, ?)";    

    public static String SQL_INSERT_INTO_ATTRACTIONS = 
    "insert into attractions (attraction_uuid, attraction_name, type, description, image_uuid, latitude, longitude, image_url, pricing) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";    

    public static String SQL_INSERT_INTO_ATTRACTION_REVIEWS = "insert into attraction_reviews (author_name, rating, review, review_date, attraction_uuid) values (?, ?, ?, ?, ?)";    

}
