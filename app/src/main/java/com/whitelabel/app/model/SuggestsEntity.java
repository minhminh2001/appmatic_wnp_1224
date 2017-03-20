package com.whitelabel.app.model;

/**
 * Created by jay on 2017/2/8.
 */
public class SuggestsEntity {
    /**
     *  "suggests": [
     {
     "title": "Laptop Battery Acer Aspire AS10D31 / AS10D3E / AS10D41 / AS10D51 / AS10D5E / AS10D7E SERIES [ FREE SHIPPING ]",
     "row_type": "product",
     "id": 241169,
     "num_of_results": 1
     },
     */

    private String title;
    private String row_type;
    private String id;
    private String num_of_results;

    public String getNum_of_results() {
        return num_of_results;
    }

    public void setNum_of_results(String num_of_results) {
        this.num_of_results = num_of_results;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRow_type() {
        return row_type;
    }

    public void setRow_type(String row_type) {
        this.row_type = row_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
