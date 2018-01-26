package com.whitelabel.app.ui.search;

import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.model.SVRAppserviceProductSearchReturnEntity;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.RxUtil;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by img on 2018/1/26.
 */

public class SearchPresenterImpl extends RxPresenter<SearchContract.View> implements SearchContract.Presenter {
    private ICommodityManager iCommodityManager;

    public SearchPresenterImpl(ICommodityManager iCommodityManager) {
        this.iCommodityManager = iCommodityManager;
    }

    @Override
    public void autoSearch(Map<String,String> params) {
        Subscription subscribe = iCommodityManager.autoHintSearch(params)
            .compose(RxUtil.<SVRAppserviceProductSearchReturnEntity>rxSchedulerHelper()).subscribe(

                new Subscriber<SVRAppserviceProductSearchReturnEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(ExceptionParse.parseException(e).getErrorType()== ExceptionParse.ERROR.HTTP_ERROR){
                            mView.showErrorMsg(ExceptionParse.parseException(e).getErrorMsg());
                        };
                    }

                    @Override
                    public void onNext(
                        SVRAppserviceProductSearchReturnEntity
                            svrAppserviceProductSearchReturnEntity) {
                        mView.loadAutoHintSearchData(svrAppserviceProductSearchReturnEntity);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public Map<String,String> transformSearchMap(String storeId, String p, String limit, String order, String dir,
        String brand, String categoryId, String modelType, String q,String keywords, String price, String key,String fromPage) {
        Map<String,String> params=new HashMap<>();
        params.put("store_id", storeId);
        params.put("p", p);
        params.put("limit", limit);
        if (!TextUtils.isEmpty(order)) {
            params.put("order", order);
        }
        if (!TextUtils.isEmpty(dir)) {
            params.put("dir", dir);
        }
        if (!TextUtils.isEmpty(brand)) {
            params.put("brand", brand);
        }
        if (!TextUtils.isEmpty(categoryId)) {
            params.put("category_id", categoryId);
        }
        if (!TextUtils.isEmpty(modelType)) {
            params.put("model_type", modelType);
        }
        if (!TextUtils.isEmpty(q)) {
            params.put("q", q);
        }
        if (!TextUtils.isEmpty(keywords)) {
            params.put("keywords", keywords);
        }
        if (!TextUtils.isEmpty(price)) {
            params.put("price", price);
        }
        String sessionKey = WhiteLabelApplication.getAppConfiguration().getUserInfo(WhiteLabelApplication.getInstance()).getSessionKey();
        if (!TextUtils.isEmpty(sessionKey)) {
            params.put("session_key", sessionKey);
        }
        if(!TextUtils.isEmpty(fromPage)){
            params.put("pageType",fromPage);
        }
        return params;
    }


}
