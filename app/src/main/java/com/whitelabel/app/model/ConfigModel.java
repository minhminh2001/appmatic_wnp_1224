package com.whitelabel.app.model;

import android.graphics.Color;

import com.whitelabel.app.R;
import com.whitelabel.app.application.GemfiveApplication;

import java.io.Serializable;

/**
 * Created by ray on 2017/3/24.
 */

public class ConfigModel  implements Serializable{
    private  int primaryColor;
    private int  secondaryColor;
    private int  buttonClickColor;
    public ConfigModel(){
        primaryColor= Color.parseColor("#FBC801");
        buttonClickColor=Color.parseColor("#17B1C8");
        secondaryColor=GemfiveApplication.getInstance().getResources().getColor(R.color.colorPrimary);
    }
    public int getPrimaryColor() {
        return primaryColor;
    }


    public int getButtonClickColor() {
        return buttonClickColor;
    }

    public void setButtonClickColor(int buttonClickColor) {
        this.buttonClickColor = buttonClickColor;
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
