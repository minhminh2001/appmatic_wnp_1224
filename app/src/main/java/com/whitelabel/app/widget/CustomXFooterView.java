/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.whitelabel.app.widget;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.application.WhiteLabelApplication;


public class CustomXFooterView extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;
    View line;
    private View mContentView;
    private ProgressBar mProgressBar;
    private TextView mHintView;
    private boolean isGoneLine;

    public CustomXFooterView(Context context) {
        super(context);
        initView(context);
    }

    public CustomXFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setState(int state) {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        //mHintView.setVisibility(View.INVISIBLE);
        if (state == STATE_READY) {
/*<<<<<<< HEAD
            mHintView.setVisibility(View.GONE);
            //mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(R.string.customxlistview_footer_hint_ready);
        } else if (state == STATE_LOADING) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mHintView.setVisibility(View.GONE);
            //mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(R.string.customxlistview_footer_hint_normal);
=======*/
            mHintView.setVisibility(View.VISIBLE);
            //mHintView.setText(R.string.customxlistview_footer_hint_ready);
            mHintView.setText("");//delete "load more"
        } else if (state == STATE_LOADING) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mHintView.setVisibility(View.VISIBLE);
            //mHintView.setText(R.string.customxlistview_footer_hint_normal);
            mHintView.setText("");//delete "load more"
        }
    }

    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mContentView
                .getLayoutParams();
        return lp.bottomMargin;
    }

    public void setBottomMargin(int height) {
        if (height < 0)
            return;
        LayoutParams lp = (LayoutParams) mContentView
                .getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }

    /**
     * normal status
     */
    public void normal() {
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * loading status
     */
    public void loading() {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideLine() {
        isGoneLine = true;
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mContentView
                .getLayoutParams();
        if (!isGoneLine) {
//			line.setVisibility(View.VISIBLE);
        }
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    public boolean isHide() {
        if (line.getVisibility() == View.GONE) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mContentView
                .getLayoutParams();
        line.setVisibility(View.GONE);
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }

    private void initView(Context context) {
        Context mContext = context;
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.layout_customxlistview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        line = moreView.findViewById(R.id.line);
        mContentView = moreView.findViewById(R.id.xlistview_footer_content);
        mProgressBar = (ProgressBar) moreView.findViewById(R.id.xlistview_footer_progressbar);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {

            mProgressBar.setIndeterminateTintMode(PorterDuff.Mode.SRC_ATOP);
            mProgressBar.setIndeterminateTintList(ColorStateList.
                    valueOf(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor()));
//            mProgressBar.getIndeterminateDrawable().setColorFilter(
//                    WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor(), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        mHintView = (TextView) moreView
                .findViewById(R.id.xlistview_footer_hint_textview);
    }

}
