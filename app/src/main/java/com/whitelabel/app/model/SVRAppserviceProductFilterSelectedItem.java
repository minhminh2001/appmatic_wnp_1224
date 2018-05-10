package com.whitelabel.app.model;

import java.util.ArrayList;
import java.util.List;

public class SVRAppserviceProductFilterSelectedItem {
    private String categoryOption = "";
    private List<String> brandOptions = new ArrayList<>();
    private List<String> flavorOptions = new ArrayList<>();
    private List<String> lifeStageOptions = new ArrayList<>();

    public String getCategoryOption() {
        return categoryOption;
    }

    public void clearCategoryOption(){
        categoryOption = "";
    }

    public void setCategoryOption(String categoryOption) {
        this.categoryOption = categoryOption;
    }

    public List<String> getBrandOptions() {
        return brandOptions;
    }

    public void clearBrandOptions(){
        brandOptions.clear();
    }

    public void setBrandOptions(List<String> brandOptions) {
        this.brandOptions.clear();
        this.brandOptions.addAll(brandOptions);
    }

    public List<String> getFlavorOptions() {
        return flavorOptions;
    }

    public void clearFlavorOptions(){
        flavorOptions.clear();
    }

    public void setFlavorOptions(List<String> flavorOptions) {
        this.flavorOptions.clear();
        this.flavorOptions.addAll(flavorOptions);
    }

    public List<String> getLifeStageOptions() {
        return lifeStageOptions;
    }

    public void clearLiftStageOptions(){
        lifeStageOptions.clear();
    }

    public void setLifeStageOptions(List<String> lifeStageOptions) {
        this.lifeStageOptions.clear();
        this.lifeStageOptions.addAll(lifeStageOptions);
    }

    public void clearAll(){
        categoryOption = "";
        brandOptions.clear();
        flavorOptions.clear();
        lifeStageOptions.clear();
    }
}
