package com.whitelabel.app.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.whitelabel.app.callback.ZoomImageViewCallBack;

/**
 * 加载完图片后初始化
 *  onLoadingComplete(){
 *    img.setBitmapWH(640, 640 * 240 / 490,  screenWidth);
      img.init();
 }

 * itemHolder.img.setZoomImageContainer(this);(只是简单实现isJumping的getter setter方式)
 *
 * itemHolder.img.setZoomImageCallBack(new ...)
 *
 * 将img的touch事件和itemview绑定setOnTouch(event);
 */
public class ZoomInImageView extends ImageView{

    private Drawable mDrawable;
    private int mDrawableW;
    private int mDrawableH;
    private Matrix mMatrix;
    private float[] mValues = new float[9];
    private int mImageViewW;
    private int mImageViewH;

    private float narrowScaleSpan=0.9f;
    private float scaleSpan=1.1f;
    public int duration=260;

    /*
    setScaleSpan  和 setNarrowScaleSpan 来设置缩放比例
     */
    public void setScaleSpan(float scaleSpan) {
        this.scaleSpan = scaleSpan;
    }
    public void setNarrowScaleSpan(float narrowScaleSpan) {
        this.narrowScaleSpan = narrowScaleSpan;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * 根据图片大小以及imageview的大小，初始化图片的显示位置
     * 图片加载完毕后需  调用此方法来确保图片位于 imageview 中央
     */
    public void init(){
        initInternalValues();
        initPicturePosition();
    }

    private void initPicturePosition(){
        updateMatrixValuesOrigin(mMatrix, mValues, mDrawableW, mDrawableH, mImageViewW, mImageViewH);
        setImageMatrix(mMatrix);
    }
    //初始化位置
    private void updateMatrixValuesOrigin(Matrix outMatrix, float[] outValues, float drawW, float drawH, float imageW, float imageH){

        if(outMatrix == null || outValues == null){
            throw new IllegalArgumentException("please set source of this ImageView's matrix and values");
        }

        outMatrix.reset();

        if((imageH * drawW > drawH * imageW)){
            //如果iv更细高
            float scale1 = ((float)imageH)/((float)drawH);
            float offset1 = (drawW * scale1 - (float)imageW)/2;

            outMatrix.postScale(scale1, scale1);
            outMatrix.postTranslate(-offset1, 0);

        }else{
            //如果iv更宽扁
            float scale2 = ((float)imageW)/((float)drawW);
            float offset2 = (drawH * scale2 - (float)imageH)/2;

            outMatrix.postScale(scale2, scale2);
            outMatrix.postTranslate(0, -offset2);
        }
        outMatrix.getValues(outValues);
    }
    //无法自动获取宽高时可按比例手动获取
    public void setBitmapWH(int w,int h,int phoneW){
        Float a=(float)w/(float)h;
        mImageViewW=phoneW;
        mImageViewH=(int)(phoneW/a);
    }

    private void initInternalValues(){
        mDrawable = getDrawable();

        if(mDrawable == null){
            throw new IllegalArgumentException("please set source of this ImageView");
        }

        mDrawableW = mDrawable.getIntrinsicWidth();
        mDrawableH = mDrawable.getIntrinsicHeight();
        int measuredWidth=getMeasuredWidth();
        int measuredHeight=getMeasuredHeight();
        if(measuredWidth>0) {
            mImageViewW =measuredWidth;
        }
        if(measuredHeight>0) {
            mImageViewH = getMeasuredHeight();
        }


        mMatrix = getImageMatrix();
        mMatrix.getValues(mValues);
    }


    private ZoomImageCallBack zoomImageCallBack;
    public void setZoomImageCallBack(ZoomImageCallBack zoomImageCallBack){
        this.zoomImageCallBack=zoomImageCallBack;
    }
    public interface ZoomImageCallBack{
        //开始放大
        void largeBeginCallBack();

        //放大结束
        void largeEndCallBack();
        //开始缩小
        void narrowBeginCallBack();
        //缩小结束
        void narrowEndCallBack();
        //点击事件
        void onClick();
    }
    private ZoomImageContainer zoomImageContainer;
    public void setZoomImageContainer(ZoomImageContainer  zoomImageContainer){
        this.zoomImageContainer=zoomImageContainer;
    }

    public boolean setOnTouch( MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(onTouchBegin||zoomImageContainer.isJumping()){
                    return false;
                }

                onTouchBegin=true;
                wait(new ZoomImageViewCallBack() {
                    @Override
                    public void imageAniCallBack() {
                        //开始放大效果
                        if (onTouchBegin) {
                            if (zoomImageContainer.isJumping()) {
                                onTouchBegin = false;
                                return;
                            }
                            onTouching = true;
                            zoomImageContainer.setJumping(true);
                            //点击放大image（唯一一处是放大的代码），
                            zoomImageCallBack.largeBeginCallBack();
                            enlarge(new ZoomImageViewCallBack() {
                                @Override
                                public void imageAniCallBack() {
                                    onTouchBegin = false;
                                    zoomImageCallBack.largeEndCallBack();
                                    if (needOnClick) {
                                        onTouching = false;
                                        needOnClick = false;

                                        zoomImageCallBack.onClick();
                                    }
                                    if (needOnTouchUp) {
                                        onTouching = false;
                                        needOnTouchUp = false;
                                        //如果此imag正在切换，说明touch down 和onCLick是连接触发的，需要等待一会再缩小
                                        if (taggling) {
                                            setAnimAndColorWithNarrow(true);
                                        } else {
                                            setAnimAndColorWithNarrow(true);
                                        }
                                    }
                                }
                            });
                        }
                    }
                }, 130);
                break;
            case MotionEvent.ACTION_CANCEL:
                //手指滑动的话onTouchBegin=false 则取消 ACTION_DOWN 得效果
                onTouchBegin=false;
                //isJumping是false标示只是滑动，不触发缩放效果
                if(!zoomImageContainer.isJumping()){
                    return false;
                }
                //当点击后并且手指移动了，如果当前正在放大，延迟后缩小image，否则直接缩小image.
                if(!onTouching){
                    return false;
                }
                onTouching=false;
                //如果此imag正在切换，说明touch down 和onCLick是连接触发的，需要等待一会再缩小
                if(taggling) {
                    setAnimAndColorWithNarrow(true);
                }else{
                    setAnimAndColorWithNarrow(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(onTouchBegin){
                    needOnTouchUp=true;
                    return false;
                }
                //如果此imag正在切换，说明touch down 和onCLick是连接触发的，需要等待一会再缩小
                if(taggling) {
                    setAnimAndColorWithNarrow(true);
                }else{
                    setAnimAndColorWithNarrow(true);
                }
        }
        return false;
    }

    //缩小图片流程
    private void setAnimAndColorWithNarrow(final boolean useWaitTime){
        //是否需要延迟n秒后再缩小
        int waitTime =-1;
        if(useWaitTime) {
            waitTime=duration+50;
        }
        if(!useWaitTime) {
            zoomImageCallBack.narrowBeginCallBack();
        }
        //等 n 秒后，直接缩小，否则会卡，和放大发生冲突
        waitNarrow(new ZoomImageViewCallBack() {
            @Override
            public void imageAniCallBack() {
                //wait时间结束后，进行缩小，缩小完毕后将isJumping状态置为false
                narrow(new ZoomImageViewCallBack() {
                    @Override
                    public void imageAniCallBack() {
                        zoomImageContainer.setJumping(false);
                        zoomImageCallBack.narrowEndCallBack();
                    }
                });
                if (useWaitTime) {
                    zoomImageCallBack.narrowBeginCallBack();
                }
            }
        },  waitTime);
    }








    //立马缩小
    public void narrowNow(){
        if(type!=1){
            return;
        }
        type=0;
        //设置大小
        Matrix ma = getImageMatrix();
        float[] dValues = new float[9];
        ma.getValues(dValues);

        dValues[0] = dValues[0] -0.1f;
        dValues[4] = dValues[4] -0.1f;
        // 重新调整位置
        float offsetheight = (mDrawableH *dValues[0] - (float)mImageViewH )/2;
        dValues[5] = - offsetheight;
        float offsetwidth = (mDrawableW *dValues[0] - (float)mImageViewW )/2;
        dValues[2] = - offsetwidth;
        Matrix m = new Matrix();
        m.setValues(dValues);
        setImageMatrix(m);
    }
    public void wait(final ZoomImageViewCallBack zoomImageViewCallBackn,final int waitTime){
        ValueAnimator va = ValueAnimator.ofFloat(narrowScaleSpan, 1f);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float span = (Float) animation.getAnimatedValue();
                if (span >= 1f) {
                    zoomImageViewCallBackn.imageAniCallBack();
                }
            }
        });
        va.setDuration(waitTime);
        va.start();
    }
    //等候 n 秒后回调
    public void waitNarrow(final ZoomImageViewCallBack zoomImageViewCallBackn,final int waitTime){
        //object and position 有可能为 null 和0
        if(type!=1){
            //zoomImageViewCallBackn.imageAniCallBack(o,position);
            return;
        }
        ValueAnimator va = ValueAnimator.ofFloat(narrowScaleSpan, 1f);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float span = (Float) animation.getAnimatedValue();
                if (span >= 1f) {
                    zoomImageViewCallBackn.imageAniCallBack();
                }
            }
        });
        //动画时间
        if(waitTime>=0) {
            va.setDuration(waitTime);
        }else{
            va.setDuration(duration-70);
        }
        va.start();
    }
    public boolean needOnClick=false ;//onClick后但动画未执行完，需要在callBack里执行
    public boolean taggling=false;
    public int type=0;  //0为正常的，1为放大的
    public boolean onTouching=false;  //按下状态
    public boolean onTouchBegin=false;  ///onTouchDown 后直接onClick，需要在touchDown wait 方法的callBack里执行
    public boolean needOnTouchUp=false;  ///onTouchUp 后才执行的onTouchDown动画效果，需要在touchDown wait 方法的callBack里补上
    //缩小
    public void narrow(final ZoomImageViewCallBack zoomImageViewCallBack2){
        if(type!=1||taggling){
            return;
        }
        type=0;
        // 如从0.8开始，span=0.9  ， 0.9-0.8=0.1 ，lastChangeValue=0.8，  matrix+  0-0.1
        ValueAnimator va = ValueAnimator.ofFloat(narrowScaleSpan, 1f);
        lastChangeValue = narrowScaleSpan;
        taggling=true;
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                Float span = (Float) animation.getAnimatedValue();
                if (span >= 1f) {
                    taggling = false;
                    zoomImageViewCallBack2.imageAniCallBack();
                    animation.cancel();
                }
                Float spanP = span - lastChangeValue;
                lastChangeValue = span;
                float spanPP = 0 - spanP;
                startZoom(spanPP);
            }
        });
        //动画时间
        va.setDuration(duration);
        va.start();
    }
    //放大
    public void enlarge(final ZoomImageViewCallBack zoomImageViewCallBack){
        if(type==1||taggling){
            //不需缩放，直接回调
            zoomImageViewCallBack.imageAniCallBack();
            return;
        }
        type=1;
        ValueAnimator va = ValueAnimator.ofFloat(1, scaleSpan);
        lastChangeValue=1f;
        taggling=true;
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float span = (Float) animation.getAnimatedValue();
                if (span >= scaleSpan) {
                    taggling = false;
                    zoomImageViewCallBack.imageAniCallBack();
                    animation.cancel();
                }
                Float spanP = span - lastChangeValue;
                lastChangeValue = span;
                startZoom(spanP);
            }

        });
        //动画时间
        va.setDuration(duration);
        va.start();
    }
    Float lastChangeValue=0.0f;
    public void startZoom(float span){
        //设置大小
        Matrix ma = getImageMatrix();
        float[] dValues = new float[9];
        ma.getValues(dValues);
        dValues[0] = dValues[0] + span;
        dValues[4] = dValues[4] + span;
        // 重新调整位置
        float offsetheight = (mDrawableH *dValues[0] - (float)mImageViewH )/2;
        dValues[5] = - offsetheight;
        float offsetwidth = (mDrawableW *dValues[0] - (float)mImageViewW )/2;
        if(offsetwidth<5&&type==0){
            //特殊处理，加载前点击放大
            //it0 是15 it1是30   it2 是-15  it3 -30到1  it4,5  1.7到1.6
            return;
        }
        dValues[2] = - offsetwidth;
        Matrix m = new Matrix();
        m.setValues(dValues);
        setImageMatrix(m);
    }





    public ZoomInImageView(Context context) {
        this(context, null);
    }

    public ZoomInImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.setScaleType(ScaleType.MATRIX);

    }

    public ZoomInImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setScaleType(ScaleType.MATRIX);
    }
}