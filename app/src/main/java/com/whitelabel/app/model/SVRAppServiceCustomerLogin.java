package com.whitelabel.app.model;

/**
 * Created by Administrator on 2015/6/18.
 */
public class SVRAppServiceCustomerLogin extends SVRReturnEntity {
    private String sessionKey;
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private int wishListItemCount;
    private int cartItemCount;
    private String headImage;
    private int newsletterSubscribed;
    private int status;
    private int confirmation;

    public int getConfirmation() {return confirmation;}

    public void setConfirmation(int confirmation) {this.confirmation = confirmation;}

    public int getStatus() {return status;}

    public void setStatus(int status) {this.status = status;}



    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getFirstName() {return firstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {this.sessionKey = sessionKey;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNewsletterSubscribed() {return newsletterSubscribed;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWishListItemCount() {
        return wishListItemCount;
    }

    public void setWishListItemCount(int wishListItemCount) {
        this.wishListItemCount = wishListItemCount;
    }

    public int getCartItemCount() {
        return cartItemCount;
    }

    public void setCartItemCount(int cartItemCount) {
        this.cartItemCount = cartItemCount;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public void setNewsletterSubscribed(int newsletterSubscribed) {this.newsletterSubscribed = newsletterSubscribed;}
}
