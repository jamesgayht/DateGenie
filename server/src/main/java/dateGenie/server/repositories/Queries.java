package dateGenie.server.repositories;

public class Queries {

    public static String SQL_INSERT_INTO_RESTAURANTS = 
    "insert into restaurants (restaurant_uuid, restaurant_name, type, description, image_uuid, cuisine, latitude, longitude, image_url, pricing, username) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";    

    public static String SQL_INSERT_INTO_RESTAURANT_REVIEWS = "insert into restaurant_reviews (author_name, rating, review, review_date, restaurant_uuid, username) values (?, ?, ?, ?, ?, ?)";    

    public static String SQL_INSERT_INTO_ATTRACTIONS = 
    "insert into attractions (attraction_uuid, attraction_name, type, description, image_uuid, latitude, longitude, image_url, pricing, username) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";    

    public static String SQL_INSERT_INTO_ATTRACTION_REVIEWS = "insert into attraction_reviews (author_name, rating, review, review_date, attraction_uuid, username) values (?, ?, ?, ?, ?, ?)";    

    public static String SQL_INSERT_INTO_USERS = "insert into users (username, email, password) values (?, ?, ?)";    

    public static String SQL_GET_USER_BY_USERNAME = "select * from users where username = ?";
    
    public static String SQL_GET_USER_BY_EMAIL = "select * from users where email = ?";
    
    public static String SQL_GET_USER_BY_USERNAME_AND_PASSWORD = "select * from users where username = ? AND password = ?";

    public static String SQL_GET_RESTAURANTS_BY_USERNAME = "select * from restaurants where username = ?";

    public static String SQL_GET_ATTRACTIONS_BY_USERNAME = "select * from attractions where username = ?";

    public static String SQL_DELETE_FROM_RESTAURANTS = "delete from restaurants where username = ? AND restaurant_uuid = ?";

    public static String SQL_DELETE_FROM_RESTAURANT_REVIEWS = "delete from restaurant_reviews where username = ? AND restaurant_uuid = ?";
    
    public static String SQL_DELETE_FROM_ATTRACTIONS = "delete from attractions where username = ? AND attraction_uuid = ?";

    public static String SQL_DELETE_FROM_ATTRACTION_REVIEWS = "delete from attraction_reviews where username = ? AND attraction_uuid = ?";
    
    public static String SQL_GET_RESTAURANTS_BY_UUID = "select * from restaurants where restaurant_uuid = ?";

    public static String SQL_GET_RESTAURANTS_REVIEWS_BY_UUID_AND_USERNAME = "select * from restaurant_reviews where restaurant_uuid = ? AND username = ?";
   
    public static String SQL_GET_ATTRACTIONS_REVIEWS_BY_UUID_AND_USERNAME = "select * from attraction_reviews where attraction_uuid = ? and username = ?";

    // public static String SQL_GET_RESTAURANT_IMAGE_BY_UUID = "select * from restaurants where restaurant_uuid = ?";
    
    // public static String SQL_GET_ATTRACTION_IMAGE_BY_UUID = "select * from attractions where attraction_uuid = ?";
}
