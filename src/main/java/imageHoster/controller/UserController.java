package imageHoster.controller;

import imageHoster.model.User;
import imageHoster.model.UserProfile;
import imageHoster.service.ImageService;
import imageHoster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    ImageService imageService;

    @RequestMapping("users/registration")
    public String registration(Model model) {
        User user = new User();
        UserProfile profile = new UserProfile();
        user.setProfile(profile);
        model.addAttribute("User", user);
        return "users/registration";
    }

    @RequestMapping(value = "users/registration", method = RequestMethod.POST)
    public String registerUser(User user, Model model) {
        //Complete this method
        //Call the business logic which currently does not store the details of the user in the database
        //After registration, again redirect to the registration page
        if (isValidPassword(user.getPassword())) {
            userService.registerUser(user);
            return "users/login";
        } else {
            String error = "Password must contain atleast 1 alphabet, 1 number & 1 special character";
            model.addAttribute("User", user);
            model.addAttribute("passwordTypeError", error);
            return "users/registration";
        }
    }

    @RequestMapping("users/login")
    public String login() {
        return "users/login";
    }

    @RequestMapping(value = "users/login", method = RequestMethod.POST)
    public String loginUser(User user, HttpSession session) {
        User existingUser = userService.login(user);
        if (existingUser != null) {
            session.setAttribute("loggedUser", existingUser);
            return "redirect:/images";
        } else {
            return "users/login";
        }
    }

    //This imageHoster.controller method is called when the request pattern is of type 'users/logout' and also the incoming request is of POST type
    //The method receives the Http Session and the Model type object
    //session is invalidated
    //All the images are fetched from the database and added to the model with 'images' as the key
    //'index.html' file is returned showing the landing page of the application and displaying all the images in the application
    @RequestMapping(value = "users/logout", method = RequestMethod.POST)
    public String logout(Model model, HttpSession session) {
        session.invalidate();
        model.addAttribute("images", imageService.getAllImages());
        return "index";
    }

    private boolean isValidPassword(String password) {
        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-zA-Z])"
                + "(?=.*[@#$%^&-+=()])"
                + ".{3,40}$";
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (password == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);

        // Return if the password
        // matched the ReGex
        return m.matches();
    }

}
