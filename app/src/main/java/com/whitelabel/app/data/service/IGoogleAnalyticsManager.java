package com.whitelabel.app.data.service;

/**
 * Created by Administrator on 2017/8/23.
 */

public interface IGoogleAnalyticsManager {
        public String CATEGORY_PROCDUCT="Procduct Action";
        public String ACTION_ADDWISH="Add To Wishlist";
        public String ACTION_ADDTOCART="Add To Cart";
        public  void googleAnalyticsEvent(String category,String action,String label,String value);
        public void googleAnalyticsProductDetail(String productId);
        public void googleAnalyticsAddCart(String id,String name);
}
