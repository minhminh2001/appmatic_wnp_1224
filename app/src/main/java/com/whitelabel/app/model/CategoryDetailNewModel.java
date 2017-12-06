package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ray on 2017/3/31.
 */

public class CategoryDetailNewModel implements Serializable {
    /**
     * category_img : catalog/category/cat.jpg
     * image_width : 750
     * image_height : 348
     * carousels : [{"title":"Cat Stuff We Love","items":[{"productId":"126","name":"Wholesome Essentials, Senior Farm-Raised Chicken, Brown Rice & Sweet Potato Recipe","brand":"Nutro","brandId":"191","inStock":1,"maxSaleQty":0,"price":"368.00","final_price":"368.00","smallImage":"catalog/product/n/e/new_senior_chicken_15lb.jpg","isLike":0,"itemId":0},{"productId":"140","name":"Wholesome Essentials, Small Breed Adult Farm-Raised Chicken, Whole Brown Rice & Sweet Potato Recipe","brand":"Nutro","brandId":"191","inStock":1,"maxSaleQty":0,"price":"128.00","final_price":"128.00","smallImage":"catalog/product/_/2/_20150708165959_17014_23.jpg","isLike":0,"itemId":0},{"productId":"223","name":"Wholesome Essentials, Healthy Weight Adult Small Breed Farm-Raised Chicken, Brown Rice & Sweet Potato Recipe","brand":"Nutro","brandId":"191","inStock":1,"maxSaleQty":0,"price":"145.00","final_price":"145.00","smallImage":"catalog/product/n/e/new_new_small_breed_weight.png","isLike":0,"itemId":0},{"productId":"243","name":"Four-Star Nutritionals- Salmon A La Veg for Dogs /P","brand":"Fromm","brandId":"84","inStock":1,"maxSaleQty":0,"price":"188.00","final_price":"188.00","smallImage":"catalog/product/_/2/_20120514165045_6978_23.jpg","isLike":0,"itemId":0},{"productId":"263","name":"Wholesome Essentials, Adult Venison, Brown Rice & Oatmeal Recipe","brand":"Nutro","brandId":"191","inStock":1,"maxSaleQty":0,"price":"178.00","final_price":"178.00","smallImage":"catalog/product/n/e/new_venison_wholesome.png","isLike":0,"itemId":0},{"productId":"718","name":"Moist Pouch - Chicken, Brown Rice & Vegetables","brand":"Burns","brandId":"121","inStock":1,"maxSaleQty":0,"price":"228.00","final_price":"228.00","smallImage":"catalog/product/_/2/_20141028164930_14886_19.jpg","isLike":0,"itemId":0},{"productId":"720","name":"Moist Pouch - Fish, Brown Rice, Vegetables","brand":"Burns","brandId":"121","inStock":1,"maxSaleQty":0,"price":"228.00","final_price":"228.00","smallImage":"catalog/product/_/2/_20141028171822_14888_11.jpg","isLike":0,"itemId":0},{"productId":"722","name":"Moist Pouch - Lamb, Brown Rice & Vegetables","brand":"Burns","brandId":"121","inStock":1,"maxSaleQty":0,"price":"228.00","final_price":"228.00","smallImage":"catalog/product/_/2/_20141028174906_14891_11.jpg","isLike":0,"itemId":0},{"productId":"1311","name":"Recall Long Line Leash, 10m","brand":"The Company of Animals","brandId":"115","inStock":1,"maxSaleQty":0,"price":"245.00","final_price":"245.00","smallImage":"catalog/product/_/2/_20120329133505_5430_13.jpg","isLike":0,"itemId":0},{"productId":"1327","name":"Gentle Leader Quick Release Head Collar","brand":"Premier","brandId":"173","inStock":1,"maxSaleQty":0,"price":"185.00","final_price":"185.00","smallImage":"catalog/product/_/2/_20130528104147_10288_35.jpg","isLike":0,"itemId":0},{"productId":"1332","name":"The Sporn Halter","brand":"Sporn","brandId":"110","inStock":1,"maxSaleQty":0,"price":"169.00","final_price":"169.00","smallImage":"catalog/product/_/2/_20130528114844_10294_29.jpg","isLike":0,"itemId":0},{"productId":"1340","name":"Halti - Headcollar","brand":"The Company of Animals","brandId":"115","inStock":1,"maxSaleQty":0,"price":"178.00","final_price":"178.00","smallImage":"catalog/product/_/2/_20150522155619_16648_79.jpg","isLike":0,"itemId":0}]},{"title":"Things That Make Us Go Meow","items":[{"productId":"1762","name":"Goat Milk Powder","brand":"Natural Animal Solutions","brandId":"225","inStock":1,"maxSaleQty":0,"price":"448.00","final_price":"448.00","smallImage":"catalog/product/_/2/_20150407122845_16330_55.jpg","isLike":0,"itemId":0},{"productId":"1891","name":"Kwik Stop Powder","brand":"Gimborn","brandId":"157","inStock":1,"maxSaleQty":0,"price":"88.00","final_price":"88.00","smallImage":"catalog/product/_/2/_20120330134558_5672_23.jpg","isLike":0,"itemId":0},{"productId":"1962","name":"Pet Electrolyte - Concentrate","brand":"NaturVet","brandId":"192","inStock":1,"maxSaleQty":0,"price":"89.00","final_price":"89.00","smallImage":"catalog/product/_/2/_20121108160653_8747_35.jpg","isLike":0,"itemId":0},{"productId":"2019","name":"Pill Popper","brand":"Lixit","brandId":"64","inStock":1,"maxSaleQty":0,"price":"48.00","final_price":"48.00","smallImage":"catalog/product/_/2/_20150411143151_16354_23.jpg","isLike":0,"itemId":0},{"productId":"2021","name":"Pet Medication & Feeding Dropper","brand":"Lixit","brandId":"64","inStock":1,"maxSaleQty":0,"price":"38.00","final_price":"38.00","smallImage":"catalog/product/_/2/_20150411144251_16356_23.jpg","isLike":0,"itemId":0},{"productId":"2931","name":"Feline High Calorie Nutritional Gel","brand":"Tomlyn","brandId":"74","inStock":1,"maxSaleQty":0,"price":"96.00","final_price":"96.00","smallImage":"catalog/product/f/e/feline_high_calorie.jpg","isLike":0,"itemId":0},{"productId":"2934","name":"KMR Powder Milk Replacer for Kittens","brand":"PetAg","brandId":"66","inStock":1,"maxSaleQty":0,"price":"140.00","final_price":"140.00","smallImage":"catalog/product/_/2/_20120502145155_6558_17.jpg","isLike":0,"itemId":0},{"productId":"9261","name":"Adjustable E Collar for Cats","brand":"Kong","brandId":"160","inStock":1,"maxSaleQty":0,"price":"88.00","final_price":"88.00","smallImage":"catalog/product/e/_/e_collar.jpg","isLike":0,"itemId":0},{"productId":"9268","name":"Quick & Easy Pill Dispenser","brand":"Four Paws","brandId":"101","inStock":1,"maxSaleQty":0,"price":"38.00","final_price":"38.00","smallImage":"catalog/product/d/i/dispenser.jpg","isLike":0,"itemId":0},{"productId":"10282","name":"Pet First Aid Kit ( Indoor / Outdoor)","brand":"RC Pet Products","brandId":"411","inStock":1,"maxSaleQty":0,"price":"108.00","final_price":"108.00","smallImage":"catalog/product/p/e/pet_first_aid.jpg","isLike":0,"itemId":0}]},{"title":"Cat Food","items":[{"productId":"2292","name":"Feline Canada / USA - Six Fish","brand":"Orijen","brandId":"194","inStock":1,"maxSaleQty":0,"price":"295.00","final_price":"295.00","smallImage":"catalog/product/f/e/feline_six_fish_usa.jpg","isLike":0,"itemId":0},{"productId":"2392","name":"Feline Canada / USA - Regional Red","brand":"Orijen","brandId":"194","inStock":1,"maxSaleQty":0,"price":"348.00","final_price":"348.00","smallImage":"catalog/product/f/e/feline_regional_red_usa.jpg","isLike":0,"itemId":0},{"productId":"12340","name":"Gold Label Grain Free Shredded Canned for Cats","brand":"Fussie Cat","brandId":"472","inStock":1,"maxSaleQty":0,"price":"9.00","final_price":"9.00","smallImage":"catalog/product/c/h/chicken_formula_in_gravy.jpg","isLike":0,"itemId":0},{"productId":"12316","name":"Feline Frozen Raw Food For Cats ","brand":"Big Dog - Frozen","brandId":"471","inStock":1,"maxSaleQty":0,"price":"162.00","final_price":"162.00","smallImage":"catalog/product/c/a/cat_frozen.jpg","isLike":0,"itemId":0},{"productId":"10006","name":"Feline Core Grain-Free Canned Food","brand":"Wellness - Core","brandId":"405","inStock":1,"maxSaleQty":0,"price":"20.00","final_price":"20.00","smallImage":"catalog/product/f/e/feline_core_group.jpg","isLike":0,"itemId":0},{"productId":"8772","name":"Feline Grain-Free Core Indoor Recipe","brand":"Wellness - Core","brandId":"405","inStock":1,"maxSaleQty":0,"price":"324.00","final_price":"324.00","smallImage":"catalog/product/n/e/new_core_feline_indoor.png","isLike":0,"itemId":0},{"productId":"2276","name":"Feline Complete Health Healthy Weight Recipe","brand":"Wellness ","brandId":"187","inStock":1,"maxSaleQty":0,"price":"268.00","final_price":"268.00","smallImage":"catalog/product/n/e/new_feline_healthy_weight_6lb.png","isLike":0,"itemId":0},{"productId":"2280","name":"Feline Complete Health Recipe","brand":"Wellness ","brandId":"187","inStock":1,"maxSaleQty":0,"price":"268.00","final_price":"268.00","smallImage":"catalog/product/n/e/new_feline_salmon_6lb.png","isLike":0,"itemId":0},{"productId":"2278","name":"Feline Complete Health Indoor Recipe","brand":"Wellness ","brandId":"187","inStock":1,"maxSaleQty":0,"price":"268.00","final_price":"268.00","smallImage":"catalog/product/n/e/new_feline_indooe_chicken_6lb.png","isLike":0,"itemId":0},{"productId":"12169","name":"Bubbles Dinner Placemat","brand":"Waggo","brandId":"462","inStock":1,"maxSaleQty":0,"price":"95.00","final_price":"95.00","smallImage":"catalog/product/g/r/group_of_mat_1_1.jpg","isLike":0,"itemId":0},{"productId":"9511","name":"Feline 85/15 Grain-Free Mono Protein - Salmon & Herring","brand":"AATU","brandId":"380","inStock":1,"maxSaleQty":0,"price":"155.00","final_price":"155.00","smallImage":"catalog/product/s/a/salmon_herring_for_cats.jpg","isLike":0,"itemId":0},{"productId":"12172","name":"Pet Dinner Mat","brand":"Van Ness","brandId":"75","inStock":1,"maxSaleQty":0,"price":"77.00","final_price":"77.00","smallImage":"catalog/product/d/i/dinner_mat_double_1.jpg","isLike":0,"itemId":0}]},{"title":"Cat Litter","items":[{"productId":"2855","name":"Cat Litter Deodorizer Powder","brand":"Arm & Hammer","brandId":"43","inStock":1,"maxSaleQty":0,"price":"43.00","final_price":"43.00","smallImage":"catalog/product/_/2/_20120323121851_5336_11.jpg","isLike":0,"itemId":0},{"productId":"12026","name":"Eco-Friendly Litter Scoop","brand":"Beco Pets","brandId":"436","inStock":1,"maxSaleQty":0,"price":"58.00","final_price":"58.00","smallImage":"catalog/product/l/i/litter_scoop.jpg","isLike":0,"itemId":0},{"productId":"10786","name":"Instantly Traps Cat Litter Mat w/ Super Gripper Backing ","brand":"Dog Gone Smart","brandId":"55","inStock":1,"maxSaleQty":0,"price":"285.00","final_price":"285.00","smallImage":"catalog/product/c/a/catdirty1.jpg","isLike":0,"itemId":0},{"productId":"10535","name":"Tracking Free Litter Box","brand":"Richell","brandId":"71","inStock":1,"maxSaleQty":0,"price":"494.00","final_price":"494.00","smallImage":"catalog/product/b/u/bucket.jpg","isLike":0,"itemId":0},{"productId":"10532","name":"California Green Natural Litter Box Deodorizer","brand":"Skout's Honor","brandId":"435","inStock":1,"maxSaleQty":0,"price":"165.00","final_price":"165.00","smallImage":"catalog/product/l/i/litter_box.jpg","isLike":0,"itemId":0},{"productId":"10224","name":"Corole Litter Box w/ Hood - Large","brand":"Richell","brandId":"71","inStock":1,"maxSaleQty":0,"price":"328.00","final_price":"328.00","smallImage":"catalog/product/o/l/old_version_large_orange.jpg","isLike":0,"itemId":0},{"productId":"10218","name":"Corole Litter Pan w/ Low Rim 55","brand":"Richell","brandId":"71","inStock":1,"maxSaleQty":0,"price":"234.00","final_price":"234.00","smallImage":"catalog/product/l/o/low_rim_group.jpg","isLike":0,"itemId":0},{"productId":"10200","name":"Corole Litter Box w/ Hood - Large","brand":"Richell","brandId":"71","inStock":1,"maxSaleQty":0,"price":"342.00","final_price":"342.00","smallImage":"catalog/product/n/e/new_large_box.jpg","isLike":0,"itemId":0},{"productId":"10196","name":"Corole Litter Box w/ Hood - Medium","brand":"Richell","brandId":"71","inStock":1,"maxSaleQty":0,"price":"318.00","final_price":"318.00","smallImage":"catalog/product/n/e/new_corole_box.jpg","isLike":0,"itemId":0},{"productId":"10064","name":"Modkat Reusable Tarp Liner","brand":"Modko","brandId":"404","inStock":1,"maxSaleQty":0,"price":"138.00","final_price":"138.00","smallImage":"catalog/product/g/r/grey_liner.jpg","isLike":0,"itemId":0},{"productId":"10063","name":"Modkat Tracking Free Litter Box","brand":"Modko","brandId":"404","inStock":1,"maxSaleQty":0,"price":"1780.00","final_price":"1780.00","smallImage":"catalog/product/m/o/modkat6.jpg","isLike":0,"itemId":0},{"productId":"9160","name":"Corner Litter Box","brand":"Smart Cat","brandId":"177","inStock":1,"maxSaleQty":0,"price":"188.00","final_price":"188.00","smallImage":"catalog/product/c/o/corner_1.jpg","isLike":0,"itemId":0}]}]
     */

