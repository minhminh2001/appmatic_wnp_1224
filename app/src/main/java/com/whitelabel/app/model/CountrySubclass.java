package com.whitelabel.app.model;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/8.
 */
public class CountrySubclass extends SVRReturnEntity {
    private String country_id;
    private String name;
    private ArrayList<CountryRegions>  regions;

    public CountrySubclass(){

    }
    public CountrySubclass(String id,String name){
      country_id=id;
        this.name=name;
    }
    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public ArrayList<CountryRegions> getRegions() {return regions;}

    public void setRegions(ArrayList<CountryRegions> regions) {this.regions = regions;}
}
