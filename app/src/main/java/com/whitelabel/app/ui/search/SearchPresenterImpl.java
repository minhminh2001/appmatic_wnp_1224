package com.whitelabel.app.ui.search;

import com.whitelabel.app.adapter.SearchFilterAdapter;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.model.RecentSearchKeyword;
import com.whitelabel.app.model.RecentSearchKeywordResponse;
import com.whitelabel.app.model.RecentSearchKeywordResponse.Keyword;
import com.whitelabel.app.model.SearchFilterResponse;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.LanguageUtils;
import com.whitelabel.app.utils.RxUtil;
import com.whitelabel.app.utils.StoreUtils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by img on 2018/1/26.
 */

public class SearchPresenterImpl extends RxPresenter<SearchContract.View> implements SearchContract.Presenter {

    private static int LOCAL_RECENT_SEARCH_KEYWORD_LIMIT = 10;

    private ICommodityManager iCommodityManager;
    private IBaseManager iBaseManager;
    private List<SearchFilterResponse.SuggestsBean.ItemsBean> searchResponse;
    private List<RecentSearchKeyword> recentSearchKeywordList;

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
                        }
                    }

                    @Override
                    public void onNext(SearchFilterResponse searchFilterResponse) {
//                        mView.closeProgressDialog();
                        List<SearchFilterResponse.SuggestsBean.ItemsBean> itemsBeans =
                            parseRecyclerDatas(searchFilterResponse);

                            mView.loadAutoHintSearchData(itemsBeans);
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
    public void getRecentSearchKeywordsFromService(boolean isQuiet) {

        if(!isQuiet) {
            mView.showProgressDialog(true);
        }

        if(!iBaseManager.isSign()){
            mView.showProgressDialog(false);
            mView.updateRecentSearchView(sortRecentSearchKeyword(recentSearchKeywordList));
            return;
        }

        final String sessionKey = iBaseManager.getUser().getSessionKey();
        String languageCode = LanguageUtils.getCurrentLanguageCode();
        String storeId = StoreUtils.getStoreIdByLanguageCode(languageCode
                                                                .equalsIgnoreCase(LanguageUtils.LANGUAGE_CODE_AUTO)
                                                                ? LanguageUtils.LANGUAGE_CODE_ENGLISH
                                                                : languageCode);
        Subscription subscribe = iCommodityManager.getRecentSearchKeywords(storeId, sessionKey)
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

                        if(recentSearchKeywordResponse.getKeywords().size() > 0){
                            List<Keyword> keywords = recentSearchKeywordResponse.getKeywords();
                            for(Keyword keyword : keywords){
                                RecentSearchKeyword recentSearchKeyword = new RecentSearchKeyword();
                                recentSearchKeyword.setKeyword(keyword.getKeyword());
                                recentSearchKeyword.setTime(utcToCurrentDateTime(keyword.getTimeStamp()));
                                recentSearchKeyword.setSearchType(keyword.getType());
                                recentSearchKeyword.setCategroyId(keyword.getCategroyId());
                                recentSearchKeyword.setBrandId(keyword.getBrandId());
                                recentSearchKeywordList.add(recentSearchKeyword);
                            }

                            // recent search keyword desc sort by datetime
                            recentSearchKeywordListDescSortByDateTime(recentSearchKeywordList);

                            // recent search keyword with duplicate
                            List duplicateList = recentSearchKeywordListWithDuplicate(recentSearchKeywordList);
                            recentSearchKeywordList.clear();
                            recentSearchKeywordList.addAll(duplicateList);
                        }

                        mView.showProgressDialog(false);
                        mView.updateRecentSearchView(sortRecentSearchKeyword(recentSearchKeywordList));
                    }
                });

        addSubscrebe(subscribe);
    }

    @Override
    public List<RecentSearchKeyword> getRecentSearchKeywordsFromLocal(){
        return iBaseManager.getRecentSearchKeywordFromLocal();
    }

    /**
     * Get recent search keyword, follow step:
     * 1.invoke getRecentSearchKeywordsFromLocal
     * 2.invoke getRecentSearchKeywordsFromService
     */
    @Override
    public void getRecentSearchKeywords(boolean isQuiet) {

        if(recentSearchKeywordList == null) {
            recentSearchKeywordList = new ArrayList<RecentSearchKeyword>();
        }
        recentSearchKeywordList.clear();

        // Get recent search keyword from local
        List<RecentSearchKeyword> localRecentSearchKeywords = getRecentSearchKeywordsFromLocal();
        if(localRecentSearchKeywords != null){
            recentSearchKeywordList.addAll(localRecentSearchKeywords);
        }

        // Get recent search keyword from service
        getRecentSearchKeywordsFromService(isQuiet);
    }

    @Override
    public void saveRecentSearchKeywordToService(final RecentSearchKeyword keyword) {
        final String sessionKey = iBaseManager.isSign() ? iBaseManager.getUser().getSessionKey():"";
        Subscription subscribe = iCommodityManager.saveRecentSearchKeyword(keyword.getKeyword(), sessionKey)
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
                        // TODO: don't need to process
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void saveRecentSearchKeywordToLocal(RecentSearchKeyword keyword) {

        if(keyword == null) {
            return;
        }

        // local is empty
        List<RecentSearchKeyword> recentSearchKeywords = iBaseManager.getRecentSearchKeywordFromLocal();
        if(recentSearchKeywords == null){
            recentSearchKeywords = new ArrayList<>();
            recentSearchKeywords.add(keyword);
        }
        // insert and remove old data
        else {

            // replace keyword if already exits
            boolean isExits = false;
            for(RecentSearchKeyword recentSearchKeyword : recentSearchKeywords){
                if(recentSearchKeyword.getKeyword().equalsIgnoreCase(keyword.getKeyword())){
                    recentSearchKeyword.setKeyword(keyword.getKeyword());
                    recentSearchKeyword.setTime(keyword.getTime());
                    recentSearchKeyword.setSearchType(keyword.getSearchType());
                    recentSearchKeyword.setCategroyId(keyword.getCategroyId());
                    recentSearchKeyword.setBrandId(keyword.getBrandId());

                    isExits = true;
                    break;
                }
            }

            // add to list if not exits
            if(!isExits) {
                recentSearchKeywords.add(0, keyword);
            }

            // desc sort by datetime
            recentSearchKeywordListDescSortByDateTime(recentSearchKeywords);

            // remove last item when more than LOCAL_RECENT_SEARCH_KEYWORD_LIMIT
            if(recentSearchKeywords.size() > LOCAL_RECENT_SEARCH_KEYWORD_LIMIT){
                recentSearchKeywords.remove(recentSearchKeywords.size() -1);
            }
        }

        iBaseManager.updateRecentSearchKeywordToLocal(recentSearchKeywords);
    }


    @Override
    public void clearAllRecentSearchKeyword() {

        clearAllRecentSearchKeywordFromLocal();
        clearAllRecentSearchKeywordFromService();
    }

    private void clearAllRecentSearchKeywordFromService(){
        final String sessionKey = iBaseManager.isSign() ? iBaseManager.getUser().getSessionKey():"";
        String languageCode = LanguageUtils.getCurrentLanguageCode();
        String storeId = StoreUtils.getStoreIdByLanguageCode(languageCode
                .equalsIgnoreCase(LanguageUtils.LANGUAGE_CODE_AUTO)
                ? LanguageUtils.LANGUAGE_CODE_ENGLISH
                : languageCode);
        Subscription subscribe = iCommodityManager.clearAllRecentSearchKeyword(storeId, sessionKey)
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

                        // Clear recent search view
                        mView.updateRecentSearchView(null);
                    }
                });
    }

    private void clearAllRecentSearchKeywordFromLocal(){
        iBaseManager.updateRecentSearchKeywordToLocal(null);
    }

    private List<RecentSearchKeyword> sortRecentSearchKeyword(List<RecentSearchKeyword> sortRecentSearchKeyword){
        if(sortRecentSearchKeyword == null){
            return null;
        }

        return sortRecentSearchKeyword;
    }

    private String getCurrentDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    private String utcToCurrentDateTime(String timeStamp){
        if(TextUtils.isEmpty(timeStamp))
            return null;

        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date(Long.valueOf(timeStamp + "000")));
        ca.add(Calendar.HOUR_OF_DAY, 8);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(ca.getTime());
    }

    // recent search keyword desc sort by datetime
    private void recentSearchKeywordListDescSortByDateTime(List<RecentSearchKeyword> recentSearchKeywords){
        if(recentSearchKeywords == null){
            return;
        }

        Collections.sort(recentSearchKeywords, new Comparator<RecentSearchKeyword>() {
            @Override
            public int compare(RecentSearchKeyword o1, RecentSearchKeyword o2) {

                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .parse(o1.getTime());

                    date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .parse(o2.getTime());

                } catch (ParseException ex){
                    ex.printStackTrace();
                }

                if(date1.before(date2)){
                    return 1;
                } else if(date1.after(date2)){
                    return -1;
                }

                return 0;
            }
        });
    }

    // recent search keyword with duplicate
    private List<RecentSearchKeyword> recentSearchKeywordListWithDuplicate(List<RecentSearchKeyword> recentSearchKeywords){
        if(recentSearchKeywords == null) {
            return null;
        }

        List<RecentSearchKeyword> srcList = new ArrayList<>();
        List<RecentSearchKeyword> resultList = new ArrayList<>();

        srcList.addAll(recentSearchKeywords);
        for(int i = 0; i < srcList.size(); i++){
            boolean isDuplicate = false;
            RecentSearchKeyword src = srcList.get(i);

            for(int j = 0; j < resultList.size(); j++){
                RecentSearchKeyword result = resultList.get(j);
                if(src.getKeyword().equalsIgnoreCase(result.getKeyword())){
                    isDuplicate = true;
                    break;
                }
            }

            if(!isDuplicate){
                resultList.add(src);
            }
        }

        return resultList;
    }
}
