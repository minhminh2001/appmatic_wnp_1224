package com.whitelabel.app.ui.home.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whitelabel.app.Const;
import com.whitelabel.app.R;
import com.whitelabel.app.model.ShopBrandResponse;

import java.util.List;

/**
 * Created by img on 2017/11/20.
 * ShopBrandActivity not use,so this adapter not use
 */

public class ShopBrandDetailAdapter extends BaseMultiItemQuickAdapter<ShopBrandResponse.BrandsBean.ItemsBean, BaseViewHolder> {
    private Context mContext;
    public ShopBrandDetailAdapter(Context context,List<ShopBrandResponse.BrandsBean.ItemsBean> data) {
        super(data);
        addItemType(Const.HEADER, R.layout.item_shop_brand_title);
        addItemType(Const.ITEM,R.layout.item_shop_brand_image);
        mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopBrandResponse.BrandsBean.ItemsBean response) {
        switch (helper.getItemViewType()) {
            case Const.HEADER:
                helper.setText(R.id.tv_brand_title, response.getTitle());
                break;
            case Const.ITEM:
                ImageView view = helper.getView(R.id.iv_brand_icon);
                ImageView border = helper.getView(R.id.iv_shop_brand_border);
//                helper.setImageResource(R.id.iv_brand_icon, R.mipmap.ic_launcher);
                helper.setText(R.id.tv_brand_image_msg, response.getTitle());
                Glide
                .with(mContext)
                .load(response.getIcon())
                .fitCenter()
                .thumbnail(0.2f)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);
                break;

        }
    }


}
