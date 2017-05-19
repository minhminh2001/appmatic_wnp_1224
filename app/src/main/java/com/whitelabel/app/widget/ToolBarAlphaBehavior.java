package com.whitelabel.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.Toolbar;

import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;

/**
 * toolBar透明值渐变处理类
 */
public class ToolBarAlphaBehavior {
    private static final String TAG = "ToolbarAlphaBehavior";
    private int offset = 0;
    private int startOffset = 0;
    private int endOffset = 0;

    public static int minAlpha = 50;//最小透明度
    private int maxAlpha = 255;//最大透明度
    private int maxY = 361;//透明度变化结束的位置

    private Toolbar toolbar;
    private Context context;
    private String defaultColor;
    private CallBack callBack;
    public ToolBarAlphaBehavior(Context context, Toolbar toolbar, String defaultColor, CallBack callBack) {
        this.context = context;
        this.toolbar=toolbar;
        this.defaultColor=defaultColor;
        this.callBack=callBack;
        //  渐变色最大差距值变化滑动距离
        startOffset= toolbar.getHeight();//开始变色的位置
        endOffset = JToolUtils.dip2px(context,maxY)- toolbar.getHeight();//结束变色的位置
    }





    public ToolBarAlphaBehavior(Context context, Toolbar toolbar, int defaultColor, CallBack callBack) {
        this.context = context;
        this.toolbar=toolbar;
        String colorStr=String.format("#%06X", 0xFFFFFF & defaultColor);
        this.defaultColor=colorStr.substring(1);
        this.callBack=callBack;
        //  渐变色最大差距值变化滑动距离
        startOffset= toolbar.getHeight();//开始变色的位置
        endOffset = JToolUtils.dip2px(context,maxY)- toolbar.getHeight();//结束变色的位置
    }

    //.getBackground().setAlpha方法會改變app所有toolbar的顏色，bug
    public void setAlPha(int num){
        if(num>maxAlpha){
            num=maxAlpha;
        }
        if(num<minAlpha){
            num=minAlpha;
        }
        String topAl=Integer.toHexString(num);
        if(topAl.length()==1){
            topAl="0"+topAl;
        }


        //计算底部的颜色值
        int bottomAlphaNum;
        if(num>=maxAlpha-minAlpha){
            bottomAlphaNum=num-minAlpha+(num-(maxAlpha-minAlpha));
        }else{
            bottomAlphaNum=num-minAlpha;
        }
        String bottomAlpha;
        if(bottomAlphaNum!=0) {
            bottomAlpha= Integer.toHexString(bottomAlphaNum);
            if (bottomAlpha.length() == 1) {
                bottomAlpha = "0" + bottomAlpha;
            }
        }else{
            bottomAlpha= "00";
        }


        int colors[] = { Color.parseColor("#" + topAl + defaultColor) , Color.parseColor("#" + bottomAlpha + defaultColor) };
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        toolbar.setBackgroundDrawable(g);

        JLogUtils.d(TAG,"#" + topAl + defaultColor);
        callBack.callBack(Color.parseColor("#"+topAl + defaultColor));
    }


    public void onNestedScroll(int y) {
        offset =y;
        if (offset <= startOffset) {  //alpha为0
            setAlPha(minAlpha);
            //toolbar.getBackground().setAlpha(minAlpha);
        } else if (offset > startOffset && offset < endOffset) { //alpha为0到235
            float precent = (float) (offset - startOffset) / (float)(endOffset-startOffset);
            int alpha = Math.round(precent * (maxAlpha-minAlpha));
            setAlPha(alpha + minAlpha);
            //toolbar.getBackground().setAlpha(alpha+minAlpha);
        } else if (offset >= endOffset) {  //alpha为235
            setAlPha(maxAlpha);
            //toolbar.getBackground().setAlpha(maxAlpha);
        }
    }

    public interface CallBack{
        void callBack(int color);
    }
}
