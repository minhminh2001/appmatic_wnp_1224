package com.whitelabel.app.model;

import android.graphics.Color;

import java.io.Serializable;

/**
 * Created by ray on 2017/3/24.
 */

public class ThemeConfigModel implements Serializable{


    /**
     * keyColor : #FBC801
     * buttonTextColor : #009E82
     * buttonColorTapping : #009E82
     * navBarBackgroundColor : 009E82
     * navBarTextIconColorDefault : 009E82
     * navBarTextIconColorTapping : 009E82
     * sideMenuBackgroudColorDefault : #FBåC801
     * sideMenuBackgroundColorSelected : #FBC801
     * sideMenuTextIconColorDefault : #FBC801
     * sldeMenuTextIconColorSelected : #FBC801
     * sldeMenuTextIconColorTapping : #FBC801
     * searchBoxBackgroundColor : #FBC801
     * searchBoxTextColor : #FBC801
     */

    private String keyColor="#4d9cf2";
    private String buttonTextColor="#6f6f6f";
    private String buttonColorTapping="#4c0052";
    private String navBarBackgroundColor="#4d9cf2";
    private String navBarTextIconColorDefault="#17B1C8";
    private String navBarTextIconColorTapping="#B0CE27";
    private String sideMenuBackgroudColorDefault="#FBC801";
    private String sideMenuBackgroundColorSelected="#FBC801";
    private String sideMenuTextIconColorDefault="#7D66006E";
    private String sideMenuTextIconColorSelected="#D82529";
    private String sideMenuTextIconColorTapping="#66006e";
    private String searchBoxBackgroundColor="#7f66006e";
    private String searchBoxTextColor="#EAFFFFFF";

    public int getKeyColor() {
        try {
            return Color.parseColor(keyColor);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;
    }

    public void setKeyColor(String keyColor) {
        this.keyColor = keyColor;
    }

    public int  getButtonTextColor() {
        try {
            return Color.parseColor(buttonTextColor);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;
    }

    public void setButtonTextColor(String buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
    }

    public int getButtonColorTapping() {
        try {
            return Color.parseColor(buttonColorTapping);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;

    }

    public void setButtonColorTapping(String buttonColorTapping) {
        this.buttonColorTapping = buttonColorTapping;
    }

    public int getNavBarBackgroundColor() {

        try {
            return Color.parseColor(navBarBackgroundColor);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;

    }

    public void setNavBarBackgroundColor(String navBarBackgroundColor) {
        this.navBarBackgroundColor = navBarBackgroundColor;
    }

    public int getNavBarTextIconColorDefault() {
        try {
            return Color.parseColor(navBarTextIconColorDefault);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;
    }
    public void setNavBarTextIconColorDefault(String navBarTextIconColorDefault) {
        this.navBarTextIconColorDefault = navBarTextIconColorDefault;
    }
    public int getNavBarTextIconColorTapping() {
        try {
            return Color.parseColor(navBarTextIconColorTapping);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;
    }

    public void setNavBarTextIconColorTapping(String navBarTextIconColorTapping) {
        this.navBarTextIconColorTapping = navBarTextIconColorTapping;
    }

    public int getSideMenuBackgroudColorDefault() {
        try {
            return Color.parseColor(sideMenuBackgroudColorDefault);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;

    }

    public void setSideMenuBackgroudColorDefault(String sideMenuBackgroudColorDefault) {
        this.sideMenuBackgroudColorDefault = sideMenuBackgroudColorDefault;
    }

    public int getSideMenuBackgroundColorSelected() {

        try {
            return Color.parseColor(sideMenuBackgroundColorSelected);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;

    }

    public void setSideMenuBackgroundColorSelected(String sideMenuBackgroundColorSelected) {
        this.sideMenuBackgroundColorSelected = sideMenuBackgroundColorSelected;
    }

    public int getSideMenuTextIconColorDefault() {
        try {
            return Color.parseColor(sideMenuTextIconColorDefault);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;

    }

    public void setSideMenuTextIconColorDefault(String sideMenuTextIconColorDefault) {
        this.sideMenuTextIconColorDefault = sideMenuTextIconColorDefault;
    }

    public int getSideMenuTextIconColorSelected() {

        try {
            return Color.parseColor(sideMenuTextIconColorSelected);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;

    }

    public void setSideMenuTextIconColorSelected(String sldeMenuTextIconColorSelected) {
        this.sideMenuTextIconColorSelected = sldeMenuTextIconColorSelected;
    }

    public int getSideMenuTextIconColorTapping() {

        try {
            return Color.parseColor(sideMenuTextIconColorTapping);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;

    }

    public void setSldeMenuTextIconColorTapping(String sldeMenuTextIconColorTapping) {
        this.sideMenuTextIconColorTapping = sldeMenuTextIconColorTapping;
    }

    public int getSearchBoxBackgroundColor() {
        try {
            return Color.parseColor(searchBoxBackgroundColor);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;

    }

    public void setSearchBoxBackgroundColor(String searchBoxBackgroundColor) {
        this.searchBoxBackgroundColor = searchBoxBackgroundColor;
    }

    public int getSearchBoxTextColor() {
        try {
            return Color.parseColor(searchBoxTextColor);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;
    }

    public void setSearchBoxTextColor(String searchBoxTextColor) {
        this.searchBoxTextColor = searchBoxTextColor;
    }
}
