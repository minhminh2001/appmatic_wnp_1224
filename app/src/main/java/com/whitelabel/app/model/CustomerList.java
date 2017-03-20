package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/10.
 */
public class CustomerList extends SVRReturnEntity {
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String birthday;
    private String headImage;
    private String city;
    private String postcode;
    private String telephone_code;
    private String telephone;
    private String income;
    private String default_billing;
    private String country_id;
    private String region;
    private String region_id;

    public String getRegion() {return region;}

    public void setRegion(String region) {this.region = region;}

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getCountry_id() {return country_id;}

    public void setCountry_id(String country_id) {this.country_id = country_id;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {this.email = email;}

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone_code() {
        return telephone_code;
    }

    public void setTelephone_code(String telephone_code) {
        this.telephone_code = telephone_code;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDefault_billing() {
        return default_billing;
    }

    public void setDefault_billing(String default_billing) {this.default_billing = default_billing;}
}
