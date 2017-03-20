package com.whitelabel.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.model.MyAccountOrderListCellNew;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by imaginato on 2015/6/29.
 */
public class MyAccountOrderListNewAdapter extends ArrayAdapter {

    public ArrayList<MyAccountOrderListCellNew> mbeans;
    private Context context;
    private HomeActivity homeActivity;
    private String TAG = MyAccountOrderListNewAdapter.class.getSimpleName();
    private final ImageLoader mImageLoader;

    public MyAccountOrderListNewAdapter(Context context, ArrayList<MyAccountOrderListCellNew> list, ImageLoader imageLoader) {
        super(context, 0, list);
        this.context = context;
        this.mbeans = list;
        mImageLoader = imageLoader;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_myorder_list_new_item, null);
//            viewHolder = new ViewHolder();
//            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_orderlist_new);
//            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_orderlist_new_name);
//            viewHolder.tvQty = (TextView) convertView.findViewById(R.id.tv_orderlist_new_qty);
//            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_orderlist_new_status);
//            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_orderlist_new_time);

            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_myorder_list_new_item1, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_orderlist_new);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_orderlist_new_name);
            viewHolder.tvQty = (TextView) convertView.findViewById(R.id.tv_orderlist_new_qty);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_orderlist_new_status);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_orderlist_new_pri);
            viewHolder.tvExt = (TextView) convertView.findViewById(R.id.tv_orderlist_new_ext);
            viewHolder.tvStatusMark = (TextView) convertView.findViewById(R.id.order_status);
            viewHolder.tvColorsAndSize = (TextView) convertView.findViewById(R.id.tv_orderlist_new_det);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JLogUtils.i(TAG, "MyAccountOrderListNewAdapter====" + position);

        MyAccountOrderListCellNew orderListCellNew = mbeans.get(position);
        viewHolder.tvName.setText(orderListCellNew.getName());
        //      viewHolder.tvQty.setText(context.getResources().getString(R.string.Quantity__) + orderListCellNew.getQty());
//        viewHolder.tvTime.setText(orderListCellNew.getTime());
        viewHolder.tvQty.setText(orderListCellNew.getQty());
        viewHolder.tvPrice.setText(orderListCellNew.getPrice());
        viewHolder.tvExt.setText("Quantity: " + orderListCellNew.getQty());

        /**
         * set background of status according to status code:
         *
         * status in frontend       actual status       status code
         *
         * Shipped                  Shipped             shipped
         * Delivered                Delivered           delivered
         * Processing               Processing          processing
         * Processing               Packed              dropship_tracking_number_entered
         * Suborder Verified        Suborder Verified   suborder_verified
         * Delivery Failed          Delivery Failed     delivery_failed
         * Pending                  Pending             pending
         * Canceled                 Canceled            canceled
         */
        viewHolder.tvStatus.setText(orderListCellNew.getStatus());
        JViewUtils.setStatus(viewHolder.tvStatusMark, orderListCellNew.getStatusCode());

        if (orderListCellNew.getImage() != null && !orderListCellNew.equals(viewHolder.imageView.getTag())) {
            JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, viewHolder.imageView, orderListCellNew.getImage(), JToolUtils.dip2px(context, 85), JToolUtils.dip2px(context, 85));
        } else {
            if (orderListCellNew.getImage() == null) {
                JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, viewHolder.imageView, orderListCellNew.getImage(), JToolUtils.dip2px(context, 85), JToolUtils.dip2px(context, 85));
            }
        }

        ArrayList<HashMap<String, String>> colorAndSizes = orderListCellNew.getOptions();
        if (colorAndSizes != null && colorAndSizes.size() > 0) {
            viewHolder.tvColorsAndSize.setVisibility(View.VISIBLE);
            if (1 == colorAndSizes.size()) {
                HashMap<String, String> map = colorAndSizes.get(0);
                //viewHolder.tvColorsAndSize.setText(map.get("label") + ": " + map.get("value"));
                viewHolder.tvColorsAndSize.setText(map.get("value"));
            } else if (2 == colorAndSizes.size()) {//two attribute
                String colorAndSize;
//                if ("Color".equalsIgnoreCase(colorAndSizes.get(0).get("label"))) {
//
//                    colorAndSize= colorAndSizes.get(0).get("label") + ": " + colorAndSizes.get(0).get("value");
//                    colorAndSize += "  " + colorAndSizes.get(1).get("label") + ": " + colorAndSizes.get(1).get("value");
////                    colorAndSize=colorAndSizes.get(0).get("value")+" | ";
////                    colorAndSize += colorAndSizes.get(1).get("value");
//
//                } else {
//
//                    colorAndSize= colorAndSizes.get(1).get("label") + ": " + colorAndSizes.get(1).get("value");
//                    colorAndSize += "  " + colorAndSizes.get(0).get("label") + ": " + colorAndSizes.get(0).get("value");
////                    colorAndSize=colorAndSizes.get(1).get("value")+" | ";
////                    colorAndSize +=colorAndSizes.get(0).get("value");
//                }
                if ("Color".equalsIgnoreCase(colorAndSizes.get(0).get("label"))) {
                    colorAndSize = colorAndSizes.get(0).get("value");
                    colorAndSize += " | " + colorAndSizes.get(1).get("value");
                } else {
                    colorAndSize = colorAndSizes.get(1).get("value");
                    colorAndSize += " | " + colorAndSizes.get(0).get("value");
                }
                viewHolder.tvColorsAndSize.setText(colorAndSize);
            }
        } else {
            viewHolder.tvColorsAndSize.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView tvName, tvQty, tvStatus, tvTime, tvPrice, tvExt, tvStatusMark, tvColorsAndSize;
    }
}
