package com.whitelabel.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.listener.OnMyAccountUserGuide;
import com.whitelabel.app.utils.JLogUtils;

import java.util.List;
import java.util.Locale;

public class MyAccountTopMenuView extends HorizontalScrollView {
    // @formatter:off
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };
    private String currTag="CustomTabCustomPageIndicator";

    // @formatter:on
    public OnPageChangeListener delegatePageListener;
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;
    private LinearLayout tabsContainer;
    private ViewPager pager;
    private int tabCount;
    private int currentPosition = 0;
    private float currentPositionOffset = 0f;
    private Paint rectPaint;
    private Paint dividerPaint;
    private int indicatorColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private int dividerColor = 0x1A000000;
    private boolean shouldExpand = false;
    private boolean textAllCaps = true;
    private int scrollOffset = 52;
    private int indicatorHeight = 8;
    private int underlineHeight = 1;
    private int dividerPadding = 12;
    private int tabPadding = 15;
    private int dividerWidth = 1;
    private int tabTextSize = 12;
    private int tabTextColor = Color.BLACK;
    private Typeface tabTypeface = Typeface.SANS_SERIF;
    private int tabTypefaceStyle = Typeface.NORMAL;
    private int lastScrollX = 0;
    private List<String> mTitle;
    private int tabBackgroundResId = R.drawable.background_tab;
    private Locale locale;
    private OnMyAccountUserGuide onMyAccountUserGuide;
    public MyAccountTopMenuView(Context context) {
        this(context, null);
    }

    public MyAccountTopMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    @SuppressWarnings("ResourceType")
    public MyAccountTopMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFillViewport(true);
        setWillNotDraw(false);
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);

        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
        dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, dividerPadding);
        tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, tabBackgroundResId);
        shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }
    public void setOnMyAccountUserGuide(OnMyAccountUserGuide myAccountUserGuide){
        this.onMyAccountUserGuide=myAccountUserGuide;
    }
    public void setCurrentPosition(int index){
        notifyDataSetChanged(index);
    }
    public void setOldPosition(int position){
        this.mOldIndex=position;
    }
    public void setTitles(List<String> titles){
        this.mTitle=titles;
        tabCount = titles.size();
        for(int i=0;i<tabCount;i++){
            addTextTab(i,mTitle.get(i));
        }
        updateTabStyles();
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                scrollToChild(currentPosition, 0);
            }
        });
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }
    private void addTextTab(final int position, String title) {
        CustomTextView tab = new CustomTextView(getContext());
        tab.setText(title);
        tab.setTag(position);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
//        tab.setTypeface(tabTypeface, tabTypefaceStyle);
//        Typeface typeFace = JFontUtils.getTypeface(getContext(), 28);
//        if (typeFace != null) {
//            tab.setTypeface(typeFace);
//        }
//        CustomTextView tab=new CustomTextView(getContext(),null);
//        tab.setText(title);
//        tab.setGravity(Gravity.CENTER);
//        tab.setSingleLine();

        addTab(position, tab);
    }

    private int mOldIndex;
    private CustomAnim  mAnim;
    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEnabled()) {
                    notifyDataSetChanged(position);
                }
            }
        });
        tab.setPadding(tabPadding, 0, tabPadding, 0);
        tabsContainer.addView(tab, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
    }

    public void setSelectTextColor(int position){
        try {
            for (int i = 0; i < tabCount; i++) {
                View view = tabsContainer.getChildAt(i);
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    if (((Integer) view.getTag()) == position) {
                        textView.setTextColor(getContext().getResources().getColor(R.color.purple66006E));
                    } else {
                        textView.setTextColor(getContext().getResources().getColor(R.color.black000000));
                    }
                }
            }
        }catch (Exception ex){
            ex.getStackTrace();
        }
    }

    public void notifyDataSetChanged(int position) {
        if(currentPosition!=position) {
            MyAccountTopMenuView.this.clearAnimation();
            if (mAnim == null) {
                mAnim = new CustomAnim();
            }
            if (position > currentPosition) {
                mAnim.setType(CustomAnim.ADVANCE);
            } else {
                mAnim.setType(CustomAnim.BACK);
            }
            setSelectTextColor(position);
            currentPosition = position;
            JLogUtils.i(currTag,"position:"+position+",currentPosition"+currentPosition+",mOldIndex:"+mOldIndex);
            mAnim.setPosition(mOldIndex);
            MyAccountTopMenuView.this.startAnimation(mAnim);
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }
    }

    class CustomAnim extends Animation implements Animation.AnimationListener{
        private int mType;
        public static final int ADVANCE=100;
        public static final int BACK=200;
        private int mOldIndex;
        public CustomAnim(){
            setDuration(250);
            setFillAfter(true);
            setInterpolator(new LinearInterpolator());
            setAnimationListener(this);
        }
        public void setType(int type){
            mType=type;
        }
        public void setPosition(int position){
            mOldIndex=position;
        }
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            currentPositionOffset=interpolatedTime;
                if(mType==ADVANCE) {
                    scrollToChild(currentPosition, (int) (interpolatedTime * tabsContainer.getChildAt(currentPosition).getWidth()));
                }else if(mType==BACK){
                    scrollToChild(currentPosition, (int) ((1-interpolatedTime) * tabsContainer.getChildAt(mOldIndex).getWidth()));
                }
                invalidate();
            super.applyTransformation(interpolatedTime, t);
        }
        @Override
        public void onAnimationStart(Animation animation) {

        }
        @Override
        public void onAnimationEnd(Animation animation) {
            MyAccountTopMenuView.this.mOldIndex=currentPosition;
        }
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private void updateTabStyles() {
        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);
            v.setBackgroundResource(tabBackgroundResId);
            if (v instanceof TextView) {
                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                 tab.setTypeface(tabTypeface, tabTypefaceStyle);
                tab.setTextColor(tabTextColor);
//                Typeface typeFace = JFontUtils.getTypeface(getContext(), 34);
//                if (typeFace != null) {
//                    tab.setTypeface(typeFace);
//                } else {
//                    tab.setTypeface(tabTypeface, tabTypefaceStyle);
//                }
                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                // pre-ICS-build
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab.setAllCaps(true);
                    } else {
                        tab.setText(tab.getText().toString().toUpperCase(locale));
                    }
                }
            }
        }
    }
    private void scrollToChild(int position, int offset) {
        if (tabCount == 0) {
            return;
        }
        int newScrollX = tabsContainer.getChildAt(position).getLeft() ;
        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || tabCount == 0) {
            return;
        }
        final int height = getHeight();
        rectPaint.setColor(indicatorColor);
        View currentTab = tabsContainer.getChildAt(mOldIndex);

        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();
        JLogUtils.i(currTag,"mOldIndex:"+mOldIndex+",lineLeft:"+lineLeft+",lineRight:"+lineRight+",currentPosition:"+currentPosition);
        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount) {
            View nextTab = tabsContainer.getChildAt(currentPosition);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();
            lineLeft = (lineLeft+ currentPositionOffset * (nextTabLeft-lineLeft));
            lineRight = (lineRight + currentPositionOffset * (nextTabRight-lineRight));
            JLogUtils.i(currTag,"NewlineLeft:"+lineLeft+",newlineRight:"+lineRight+",currentPositionOffset:"+currentPositionOffset);
        }
        canvas.drawRect(lineLeft, height-indicatorHeight, lineRight, height, rectPaint);

        // draw underline
        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

        // draw divider
        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

//    public void setTabBackground(int resId) {
//        this.tabBackgroundResId = resId;
//    }
//
//    public int getTabPaddingLeftRight() {
//        return tabPadding;
//    }
//
//    public void setTabPaddingLeftRight(int paddingPx) {
//        this.tabPadding = paddingPx;
//        updateTabStyles();
//    }
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
//        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    public interface IconTabProvider {
        public int getPageIconResId(int position);
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }
    }
}
