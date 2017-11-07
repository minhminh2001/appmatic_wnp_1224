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
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.model.CategoryDetailNewModel;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ray on 2017/5/9.
 */
public class CategoryDetailHorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private CategoryDetailNewModel mCategoryDetailModel;
    private final ImageLoader mImageLoader;
    private final static int HEADER = 1;
    private final static int ITEM = 2;
    private final static int TITLE = 3;
    private   double screenWidth;
    private   OnItemClickListener onProductClick;

    public ArrayList<String> titles=new ArrayList<>();
    public CategoryDetailHorizontalAdapter(Activity activity, CategoryDetailNewModel categoryDetailModel, ImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
        this.mCategoryDetailModel = categoryDetailModel;
        screenWidth=WhiteLabelApplication.getPhoneConfiguration().getScreenWidth(activity);
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.line)
        View line;
        @BindView(R.id.tv_txt)
        TextView tvTxt;
        TitleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public void setOnProductItemClickListener(OnItemClickListener bestSellersClickListener){
        this.onProductClick=bestSellersClickListener;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else if (position%2==1) {
            return TITLE;
        } else {
            return ITEM;
        }
    }
    public interface OnItemClickListener {

        void onItemClick(RecyclerView.ViewHolder itemViewHolder,int parentPosition, int childPosition);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_category_detail_header, null);
            return new HeaderViewHolder(view);
        } else if (viewType == TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_base_sellers, null);
            return new TitleViewHolder(view);
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
            int   imageHeight= (int) (screenWidth*(348.0/750));
            headerViewHolder.detailViewpager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageHeight));
            if (TextUtils.isEmpty(mCategoryDetailModel.getCategory_img())) {
                headerViewHolder.detailViewpager.setVisibility(View.VISIBLE);
            } else {
                if (headerViewHolder.detailViewpager.getTag() == null) {
                    List<String> imgs=new ArrayList<>();
                    imgs.add(mCategoryDetailModel.getCategory_img());
                    headerViewHolder.detailViewpager.setAdapter(new FlowViewAdapter(createImageViewList(headerViewHolder.itemView.getContext(),
                            mImageLoader, imgs,imageHeight)));
                    headerViewHolder.detailViewpager.setTag("use");
                }
            }
        }else if(holder instanceof TitleViewHolder){
            TitleViewHolder viewHolder= (TitleViewHolder) holder;
            if (mCategoryDetailModel.getCarousels()!=null && !mCategoryDetailModel.getCarousels().isEmpty()){
                List<CategoryDetailNewModel.CarouselsBean> carousels = mCategoryDetailModel.getCarousels();
                int size=carousels.size();
                //title position like 1 3 5 7 9,base this index(from 0 start) to get list's title
                int newPosition=(position + 1) / 2 - 1;
                if (position>0&&newPosition<size){
                    CategoryDetailNewModel.CarouselsBean carouselsBean = mCategoryDetailModel.getCarousels().get(newPosition);
                    if (carouselsBean !=null && carouselsBean.getTitle()!=null){
                        viewHolder.tvTxt.setText(carouselsBean.getTitle());
                    }
                }
            }

        }else if(holder instanceof ItemViewHolder){
            CategoryDetailItemAdapter mCategoryDetailAdapater=null;
            if (mCategoryDetailModel.getCarousels()!=null && !mCategoryDetailModel.getCarousels().isEmpty()){
                List<CategoryDetailNewModel.CarouselsBean> carousels = mCategoryDetailModel.getCarousels();
                int size=carousels.size();
                //item position such as 2,4,6,8 transform to true list's position
                int newPosition=position/2-1;
                if (position>0&&newPosition<size){
                    mCategoryDetailAdapater=new CategoryDetailItemAdapter(holder.itemView.getContext(),newPosition,carousels.get(newPosition).getItems(),mImageLoader);
                    mCategoryDetailAdapater.setOnItemClickLitener(onProductClick);
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
        }
    }

    //header + (title+Rcy)*5count
    @Override
    public int getItemCount() {
        return 1+2*5;
    }
    private List<ImageView> createImageViewList(Context context, ImageLoader imageLoader, List<String> images,int imageHeight) {
        List<ImageView> imgs = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            JImageUtils.downloadImageFromServerByUrl(context, imageLoader, imageView, images.get(i), (int) screenWidth,imageHeight);
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
