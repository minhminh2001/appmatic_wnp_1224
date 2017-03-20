package com.whitelabel.app.model;

/**
 * Created by Administrator on 2015/6/18.
 */
public class SVRAppServiceCustomerLoginReturnEntity extends SVRReturnEntity {
    private String sessionKey;
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private long wishListItemCount;
    private long cartItemCount;
    private String headImage;
    private int newsletterSubscribed;
    private long status;
    private int confirmation;
    private boolean emailLogin;
    private long orderCount;
    private String loginType;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public long getOrderCount() {return orderCount;}

    public void setOrderCount(long orderCount) {this.orderCount = orderCount;}

    public int getConfirmation() {return confirmation;}

    public void setConfirmation(int confirmation) {this.confirmation = confirmation;}

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getWishListItemCount() {
        return wishListItemCount;
    }

    public void setWishListItemCount(long wishListItemCount) {
        this.wishListItemCount = wishListItemCount;
    }

    public long getCartItemCount() {
        return cartItemCount;
    }

    public void setCartItemCount(long cartItemCount) {
        this.cartItemCount = cartItemCount;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public int getNewsletterSubscribed() {
        return newsletterSubscribed;
    }

    public void setNewsletterSubscribed(int newsletterSubscribed) {
        this.newsletterSubscribed = newsletterSubscribed;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public boolean isEmailLogin() {return emailLogin;}

    public void setEmailLogin(boolean emailLogin) {this.emailLogin = emailLogin;}
}
