package com.whitelabel.app.widget.swipe;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.whitelabel.app.R;


/**
 * Created by MarshalChen on 15-6-2.
 */
public class UltimateRecyclerviewViewHolder extends RecyclerView.ViewHolder implements
        ItemTouchHelperViewHolder {

    public SwipeLayout swipeLayout = null;
    public SwipeLayout.OnLayout onLayoutListener = null;
    public SwipeLayout.SwipeListener swipeMemory = null;
    public int position = -1;

    public UltimateRecyclerviewViewHolder(View itemView) {
        super(itemView);
        swipeLayout = (SwipeLayout) itemView.findViewById(R.id.recyclerview_swipe);
    }


    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}