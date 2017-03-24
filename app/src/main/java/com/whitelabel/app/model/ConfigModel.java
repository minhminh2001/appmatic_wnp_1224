package com.whitelabel.app.model;

import com.whitelabel.app.R;
import com.whitelabel.app.application.GemfiveApplication;

import java.io.Serializable;

/**
 * Created by ray on 2017/3/24.
 */

public class ConfigModel  implements Serializable{
    private  int primaryColor;
    private int  secondaryColor;
    public ConfigModel(){
        primaryColor= GemfiveApplication.getInstance().getResources().getColor(R.color.order_status_hold);
        secondaryColor=GemfiveApplication.getInstance().getResources().getColor(R.color.colorPrimary);
    }
    public int getPrimaryColor() {
        return primaryColor;
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
