package imageHoster.service;

import imageHoster.model.User;
import imageHoster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void registerUser(User newUser) {
        userRepository.registerUser(newUser);
    }

    public User login(User user) {
        User existingUser = userRepository.checkUser(user.getUsername(), user.getPassword());

        if(existingUser!=null){
            return existingUser;
        } else {
            return null;
        }
    }


}
