package com.whitelabel.app.widget;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.whitelabel.app.R;
import com.whitelabel.app.callback.ProductListFilterHideCallBack;
import com.whitelabel.app.utils.JDataUtils;

import java.lang.ref.WeakReference;


/**
 * Created by kevin on 2016/10/18.
 */
public class FilterSortBottomView {
    private final int TABBAR_INDEX_NONE = -1;
    //filter
    public boolean needShowFilter = true;
    public boolean showFilter = true;
    private boolean toggleFiltering = false;
    public FilterHandler filterHandler;

    private View bottomBar, iv_bottom_slideto_top;
    private FilterSortBottomViewCallBack filterSortBottomViewCallBack;

    public void initView(View bottomBar, View iv_bottom_slideto_top, FilterSortBottomViewCallBack filterSortBottomViewCallBack) {
        this.bottomBar = bottomBar;
        this.iv_bottom_slideto_top = iv_bottom_slideto_top;
        filterHandler = new FilterHandler(this);
        this.filterSortBottomViewCallBack = filterSortBottomViewCallBack;
    }

    public View getBottomBar() {
        if (bottomBar != null) {
            return bottomBar;
        }
        return null;
    }

    public void setIsShowSwitchFilterBar(boolean show) {
        boolean mShowSwitchFilterBar = show;
        if (bottomBar == null || iv_bottom_slideto_top == null || filterHandler == null) {
            return;
        }
        if (!mShowSwitchFilterBar) {
            filterHandler.removeCallbacksAndMessages(null);
            bottomBar.clearAnimation();
        }
    }


    public void setFilterShow(int type, ProductListFilterHideCallBack productListFilterHideCallBack) {
        if (bottomBar == null || iv_bottom_slideto_top == null || filterHandler == null) {
            return;
        }
        if (type == 0) {
            needShowFilter = true;
            //延迟判断
            if (showFilter == false && needShowFilter == true) {
                filterHandler.sendEmptyMessageDelayed(1, 500);
            }
        } else {
            setFilterStatus(type, productListFilterHideCallBack);
            needShowFilter = false;
        }
    }

    public void hideSwitchAndFilterBar(boolean isHide) {
        if (isHide) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) bottomBar.getLayoutParams();
            if (lp != null) {
                lp.height = 0;
                bottomBar.setLayoutParams(lp);
            }
        } else {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) bottomBar.getLayoutParams();
            if (lp != null) {
                lp.height = JDataUtils.dp2Px(40);
                bottomBar.setLayoutParams(lp);
            }
        }
    }

    public void hideBottomSlideToTop(boolean isHide) {
        if (isHide) {
            if (iv_bottom_slideto_top.getVisibility() != View.INVISIBLE) {
                iv_bottom_slideto_top.setVisibility(View.INVISIBLE);
            }
        } else {
            if (iv_bottom_slideto_top.getVisibility() != View.VISIBLE
                    && TABBAR_INDEX_NONE == filterSortBottomViewCallBack.getCurrentFilterSortTabIndex()) {
                iv_bottom_slideto_top.setVisibility(View.VISIBLE);
            }
        }
    }

    //延迟回调
    private static class FilterHandler extends Handler {
        private final WeakReference<FilterSortBottomView> view;

        public FilterHandler(FilterSortBottomView view) {
            this.view = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            if (view.get() == null) {
                return;
            }
            switch (msg.what) {
                case 1:
                    //show
                    if (view.get().showFilter == false && view.get().needShowFilter == true) {
                        view.get().setFilterStatus(0, null);
                    }
                    break;
            }
            super.handleMessage(msg);
        }

    }

    public void setFilterStatus(int type, final ProductListFilterHideCallBack productListFilterHideCallBack) {
        if (type == 0 && toggleFiltering == false && showFilter != true
                && filterSortBottomViewCallBack.getCurrentFilterSortTabIndex() == TABBAR_INDEX_NONE) {
            //show
            toggleFiltering = true;
            showFilter = true;
            final Animation show = AnimationUtils.loadAnimation(bottomBar.getContext(), R.anim.anim_filter_fade_in);
            bottomBar.clearAnimation();
            show.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    hideSwitchAndFilterBar(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    toggleFiltering = false;
                }
            });
            bottomBar.startAnimation(show);
        } else if (type != 0 && toggleFiltering == false && showFilter != false
                && filterSortBottomViewCallBack.getCurrentFilterSortTabIndex() == TABBAR_INDEX_NONE) {
            //hide
            toggleFiltering = true;
            showFilter = false;
            if (productListFilterHideCallBack != null) {
                productListFilterHideCallBack.callBack();
            }
            final Animation show = AnimationUtils.loadAnimation(bottomBar.getContext(), R.anim.anim_filter_fade_out);
            bottomBar.clearAnimation();
            show.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    toggleFiltering = false;
                    hideSwitchAndFilterBar(true);
                }
            });
            bottomBar.startAnimation(show);
        }
    }

    public interface FilterSortBottomViewCallBack {
        int getCurrentFilterSortTabIndex();
    }
}
