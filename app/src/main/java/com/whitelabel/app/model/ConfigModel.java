package com.whitelabel.app.model;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.whitelabel.app.R;
import com.whitelabel.app.application.GemfiveApplication;

import java.io.Serializable;

/**
 * Created by ray on 2017/3/24.
 */

public class ConfigModel  implements Serializable{
    private  int primaryColor;
    private int  secondaryColor;
    private int  buttonPressColor;

    public ConfigModel(){
        primaryColor= Color.parseColor("#FBC801");
        buttonPressColor=Color.parseColor("#17B1C8");
        secondaryColor= ContextCompat.getColor(GemfiveApplication.getInstance(),R.color.blue4d9cf2);
    }
    public int getPrimaryColor() {
        return primaryColor;
    }
    public int getButtonPressColor() {
        return buttonPressColor;
    }
    public void setButtonPressColor(int buttonPressColor) {
        this.buttonPressColor = buttonPressColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }
    public int getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }
}
