package com.whitelabel.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.whitelabel.app.R;
import com.whitelabel.app.widget.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ray on 2017/5/23.
 */

public class BindProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bind_product, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return 3;
    }

    static class ItemViewHolder  extends  RecyclerView.ViewHolder{
        @BindView(R.id.iv_img)
        ImageView ivImg;
        @BindView(R.id.tv_product_name)
        CustomTextView tvProductName;
        @BindView(R.id.tv_property)
        CustomTextView tvProperty;
        @BindView(R.id.tv_old_price)
        CustomTextView tvOldPrice;
        @BindView(R.id.tv_final_price)
        CustomTextView tvFinalPrice;
        @BindView(R.id.ivChildPriceMinus)
        ImageView ivChildPriceMinus;
        @BindView(R.id.tv_child_number)
        CustomTextView tvChildNumber;
        @BindView(R.id.ivChildPricePlus)
        ImageView ivChildPricePlus;
        @BindView(R.id.rl_product_quantity)
        RelativeLayout rlProductQuantity;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
