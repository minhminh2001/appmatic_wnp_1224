package com.whitelabel.app.widget.swipe;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.whitelabel.app.R;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Created by Administrator on 2016/3/9.
 */
public class SwipeableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeItemManagerInterface,SwipeLayoutCallBack {
    public Set<SwipeLayout> openLayouts;
    {
        openLayouts=new HashSet<SwipeLayout>();
    }
    @Override
    public void swipeCallback(SwipeLayout v,int type) {
        if(type==type_add) {
            openLayouts.add(v);
        }else if(type==type_re){
            openLayouts.remove(v);
        }else{
            Iterator<SwipeLayout> it=openLayouts.iterator();
            while(it.hasNext()){
                SwipeLayout sl = it.next();
                sl.close(true,true,false);
            }
            openLayouts.clear();
        }
    }
    public void closeAllSwipeLayout(){
        Iterator<SwipeLayout> it=openLayouts.iterator();
        while(it.hasNext()){
            SwipeLayout sl = it.next();
            sl.close(true,true,false);
        }
        openLayouts.clear();
    }
    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected SwipeItemManagerImpl mItemManger = new SwipeItemManagerImpl(this);




@Override
public void openItem(int position) {
        mItemManger.openItem(position);
        }

@Override
public void closeItem(int position) {
        mItemManger.closeItem(position);
        }

@Override
public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
        }


@Override
public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
        }

@Override
public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
        }

@Override
public SwipeItemManagerImpl.Mode getMode() {
        return mItemManger.getMode();
        }

@Override
public void setMode(SwipeItemManagerImpl.Mode mode) {
        mItemManger.setMode(mode);
        }

public static class BaseSwipeableViewHolder extends RecyclerView.ViewHolder {

    public SwipeLayout               swipeLayout      = null;
    public SwipeLayout.OnLayout      onLayoutListener = null;
    public SwipeLayout.SwipeListener swipeMemory      = null;
    public int                       position         = -1;

    public BaseSwipeableViewHolder(View itemView) {
        super(itemView);

        swipeLayout = (SwipeLayout) itemView.findViewById(R.id.recyclerview_swipe);
    }
}


}
