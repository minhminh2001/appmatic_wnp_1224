package com.common.widget;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by kevin on 2016/7/12.
 */
public class NoUnderLineClickSpan extends ClickableSpan {

    private int color;
    private boolean hasUnderLine = true;
    private boolean bold = false;

    public NoUnderLineClickSpan(int color, boolean hasUnderLine) {
        super();
        this.color = color;
        this.hasUnderLine = hasUnderLine;
    }

    public NoUnderLineClickSpan(int color, boolean hasUnderLine,boolean bold){
        super();
        this.color=color;
        this.hasUnderLine=hasUnderLine;
        this.bold=bold;
    }
        public NoUnderLineClickSpan() {
            super();
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(color);
            if(bold) {//主要去设置加粗时才动用这个属性
                ds.setTypeface(Typeface.DEFAULT_BOLD);
            }
            if(!hasUnderLine) {
                ds.setUnderlineText(false);
            }
        }

        private View.OnClickListener onClickListener;
        public void setOnClickListener(View.OnClickListener onClickListener){
            this.onClickListener=onClickListener;
        }
        @Override
        public void onClick(View widget) {
            if(onClickListener!=null){
                onClickListener.onClick(widget);
            }
        }
    }

