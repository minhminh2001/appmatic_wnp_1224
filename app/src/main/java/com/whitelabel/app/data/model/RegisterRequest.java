package com.whitelabel.app.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ray on 2018/3/14.
 */

public class RegisterRequest {
    @SerializedName("firstname")
    private String firstName;

    @SerializedName("lastname")
    private String lastName;

    private String email;

    @SerializedName("customer_telephone")
    private String phone;


    private String password;

    @SerializedName("is_subscribed")
    private String subscribed;

    @SerializedName("device_token")
    private String  deviceToken;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(String subscribed) {
        this.subscribed = subscribed;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
