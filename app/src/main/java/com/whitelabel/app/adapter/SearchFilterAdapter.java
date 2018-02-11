package com.whitelabel.app.adapter;

import com.bumptech.glide.Glide;
import com.whitelabel.app.Const;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.model.SearchFilterResponse;
import com.whitelabel.app.utils.PageIntentUtils;
import com.whitelabel.app.utils.logger.Logger;
import com.whitelabel.app.widget.CustomTextView;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by img on 2018/2/5.
 */

public class SearchFilterAdapter extends RecyclerView.Adapter{
    public static final int HEADER_TITLE=0;
    public static final int IMG_AND_TEXT=1;
    public static final int TEXT=2;

    public interface IRecyclerClick{
        void onRecyclerItemClick(SearchFilterResponse.SuggestsBean.ItemsBean itemsBean);
    }
    IRecyclerClick iRecyclerClick;
    private  List<SearchFilterResponse.SuggestsBean.ItemsBean> itemsBeans=new ArrayList<>();
    private  String keyword;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case HEADER_TITLE:
                View titleView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_search_filter_title, parent, false);
                viewHolder = new HeaderHolder(titleView);
                break;
            case IMG_AND_TEXT:
                View imgAndText = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_search_filter_image_and_text, parent, false);
                viewHolder = new ImgAndTextHolder(imgAndText);
                break;
            case TEXT:
                View textView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_search_filter_text, parent, false);
                viewHolder = new TextHolder(textView);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SearchFilterResponse.SuggestsBean.ItemsBean itemsBean = itemsBeans.get(position);
        if (viewHolder instanceof HeaderHolder){
            ((HeaderHolder)viewHolder).tvSearchFilterTitle.setText(itemsBean.getTitle());
        }else if (viewHolder instanceof ImgAndTextHolder){
            ImgAndTextHolder holder= (ImgAndTextHolder) viewHolder;
            holder.rlBottomLine.setVisibility(itemsBean.getIsLast()?View.VISIBLE:View.GONE);
            holder.tvSearchFilterProductName.setText(loadKeywordColor(itemsBean.getName()));
            Glide.with(holder.itemView.getContext()).load(itemsBean.getImage()).into(holder.ivSearchFilterProductIcon);
            clickItem(viewHolder,itemsBean);
        }else if (viewHolder instanceof TextHolder){
            ((TextHolder)viewHolder).rlBottomLine.setVisibility(itemsBean.getIsLast()?View.VISIBLE:View.GONE);
            ((TextHolder)viewHolder).tvSearchFilterText.setText(loadKeywordColor(itemsBean.getName()));
            clickItem(viewHolder,itemsBean);
        }
    }

    private void clickItem(RecyclerView.ViewHolder viewHolder, final SearchFilterResponse.SuggestsBean.ItemsBean itemsBean){
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iRecyclerClick!=null) {
                    iRecyclerClick.onRecyclerItemClick(itemsBean);
                }
            }
        });
    }

    public void updateData(List<SearchFilterResponse.SuggestsBean.ItemsBean> itemsBeans,String keyWord){
        this.itemsBeans = itemsBeans;
        this.keyword = keyWord;
        notifyDataSetChanged();
    }

    private SpannableString loadKeywordColor(String content){
        SpannableString spannableString=new SpannableString(content);
        String lowerContent=content.toLowerCase();
        String lowerKeyword=keyword.toLowerCase();

        int theme_color = WhiteLabelApplication.getAppConfiguration().getThemeConfig()
            .getTheme_color();
        int start = lowerContent.indexOf(lowerKeyword);
        int end = lowerContent.indexOf(lowerKeyword) + lowerKeyword.length();
        //-1 mean not conform
        if (start!=-1){
            spannableString.setSpan(new ForegroundColorSpan(theme_color),start , end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    public void setiRecyclerClick(
        IRecyclerClick iRecyclerClick) {
        this.iRecyclerClick = iRecyclerClick;
    }

    @Override
    public int getItemCount() {
        return itemsBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemsBeans.get(position).getRecyclerType();
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_search_filter_title)
        CustomTextView tvSearchFilterTitle;
        public HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    static class ImgAndTextHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_search_filter_product_icon)
        ImageView ivSearchFilterProductIcon;
        @BindView(R.id.tv_search_filter_product_name)
        CustomTextView tvSearchFilterProductName;
        @BindView(R.id.rl_bottom_line)
        RelativeLayout rlBottomLine;
        public ImgAndTextHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    static class TextHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_search_filter_text)
        CustomTextView tvSearchFilterText;
        @BindView(R.id.rl_bottom_line)
        RelativeLayout rlBottomLine;
        public TextHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
