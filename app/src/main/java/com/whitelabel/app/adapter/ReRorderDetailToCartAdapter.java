package com.whitelabel.app.adapter;

import com.whitelabel.app.R;
import com.whitelabel.app.bean.OrderBody;
import com.whitelabel.app.model.MyAccountOrderInner;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JToolUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ReRorderDetailToCartAdapter extends ArrayAdapter<MyAccountOrderInner> {
    private List<MyAccountOrderInner> mBeans;
    private final ImageLoader mImageLoader;

    public ReRorderDetailToCartAdapter(Context context, List<MyAccountOrderInner> beans, ImageLoader imageLoader) {
        super(context, R.layout.dialog_product, beans);
        mBeans = beans;
        mImageLoader = imageLoader;
    }

    class Holder {
        ImageView img;
        TextView tvName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder = null;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_product, null);
            holder = new Holder();
            holder.img = (ImageView) view.findViewById(R.id.img);
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        MyAccountOrderInner myAccountOrderInner = mBeans.get(position);
        JImageUtils.downloadImageFromServerByUrl(getContext(), mImageLoader, holder.img, myAccountOrderInner.getImage(), JToolUtils.dip2px(getContext(), 45), JToolUtils.dip2px(getContext(), 45));
        holder.tvName.setText(myAccountOrderInner.getName());
        return view;
    }
}
