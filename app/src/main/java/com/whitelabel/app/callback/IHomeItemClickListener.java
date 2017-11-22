package com.whitelabel.app.callback;

import android.support.v7.widget.RecyclerView;


/**
 * Created by img on 2017/11/22.
 */

public interface IHomeItemClickListener {
    public interface IVerticalItemClickLitener {
        void onItemClick(RecyclerView.ViewHolder itemViewHolder, int position);
    }
    public interface IHorizontalItemClickListener {
        void onItemClick(RecyclerView.ViewHolder itemViewHolder,int parentPosition, int childPosition);
    }
    public interface IHeaderItemClickListener{
        void onItemClick(RecyclerView.ViewHolder headerViewHolder, int position);
    }
}
