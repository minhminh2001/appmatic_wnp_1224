package com.whitelabel.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.whitelabel.app.utils.JToolUtils;


/**
 * Created by tan6458 on 2016/4/16.
 */
public class RecycleViewDivider extends RecyclerView.ItemDecoration {
    private Paint paint;
    private int dividerHeight = 1;

    public RecycleViewDivider(Context context, int dividerHeight) {
        paint = new Paint();
        //paint.setColor(context.getResources().getColor(R.color.gray_divide));
        this.dividerHeight = dividerHeight;
    }
    public RecycleViewDivider(Context context, int dividerHeight,int colorId) {
        paint = new Paint();
        paint.setColor(JToolUtils.getColor(colorId));
        this.dividerHeight = dividerHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, dividerHeight);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + dividerHeight;
            if (paint != null) {
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }
    }
}