package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/10.
 */
public class SVRAppServiceCustomerSubscribed extends SVRReturnEntity {
    private int status;
    private int newsletterSubscribed;

    public int getStatus() {return status;}

    public void setStatus(int status) {this.status = status;}

    public int getNewsletterSubscribed() {return newsletterSubscribed;}

    public void setNewsletterSubscribed(int newsletterSubscribed) {this.newsletterSubscribed = newsletterSubscribed;}
}
