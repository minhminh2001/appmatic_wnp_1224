package com.whitelabel.app.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by img on 2017/11/20.
 */

public class ShopBrandResponse implements Serializable {
    private List<BrandsBean> brands;
    public List<BrandsBean> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandsBean> brands) {
        this.brands = brands;
    }

    public static class BrandsBean {
        /**
         * title : 0-9
         * count : 1
         * items : [{"title":"3M","link":"https://dev2.wnp.com.hk/brand/3m.html","icon":"https://dev2cdn.petonline.com.hk/media//vesbrand/icon/20111204144027_204.jpg","identifier":"3m"}]
         */

        private String title;
        private int count;
        private List<ItemsBean> items;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean implements MultiItemEntity ,Serializable{
            /**
             * title : 3M
             * link : https://dev2.wnp.com.hk/brand/3m.html
             * icon : https://dev2cdn.petonline.com.hk/media//vesbrand/icon/20111204144027_204.jpg
             * identifier : 3m
             */
            private int type;
            private int position;
            private String title;
            private String link;
            private String icon;
            private String identifier;

            @Override
            public int getItemType() {
                return type;
            }

            public void setItemType(int type) {
                this.type = type;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getIdentifier() {
                return identifier;
            }

            public void setIdentifier(String identifier) {
                this.identifier = identifier;
            }

        }
    }
}
