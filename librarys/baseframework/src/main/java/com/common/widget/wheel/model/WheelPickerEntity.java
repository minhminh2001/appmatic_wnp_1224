package com.common.widget.wheel.model;

/**
 * Created by imaginato on 2015/7/1.
 */
public class WheelPickerEntity {
    private String display;
    private String value;
    private int index;
    public WheelPickerEntity(String display, String value){
        this.display=display;
        this.value=value;
    }


    public WheelPickerEntity(){

    }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
