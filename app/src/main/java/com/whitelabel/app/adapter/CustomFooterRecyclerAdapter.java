package com.whitelabel.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by kevin on 2016/4/20.
 */
public interface CustomFooterRecyclerAdapter {
    /*
    entity need extends CustomFooterRecyclerAdapter.RecyclerEntity
     */
    int TYPE_FOOTER=1;
    int TYPE_ITEM=2;
    void addFooter();

    void showFooter();

    void hideFooter();
    void delFooter();
    void setFooterLayout();
    void notifyChange();
    public class RecyclerEntity {
    }
    public class FooterEntity extends RecyclerEntity {
    }
    public class FooterHolder extends RecyclerView.ViewHolder{
        View footerView;
        public FooterHolder(View itemView) {
            super(itemView);

            itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            footerView=itemView;
        }
    }
}
