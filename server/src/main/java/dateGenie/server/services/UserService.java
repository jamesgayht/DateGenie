package dateGenie.server.services;

import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import dateGenie.server.models.Attraction;
import dateGenie.server.models.Favourites;
import dateGenie.server.models.Restaurant;
import dateGenie.server.models.User;
import dateGenie.server.repositories.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JavaMailSender mailSender;

    public void deleteFavouriteRestaurant(String username, String uuid) {
        userRepo.deleteRestaurantReviewsByUsernameAndUuid(username, uuid);
        userRepo.deleteRestaurantByUsernameAndUuid(username, uuid);
    }
    
    public void deleteFavouriteAttraction(String username, String uuid) {
        userRepo.deleteAttractionReviewsByUsernameAndUuid(username, uuid);
        userRepo.deleteAttractionByUsernameAndUuid(username, uuid);
    }

    public Optional<Favourites> getFavouritesByUsername(String username) {

        List<Restaurant> restaurants = new LinkedList<>();
        List<Attraction> attractions = new LinkedList<>();
        Favourites favourites = new Favourites();

        Optional<List<Restaurant>> optRes = userRepo.findRestaurantsByUsername(username);

        Optional<List<Attraction>> optAttr = userRepo.findAttractionsByUsername(username);

        if (optRes.isEmpty())
            favourites.setRestaurants(restaurants);
        if (optAttr.isEmpty())
            favourites.setAttractions(attractions);

        favourites.setRestaurants(optRes.get());
        favourites.setAttractions(optAttr.get());

        System.out.printf("attr in userservice >>> %s \n", favourites.getAttractions().toString());
        System.out.printf("res in userservice >>> %s \n", favourites.getRestaurants().toString());

        return Optional.of(favourites);
    }

    public Optional<User> verifyLoginCredentials(String username, String password) {
        System.out.println("username >>> " + username);
        System.out.println("password >>> " + password);

        String hash = "";
        try {
            // Message digest = md5, sha1, sha512
            // Get an instance of MD5
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // Calculate our hash
            // Update our message digest
            md5.update(password.getBytes());
            // Get the MD5 digest
            byte[] h = md5.digest();
            // Stringify the MD5 digest
            hash = HexFormat.of().formatHex(h);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return userRepo.findUserByUsernameAndPassword(username, hash);
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jamesgayht@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public boolean insertNewUser(User user) {
        return userRepo.insertNewUser(user);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepo.findUserByUsername(username);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

}
