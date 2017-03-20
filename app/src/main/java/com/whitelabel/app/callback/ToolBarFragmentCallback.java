package com.whitelabel.app.callback;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by imaginato on 2016/3/15.
 */
public interface ToolBarFragmentCallback {
    void setToolBarLeftIconAndListenter(Drawable drawable, View.OnClickListener onClickListenter);
    void setToolBarTitle(String title);
    void showToolBar(Boolean show);
    void updateRightIconNum(int itemId, long number);
    void setToolBarColor(int colorId);
}
