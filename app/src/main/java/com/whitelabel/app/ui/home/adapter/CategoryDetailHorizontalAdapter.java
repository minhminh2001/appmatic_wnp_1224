package com.whitelabel.app.ui.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.whitelabel.app.R;
import com.whitelabel.app.adapter.FlowViewAdapter;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JImageUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ray on 2017/5/9.
 */
public class CategoryDetailHorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private CategoryDetailModel mCategoryDetailModel;
    private final ImageLoader mImageLoader;
    private final static int HEADER = 1;
    private final static int ITEM = 2;
    private final static int TITLE = 3;
    public CategoryDetailHorizontalAdapter(CategoryDetailModel categoryDetailModel, ImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
        mCategoryDetailModel = categoryDetailModel;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.line)
        View line;
        @BindView(R.id.tv_txt)
        TextView tvTxt;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    private   OnItemClickListener bestSellersClickListener;
    private OnItemClickListener newArrivalsClickListener;
    public void setOnBestProductionItemClickListener(OnItemClickListener bestSellersClickListener){
        this.bestSellersClickListener=bestSellersClickListener;
    }
    public void setOnNewArrivalsItemClickListener(OnItemClickListener  newArrivalsClickListener){
        this.newArrivalsClickListener=newArrivalsClickListener;
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else if (position == 2) {
            return TITLE;
        } else {
            return ITEM;
        }
    }
    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder itemViewHolder, int position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_category_detail_header, null);
            return new HeaderViewHolder(view);
        } else if (viewType == TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_base_sellers, null);
            return new ViewHolder(view);
        } else {
            View recyclerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_head_curation, null);
            return new ItemViewHolder(recyclerView);
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            if (mCategoryDetailModel == null) return;
            int width = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth((Activity)
                    headerViewHolder.detailViewpager.getContext());
            int imageHeight = width * 240 / 490;
            headerViewHolder.detailViewpager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageHeight));
            if (TextUtils.isEmpty(mCategoryDetailModel.getCategory_img())) {
                headerViewHolder.detailViewpager.setVisibility(View.VISIBLE);
            } else {
                if (headerViewHolder.detailViewpager.getTag() == null) {
                    List<String> imgs=new ArrayList<>();
                    imgs.add(mCategoryDetailModel.getCategory_img());
                    headerViewHolder.detailViewpager.setAdapter(new FlowViewAdapter(createImageViewList(headerViewHolder.itemView.getContext(), mImageLoader, imgs)));
                    headerViewHolder.detailViewpager.setTag("use");
                }
            }
        }else if(holder instanceof  ViewHolder){
          ViewHolder viewHolder= (ViewHolder) holder;
            if(position==2){
                viewHolder.tvTxt.setText(viewHolder.itemView.getContext().getResources().getString(R.string.home_new_arrivals));
            }
        }else if(holder instanceof ItemViewHolder){
            CategoryDetailItemAdapter mCategoryDetailAdapater=null;
            if(position==1){
                mCategoryDetailAdapater=new CategoryDetailItemAdapter(holder.itemView.getContext(),mCategoryDetailModel.getBestSellerProducts(),mImageLoader);
                mCategoryDetailAdapater.setOnItemClickLitener(bestSellersClickListener);
            }else{
                mCategoryDetailAdapater=new CategoryDetailItemAdapter(holder.itemView.getContext(),mCategoryDetailModel.getNewArrivalProducts(),mImageLoader);
                mCategoryDetailAdapater.setOnItemClickLitener(newArrivalsClickListener);
            }
            ItemViewHolder  itemViewHolder= (ItemViewHolder) holder;
            itemViewHolder.rvCategory.setVisibility(View.VISIBLE);
            itemViewHolder.rvCategory.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemViewHolder.itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            itemViewHolder.rvCategory.setLayoutManager(linearLayoutManager);
            itemViewHolder.rvCategory.setAdapter(mCategoryDetailAdapater);
        }
    }
    @Override
    public int getItemCount() {
        return 4;
    }
    private List<ImageView> createImageViewList(Context context, ImageLoader imageLoader, List<String> images) {
        List<ImageView> imgs = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            JImageUtils.downloadImageFromServerByUrl(context, imageLoader, imageView, images.get(i), 640, 640 * 240 / 490);
            imgs.add(imageView);
        }
        return imgs;
    }
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_viewpager)
        ViewPager detailViewpager;
        @BindView(R.id.ll_tips)
        LinearLayout llTips;
        @BindView(R.id.rl_switch_img)
        RelativeLayout rlSwitchImg;
        @BindView(R.id.line)
        View line;
        @BindView(R.id.tv_txt)
        TextView tvTxt;
        HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    class ItemViewHolder  extends  RecyclerView.ViewHolder {
        @BindView(R.id.rvCategory)
        RecyclerView rvCategory;
        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
