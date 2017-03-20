package com.whitelabel.app.listener;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by imaginato on 2015/8/11.
 */
public interface OnFilterSortFragmentListener {
    void onCancelClick(View view);
    void onFilterSortListItemClick(int type, Object object);
    void onAnimationFinished(Fragment fragment);
}
