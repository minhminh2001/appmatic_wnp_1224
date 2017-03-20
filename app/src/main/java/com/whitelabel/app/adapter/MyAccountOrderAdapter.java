package com.whitelabel.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.google.gson.Gson;
import com.whitelabel.app.model.MyAccountOrderInner;
import com.whitelabel.app.model.MyAccountOrderMiddle;
import com.whitelabel.app.model.MyAccountOrderOuter;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JToolUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Datas in order list are divided in three part,and this Adapter is the outer one.
 */
public class MyAccountOrderAdapter extends BaseAdapter {

    public LinkedList<MyAccountOrderOuter> list;
    private Context context;
    private Gson gson = new Gson();
    private final ImageLoader mImageLoader;

    public MyAccountOrderAdapter(Context context, ImageLoader imageLoader) {
        this.context = context;
        this.list = new LinkedList<>();
        mImageLoader = imageLoader;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_myaccount_orders_cell_outer, null);
        }

        //GET outer order datas
        MyAccountOrderOuter orderOuter = list.get(position);

        //TO PUT outer datas
        TextView tvOrderNumber = (TextView) convertView.findViewById(R.id.tv_myaccount_order_ordernumber);
        TextView tvOrderDate = (TextView) convertView.findViewById(R.id.tv_myaccount_order_date);
        TextView tvTotalMoney = (TextView) convertView.findViewById(R.id.tv_myaccount_order_totalmoney);
        tvOrderNumber.setText(context.getResources().getString(R.string.order_number) + orderOuter.getOrderSn());
        tvOrderDate.setText(orderOuter.getDate());
        tvTotalMoney.setText(orderOuter.getTotalFormatted());

        //GET middle order datas
        MyAccountOrderMiddle[] orderMiddleFromOuter = orderOuter.getSuborders();
        MyAccountOrderMiddle[] orderMiddles = orderMiddleFromOuter;

        //TO PUT middle datas into outer body
        LinearLayout llOuterBody = (LinearLayout) convertView.findViewById(R.id.ll_myaccount_order_cellouter_body);

        llOuterBody.removeAllViews();

        for (MyAccountOrderMiddle orderMiddle : orderMiddles) {
            //Constructor middle view
            View view_middle = inflater.inflate(R.layout.fragment_myaccount_orders_cell_middle, null);

            TextView tvShippedId = (TextView) view_middle.findViewById(R.id.tv_myaccount_order_cell_middle_shipped_id);//middle datas
            tvShippedId.setText(orderMiddle.getId());

            TextView tvStatus = (TextView) view_middle.findViewById(R.id.tv_myaccount_order_cell_middle_shipped_text);
            tvStatus.setText(orderMiddle.getStatus() + ": ");

            //To put inner datas into middle body
            LinearLayout llMiddleBody = (LinearLayout) view_middle.findViewById(R.id.ll_myaccount_order_cellmiddle_body);

            //GET Inner order datas
            MyAccountOrderInner[] orderInners = orderMiddle.getItems();

            for (MyAccountOrderInner orderInner : orderInners) {
                //Constructor inner view
                View view_inner = inflater.inflate(R.layout.fragment_checkout_review_shopping_cart_cell, null);

                TextView tvBrand = (TextView) view_inner.findViewById(R.id.tv_checkout_review_shoppingcart_cell_brand);
                TextView tvProductName = (TextView) view_inner.findViewById(R.id.tv_checkout_review_shoppingcart_cell_productname);
                TextView tvQuantity = (TextView) view_inner.findViewById(R.id.tv_checkout_review_shoppingcart_cell_quantity);
                TextView tvPrice = (TextView) view_inner.findViewById(R.id.tv_checkout_review_shoppingcart_cell_price);
                ImageView image = (ImageView) view_inner.findViewById(R.id.image_checkout_review_shoppingcart_cell);
                TextView tvColorAndSize = (TextView) view_inner.findViewById(R.id.tv_checkout_review_shoppingcart_cell_colorandsize);

                tvBrand.setText(orderInner.getCategory());
                tvProductName.setText(orderInner.getName());
                tvQuantity.setText(orderInner.getQty());
                tvPrice.setText(orderInner.getPrice());

                // Image download
                JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, image, orderInner.getImage(), JToolUtils.dip2px(context, 160), JToolUtils.dip2px(context, 160));

                //GET color and size: {"label":xxx,"value":xxx}
                if (null != orderInner.getOptions() && orderInner.getOptions().size() > 0) {
                    //------------------------------------------------------wipe off "" on both ends to escape Exception caused by Json parsing.
                    //HashMap<String,String> map_colorandsize = gson.fromJson(orderInner.getOptions().substring(1,orderInner.getOptions().length()-1), type_colorandsize);

                    ArrayList<HashMap<String, String>> list = orderInner.getOptions();
                    if (list.size() == 1) {
                        tvColorAndSize.setText(context.getResources().getString(R.string.color) + ": " + list.get(0).get("value"));
                    } else if (list.size() == 2) {

                        tvColorAndSize.setText(context.getResources().getString(R.string.size) + ": " + list.get(1).get("value") + "  " + context.getResources().getString(R.string.color) + ": " + list.get(0).get("value"));
                    }
                }

                llMiddleBody.addView(view_inner);
            }

            llOuterBody.addView(view_middle);
        }

        return convertView;
    }
}
