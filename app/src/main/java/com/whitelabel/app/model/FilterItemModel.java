package com.whitelabel.app.model;

import com.whitelabel.app.widget.ExpandableRecyclerAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */

public class FilterItemModel extends ExpandableRecyclerAdapter.ListItem implements Serializable {
    private  String id;
    private String label;
    private String searchField;
    private List<FilterItemValueModel>  values;
    private String value;
    private boolean expaned;
    @Override
    public boolean isExpaned() {
        return expaned;
    }

    @Override
    public void setExpaned(boolean expaned) {
        this.expaned = expaned;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public FilterItemModel (){

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FilterItemModel(String label,String value){
        this.label=label;
        this.value=value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public List<FilterItemValueModel> getValues() {
        return values;
    }

    public void setValues(List<FilterItemValueModel> values) {
        this.values = values;
    }
}
