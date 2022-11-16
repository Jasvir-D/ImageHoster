package imageHoster.model;

import javax.persistence.*;
import java.time.LocalDate;

@SuppressWarnings("ALL")
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    // Text is a Postgres specific column type that allows you to save
    // text based data that will be longer than 256 characters
    // this is a base64 encoded version of the image
    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "created_date")
    private LocalDate createdDate;

    //The 'comments' table is mapped to 'users' table with Many:One mapping
    //One comments can have only one user but one user can have multiple comments
    //FetchType is EAGER
    @ManyToOne(fetch = FetchType.EAGER)
    //this annotation indicates that the name of the column in 'comments' table is referring to the primary key in 'user' table will be 'user_id'
    @JoinColumn(name = "user_id")
    private User user;

    //The 'comments' table is mapped to 'images' table with Many:One mapping
    //One comments can have only one images but one image can have multiple comments
    //FetchType is EAGER
    @ManyToOne(fetch = FetchType.EAGER)
    //This annotation indicates that the name of the column in 'comments' table is referring to the primary key in 'images' table will be 'image_id'
    @JoinColumn(name = "image_id")
    private Image image;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
