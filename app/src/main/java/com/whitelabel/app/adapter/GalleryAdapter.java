package com.whitelabel.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchCategoryItemReturnEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JToolUtils;

import java.util.ArrayList;

public class GalleryAdapter extends
        RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private int mDestWidthChildCategory;
    private int mDestHeightChildCategory;
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> mDatas;
    private final ImageLoader mImageLoader;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void addDatas(ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> datas) {
        if (datas != null) {
            mDatas.clear();
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public GalleryAdapter(Activity context, ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> datats, ImageLoader imageLoader) {
        final int destScreenWidth = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth(context);
//		final int destPageMargin = 3 * destScreenWidth / 10;
        mContext = context;
        mDestWidthChildCategory = (int) ((destScreenWidth - JToolUtils.dip2px(mContext, 24)) / 2.5);
//		图片的宽高比为：1000：4444
        mDestHeightChildCategory = (int) (mDestWidthChildCategory * 0.444);
        mInflater = LayoutInflater.from(context);
        mDatas = new ArrayList<>();
        mDatas.addAll(datats);
        mImageLoader = imageLoader;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            mTxt = (TextView) arg0.findViewById(R.id.ctvCategoryName);
            mImg = (ImageView) arg0.findViewById(R.id.id_index_gallery_item_image);
            vRight = arg0.findViewById(R.id.v_right);
        }

        ImageView mImg;
        TextView mTxt;
        View vRight;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.activity_index_gallery_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.mImg.getLayoutParams();
        params.width = mDestWidthChildCategory;
        params.height = mDestHeightChildCategory;
        viewHolder.mImg.setLayoutParams(params);
        RelativeLayout.LayoutParams categoryNamelp = (RelativeLayout.LayoutParams) viewHolder.mTxt.getLayoutParams();
        if (categoryNamelp != null) {
            categoryNamelp.width = (int) (mDestWidthChildCategory / 1.8);
            viewHolder.mTxt.setLayoutParams(categoryNamelp);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
//		viewHolder.mImg.setImageResource(mDatas.get(i));                                               //将请求的图片扩大10显示，避免图片过小而失真
//		JImageUtils.downloadImageFromServerByUrl(mContext, viewHolder.mImg, mDatas.get(i).getImage(), (int) (mDestWidthChildCategory*1.5), (int) (mDestHeightChildCategory*1.5));
        try {
            if (viewHolder.mImg.getTag() == null || !String.valueOf(viewHolder.mImg.getTag()).equals(mDatas.get(i).getImage())) {
                JImageUtils.downloadImageFromServerByUrl(mContext, mImageLoader, viewHolder.mImg, mDatas.get(i).getImage(), -1, -1);
                viewHolder.mImg.setTag(mDatas.get(i).getImage());
            }
            if (i == mDatas.size() - 1) {
                viewHolder.vRight.setVisibility(View.VISIBLE);
            } else {
                viewHolder.vRight.setVisibility(View.GONE);
            }
            String categoryName = "";
            if (mDatas.get(i).getName() != null) {
                categoryName = mDatas.get(i).getName().toUpperCase();
            }
            viewHolder.mTxt.setText(categoryName);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });

        }

    }

}