    private String category_img;
    private int image_width;
    private int image_height;
    private int thumbnail_width;
    private int thumbnail_height;

    private String category_name;
    private String category_id;
    private List<CarouselsBean> carousels;
    private List<BannersBean> banners;

    public String getCategory_img() {
        return category_img;
    }

    public void setCategory_img(String category_img) {
        this.category_img = category_img;
    }

    public int getImage_width() {
        return image_width;
    }

    public void setImage_width(int image_width) {
        this.image_width = image_width;
    }

    public int getImage_height() {
        return image_height;
    }

    public void setImage_height(int image_height) {
        this.image_height = image_height;
    }

    public int getThumbnail_width() {
        return thumbnail_width;
    }

    public void setThumbnail_width(int thumbnail_width) {
        this.thumbnail_width = thumbnail_width;
    }

    public int getThumbnail_height() {
        return thumbnail_height;
    }

    public void setThumbnail_height(int thumbnail_height) {
        this.thumbnail_height = thumbnail_height;
    }

    public List<CarouselsBean> getCarousels() {
        return carousels;
    }

    public void setCarousels(List<CarouselsBean> carousels) {
        this.carousels = carousels;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public List<BannersBean> getBanners() {
        return banners;
    }

    public void setBanners(List<BannersBean> banners) {
        this.banners = banners;
    }

    @Override
    public String toString() {
        return "CategoryDetailNewModel{" +
                "category_img='" + category_img + '\'' +
                ", image_width=" + image_width +
                ", image_height=" + image_height +
                ", category_name='" + category_name + '\'' +
                ", category_id='" + category_id + '\'' +
                ", carousels=" + carousels +
                '}';
    }

    public static class BannersBean implements Serializable{
        private String id;
        private String name;
        private String image;
        private int image_width;
        private int image_height;
        private String type;
        private String key;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getImageWidth() {
            return image_width;
        }

        public void setImageWidth(int image_width) {
            this.image_width = image_width;
        }

        public int getImageHeight() {
            return image_height;
        }

        public void setImageHeight(int image_height) {
            this.image_height = image_height;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    public static class CarouselsBean {
        /**
         * title : Cat Stuff We Love
         * items : [{"productId":"126","name":"Wholesome Essentials, Senior Farm-Raised Chicken, Brown Rice & Sweet Potato Recipe","brand":"Nutro","brandId":"191","inStock":1,"maxSaleQty":0,"price":"368.00","final_price":"368.00","smallImage":"catalog/product/n/e/new_senior_chicken_15lb.jpg","isLike":0,"itemId":0},{"productId":"140","name":"Wholesome Essentials, Small Breed Adult Farm-Raised Chicken, Whole Brown Rice & Sweet Potato Recipe","brand":"Nutro","brandId":"191","inStock":1,"maxSaleQty":0,"price":"128.00","final_price":"128.00","smallImage":"catalog/product/_/2/_20150708165959_17014_23.jpg","isLike":0,"itemId":0},{"productId":"223","name":"Wholesome Essentials, Healthy Weight Adult Small Breed Farm-Raised Chicken, Brown Rice & Sweet Potato Recipe","brand":"Nutro","brandId":"191","inStock":1,"maxSaleQty":0,"price":"145.00","final_price":"145.00","smallImage":"catalog/product/n/e/new_new_small_breed_weight.png","isLike":0,"itemId":0},{"productId":"243","name":"Four-Star Nutritionals- Salmon A La Veg for Dogs /P","brand":"Fromm","brandId":"84","inStock":1,"maxSaleQty":0,"price":"188.00","final_price":"188.00","smallImage":"catalog/product/_/2/_20120514165045_6978_23.jpg","isLike":0,"itemId":0},{"productId":"263","name":"Wholesome Essentials, Adult Venison, Brown Rice & Oatmeal Recipe","brand":"Nutro","brandId":"191","inStock":1,"maxSaleQty":0,"price":"178.00","final_price":"178.00","smallImage":"catalog/product/n/e/new_venison_wholesome.png","isLike":0,"itemId":0},{"productId":"718","name":"Moist Pouch - Chicken, Brown Rice & Vegetables","brand":"Burns","brandId":"121","inStock":1,"maxSaleQty":0,"price":"228.00","final_price":"228.00","smallImage":"catalog/product/_/2/_20141028164930_14886_19.jpg","isLike":0,"itemId":0},{"productId":"720","name":"Moist Pouch - Fish, Brown Rice, Vegetables","brand":"Burns","brandId":"121","inStock":1,"maxSaleQty":0,"price":"228.00","final_price":"228.00","smallImage":"catalog/product/_/2/_20141028171822_14888_11.jpg","isLike":0,"itemId":0},{"productId":"722","name":"Moist Pouch - Lamb, Brown Rice & Vegetables","brand":"Burns","brandId":"121","inStock":1,"maxSaleQty":0,"price":"228.00","final_price":"228.00","smallImage":"catalog/product/_/2/_20141028174906_14891_11.jpg","isLike":0,"itemId":0},{"productId":"1311","name":"Recall Long Line Leash, 10m","brand":"The Company of Animals","brandId":"115","inStock":1,"maxSaleQty":0,"price":"245.00","final_price":"245.00","smallImage":"catalog/product/_/2/_20120329133505_5430_13.jpg","isLike":0,"itemId":0},{"productId":"1327","name":"Gentle Leader Quick Release Head Collar","brand":"Premier","brandId":"173","inStock":1,"maxSaleQty":0,"price":"185.00","final_price":"185.00","smallImage":"catalog/product/_/2/_20130528104147_10288_35.jpg","isLike":0,"itemId":0},{"productId":"1332","name":"The Sporn Halter","brand":"Sporn","brandId":"110","inStock":1,"maxSaleQty":0,"price":"169.00","final_price":"169.00","smallImage":"catalog/product/_/2/_20130528114844_10294_29.jpg","isLike":0,"itemId":0},{"productId":"1340","name":"Halti - Headcollar","brand":"The Company of Animals","brandId":"115","inStock":1,"maxSaleQty":0,"price":"178.00","final_price":"178.00","smallImage":"catalog/product/_/2/_20150522155619_16648_79.jpg","isLike":0,"itemId":0}]
         */

        private String title;
        private int position;
        private int type;
        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        private List<SVRAppserviceProductSearchResultsItemReturnEntity> items;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<SVRAppserviceProductSearchResultsItemReturnEntity> getItems() {
            return items;
        }

        public void setItems(List<SVRAppserviceProductSearchResultsItemReturnEntity> items) {
            this.items = items;
        }

        @Override
        public String toString() {
            return "CarouselsBean{" +
                    "title='" + title + '\'' +
                    ", position=" + position +
                    ", type=" + type +
                    ", items=" + items +
                    '}';
        }
    }
}

