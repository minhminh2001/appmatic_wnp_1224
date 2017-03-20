package com.whitelabel.app.model;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppServiceCustomerCountry extends SVRReturnEntity {
    private ArrayList<CountrySubclass> country;
    private int status;

    public int getStatus() {return status;}

    public void setStatus(int status) {this.status = status;}

    public ArrayList<CountrySubclass> getCountry() {return country;}

    public void setCountry(ArrayList<CountrySubclass> country) {this.country = country;}

  }
