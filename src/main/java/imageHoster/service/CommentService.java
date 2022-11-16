package imageHoster.service;

import imageHoster.model.Comment;
import imageHoster.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    public void postComment(Comment comment) {
        commentRepository.postComment(comment);
    }
}
