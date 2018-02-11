package com.whitelabel.app.utils;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;

import com.whitelabel.app.Const;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.model.ShoppingCartListEntityCell;
import com.whitelabel.app.utils.logger.Logger;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/22.
 */
public class GaTrackHelper {
    public static String GA_TIME_CATEGORY_IMPRESSION="Impression Screens Loading";
    public static String GA_TIME_CATEGORY_CHECKOUT="Checkout Step";
    public static String GA_TIME_CATEGORY_PAYMENT="Payment";
    public static GaTrackHelper gaTrackHelper;
    private  GaTrackHelper(){

    }
    public static GaTrackHelper getInstance(){
            if(gaTrackHelper==null){
                synchronized (GaTrackHelper.class){
                    if(gaTrackHelper==null){
                        gaTrackHelper=new GaTrackHelper();
                    }
                }
            }
        return gaTrackHelper;
    }


    public  void googleAnalyticsTransaction(String transactionId,String affiliation,double revenue,double tax,double shipping,String currCode){
        Tracker mTracker= WhiteLabelApplication.getInstance().getAnalyticTracherInstance();
        HitBuilders.TransactionBuilder transactionBuilder=new HitBuilders.TransactionBuilder();
        transactionBuilder.setTransactionId(transactionId)
                .setAffiliation(affiliation)
                .setRevenue(revenue)
                .setTax(tax)
                .setShipping(shipping)
                .setCurrencyCode(null);
        mTracker.send(transactionBuilder.build());

    }
    public  void googleAnalyticsItem(String transactionId,String name,String sku,String category,double price,Long qty,String currCode){
        Tracker mTracker= WhiteLabelApplication.getInstance().getAnalyticTracherInstance();
        HitBuilders.ItemBuilder itemBuilder=new HitBuilders.ItemBuilder();
        itemBuilder.setTransactionId(transactionId)
                .setName(name)
                .setSku(sku)
                .setCategory(category)
                .setPrice(price)
                .setQuantity(qty)
                .setCurrencyCode(currCode);
        mTracker.send(itemBuilder.build());
    }
    public  void googleAnalyticsEvent(String category,String action,String lable,Long value){
        Tracker mTracker= WhiteLabelApplication.getInstance().getAnalyticTracherInstance();
        HitBuilders.EventBuilder eventBuilder=new HitBuilders.EventBuilder();
        eventBuilder.setCategory(category);
        eventBuilder.setAction(action);
        if(!TextUtils.isEmpty(lable)){
            eventBuilder.setLabel(lable);
        }
        if(value!=null){
            eventBuilder.setValue(value);
        }
        mTracker.send(eventBuilder.build());
    }

