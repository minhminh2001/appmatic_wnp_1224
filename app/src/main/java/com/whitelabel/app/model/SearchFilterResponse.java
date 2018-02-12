package com.whitelabel.app.model;

import com.google.gson.annotations.SerializedName;

import com.whitelabel.app.utils.JLogUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by img on 2018/2/5.
 */

public class SearchFilterResponse extends ResponseModel{

    private List<SuggestsBean> suggests;

    public List<SuggestsBean> getSuggests() {
        return suggests;
    }

    public void setSuggests(List<SuggestsBean> suggests) {
        this.suggests = suggests;
    }

    public static class SuggestsBean {

        /**
         * type : 1
         * title : Products
         * items : [{"product_id":"300","name":"Adult Chicken Dog Food","image":"https://devcdn
         * .petonline.com.hk/media/catalog/product/cache/1/small_image/45x
         * /9df78eab33525d08d6e5fb8d27136e95/a/d/adult_chicken.png"},{"product_id":"310",
         * "name":"Adult Grain Free Fish Dog Food","image":"https://devcdn.petonline.com
         * .hk/media/catalog/product/cache/1/small_image/45x/9df78eab33525d08d6e5fb8d27136e95/n/e
         * /new_grain_free_fish_adult.png"},{"product_id":"2339","name":"Feline Fish Adult Food",
         * "image":"https://devcdn.petonline.com
         * .hk/media/catalog/product/cache/1/small_image/45x/9df78eab33525d08d6e5fb8d27136e95/n/e
         * /new_cat_fish_adult.png"},{"product_id":"2342","name":"Feline Chicken Adult Food",
         * "image":"https://devcdn.petonline.com
         * .hk/media/catalog/product/cache/1/small_image/45x/9df78eab33525d08d6e5fb8d27136e95/n/e
         * /new_feline_chicken.jpg"},{"product_id":"12833","name":"Organic Vegan Adult Dog Food",
         * "image":"https://devcdn.petonline.com
         * .hk/media/catalog/product/cache/1/small_image/45x/9df78eab33525d08d6e5fb8d27136e95/o/r
         * /organic_vegan_adult_dog_food.jpg"}]
         * sort : 1
         */

        private int type;

        private String title;

        private int sort;

        private List<ItemsBean> items;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean implements Serializable{

            /**
             * product_id : 300
             * name : Adult Chicken Dog Food
             * image : https://devcdn.petonline.com
             * .hk/media/catalog/product/cache/1/small_image/45x/9df78eab33525d08d6e5fb8d27136e95
             * /a/d/adult_chicken.png
             */
            //net
            @SerializedName("product_id")
            private String productId;
            @SerializedName("brand_id")
            private String brandId;
            @SerializedName("category_id")
            private String categoryId;

            private String name;

            private String image;
            //local
            private int position;
            private int recyclerType;
            private int type;
            private String title;
            private int sort;
            //bottom line identifying
            private boolean isLast;
            private String key;


            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getBrandId() {
                return brandId;
            }

            public void setBrandId(String brandId) {
                this.brandId = brandId;
            }

            public String getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(String categoryId) {
                this.categoryId = categoryId;
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

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public int getRecyclerType() {
                return recyclerType;
            }

            public void setRecyclerType(int recyclerType) {
                this.recyclerType = recyclerType;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public boolean getIsLast() {
                return isLast;
            }

            public void setIsLast(boolean isLast) {
                this.isLast = isLast;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }
    }
}
