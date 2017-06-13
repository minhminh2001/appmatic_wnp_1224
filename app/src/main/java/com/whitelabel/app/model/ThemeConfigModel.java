package com.whitelabel.app.model;

import android.graphics.Color;

import java.io.Serializable;

/**
 * Created by ray on 2017/3/24.
 */

public class ThemeConfigModel implements Serializable{


    /**
     * theme_color : #FBC801
     * button_text_color : #009E82
     * button_tapping_color : #009E82
     * navigation_bar_background_color : 009E82
     * navigation_bar_text_icon_default_color : 009E82
     * navigation_bar_text_icon_tapping_color : 009E82
     * side_menu_background_default_color : #FBåC801
     * side_menu_background_selected_color : #FBC801
     * side_menu_text_icon_default_color : #FBC801
     * sldeMenuTextIconColorSelected : #FBC801
     * sldeMenuTextIconColorTapping : #FBC801
     * search_box_background_color : #FBC801
     * search_box_text_color : #FBC801
     */

    private String theme_color ="#4d9cf2";
    private String button_text_color ="#6f6f6f";
    private String button_tapping_color ="#4c0052";
    private String navigation_bar_background_color ="#4d9cf2";
    private String navigation_bar_text_icon_default_color ="#17B1C8";
    private String navigation_bar_text_icon_tapping_color ="#B0CE27";
    private String side_menu_background_default_color ="#FBC801";
    private String side_menu_background_selected_color ="#FBC801";
    private String side_menu_text_icon_default_color ="#7D66006E";
    private String side_menu_text_icon_selected_color ="#D82529";
    private String side_menu_text_icon_tapping_color ="#66006e";
    private String search_box_background_color ="#7f66006e";
    private String search_box_text_color ="#EAFFFFFF";

    public int getTheme_color() {
        try {
            return Color.parseColor(theme_color);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;
    }

    public void setTheme_color(String theme_color) {
        this.theme_color = theme_color;
    }

    public int getButton_text_color() {
        try {
            return Color.parseColor(button_text_color);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;
    }

    public void setButton_text_color(String button_text_color) {
        this.button_text_color = button_text_color;
    }

    public int getButton_tapping_color() {
        try {
            return Color.parseColor(button_tapping_color);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;
    }

    public void setButton_tapping_color(String button_tapping_color) {
        this.button_tapping_color = button_tapping_color;
    }

    public int getNavigation_bar_background_color() {

        try {
            return Color.parseColor(navigation_bar_background_color);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;

    }

    public void setNavigation_bar_background_color(String navigation_bar_background_color) {
        this.navigation_bar_background_color = navigation_bar_background_color;
    }

    public int getNavigation_bar_text_icon_default_color() {
        try {
            return Color.parseColor(navigation_bar_text_icon_default_color);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;
    }
    public void setNavigation_bar_text_icon_default_color(String navigation_bar_text_icon_default_color) {
        this.navigation_bar_text_icon_default_color = navigation_bar_text_icon_default_color;
    }
    public int getNavigation_bar_text_icon_tapping_color() {
        try {
            return Color.parseColor(navigation_bar_text_icon_tapping_color);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;
    }

    public void setNavigation_bar_text_icon_tapping_color(String navigation_bar_text_icon_tapping_color) {
        this.navigation_bar_text_icon_tapping_color = navigation_bar_text_icon_tapping_color;
    }

    public int getSide_menu_background_default_color() {
        try {
            return Color.parseColor(side_menu_background_default_color);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;

    }

    public void setSide_menu_background_default_color(String side_menu_background_default_color) {
        this.side_menu_background_default_color = side_menu_background_default_color;
    }

    public int getSide_menu_background_selected_color() {

        try {
            return Color.parseColor(side_menu_background_selected_color);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;

    }

    public void setSide_menu_background_selected_color(String side_menu_background_selected_color) {
        this.side_menu_background_selected_color = side_menu_background_selected_color;
    }

    public int getSide_menu_text_icon_default_color() {
        try {
            return Color.parseColor(side_menu_text_icon_default_color);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;

    }

    public void setSide_menu_text_icon_default_color(String side_menu_text_icon_default_color) {
        this.side_menu_text_icon_default_color = side_menu_text_icon_default_color;
    }

    public int getSide_menu_text_icon_selected_color() {

        try {
            return Color.parseColor(side_menu_text_icon_selected_color);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;

    }

    public void setSide_menu_text_icon_selected_color(String sldeMenuTextIconColorSelected) {
        this.side_menu_text_icon_selected_color = sldeMenuTextIconColorSelected;
    }

    public int getSide_menu_text_icon_tapping_color() {

        try {
            return Color.parseColor(side_menu_text_icon_tapping_color);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;

    }

    public void setSldeMenuTextIconColorTapping(String sldeMenuTextIconColorTapping) {
        this.side_menu_text_icon_tapping_color = sldeMenuTextIconColorTapping;
    }

    public int getSearch_box_background_color() {
        try {
            return Color.parseColor(search_box_background_color);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;

    }

    public void setSearch_box_background_color(String search_box_background_color) {
        this.search_box_background_color = search_box_background_color;
    }

    public int getSearch_box_text_color() {
        try {
            return Color.parseColor(search_box_text_color);
        }catch (Exception ex){
            ex.getMessage();
        }
        return 0;
    }

    public void setSearch_box_text_color(String search_box_text_color) {
        this.search_box_text_color = search_box_text_color;
    }
}
