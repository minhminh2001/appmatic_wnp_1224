package com.whitelabel.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by ray on 2015/8/20.
 */
public class CustomFrame extends FrameLayout {

    private  CanvasTransformer mTransformer;
    private float mpercentOpen;

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        if (mTransformer != null)
            invalidate();
    }

    public void setCanvasTransformer(CanvasTransformer transformer){
        this.mTransformer=transformer;
    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mTransformer != null) {
            canvas.save();
            mTransformer.transformCanvas(canvas, mpercentOpen);
            super.dispatchDraw(canvas);
            canvas.restore();
        } else
            super.dispatchDraw(canvas);
    }

   public void resetPercentOpen(){
       this.mpercentOpen=0;
       if(mTransformer!=null){
           invalidate();
       }

   }


    public void setPercentOpen(float percentOpen){
        this.mpercentOpen=percentOpen;
        if(mTransformer!=null){
            invalidate();
        }
    }



    public interface CanvasTransformer {

        /**
         * Transform canvas.
         *
         * @param canvas the canvas
         *
         */
        public void transformCanvas(Canvas canvas, float percentOpen);
    }


    public CustomFrame(Context context){
        super(context);
    }

    public CustomFrame(Context context,AttributeSet set){
        super(context,set);
    }

}
