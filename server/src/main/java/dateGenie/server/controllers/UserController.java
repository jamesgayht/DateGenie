package dateGenie.server.controllers;

import java.io.StringReader;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dateGenie.server.models.Favourites;
import dateGenie.server.models.User;
import dateGenie.server.services.ChatService;
import dateGenie.server.services.UserService;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Controller
@RequestMapping(path = "/api")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService; 

    @PostMapping(path = "/user/chat")
    public ResponseEntity<String> chatWithAI(@RequestBody String body) {
        JsonReader reader = Json.createReader(new StringReader(body));
        JsonObject json = reader.readObject();

        System.out.println(json);
        String message = json.getString("message"); 
        String chatResponse = chatService.postUserInput(message);

        JsonObject resp = Json.createObjectBuilder()
            .add("resp", chatResponse)
            .build(); 

        // String resp = "{\"resp\": \"%s\"}".formatted(chatResponse);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resp.toString());
    }


    @PutMapping(path = "/user/attraction/delete")
    public ResponseEntity<String> deleteFromAttraction(@RequestBody String body) {
        
        JsonReader reader = Json.createReader(new StringReader(body));
        JsonObject json = reader.readObject();

        String uuid = json.getString("uuid");
        String username = json.getString("username");

        userService.deleteFavouriteAttraction(username, uuid);
        
        String resp = "{\"resp\": \"deleted %s\"}".formatted(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resp.toString());
    }

    @PutMapping(path = "/user/restaurant/delete")
    public ResponseEntity<String> deleteFromRestaurant(@RequestBody String body) {
        
        JsonReader reader = Json.createReader(new StringReader(body));
        JsonObject json = reader.readObject();

        String uuid = json.getString("uuid");
        String username = json.getString("username");

        userService.deleteFavouriteRestaurant(username, uuid);
        
        String resp = "{\"resp\": \"deleted %s\"}".formatted(uuid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resp.toString());
    }
    
    @GetMapping(path = "/user/favourites")
    public ResponseEntity<String> getUserFavourites(@RequestParam String username) {
        Optional<Favourites> opt = userService.getFavouritesByUsername(username);

        if (opt.isEmpty()) {
            String resp = "{\"favourites\": \"[\"restaurants\": [],\"attractions\": []]\"}";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(resp.toString());
        }

        Favourites favourites = opt.get();

        JsonObject results = favourites.toJson();
        System.out.println("favourites results >>> " + results);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(results.toString());

    }

    @GetMapping(path = "/user/login")
    public ResponseEntity<String> userLogin(@RequestParam String username, String password) {

        Optional<User> opt = userService.verifyLoginCredentials(username, password);
        if (!opt.isEmpty()) {
            User user = opt.get();
            System.out.println("user >>>> " + user.toString());
            String resp = "{\"resp\": \"login ok\", \"username\": \"%s\"}".formatted(user.getUsername());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(resp.toString());
        }

        String invalidPassword = "{\"resp\": \"invalid password\"}";
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(invalidPassword.toString());
    }

    @GetMapping(path = "/user/search")
    public ResponseEntity<String> getExisitingUser(@RequestParam String username, String email) {
        System.out.println("username >>> " + username);
        System.out.println("email >>> " + email);

        Optional<User> optUsername = userService.findUserByUsername(username);

        Optional<User> optUserEmail = userService.findUserByEmail(email);

        if (optUsername.isEmpty() && optUserEmail.isEmpty()) {
            String resp = "{\"resp\": \"user does not exist\"}";
            System.out.println("returning >>> " + resp);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(resp.toString());
        }

        String userFound = "{\"resp\": \"user exists\"}";
        System.out.println("returning >>> " + userFound);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userFound.toString());

    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postNewUser(@RequestBody String body) {
        JsonReader reader = Json.createReader(new StringReader(body));
        JsonObject json = reader.readObject();

        User user = User.createUserFromClientPayload(json);
        System.out.println("user >>> " + user);

        try {
            userService.insertNewUser(user);

            String userPostCompletion = "{\"created new user\": \"%s\"}".formatted(user.getUsername());

            String subject = "Welcome to DateGenie!";

            String welcomeMessage = "%s, thank you for choosing DateGenie".formatted(user.getUsername());

            userService.sendEmail(user.getEmail(), subject, welcomeMessage);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userPostCompletion.toString());

        } catch (Exception e) {
            String newUserSaveError = "{\"error\": \"%s\"}".formatted(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                    .body(newUserSaveError);
        }

    }

}