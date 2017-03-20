package com.whitelabel.app.model;

import java.util.List;

/**
 * Created by Administrator on 2017/2/28.
 */

public class BrandStoreModel extends  ResponseModel  {
    private String name;
    private String description;
    private List<BannerModel> bannerImages;
    private SVRAppserviceProductSearchReturnEntity allProducts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BannerModel> getBannerImages() {
        return bannerImages;
    }

    public void setBannerImages(List<BannerModel> bannerImages) {
        this.bannerImages = bannerImages;
    }

    public SVRAppserviceProductSearchReturnEntity getAllProducts() {
        return allProducts;
    }

    public void setAllProducts(SVRAppserviceProductSearchReturnEntity allProducts) {
        this.allProducts = allProducts;
    }
}
