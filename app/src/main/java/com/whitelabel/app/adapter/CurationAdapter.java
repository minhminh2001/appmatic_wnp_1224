package com.whitelabel.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchCategoryItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceLandingPagesListLandingPageItemReturnEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomRecyclerView;
import com.whitelabel.app.widget.ZoomImageContainer;
import com.whitelabel.app.widget.ZoomInImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by ray on 2015/9/5.
 */
public class CurationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ZoomImageContainer {
    private static final String TAG = "CurationAdapter";
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;
    private CurationAdapter.OnItemClickLitener mOnItemClickLitener;
    private ArrayList<SVRAppserviceLandingPagesListLandingPageItemReturnEntity> mCurationList;
    private int mDestWidth;
    private int mDestHeight;
    private Context mContext;
    private int vpCateHeight;
    private int screenWidth = 0;
    private GalleryAdapter mAdapter;
    private ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> mChildren;
    public boolean hasHead;
    private ImageLoader mImageLoader;

    @Override
    public int getItemCount() {
        if (hasHead) {
            return mCurationList.size() + 1;
        } else {
            return mCurationList.size();
        }
    }

    @Override
    public boolean isJumping() {
        return isJumping;
    }

    public boolean isJumping = false;

    @Override
    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    public void setOnItemClickLitener(CurationAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    //TODO: get rid of this try catch
    public CurationAdapter(ArrayList<SVRAppserviceLandingPagesListLandingPageItemReturnEntity> curationList,
                           Activity context, ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> children, ImageLoader imageLoader) {
        try {
            mContext = context;
            mImageLoader = imageLoader;
            mDestWidth = GemfiveApplication.getPhoneConfiguration().getScreenWidth(context);
            mDestHeight = GemfiveApplication.getPhoneConfiguration().getScreenWidth(context) * 240 / 490;
            final int destWidthChildCategory = (int) ((mDestWidth - JToolUtils.dip2px(mContext, 24)) / 2.5);
            int mDestHeightChildCategory = (int) (destWidthChildCategory * 0.444);
            vpCateHeight = mDestHeightChildCategory;
            mCurationList = curationList;
            mChildren = children;
            screenWidth = GemfiveApplication.getPhoneConfiguration().getScreenWidth(context);
            hasHead = mChildren != null && mChildren.size() != 0;
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View recyclerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_head_curation, null);
            return new HeadViewHolder(recyclerView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_category_curation_item, null);
            ItemViewHolder itemHolder = new ItemViewHolder(itemView);
            int marheight = mDestHeight * 20 / 268;
            if (itemHolder.tvDesc.getLayoutParams() != null) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) itemHolder.tvDesc.getLayoutParams();
                layoutParams.bottomMargin = marheight;
                itemHolder.tvDesc.setLayoutParams(layoutParams);
            }
            if (itemHolder.img.getLayoutParams() != null) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) itemHolder.img.getLayoutParams();
                layoutParams.width = mDestWidth;
                layoutParams.height = mDestHeight;
                JLogUtils.d("size", mDestWidth + " " + mDestHeight);
                itemHolder.img.setLayoutParams(layoutParams);
            }
            return itemHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeadViewHolder) {
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            if (mAdapter == null && mCurationList != null && mCurationList.size() > 0) {
                if (headViewHolder.recyclerView.getLayoutParams() != null) {
                    try {
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) headViewHolder.recyclerView.getLayoutParams();
                        lp.height = vpCateHeight;
                        headViewHolder.recyclerView.setLayoutParams(lp);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        headViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
                    } catch (Exception ex) {
                        ex.getStackTrace();
                    }
                }
                headViewHolder.recyclerView.setVisibility(View.VISIBLE);
                mAdapter = new GalleryAdapter((Activity) mContext, mChildren, mImageLoader);
                headViewHolder.recyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        try {
                            SVRAppserviceCatalogSearchCategoryItemReturnEntity categoryentity = mChildren.get(position);
                            Intent intent = new Intent();
                            intent.setClass(mContext, ProductListActivity.class);
                            intent.putExtra(ProductListActivity.INTENT_DATA_PREVTYPE, ProductListActivity.INTENT_DATA_PREVTYPE_VALUE_MAINCATEGOTY);
                            intent.putExtra(ProductListActivity.INTENT_DATA_FRAGMENTTYPE, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY);
                            intent.putExtra(ProductListActivity.INTENT_DATA_CATEGORYID, categoryentity);
                            mContext.startActivity(intent);
                            ((Activity) mContext).overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
//                            GaTrackHelper.getInstance().googleAnalytics(categoryentity.getName(), mContext);
                        } catch (Exception ex) {
                        }
                    }
                });
            } else if (mAdapter != null) {
                mAdapter.addDatas(mChildren);
            }
        } else {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;
            initBanner(itemHolder);
            int position1 = 0;
            if (hasHead) {
                position1 = position - 1;
                if (mCurationList != null && mCurationList.size() > 1 && position == 1) {
                    itemHolder.llGem.setVisibility(View.VISIBLE);
                } else {
                    itemHolder.llGem.setVisibility(View.GONE);
                }
            } else {
                position1 = position;
                if (mCurationList != null && mCurationList.size() > 1 && position == 0) {
                    itemHolder.llGem.setVisibility(View.VISIBLE);
                } else {
                    itemHolder.llGem.setVisibility(View.GONE);
                }
            }
            final SVRAppserviceLandingPagesListLandingPageItemReturnEntity bean = mCurationList.get(position1);
            String curationUrl = bean.getImage();
            if (!JDataUtils.isEmpty(curationUrl)) {
                if (!curationUrl.equals(String.valueOf(itemHolder.img.getTag()))) {
                    itemHolder.img.setTag(curationUrl);
                    JImageUtils.downloadImageFromServerListener(mContext.getApplicationContext(), mImageLoader, itemHolder.img, curationUrl, 640, 640 * 240 / 490, new CurationImageListener(this, itemHolder, bean));
                } else {
                    String badgeType = bean.getBadgeType();
                    String badgeValue = bean.getBadgeValue();
                    initBanner(itemHolder, badgeType, badgeValue);
                }
            } else {
                itemHolder.img.setTag(null);
                itemHolder.img.setImageBitmap(null);
                initBanner(itemHolder);
            }
            if (TextUtils.isEmpty(bean.getHeading())) {
                itemHolder.tvDesc.setVisibility(View.GONE);
            } else {
                String str = bean.getHeading();
                str = str.trim();
                if (!"".equals(str)) {
                    itemHolder.tvDesc.setVisibility(View.VISIBLE);
                } else {
                    itemHolder.tvDesc.setVisibility(View.GONE);
                }
            }

            itemHolder.tvDesc.setText(bean.getHeading());
            final int positionTemp;
            if (hasHead) {
                positionTemp = position - 1;
            } else {
                positionTemp = position;
            }
            if (mOnItemClickLitener != null) {
                //快速点击时直接进入detail,只需改变heading颜色，不需要走touch流程放大图片
                itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemHolder.tvDesc != null) {
                            itemHolder.tvDesc.setBackgroundColor(itemHolder.img.getContext().getResources().getColor(R.color.blue7D66006E));
                            itemHolder.tvDesc.setTextColor(itemHolder.tvDesc.getContext().getResources().getColor(R.color.white));
                        }
                        mOnItemClickLitener.onItemClick(itemHolder, positionTemp);
                    }
                });
            }
            itemHolder.img.setZoomImageContainer(this);
            itemHolder.img.setZoomImageCallBack(new ZoomInImageView.ZoomImageCallBack() {
                @Override
                public void largeBeginCallBack() {
                    if (itemHolder.tvDesc != null) {
                        itemHolder.tvDesc.setBackgroundColor(itemHolder.img.getContext().getResources().getColor(R.color.blue7D66006E));
                        itemHolder.tvDesc.setTextColor(itemHolder.tvDesc.getContext().getResources().getColor(R.color.white));
                    }
                }

                @Override
                public void largeEndCallBack() {
                }

                @Override
                public void narrowBeginCallBack() {
                    if (itemHolder.tvDesc != null) {
                        itemHolder.tvDesc.setBackgroundColor(itemHolder.img.getContext().getResources().getColor(R.color.whiteEAFFFFFF));
                        itemHolder.tvDesc.setTextColor(itemHolder.tvDesc.getContext().getResources().getColor(R.color.black));
                    }
                }

                @Override
                public void narrowEndCallBack() {
                }

                @Override
                public void onClick() {
                    mOnItemClickLitener.onItemClick(itemHolder, positionTemp);
                }
            });
            itemHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //将img的touch事件和itemview绑定
                    return itemHolder.img.setOnTouch(event);
                }
            });
        }
    }

    private class CurationImageListener implements RequestListener<String, Bitmap> {
        WeakReference<CurationAdapter> mAdapter;
        WeakReference<ItemViewHolder> mViewHolder;
        SVRAppserviceLandingPagesListLandingPageItemReturnEntity mBean;

        public CurationImageListener(CurationAdapter adapter, ItemViewHolder viewHolder, SVRAppserviceLandingPagesListLandingPageItemReturnEntity bean) {
            mAdapter = new WeakReference<>(adapter);
            mViewHolder = new WeakReference<>(viewHolder);
            mBean = bean;
        }

        @Override
        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
            String badgeType = mBean.getBadgeType();
            String badgeValue = mBean.getBadgeValue();
            mAdapter.get().initBanner(mViewHolder.get(), badgeType, badgeValue);
            return false;
        }

        @Override
        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
            if (mViewHolder.get() == null || mAdapter.get() == null) return false;
            String badgeType = mBean.getBadgeType();
            String badgeValue = mBean.getBadgeValue();

            //  4,7,8需要判断banner value 的长度，进而设置fontsize
            mViewHolder.get().img.setImageBitmap(resource);
            mAdapter.get().initBanner(mViewHolder.get(), badgeType, badgeValue);
            mViewHolder.get().img.setBitmapWH(640, 640 * 240 / 490, mAdapter.get().screenWidth);
            mViewHolder.get().img.init();
            return true;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    public void initBanner(ItemViewHolder itemHolder, String badgeType, String badgeValue) {

        if ("1".equals(badgeType)) {
            itemHolder.banner1.setVisibility(View.VISIBLE);
        } else if ("2".equals(badgeType)) {
            itemHolder.banner2.setVisibility(View.VISIBLE);
            if (badgeValue != null) {
                itemHolder.banner2_text.setText(badgeValue);
            }
        } else if ("3".equals(badgeType)) {
            itemHolder.banner3.setVisibility(View.VISIBLE);
            if (badgeValue != null) {
                itemHolder.banner3_text.setText(badgeValue);
            }
        } else if ("4".equals(badgeType)) {
            itemHolder.banner4.setVisibility(View.VISIBLE);
            if (badgeValue != null) {
                JViewUtils.setLandingBannerFontSize(badgeType, badgeValue, itemHolder.banner4_text);
                itemHolder.banner4_text.setText(badgeValue);
                itemHolder.banner4_text.setPadding(0, 4, 0, 0);
                itemHolder.banner4_text.setTextSize(18);
            }
        } else if ("5".equals(badgeType)) {
            itemHolder.banner5.setVisibility(View.VISIBLE);
        } else if ("6".equals(badgeType)) {
            itemHolder.banner6.setVisibility(View.VISIBLE);
        } else if ("7".equals(badgeType)) {
            itemHolder.banner7.setVisibility(View.VISIBLE);
            if (badgeValue != null) {
                JViewUtils.setLandingBannerFontSize(badgeType, badgeValue, itemHolder.banner7_text);
                itemHolder.banner7_text.setText(badgeValue);
                itemHolder.banner7_text.setPadding(JToolUtils.dip2px(mContext, 2), JToolUtils.dip2px(mContext, 20), 0, 0);
                itemHolder.banner7_text.setTextSize(18);
            }
        } else if ("8".equals(badgeType)) {
            itemHolder.banner8.setVisibility(View.VISIBLE);
            if (badgeValue != null) {
                JViewUtils.setLandingBannerFontSize(badgeType, badgeValue, itemHolder.banner8_text);
                itemHolder.banner8_text.setText(badgeValue);
                itemHolder.banner8_text.setPadding(JToolUtils.dip2px(mContext, 2), JToolUtils.dip2px(mContext, 22), 0, 0);
                itemHolder.banner8_text.setTextSize(16);
            }
        } else if ("9".equals(badgeType)) {
            itemHolder.banner9.setVisibility(View.VISIBLE);
            if (badgeValue != null) {
                //JViewUtils.setLandingBannerFontSize(badgeType, badgeValue, itemHolder.banner9_text);
                itemHolder.banner9_text.setText(badgeValue);
                itemHolder.banner9_text.setPadding(JToolUtils.dip2px(mContext, 3), JToolUtils.dip2px(mContext, 10), 0, 0);
                itemHolder.banner9_text.setTextSize(16);
            }
        } else {
            initBanner(itemHolder);
        }
    }


    private boolean isPositionHeader(int position) {
        return position == 0 && hasHead;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ZoomInImageView img;
        public TextView tvDesc;
        public View llGem;
        public RelativeLayout banner1, banner2, banner3, banner4, banner5, banner6, banner7, banner8, banner9;
        public TextView banner2_text, banner3_text, banner4_text, banner7_text, banner8_text, banner9_text;

        public ItemViewHolder(View itemView) {
            super(itemView);
            img = (ZoomInImageView) itemView.findViewById(R.id.ivCuration);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            llGem = itemView.findViewById(R.id.llGemPicks);
            banner1 = (RelativeLayout) itemView.findViewById(R.id.banner1);
            banner2 = (RelativeLayout) itemView.findViewById(R.id.banner2);
            banner3 = (RelativeLayout) itemView.findViewById(R.id.banner3);
            banner4 = (RelativeLayout) itemView.findViewById(R.id.banner4);
            banner5 = (RelativeLayout) itemView.findViewById(R.id.banner5);
            banner6 = (RelativeLayout) itemView.findViewById(R.id.banner6);
            banner7 = (RelativeLayout) itemView.findViewById(R.id.banner7);
            banner8 = (RelativeLayout) itemView.findViewById(R.id.banner8);
            banner9 = (RelativeLayout) itemView.findViewById(R.id.banner9);
            banner2_text = (TextView) itemView.findViewById(R.id.banner2_text);
            banner3_text = (TextView) itemView.findViewById(R.id.banner3_text);
            banner4_text = (TextView) itemView.findViewById(R.id.banner4_text);
            banner7_text = (TextView) itemView.findViewById(R.id.banner7_text);
            banner8_text = (TextView) itemView.findViewById(R.id.banner8_text);
            banner9_text = (TextView) itemView.findViewById(R.id.banner9_text);
        }
    }

    public void initBanner(ItemViewHolder itemHolder) {
        itemHolder.banner1.setVisibility(View.GONE);
        itemHolder.banner2.setVisibility(View.GONE);
        itemHolder.banner3.setVisibility(View.GONE);
        itemHolder.banner4.setVisibility(View.GONE);
        itemHolder.banner5.setVisibility(View.GONE);
        itemHolder.banner6.setVisibility(View.GONE);
        itemHolder.banner7.setVisibility(View.GONE);
        itemHolder.banner8.setVisibility(View.GONE);
        itemHolder.banner9.setVisibility(View.GONE);
        itemHolder.banner2_text.setText("");
        itemHolder.banner3_text.setText("");
        itemHolder.banner4_text.setText("");
        itemHolder.banner7_text.setText("");
        itemHolder.banner8_text.setText("");
        itemHolder.banner9_text.setText("");

    }

    public static class HeadViewHolder extends RecyclerView.ViewHolder {
        public CustomRecyclerView recyclerView;

        public HeadViewHolder(View itemView) {
            super(itemView);
            recyclerView = (CustomRecyclerView) itemView.findViewById(R.id.rvCategory);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(ItemViewHolder itemViewHolder, int position);
    }
}
