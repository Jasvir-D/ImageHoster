package imageHoster.controller;

import imageHoster.model.Comment;
import imageHoster.model.Image;
import imageHoster.model.User;
import imageHoster.service.CommentService;
import imageHoster.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;

@Controller
public class CommentController {
    @Autowired
    ImageService imageService;

    @Autowired
    CommentService commentService;

    @RequestMapping(value = "/image/{imageId}/{title}/comments", method = RequestMethod.POST)
    public String postComment(@PathVariable(name = "imageId") Integer imageId,
                              @PathVariable(name = "title") String title,
                              @RequestParam("comment") String comment,
                              HttpSession session) throws IOException {
        Comment imageComment = new Comment();
        imageComment.setText(comment);
        imageComment.setCreatedDate(LocalDate.now());
        Image image = imageService.getImage(imageId);
        imageComment.setImage(image);
        imageComment.setUser((User) session.getAttribute("loggedUser"));
        commentService.postComment(imageComment);
        return "redirect:/images/" + imageId + "/" + title;
    }
}
