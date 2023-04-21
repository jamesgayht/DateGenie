package dateGenie.server.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dateGenie.server.models.User;
import dateGenie.server.repositories.UserRepo;

@Service
public class UserService {
    
    @Autowired
    private UserRepo userRepo; 

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
