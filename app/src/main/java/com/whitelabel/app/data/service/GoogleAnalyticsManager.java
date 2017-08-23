package com.whitelabel.app.data.service;

import com.whitelabel.app.utils.GaTrackHelper;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/8/23.
 */

public class GoogleAnalyticsManager implements IGoogleAnalyticsManager{

    @Inject
    public GoogleAnalyticsManager(){

    }
    @Override
    public void googleAnalyticsEvent(String category, String action, String label, String value) {
        long id=0;
        try{
            id=Long.parseLong(value);
        }catch (Exception ex){
            ex.getStackTrace();
        }
        GaTrackHelper.getInstance().googleAnalyticsEvent(category, action,
                label,id);
    }

    @Override
    public void googleAnalyticsProductDetail(String productId) {
        GaTrackHelper.getInstance().googleAnalyticsProductDetail(productId);
    }

    @Override
    public void googleAnalyticsAddCart(String id, String name) {
        GaTrackHelper.getInstance().googleAnalyticsAddCart(
                id,name);
    }
}
