package imageHoster.controller;

import imageHoster.model.Image;
import imageHoster.model.Tag;
import imageHoster.model.User;
import imageHoster.service.ImageService;
import imageHoster.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
public class ImageController {
    @Autowired
    TagService tagService;
    @Autowired
    ImageService imageService;

    @RequestMapping("/images")
    public String getAllImagePosts(Model model) {

        model.addAttribute("images", imageService.getAllImages());
        return "images";
    }

    @RequestMapping("/images//{imageId}/{title}")
    public String showImage(Model model, @PathVariable String title, @PathVariable Integer imageId) {
        Date date = new Date();
        Image image = imageService.getImage(imageId);

        model.addAttribute("image", image);
        model.addAttribute("tags", image.getTags());
        model.addAttribute("comments", image.getComments());

        return "images/image";
    }

    //This imageHoster.controller method is called when the request pattern is of type 'images/upload'
    //The method returns 'images/upload.html' file
    @RequestMapping("/images/upload")
    public String newImage() {
        return "images/upload";
    }

    //This imageHoster.controller method is called when the request pattern is of type 'images/upload' and also the incoming request is of POST type
    //The method receives all the details of the image to be stored in the database, but currently we are not using database so the business logic simply retuns null and does not store anything in the database
    //After you get the imageFile, convert it to Base64 format and store it as a string
    //After storing the image, this method directs to the logged in user homepage displaying all the images

    //Get the 'tags' request parameter using @RequestParam annotation which is just a string of all the tags
    //Store all the tags in the database and make a list of all the tags using the findOrCreateTags() method
    //set the tags attribute of the image as a list of all the tags returned by the findOrCreateTags() method
    @RequestMapping(value = "/images/upload", method = RequestMethod.POST)
    public String createImage(@RequestParam("file") MultipartFile file, @RequestParam("tags") String tags, Image newImage, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("loggedUser");
        newImage.setUser(user);
        String uploadedImageData = convertUploadedFileToBase64(file);
        newImage.setImageFile(uploadedImageData);
        List<Tag> imageTags = findOrCreateTags(tags);
        newImage.setTags(imageTags);
        newImage.setDate(new Date());
        imageService.uploadImage(newImage);
        return "redirect:/images";
    }

    //This imageHoster.controller method is called when the request pattern is of type 'editImage'
    //This method fetches the image with the corresponding id from the database and adds it to the model with the key as 'image'
    //The method then returns 'images/edit.html' file wherein you fill all the updated details of the image
    @RequestMapping(value = "/editImage", method = RequestMethod.GET)
    public String editImage(@RequestParam("imageId") Integer imageId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        Image image = imageService.getImage(imageId);
        if (image.getUser().getId() != user.getId()) {
            String error = "Only the owner of the image can edit the image";
            model.addAttribute("image", image);
            model.addAttribute("tags", image.getTags());
            model.addAttribute("editError", error);
            return "images/image";
        } else {
            String tags = convertTagsToString(image.getTags());
            model.addAttribute("image", image);
            model.addAttribute("tags", tags);
            return "images/edit";
        }
    }

    //This imageHoster.controller method is called when the request pattern is of type 'images/edit' and also the incoming request is of PUT type
    //The method receives the imageFile, imageId, updated image, along with the Http Session
    //The method adds the new imageFile to the updated image if user updates the imageFile and adds the previous imageFile to the new updated image if user does not choose to update the imageFile
    //Set an id of the new updated image
    //Set the user using Http Session
    //Set the date on which the image is posted
    //Call the updateImage() method in the business logic to update the image
    //Direct to the same page showing the details of that particular updated image

    //The method also receives tags parameter which is a string of all the tags separated by a comma using the annotation @RequestParam
    //The method converts the string to a list of all the tags using findOrCreateTags() method and sets the tags attribute of an image as a list of all the tags
    @RequestMapping(value = "/editImage", method = RequestMethod.PUT)
    public String editImageSubmit(@RequestParam("file") MultipartFile file, @RequestParam("imageId") Integer imageId, @RequestParam("tags") String tags, Image updatedImage, HttpSession session) throws IOException {
        Image image = imageService.getImage(imageId);
        String updatedImageData = convertUploadedFileToBase64(file);
        List<Tag> imageTags = findOrCreateTags(tags);

        if (updatedImageData.isEmpty())
            updatedImage.setImageFile(image.getImageFile());
        else {
            updatedImage.setImageFile(updatedImageData);
        }
        updatedImage.setId(imageId);
        User user = (User) session.getAttribute("loggedUser");
        updatedImage.setTags(imageTags);
        updatedImage.setUser(user);
        imageService.updateImage(updatedImage);

        return "redirect:/images";
    }

    //This imageHoster.controller method is called when the request pattern is of type 'deleteImage' and also the incoming request is of DELETE type
    //The method calls the deleteImage() method in the business logic passing the id of the image to be deleted
    //Looks for a imageHoster.controller method with request mapping of type '/images'
    @RequestMapping(value = "/deleteImage", method = RequestMethod.DELETE)
    public String deleteImageSubmit(@RequestParam(name = "imageId") Integer imageId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        Image image = imageService.getImage(imageId);
        if (image.getUser().getId() != user.getId()) {
            String error = "Only the owner of the image can delete the image";
            model.addAttribute("image", image);
            model.addAttribute("tags", image.getTags());
            model.addAttribute("deleteError", error);
            return "images/image";
        } else {
            imageService.deleteImage(imageId);
            return "redirect:/images";
        }

    }


    //This method converts the image to Base64 format
    private String convertUploadedFileToBase64(MultipartFile file) throws IOException {
        return Base64.getEncoder().encodeToString(file.getBytes());
    }

    //findOrCreateTags() method has been implemented, which returns the list of tags after converting the ‘tags’ string to a list of all the tags and also stores the tags in the database if they do not exist in the database. Observe the method and complete the code where required for this method.
    //Try to get the tag from the database using getTagByName() method. If tag is returned, you need not to store that tag in the database, and if null is returned, you need to first store that tag in the database and then the tag is added to a list
    //After adding all tags to a list, the list is returned
    private List<Tag> findOrCreateTags(String tagNames) {
        StringTokenizer st = new StringTokenizer(tagNames, ",");
        List<Tag> tags = new ArrayList<Tag>();

        while (st.hasMoreTokens()) {
            String tagName = st.nextToken().trim();
            Tag tag = tagService.getTagByName(tagName);

            if (tag == null) {
                Tag newTag = new Tag(tagName);
                tag = tagService.createTag(newTag);
            }
            tags.add(tag);
        }
        return tags;
    }

    private String convertTagsToString(List<Tag> tags) {
        StringBuilder tagString = new StringBuilder();

        for (int i = 0; i <= tags.size() - 2; i++) {
            tagString.append(tags.get(i).getName()).append(",");
        }
        if (tags.size() > 0) {
            Tag lastTag = tags.get(tags.size() - 1);
            tagString.append(lastTag.getName());
        }

        return tagString.toString();
    }
}
