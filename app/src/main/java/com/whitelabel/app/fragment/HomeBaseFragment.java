package com.whitelabel.app.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.whitelabel.app.ui.BasePresenter;

/**
 * Created by Administrator on 2016/7/7.
 */
public class HomeBaseFragment<T extends BasePresenter> extends BaseFragmentSearchCart<T> {
    protected HomeCommonCallback mCommonCallback;
    protected String TAG = "HomeBaseFragment";
    //todo remove this and in base activity somewhere else
    protected static final int REQUEST_SHOPPINGCART = 10001;
    protected static final int REQUEST_SEARCH = 10002;
    private boolean scrollToolBarEnable = false;
    public interface HomeCommonCallback {
        int MENU_HOME = 1;
        int MENU_CATEGORYTREE = 12;
        int MENU_SHOPPINGCART = 2;
        int MENU_NOTIFICATION = 3;
        int MENU_WISHLIST = 4;
        int MENU_ORDER = 5;
        int MENU_ADDRESS = 6;
        int MENU_STORECREDITS = 7;
        int MENU_COSTOMSERVICE = 8;
        int MENU_HELPCENTER = 9;
        int MENU_SETTING = 10;
        int MENU_SHIPPING = 11;
        void setTitle(String titleText);
        void setTitleNum(int num);
        void switchMenu(int type);
        void updateRightIconNum(int itemId, long number);
        void setLeftMenuIcon(Drawable drawable);

        Toolbar getToolBar();

        //将左菜单键复原,
        void resetMenuAndListenter();

        void setLeftMenuClickListener(View.OnClickListener onClickListener);

        void setHomeSearchBarAndOnClick(View.OnClickListener onClickListener);

        void setFragmentPaddingBottom(boolean hasPaddingBottom);

        void setCoordinatorLayoutSwitch(boolean toolbarSwitchScroll);

        //iaml
//        void showMarketLayers();

//        ImageView getIvMarketLayer();
//
//        RelativeLayout getRlMarketLayer();

//        void closeMarketLayers();

//        void marketLayerClose();

//        void initMarketingLayers(MarketingLayersEntity entity);

    }

    public enum UserGuideType {
        LEFTMENU, HOMELEFTICON, HOMESECONDPAGE, ADDRESS
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeCommonCallback) {
            mCommonCallback = (HomeCommonCallback) context;
        }
    }



    public void setScrollToolBarEnable(boolean scrollToolBarEnable) {
        //可滑动toolbar的,需要手动在onActivityCreated前调一下,改变当前 (父)fragment 的paddingBottom
        //默认是不可滑动,有paddingBottom
        this.scrollToolBarEnable = scrollToolBarEnable;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mCommonCallback != null) {
            mCommonCallback.setFragmentPaddingBottom(!scrollToolBarEnable);
            mCommonCallback.setCoordinatorLayoutSwitch(scrollToolBarEnable);
        }
    }
}
