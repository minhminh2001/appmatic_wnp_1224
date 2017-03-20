package com.whitelabel.app.widget.daimajia_swipe.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.whitelabel.app.widget.daimajia_swipe.SwipeLayout;
import com.whitelabel.app.widget.daimajia_swipe.implments.SwipeItemMangerImpl;
import com.whitelabel.app.widget.daimajia_swipe.interfaces.SwipeAdapterInterface;
import com.whitelabel.app.widget.daimajia_swipe.interfaces.SwipeItemMangerInterface;
import com.whitelabel.app.widget.daimajia_swipe.interfaces.SwipeLayoutCallBack;
import com.whitelabel.app.widget.daimajia_swipe.util.Attributes;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class RecyclerSwipeAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements SwipeItemMangerInterface, SwipeAdapterInterface,SwipeLayoutCallBack {
    public Set<com.whitelabel.app.widget.daimajia_swipe.SwipeLayout> openLayouts;
    {
        openLayouts=new HashSet<com.whitelabel.app.widget.daimajia_swipe.SwipeLayout>();
    }
    @Override
    public void swipeCallback(com.whitelabel.app.widget.daimajia_swipe.SwipeLayout v,int type) {
        if(type==type_add) {
            openLayouts.add(v);
        }else if(type==type_re){
            openLayouts.remove(v);
        }else{
            Iterator<com.whitelabel.app.widget.daimajia_swipe.SwipeLayout> it=openLayouts.iterator();
            while(it.hasNext()){
                com.whitelabel.app.widget.daimajia_swipe.SwipeLayout sl = it.next();
                if(sl!=null) {
                    sl.close(true, true, false);
                }
            }
            openLayouts.clear();
        }
    }
    public void closeAllSwipeLayout(boolean smooth){
        Iterator<com.whitelabel.app.widget.daimajia_swipe.SwipeLayout> it=openLayouts.iterator();
        while(it.hasNext()){
            com.whitelabel.app.widget.daimajia_swipe.SwipeLayout sl = it.next();
            if(sl!=null) {
                if(smooth) {
                    sl.close(true, true, false);
                }else{
                    sl.close(false, true, false);
                }
            }
        }
        openLayouts.clear();
    }



    public SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);

    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(VH viewHolder, final int position);

    @Override
    public void notifyDatasetChanged() {
        super.notifyDataSetChanged();
    }

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
    public void closeAllItems() {
        mItemManger.closeAllItems();
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
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public Attributes.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(Attributes.Mode mode) {
        mItemManger.setMode(mode);
    }
}
