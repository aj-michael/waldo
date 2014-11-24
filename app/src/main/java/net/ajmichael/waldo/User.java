package net.ajmichael.waldo;

/**
 * Created by adam on 11/23/14.
 */
public class User {

    private String email;
    private String img_url;

    private User() {};

    public User(String email, String img_url) {
        this.email = email;
        this.img_url = img_url;
    }

    public String getEmail() {
        return this.email;
    }

    public String getImg_url() {
        return this.img_url;
    }
}
