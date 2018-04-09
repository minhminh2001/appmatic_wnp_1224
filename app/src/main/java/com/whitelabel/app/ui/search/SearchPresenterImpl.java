package com.whitelabel.app.ui.search;

import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.adapter.SearchFilterAdapter;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.model.RecentSearchKeywordResponse;
import com.whitelabel.app.model.SVRAppserviceProductSearchReturnEntity;
import com.whitelabel.app.model.SearchFilterResponse;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RxUtil;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by img on 2018/1/26.
 */

public class SearchPresenterImpl extends RxPresenter<SearchContract.View> implements SearchContract.Presenter {
    private ICommodityManager iCommodityManager;
    private IBaseManager iBaseManager;
    private List<SearchFilterResponse.SuggestsBean.ItemsBean> searchResponse;

    @Inject
    public SearchPresenterImpl(ICommodityManager iCommodityManager,IBaseManager iBaseManager) {
        this.iCommodityManager = iCommodityManager;
        this.iBaseManager=iBaseManager;
    }

    @Override
    public void autoSearch(final String keyword) {
//        mView.showProgressDialog();
        final String sessionKey = iBaseManager.isSign()?iBaseManager.getUser().getSessionKey():"";
        Subscription subscribe = iCommodityManager.autoHintSearch(sessionKey,keyword)
            .compose(RxUtil.<SearchFilterResponse>rxSchedulerHelper()).subscribe(

                new Subscriber<SearchFilterResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
//                        mView.closeProgressDialog();
                        if(ExceptionParse.parseException(e).getErrorType()== ExceptionParse.ERROR.HTTP_ERROR){
                            mView.showErrorMsg(ExceptionParse.parseException(e).getErrorMsg());
                        };
                    }

                    @Override
                    public void onNext(SearchFilterResponse searchFilterResponse) {
//                        mView.closeProgressDialog();
                        List<SearchFilterResponse.SuggestsBean.ItemsBean> itemsBeans =
                            parseRecyclerDatas(searchFilterResponse);

                        // login and find data
                        if(iBaseManager.isSign()
                                && itemsBeans.size() > 0){
                            searchResponse = itemsBeans;
                            saveRecentSearchKeyword(keyword);
                        } else {
                            mView.loadAutoHintSearchData(itemsBeans);
                        }
                    }
                });
        addSubscrebe(subscribe);
    }

    private List<SearchFilterResponse.SuggestsBean.ItemsBean> parseRecyclerDatas(SearchFilterResponse searchFilterResponse){
        List<SearchFilterResponse.SuggestsBean.ItemsBean> itemsBeans=new ArrayList<>();
        if (searchFilterResponse!=null){
            List<SearchFilterResponse.SuggestsBean> suggests = searchFilterResponse.getSuggests();
            if (suggests!=null && !suggests.isEmpty()){
                for (int i=0;i<suggests.size();i++){
                    SearchFilterResponse.SuggestsBean suggestsBean = suggests.get(i);
                    SearchFilterResponse.SuggestsBean.ItemsBean itemsBeanParent=new SearchFilterResponse.SuggestsBean.ItemsBean();
                    itemsBeanParent.setRecyclerType(SearchFilterAdapter.HEADER_TITLE);
                    itemsBeanParent.setType(suggestsBean.getType());
                    itemsBeanParent.setTitle(suggestsBean.getTitle());
                    List<SearchFilterResponse.SuggestsBean.ItemsBean> items = suggestsBean
                        .getItems();
                    itemsBeans.add(itemsBeanParent);
                    if (items!=null && !items.isEmpty()){
                        for (int j=0;j<items.size();j++){
                            SearchFilterResponse.SuggestsBean.ItemsBean itemsBean = items.get(j);
                            SearchFilterResponse.SuggestsBean.ItemsBean itemsBeanChild=new SearchFilterResponse.SuggestsBean.ItemsBean();
                            itemsBeanChild.setImage(itemsBean.getImage());
                            itemsBeanChild.setName(itemsBean.getName());
                            //img and text
                            if (suggestsBean.getType()==1){
                                itemsBeanChild.setRecyclerType(SearchFilterAdapter.IMG_AND_TEXT);
                            }else {
                                itemsBeanChild.setRecyclerType(SearchFilterAdapter.TEXT);
                            }
                            itemsBeanChild.setType(suggestsBean.getType());
                            itemsBeanChild.setProductId(itemsBean.getProductId());
                            itemsBeanChild.setBrandId(itemsBean.getBrandId());
                            itemsBeanChild.setCategoryId(itemsBean.getCategoryId());
                            itemsBeanChild.setKey(itemsBean.getKey());

                            itemsBeanChild.setSort(suggestsBean.getSort());
                            itemsBeanChild.setIsLast(j==items.size()-1);
                            itemsBeans.add(itemsBeanChild);
                        }
                    }else {
                        //if child not data, remove last one(parent)
                        itemsBeans.remove(itemsBeans.size()-1);
                    }
                }
            }
        }
        return itemsBeans;
    }

    @Override
    public void getRecentSearchKeywords() {
        if(!iBaseManager.isSign()){
            return;
        }

        final String sessionKey = iBaseManager.getUser().getSessionKey();
        Subscription subscribe = iCommodityManager.getRecentSearchKeywords("1", sessionKey)
                .compose(RxUtil.<RecentSearchKeywordResponse>rxSchedulerHelper())
                .subscribe(new Subscriber<RecentSearchKeywordResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(ExceptionParse.parseException(e).getErrorType()== ExceptionParse.ERROR.HTTP_ERROR){
                            mView.showErrorMsg(ExceptionParse.parseException(e).getErrorMsg());
                        }
                    }

                    @Override
                    public void onNext(RecentSearchKeywordResponse recentSearchKeywordResponse) {

                        // get recent search keyword failed from server
                        if(recentSearchKeywordResponse.getStatus() != 1) {
                            return;
                        }

                        mView.updateRecentSearchView(recentSearchKeywordResponse.getKeywords());
                    }
                });

        addSubscrebe(subscribe);
    }

    @Override
    public void saveRecentSearchKeyword(String keyword) {
        final String sessionKey = iBaseManager.isSign() ? iBaseManager.getUser().getSessionKey():"";
        Subscription subscribe = iCommodityManager.saveRecentSearchKeyword(keyword, sessionKey)
                .compose(RxUtil.<RecentSearchKeywordResponse>rxSchedulerHelper())
                .subscribe(new Subscriber<RecentSearchKeywordResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(ExceptionParse.parseException(e).getErrorType()== ExceptionParse.ERROR.HTTP_ERROR){
                            mView.showErrorMsg(ExceptionParse.parseException(e).getErrorMsg());
                        }
                    }

                    @Override
                    public void onNext(RecentSearchKeywordResponse recentSearchKeywordResponse) {
                        mView.loadAutoHintSearchData(searchResponse);
                    }
                });
        addSubscrebe(subscribe);
    }
}
