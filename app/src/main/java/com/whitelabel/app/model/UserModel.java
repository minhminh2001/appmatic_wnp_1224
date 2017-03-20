package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by ray on 2017/3/20.
 */

public class UserModel  implements Serializable{
    private String firstName;
    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