    public  void googleAnalyticsReorderEvent(String category,String action,String productNameLable,String addOrSubLable,String quantityLable,Long value){
        Tracker mTracker= WhiteLabelApplication.getInstance().getAnalyticTracherInstance();
        HitBuilders.EventBuilder eventBuilder=new HitBuilders.EventBuilder();
        eventBuilder.setCategory(category);
        eventBuilder.setAction(action);
        if(!TextUtils.isEmpty(productNameLable)){
            eventBuilder.setLabel(productNameLable);
        }
        if(!TextUtils.isEmpty(addOrSubLable)){
            eventBuilder.setLabel(addOrSubLable);
        }
        if(!TextUtils.isEmpty(quantityLable)){
            eventBuilder.setLabel(quantityLable);
        }
        if(value!=null){
            eventBuilder.setValue(value);
        }
        mTracker.send(eventBuilder.build());
    }
    public  void googleAnalytics(String screenName,Context context){
        Tracker mTracker= WhiteLabelApplication.getInstance().getAnalyticTracherInstance();
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    //ga  add card
    public void googleAnalyticsAddCart(String id,String name){
        Product gaProduct =new Product();
        gaProduct.setId(id);
        gaProduct.setName(name);
        ProductAction productAction=new ProductAction(ProductAction.ACTION_ADD);
        HitBuilders .ScreenViewBuilder builder=new HitBuilders.ScreenViewBuilder();
        builder.addProduct(gaProduct).setProductAction(productAction);
        Tracker tracker= WhiteLabelApplication.getInstance().getAnalyticTracherInstance();
        tracker.setScreenName("ProductDetail--AddCart");
        tracker.send(builder.build());

    }



    public void googleAnalyticsProductDetail(String id){
        Product product=new Product();
        product.setId(id);
        ProductAction productAction=new ProductAction(ProductAction.ACTION_DETAIL);
        HitBuilders .ScreenViewBuilder builder=new HitBuilders.ScreenViewBuilder();
        builder.addProduct(product).setProductAction(productAction);
        Tracker tracker= WhiteLabelApplication.getInstance().getAnalyticTracherInstance();
        tracker.setScreenName("Product Detail Screen");
        tracker.send(builder.build());
    }

    public void googleAnalyticsDeleteCart(Context context,String id,String name){
        Product gaProduct =new Product();
        gaProduct.setId(id);
        gaProduct.setName(name);
        ProductAction productAction=new ProductAction(ProductAction.ACTION_REMOVE);
        HitBuilders .ScreenViewBuilder builder=new HitBuilders.ScreenViewBuilder();
        builder.addProduct(gaProduct).setProductAction(productAction);
        Tracker tracker= WhiteLabelApplication.getInstance().getAnalyticTracherInstance();
        tracker.setScreenName("ShoppingCart--deleteProduct");
        tracker.send(builder.build());
    }



    public void googleAnalyticsStartCheckout(Context context,String ids,String ScreenName,int step){
        ProductAction productAction=new ProductAction(ProductAction.ACTION_CHECKOUT);
        productAction.setCheckoutStep(step);
        HitBuilders .ScreenViewBuilder builder=new HitBuilders.ScreenViewBuilder();
        builder.setProductAction(productAction);
        try {
            if (ids != null && ids.split(",").length > 0) {
                String [] productId=ids.split(",");
                for(int i=0;i<productId.length;i++){
                     Product product=new Product();
                     product.setId(productId[i]);
                    builder.addProduct(product);
                }
            }
        }catch(Exception ex){
            ex.getStackTrace();
        }
        Tracker tracker= WhiteLabelApplication.getInstance().getAnalyticTracherInstance();
        tracker.setScreenName(ScreenName);
        tracker.send(builder.build());
    }



    public void googleAnalyticsCheckoutSuccess(Context context , ArrayList<ShoppingCartListEntityCell> items,String orderId,double grandTotal,double shippingFee){
        try {
            HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder();
            ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE);
            productAction.setTransactionId(orderId);
            productAction.setTransactionRevenue(grandTotal);
            productAction.setTransactionAffiliation("In-app Store");
            productAction.setTransactionTax(0.00);
            productAction.setTransactionShipping(shippingFee);
            for (int i = 0; i < items.size(); i++) {
                ShoppingCartListEntityCell cell = items.get(i);
                Product product = new Product();
                product.setId(cell.getProductId());
                product.setName(cell.getName());
                String firstCategory = cell.getFirstCategory();
                if ("".equals(firstCategory) || firstCategory == null) {
                    firstCategory = cell.getCategory();
                }
                product.setCategory(firstCategory);
                product.setPrice(Double.parseDouble(cell.getPrice().replace(",", "").replace("RM", "").trim()));
                product.setQuantity(Integer.parseInt(cell.getQty().replace(",", "").replace("RM", "").trim()));
                builder.addProduct(product);
            }
            builder.setProductAction(productAction);
            Tracker tracker = WhiteLabelApplication.getInstance().getAnalyticTracherInstance();
            tracker.setScreenName("Checkout Sucess Screen");
            tracker.send(builder.build());
        }catch (Exception ex){
            ex.getStackTrace();
        }
    }

    public void googleAnalyticsReportActivity(Activity activity,boolean start){
        try{
            if(start){
                WhiteLabelApplication.getInstance().getAnalyticInstance().reportActivityStart(activity);
            }else{
                WhiteLabelApplication.getInstance().getAnalyticInstance().reportActivityStop(activity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public Long googleAnalyticsTimeStart(){
        return SystemClock.elapsedRealtime();
    }
    public void googleAnalyticsTimeStop(String category,Long startTime,String name){
        if(startTime==0){
            return;
        }
        Long endTime=SystemClock.elapsedRealtime();
        Tracker mTracker= WhiteLabelApplication.getInstance().getAnalyticTracherInstance();
        // Build and send timing.
        mTracker.send(new HitBuilders.TimingBuilder()
                .setCategory(category)
                .setValue(endTime-startTime)
                .setVariable(name)
                .build());
        JLogUtils.d("GaTrack",name+">>load time:"+(endTime-startTime)+">>endTime:"+endTime+">>startTime"+startTime);
    }

    /**
     * orderList or orderDetail event
     * @param isOrderListDetail true orderList ,false orderDetail
     * @param productId
     */
    public void gaOrderListOrDetail(boolean isOrderListDetail,String productId){
        String event= isOrderListDetail? Const.GA.EVENT_REORDER_ORDERLIST:Const.GA.EVENT_REORDER_ORDERDETAIL;
        GaTrackHelper.getInstance().googleAnalyticsEvent(Const.GA.ORDER_REORDER_CATEGORY,
            Const.GA.ORDER_ADD_TO_CART_EVENT,
            event,
            Long.valueOf(productId));
    }
}
