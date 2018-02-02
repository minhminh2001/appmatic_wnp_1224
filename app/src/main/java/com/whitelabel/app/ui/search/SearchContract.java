package com.whitelabel.app.ui.search;

import com.whitelabel.app.model.SVRAppserviceProductSearchReturnEntity;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

import java.util.Map;

/**
 * Created by img on 2018/1/26.
 */

public class SearchContract {
    public interface View extends BaseView{
        public void showErrorMsg(String errorMsg);
        public void loadAutoHintSearchData(SVRAppserviceProductSearchReturnEntity svrAppserviceProductSearchReturnEntity);
    }

    public interface Presenter extends BasePresenter<View>{
        public void  autoSearch(Map<String,String> params);
        public Map<String,String> transformSearchMap(String storeId, String p, String limit, String order, String dir,
            String brand, String categoryId, String modelType, String q,String keywords, String price, String key,String fromPage);
    }
}
