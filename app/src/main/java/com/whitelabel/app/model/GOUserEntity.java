package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/10.
 */
public class GOUserEntity extends GOEntity {
    private String sessionKey = "";
    private String id = "";
    private String email = "";
    private String firstName = "";
    private String lastName = "";
    private long wishListItemCount;
    private long orderCount;
    private long cartItemCount;
    private String headImage = "";
    private int newsletterSubscribed;
    private long status;
    private boolean isEmailLogin;
    private boolean closedSound;
    private String loginType = "";

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public boolean isClosedSound() {
        return closedSound;
    }

    public void setClosedSound(boolean closedSound) {
        this.closedSound = closedSound;
    }

    public long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(long orderCount) {
        this.orderCount = orderCount;
    }

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

    public boolean isEmailLogin() {
        return isEmailLogin;
    }

    public void setIsEmailLogin(boolean isEmailLogin) {
        this.isEmailLogin = isEmailLogin;
    }
}
