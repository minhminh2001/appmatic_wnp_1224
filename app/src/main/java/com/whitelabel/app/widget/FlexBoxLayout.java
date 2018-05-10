package com.whitelabel.app.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.UNSPECIFIED;

public class FlexBoxLayout extends RadioGroup {

    private static final int DEFAULT_ITEM_COLUME_SPACE = 20;
    private static final int DEFAULT_ITEM_ROW_SPACE = 20;
    private static final int DEFAULT_SHOW_ROW_NUM = 2;

    private List<Rect> childPosition = new ArrayList<>();
    private int defaultShowRowNum = DEFAULT_SHOW_ROW_NUM;
    private boolean isShowAll = false;
    private int rowCount;

    public FlexBoxLayout(Context context) {
        this(context, null);
    }

    public FlexBoxLayout(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void update(){
        invalidate();
    }

    public void setDefaultShowRowNum(int rowNum){
        defaultShowRowNum = rowNum;
    }

    public void showAll(boolean isShowAll){
        this.isShowAll = isShowAll;
        requestLayout();
    }

    public boolean isShowAll(){
        return isShowAll;
    }

    public int getRowCount(){
        return rowCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int left = 0;
        int top = 0;
        int rowHeight = 0;

        childPosition.clear();
        int childCount = getChildCount();
        for(int childIndex = 0; childIndex < childCount; childIndex++ ){
            View childView = getChildAt(childIndex);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            if(childIndex <= 0){
                rowCount = 1;
                top = DEFAULT_ITEM_ROW_SPACE;
                rowHeight = childView.getMeasuredHeight();
            }

            int tempWidth = left + childView.getMeasuredWidth() + DEFAULT_ITEM_COLUME_SPACE;
            if(tempWidth > getMeasuredWidth()){
                left = 0;
                top += childView.getMeasuredHeight() + DEFAULT_ITEM_ROW_SPACE;
                rowCount++;
            }

            Rect rect = new Rect();
            rect.left = left;
            rect.top = top;
            rect.right = left + childView.getMeasuredWidth();
            rect.bottom = top + childView.getMeasuredHeight();
            childPosition.add(rect);

            left += childView.getMeasuredWidth() + DEFAULT_ITEM_COLUME_SPACE;

        }

        // layout height is all child height
        if(isShowAll()) {
            heightSize = (rowHeight + DEFAULT_ITEM_ROW_SPACE) * rowCount;
        } else {
            heightSize = (rowHeight + DEFAULT_ITEM_ROW_SPACE ) * (rowCount > defaultShowRowNum ? defaultShowRowNum:rowCount);
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();
        for(int childIndex = 0; childIndex < childCount; childIndex++ ) {
            View childView = getChildAt(childIndex);
            Rect childPos = childPosition.get(childIndex);
            childView.layout(childPos.left, childPos.top, childPos.right, childPos.bottom);
        }
    }

}
