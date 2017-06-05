package com.whitelabel.app.ui.productlist;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.whitelabel.app.R;
import com.whitelabel.app.fragment.FilterModel;
import com.whitelabel.app.listener.OnRangeSeekBarChangeListener;
import com.whitelabel.app.model.FilterItemModel;
import com.whitelabel.app.model.FilterItemValueModel;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.widget.CustomRangeSeekBar;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.ExpandableRecyclerAdapter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by Administrator on 2017/6/2.
 */
public class ProductListFilterExpandAdapter extends ExpandableRecyclerAdapter {
    private static final int TYPE_TOPVIEW = 1;
    public static final int TYPE_CHILD = 1001;
    public static final String TAG = "ProductListFilterExpandAdapter";
    private FilterModel  mFilterModel;
    private List<FilterItemModel>  mFilterItemModels;
    public ProductListFilterExpandAdapter(Activity activity, FilterModel filterModel) {
        super(activity, activity);
        mFilterItemModels=new ArrayList<>();
        this.mFilterModel=filterModel;
        setHasHeader(true);
        setData(filterModel.getFilterList());
    }
    public void setData(List<FilterItemModel> filterList){
        for(int i=0;i<filterList.size();i++){
            FilterItemModel filterItemModel=filterList.get(i);
            filterItemModel.setItemType(TYPE_HEADER);
            mFilterItemModels.add(filterItemModel);
            for(int j=0;j<filterItemModel.getValues().size();j++){
                FilterItemValueModel   filterItemValueModel=filterItemModel.getValues().get(j);
                FilterItemModel itemModel=new FilterItemModel(filterItemValueModel.getLabel(),filterItemValueModel.getValue());
                itemModel.setParentId(filterItemModel.getId());
                itemModel.setItemType(TYPE_CHILD);
                mFilterItemModels.add(itemModel);
            }
        }
        setItems(mFilterItemModels);
    }
    @Override
    public int getItemCount() {
        return super.getItemCount()+1;
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOPVIEW;
        }else{
            return super.getItemViewType(position-1);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOPVIEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filter_price_header, null);
            return new HeaderViewHolder1(view);
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filter_title, null);
            return new TitleViewHolder(view);
        } else if (viewType == TYPE_CHILD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filter_item, null);
            return new ItemViewHolder(view);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder1) {
            final HeaderViewHolder1 headerViewHolder = (HeaderViewHolder1) holder;
            headerViewHolder.llPriceBar.removeAllViews();
            int minPrice =(int)mFilterModel.getPrice_filter().getMin_price();
            int minSelectedPrice = minPrice;
            int maxPrice = (int)mFilterModel.getPrice_filter().getMax_price();
            int maxSelectedPrice = maxPrice;
            // if the is no range, just display a text
            if (maxPrice - minPrice < 1) {
                CustomTextView textView = new CustomTextView(headerViewHolder.itemView.getContext());
                textView.setText(JDataUtils.formatPrice(minPrice));
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                textView.setGravity(Gravity.CENTER);
                textView.setSingleLine();
                textView.setFont("fonts/Lato-Regular.ttf", headerViewHolder.itemView.getContext());
                headerViewHolder.llPriceBar.addView(textView);
                headerViewHolder.ctvPriceRangeMin.setVisibility(View.INVISIBLE);
                headerViewHolder.ctvPriceRangeMax.setVisibility(View.INVISIBLE);
            } else {
                headerViewHolder.ctvPriceRangeMin.setText("" + minSelectedPrice);
                headerViewHolder.ctvPriceRangeMax.setText("" + maxSelectedPrice);
                CustomRangeSeekBar priceSeekBar = new CustomRangeSeekBar<>(minPrice, maxPrice, headerViewHolder.itemView.getContext());
                priceSeekBar.setSelectedMinValue(minSelectedPrice);
                priceSeekBar.setSelectedMaxValue(maxSelectedPrice);
                priceSeekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
                    @Override
                    public void onRangeSeekBarValuesChanged(CustomRangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                        JLogUtils.i(TAG, "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
                        headerViewHolder.ctvPriceRangeMin.setText("" + minValue);
                        headerViewHolder.ctvPriceRangeMax.setText("" + maxValue);
                    }
                    @Override
                    public void onRangeSeekBarTouchActionUp(CustomRangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                        // This is done so that the result will include the products with maxValue price
                        // service will fix this bug in the future,so app don't need to do additional processing
//                       maxValue++;
                    }
                });
                headerViewHolder.llPriceBar.addView(priceSeekBar);
                headerViewHolder.ctvPriceRangeMin.setVisibility(View.VISIBLE);
                headerViewHolder.ctvPriceRangeMax.setVisibility(View.VISIBLE);
            }
        } else if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            FilterItemModel  filterItemModel= (FilterItemModel) getItem(position-1);
            titleViewHolder.tvCategoryTreeGroupName.setText(filterItemModel.getLabel());
           if(filterItemModel.isExpaned()){
               titleViewHolder.tvCategoryTreeLine.setVisibility(View.GONE);
           }else{
               titleViewHolder.tvCategoryTreeLine.setVisibility(View.VISIBLE);
           }
        }else if( holder instanceof  ItemViewHolder){
            ItemViewHolder itemViewHolder= (ItemViewHolder) holder;
            FilterItemModel  filterItemModel= (FilterItemModel) getItem(position-1);
            itemViewHolder.tvFilterChildName.setText(filterItemModel.getLabel());
        }
    }
    static class HeaderViewHolder1 extends RecyclerView.ViewHolder {
        @BindView(R.id.ctvPriceRangeTitle)
        CustomTextView ctvPriceRangeTitle;
        @BindView(R.id.ctvPriceRangeMin)
        CustomTextView ctvPriceRangeMin;
        @BindView(R.id.llPriceBar)
        LinearLayout llPriceBar;
        @BindView(R.id.ctvPriceRangeMax)
        CustomTextView ctvPriceRangeMax;
        @BindView(R.id.llPriceRangeBar)
        LinearLayout llPriceRangeBar;
        @BindView(R.id.vPriceRangeBrandsDivider)
        View vPriceRangeBrandsDivider;
        @BindView(R.id.llFilter)
        LinearLayout llFilter;
        HeaderViewHolder1(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
     class TitleViewHolder extends HeaderViewHolder {
        @BindView(R.id.tv_category_tree_divi)
        TextView tvCategoryTreeDivi;
        @BindView(R.id.iv_category_tree_group)
        ImageView ivCategoryTreeGroup;
        @BindView(R.id.tv_category_tree_line)
        TextView tvCategoryTreeLine;
        @BindView(R.id.rl_category_tree_group_tag)
        RelativeLayout rlCategoryTreeGroupTag;
        @BindView(R.id.tv_category_tree_group_name)
        CustomTextView tvCategoryTreeGroupName;
        TitleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            super.setArrowAndChangeListener(tvCategoryTreeLine, new ArrowChangeListener() {
                @Override
                public void arrowChange(final View arrow, boolean openGroup, int position) {
                    if(openGroup){
                        Animation operatingAnim = AnimationUtils.loadAnimation(arrow.getContext(), R.anim.anim_category_rotate);
                        arrow.startAnimation(operatingAnim);
                        operatingAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                arrow.setVisibility(View.GONE);
                            }
                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                    }else{
                        arrow.setVisibility(View.VISIBLE);
                        Animation operatingAnim = AnimationUtils.loadAnimation(arrow.getContext(), R.anim.anim_category_rotate_tozero);
                        arrow.startAnimation(operatingAnim);
                        operatingAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                    }
                }
            });
        }
    }
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_filter_child_name)
        CustomTextView tvFilterChildName;
        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
