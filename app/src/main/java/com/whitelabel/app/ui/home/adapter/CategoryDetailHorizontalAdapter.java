package com.whitelabel.app.ui.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.Const;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.callback.IHomeItemClickListener;
import com.whitelabel.app.model.CategoryDetailNewModel;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.home.fragment.HomeHomeFragmentV3;
import com.whitelabel.app.utils.GlideImageLoader;
import com.whitelabel.app.utils.JImageUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

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
    private Activity activity;
    private   double screenWidth;
    private IHomeItemClickListener.IHorizontalItemClickListener onProductClick;
    private IHomeItemClickListener.IHeaderItemClickListener onHeaderClick;
    List<CategoryDetailNewModel.CarouselsBean> carousels=new ArrayList<>();
    public CategoryDetailHorizontalAdapter(Activity activity, CategoryDetailNewModel categoryDetailModel, ImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
        this.activity=activity;
        this.mCategoryDetailModel = categoryDetailModel;
        screenWidth=WhiteLabelApplication.getPhoneConfiguration().getScreenWidth(activity);
        addTitleItemList();
    }
    private HomeActivity.ICommunHomeActivity iCommunHomeActivity;
    public void setiCommunHomeActivity(HomeActivity.ICommunHomeActivity iCommunHomeActivity) {
        this.iCommunHomeActivity = iCommunHomeActivity;
    }

    //add item type position and data
    private void addTitleItemList() {
        if (mCategoryDetailModel!=null && mCategoryDetailModel.getCarousels()!=null && !mCategoryDetailModel.getCarousels().isEmpty()){
            this.carousels.clear();
            List<CategoryDetailNewModel.CarouselsBean> carousels = mCategoryDetailModel.getCarousels();
            addCarouselsBean("header", Const.HEADER,0,null);
            int titlePosition;
            int itemPosition=0;
            //header already add 1 so i from 1 start
            for (int i=1;i<carousels.size()+1;i++){
                titlePosition=2*i ;
                //Item positon
                itemPosition=titlePosition+1;

                CategoryDetailNewModel.CarouselsBean carouselsBean = carousels.get(i-1);
                addCarouselsBean(carouselsBean.getTitle(),Const.TITLE,titlePosition,null);
                if (carouselsBean.getItems()!=null&&!carouselsBean.getItems().isEmpty()){
                   addCarouselsBean(carouselsBean.getTitle(),Const.ITEM,itemPosition,carouselsBean.getItems());
                }
            }
        }
    }

    private void addCarouselsBean(String titleName,int type,int position,List<SVRAppserviceProductSearchResultsItemReturnEntity> items){
        CategoryDetailNewModel.CarouselsBean bean=new CategoryDetailNewModel.CarouselsBean();
        bean.setTitle(titleName);
        bean.setType(type);
        bean.setPosition(position);
        bean.setItems(items);
        this.carousels.add(bean);
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
    public void setOnProductItemClickListener(IHomeItemClickListener.IHorizontalItemClickListener bestSellersClickListener){
        this.onProductClick=bestSellersClickListener;
    }

    public void setOnHeaderClick(IHomeItemClickListener.IHeaderItemClickListener onHeaderClick) {
        this.onHeaderClick = onHeaderClick;
    }

    @Override
    public int getItemViewType(int position) {
        return  !this.carousels.isEmpty()?this.carousels.get(position).getType():Const.HEADER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Const.HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_category_detail_header, null);
            return new HeaderViewHolder(view);
        } else if (viewType == Const.TITLE) {
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
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            if (mCategoryDetailModel == null) return;
//            int   imageHeight= (int) (screenWidth*(348.0/750));
            int   imageHeight= (int) (screenWidth*(Const.BANNER_PIC_HEIGHT_THAN_WIDTH));
            headerViewHolder.detailViewpager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageHeight));
            if (mCategoryDetailModel.getBanners().isEmpty()) {
                headerViewHolder.detailViewpager.setVisibility(View.VISIBLE);
            } else {
                if (headerViewHolder.detailViewpager.getTag() == null) {
                    List<String> imgs=new ArrayList<>();
//                    imgs.add(mCategoryDetailModel.getCategory_img());
//                    headerViewHolder.detailViewpager.setAdapter(new FlowViewAdapter(createImageViewList(headerViewHolder.itemView.getContext(),
//                            mImageLoader, imgs,imageHeight)));
                    imgs.addAll(createImageUrlList(headerViewHolder.itemView.getContext(),mCategoryDetailModel.getBanners(),imageHeight));
                    headerViewHolder.detailViewpager.setImages(imgs)
                            .setImageLoader(new GlideImageLoader())
                            .setBannerStyle(BannerConfig.NOT_INDICATOR)
                            .setDelayTime(Const.NORMAL_BANNER_DELAY_TIME)
                            .setOnBannerListener(new OnBannerListener() {
                                @Override
                                public void OnBannerClick(int i) {
                                    onHeaderClick.onItemClick(headerViewHolder,i);
                                }
                            })
                            .start();
                    headerViewHolder.detailViewpager.setTag("use");
                }
            }
        }else if(holder instanceof TitleViewHolder){
            TitleViewHolder viewHolder= (TitleViewHolder) holder;
            if (!this.carousels.isEmpty()){
                viewHolder.tvTxt.setText(this.carousels.get(position).getTitle());
            }

        }else if(holder instanceof ItemViewHolder){
            if (!this.carousels.isEmpty()){
                CategoryDetailNewModel.CarouselsBean carouselsBean = this.carousels.get(position);
                //let HomeFragmentV3's CategoryDetailNewModel know which position items
                int CarouselsItemPosition=(position-1)/2;
                CategoryDetailItemAdapter mCategoryDetailAdapater=new CategoryDetailItemAdapter(activity,CarouselsItemPosition,carouselsBean.getItems(),mImageLoader);
                mCategoryDetailAdapater.setiCommunHomeActivity(new HomeActivity.ICommunHomeActivity() {
                    @Override
                    public void saveProductIdWhenCheckPage(String productId, int isLike, boolean isUnLogin) {
                        iCommunHomeActivity.saveProductIdWhenCheckPage(productId,isLike,isUnLogin);
                    }

                    @Override
                    public boolean isUnLoginCanWishIconRefresh(String productId) {
                         return iCommunHomeActivity.isUnLoginCanWishIconRefresh(productId);
                    }
                });
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

    @Override
    public int getItemCount() {
        return this.carousels.isEmpty()?0:this.carousels.size();
    }
    private List<ImageView> createImageViewList(Context context, ImageLoader imageLoader, List<String> images,int imageHeight) {
        List<ImageView> imgs = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            JImageUtils.downloadImageFromServerByProductUrl(context, imageLoader, imageView, images.get(i), (int) screenWidth,imageHeight);
            imgs.add(imageView);
        }
        return imgs;
    }

    private List<String> createImageUrlList(Context context, List<CategoryDetailNewModel.BannersBean> images,int  imageHeight) {
        List<String> imgs = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            CategoryDetailNewModel.BannersBean bannersBeanResponse = images.get(i);
            String norUrl=bannersBeanResponse.getImage();
            String imageServerUrlByWidthHeight = JImageUtils.getImageServerUrlByWidthHeight(context, norUrl, (int) screenWidth, imageHeight,-1);
            imgs.add(imageServerUrlByWidthHeight);
        }
        return imgs;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_viewpager)
        Banner detailViewpager;
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
