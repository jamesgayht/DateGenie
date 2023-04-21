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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dateGenie.server.models.User;
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

        System.out.println("json for new user >>> " + json.toString());

        User user = User.createUserFromClientPayload(json);
        System.out.println("user >>> " + user);

        try {
            userService.insertNewUser(user);
        } catch (Exception e) {
            String newUserSaveError = "{\"error\": \"%s\"}".formatted(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                    .body(newUserSaveError);
        }

        String userPostCompletion = "{\"created new user\": \"%s\"}".formatted(user.getUsername());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userPostCompletion.toString());
    }

}