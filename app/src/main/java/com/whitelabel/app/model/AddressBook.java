package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by imaginato on 2015/6/29.
 */
public class AddressBook  implements Serializable {
    private String addressId;
    private String firstName;
    private String lastName;
    private String company;
    private String telephone;
    private List<String> street;
    private String city;
    private String regionId;
    private String region;
    private String postcode;
    private String countryId;
    private String country;
    private String primaryBilling;
    private String primaryShipping;
    public List<String> getStreet() {
        return street;
    }


    public String getAddressId() {
        return addressId;
    }

    public void setStreet(List<String> street) {
        this.street = street;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
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

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPrimaryBilling() {
        return primaryBilling;
    }

    public void setPrimaryBilling(String primaryBilling) {
        this.primaryBilling = primaryBilling;
    }

    public String getPrimaryShipping() {
        return primaryShipping;
    }

    public void setPrimaryShipping(String primaryShipping) {
        this.primaryShipping = primaryShipping;
    }


}
