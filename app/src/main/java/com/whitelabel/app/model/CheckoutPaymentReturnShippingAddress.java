package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/14.
 */
public class CheckoutPaymentReturnShippingAddress implements Serializable{

    private String firstname;
    private String lastname;
    private String street;
    private String city;
    private String region;
    private String postcode;
    private String country;
    private String fax;
    private String telephone;

    public CheckoutPaymentReturnShippingAddress() {
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public CheckoutPaymentReturnShippingAddress(String firstname, String lastname, String street, String city, String region, String postcode, String country, String telephone) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.street = street;
        this.city = city;
        this.region = region;
        this.postcode = postcode;
        this.country = country;
        this.telephone = telephone;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
