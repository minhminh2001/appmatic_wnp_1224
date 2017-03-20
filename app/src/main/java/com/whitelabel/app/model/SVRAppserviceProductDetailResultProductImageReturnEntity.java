package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by imaginato on 2015/7/27.
 */
public class SVRAppserviceProductDetailResultProductImageReturnEntity implements Serializable {
    private String image;
    private String smallImage;
    private String thumbnail;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
