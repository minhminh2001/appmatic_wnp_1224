package com.whitelabel.app.model;

/**
 * Created by ray on 2015/9/2.
 */
public class MarketingLayersEntity extends SVRReturnEntity {
    private String title;
    private String image;
    private String duration;
    private int showItAfter;
    private int repeats;
    private String description;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getShowItAfter() {
        return showItAfter;
    }

    public void setShowItAfter(int showItAfter) {
        this.showItAfter = showItAfter;
    }

    public int getRepeats() {
        return repeats;
    }

    public void setRepeats(int repeats) {
        this.repeats = repeats;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
