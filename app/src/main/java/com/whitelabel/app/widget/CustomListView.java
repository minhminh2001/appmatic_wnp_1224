package com.whitelabel.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class CustomListView extends ListView{
    private float orginX;
    private float orginY;
    private float curX;
    private float curY;
    private boolean delete = false;
    private int itemHeight =50;
    private int ratio = 1;

    private FilpperDeleteListener filpperDeleterListener;
    public CustomListView (Context context) {
        super(context);
    }

    public CustomListView (Context context, AttributeSet attrs){
        super(context, attrs);
    }
    //触发移动事件
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            //记录按下的瞬间的坐标主要是初始化坐标数据，方便以后计算移动的距离
            case MotionEvent.ACTION_DOWN:
                int temp = getChildCount();
                itemHeight = (temp==0)?itemHeight:getChildAt(0).getHeight();
                orginX = ev.getX(0);
                orginY = ev.getY(0);
                curX = orginX;
                curY = orginY;
                if (filpperDeleterListener != null) {
                    filpperDeleterListener.getMoveY(curX, curY);
                }
                break;
            //移动监听动态位置坐标的移动处理
            case MotionEvent.ACTION_MOVE:
                float deltaX = ev.getX(ev.getPointerCount() - 1) - orginX;
                //float deltaY = Math.abs(ev.getY(ev.getPointerCount() - 1) - orginY);
                float apartX = ev.getX() - curX;
                float apartY = ev.getY() - curY;
                curX = ev.getX();
                curY = ev.getY();
                //手指触摸的上下距离不能太大(即水平移动)
                if(apartX < 0 && -20 < apartY && apartY < 20){

                    if(itemHeight> apartY && filpperDeleterListener != null){
                        filpperDeleterListener.onFlipping(orginX,orginY, apartX, apartY);
                    }
                    if (Math.abs(deltaX) > this.getWidth() / ratio) {
                        delete = true;
                    } else {
                        delete = false;
                    }
                }
                break;
            //主要是移动距离之后判断移动的位置是回原来的位置，还是移动到值得的位置(这些的操作都是的listview中实现具体的位置移动)
            case MotionEvent.ACTION_UP:
                if (delete && filpperDeleterListener != null) {
                    filpperDeleterListener.restoreView(curX, curY,true);
                }
                //判断是否符合移动的条件
                if(!delete){
                    if(filpperDeleterListener != null){
                        filpperDeleterListener.restoreView(orginX,orginY,false);
                    }
                }
                reset();
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void reset(){
        delete = false ;
        orginX = -1 ;
        orginY = -1 ;
    }
    public void setFilpperDeleteListener(FilpperDeleteListener f, int r) {
        filpperDeleterListener = f;
        ratio = r;
    }

    public interface FilpperDeleteListener {
        //方法说明控制上下移动的位置
        public void getMoveY(float moveX, float moveY);
        //移动位置的具体的接口
        public void onFlipping(float xPosition, float yPosition, float apartX, float apartY);
        //最后的接口用来item最后的位置是否改变
        public void restoreView(float x, float y, boolean tag);
    }

    public int getItemHeight() {
        return itemHeight;
    }

}