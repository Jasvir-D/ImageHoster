package imageHoster.repository;

import imageHoster.model.Comment;
import imageHoster.model.Image;
import imageHoster.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class ImageRepository {

    //Get an instance of EntityManagerFactory from persistence unit with name as 'imageHoster'
    @PersistenceUnit(unitName = "imagehost")
    private EntityManagerFactory emf;

    //The method receives the Image object to be persisted in the database
    //Creates an instance of EntityManager
    //Starts a transaction
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction
    public Image uploadImage(Image newImage) {
        //Complete the method
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.persist(newImage);
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
        }

        return newImage;
    }

    //The method creates an instance of EntityManager
    //Executes JPQL query to fetch all the images from the database
    //Returns the list of all the images fetched from the database
    public List<Image> getAllImages() {
        //Complete the code
        EntityManager entityManager = emf.createEntityManager();
        TypedQuery<Image> query = entityManager.createQuery("select i from Image i", Image.class);
        List<Image> resultList = query.getResultList();
        return resultList;
    }
    //The method creates an instance of EntityManager
    //Executes JPQL query to fetch the image from the database with corresponding id
    //Returns the image fetched from the database
    public Image getImage(Integer imageId) {
        EntityManager em = emf.createEntityManager();
        return em.find(Image.class, imageId);
    }

    //The method receives the Image object to be updated in the database
    //Creates an instance of EntityManager
    //Starts a transaction
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction
    public void updateImage(Image updatedImage) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.merge(updatedImage);
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
        }
    }


    //The method receives the Image id of the image to be deleted in the database
    //Creates an instance of EntityManager
    //Starts a transaction
    //Get the image with corresponding image id from the database
    //This changes the state of the image model from detached state to persistent state, which is very essential to use the remove() method
    //If you use remove() method on the object which is not in persistent state, an exception is thrown
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction
    public void deleteImage(Integer imageId) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            Image image = entityManager.find(Image.class, imageId);
            entityManager.remove(image);
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
        }
    }



}
