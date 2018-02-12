package com.whitelabel.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.bean.OrderBody;
import com.whitelabel.app.model.DialogProductBean;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JToolUtils;

import java.util.ArrayList;

public class ReRorderListToCartAdapter extends ArrayAdapter<OrderBody> {
    private ArrayList<OrderBody> mBeans;
    private final ImageLoader mImageLoader;

    public ReRorderListToCartAdapter(Context context, ArrayList<OrderBody> beans, ImageLoader imageLoader) {
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
        OrderBody orderBody = mBeans.get(position);
        JImageUtils.downloadImageFromServerByUrl(getContext(), mImageLoader, holder.img, orderBody.getOrderImage(), JToolUtils.dip2px(getContext(), 45), JToolUtils.dip2px(getContext(), 45));
        holder.tvName.setText(orderBody.getOrderName());
        return view;
    }
}
