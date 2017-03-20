package com.whitelabel.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by kevin on 2016/1/27.
 */
public class CustomVideoView extends VideoView {

    private int mVideoWidth=1080;
    private int mVideoHeight=1920;

    public CustomVideoView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        // this code can change video height and width by param

        int width=getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height=getDefaultSize(mVideoHeight, heightMeasureSpec);

        if (mVideoWidth > 0 && mVideoHeight > 0) {
            if((widthMeasureSpec*mVideoHeight)>(heightMeasureSpec*mVideoWidth)){
                width=widthMeasureSpec;
                height=mVideoHeight*widthMeasureSpec/mVideoWidth;
            }else{
                width=mVideoWidth*heightMeasureSpec/mVideoHeight;
                height=heightMeasureSpec;
            }
        }
        setMeasuredDimension(width, height);
    }
}
