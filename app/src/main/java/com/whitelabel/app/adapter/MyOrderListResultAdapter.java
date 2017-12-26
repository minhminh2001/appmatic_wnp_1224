package com.whitelabel.app.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.model.MyAccountOrderInner;
import com.whitelabel.app.model.MyAccountOrderMiddle;
import com.whitelabel.app.model.MyAccountOrderOuter;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/4/7.
 */
public class MyOrderListResultAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MyAccountOrderOuter> entityResult;
    private final ImageLoader mImageLoader;

    public MyOrderListResultAdapter(Context context, ArrayList<MyAccountOrderOuter> entityResult, ImageLoader imageLoader) {
        this.context = context;
        this.entityResult = entityResult;
        mImageLoader = imageLoader;
    }

    private ArrayList initDate(ArrayList<MyAccountOrderOuter> result) {
        return null;
    }

    @Override
    public int getCount() {
        return entityResult.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.order_list_tabs, null);
        } else {
            view = null;
            OrderHolder orderHolder = new OrderHolder();
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.order_list_item, null);
            }
            orderHolder.orderNumber = (TextView) view.findViewById(R.id.tv_myaccount_orderlist_ordernumber);
            orderHolder.orderDate = (TextView) view.findViewById(R.id.tv_myaccount_orderlist_date);
            orderHolder.orderStatus = (TextView) view.findViewById(R.id.tv_myaccount_orderlist_status);
            // orderHolder.orderEvery=(LinearLayout)view.findViewById(R.id.order_list_every);

            if (!TextUtils.isEmpty(entityResult.get(i - 1).getOrderSn())) {
                orderHolder.orderNumber.setText("Order No. " + entityResult.get(i - 1).getOrderSn());
            }
            orderHolder.orderDate.setText(entityResult.get(i - 1).getDate());
            orderHolder.orderStatus.setText(entityResult.get(i - 1).getStatus());
            List<MyAccountOrderMiddle> orderlist = entityResult.get(i - 1).getSuborders();

            orderHolder.orderEvery.removeAllViews();
            SubOrderHolder subOrderHolder = new SubOrderHolder();

            for (int j = 0; j < orderlist.size(); j++) {
                for (int k = 0; k < orderlist.get(j).getItems().size(); k++) {
                    View itemView = LayoutInflater.from(context).inflate(R.layout.fragment_myorder_list_new_item1, null);
                    MyAccountOrderInner orderInners = orderlist.get(j).getItems().get(k);
                    subOrderHolder.orderImage = (ImageView) itemView.findViewById(R.id.iv_orderlist_new);
                    subOrderHolder.orderName = (TextView) itemView.findViewById(R.id.tv_orderlist_new_name);
                    subOrderHolder.orderCS = (TextView) itemView.findViewById(R.id.tv_orderlist_new_det);
                    subOrderHolder.orderPrice = (TextView) itemView.findViewById(R.id.tv_orderlist_new_pri);
                    subOrderHolder.orderNum = (TextView) itemView.findViewById(R.id.tv_orderlist_new_ext);
                    subOrderHolder.orderStatus = (TextView) itemView.findViewById(R.id.order_status);
                    subOrderHolder.orderNewStatus = (TextView) itemView.findViewById(R.id.tv_orderlist_new_status);

                    JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, subOrderHolder.orderImage, orderInners.getImage(), JToolUtils.dip2px(context, 85), JToolUtils.dip2px(context, 85));
                    subOrderHolder.orderName.setText(orderInners.getName());
                    setCS(subOrderHolder.orderCS, orderInners);
                    subOrderHolder.orderPrice.setText(orderInners.getPrice());
                    subOrderHolder.orderNum.setText("Quantity: " + orderInners.getQty());
                    subOrderHolder.orderNewStatus.setText(orderlist.get(j).getStatus());
                    JViewUtils.setStatus(subOrderHolder.orderStatus, orderlist.get(j).getStatusCode());
                    orderHolder.orderEvery.addView(itemView);
                }
            }
        }
        return view;
    }

    public void setCS(TextView textView, MyAccountOrderInner orderInners) {
        ArrayList<HashMap<String, String>> colorAndSizes = orderInners.getOptions();
        if (colorAndSizes != null && colorAndSizes.size() > 0) {
            textView.setVisibility(View.VISIBLE);
            if (1 == colorAndSizes.size()) {
                HashMap<String, String> map = colorAndSizes.get(0);
                textView.setText(map.get("label") + ": " + map.get("value"));

            } else if (2 == colorAndSizes.size()) {//two attribute
                String colorAndSize;
                if ("Color".equalsIgnoreCase(colorAndSizes.get(0).get("label"))) {
                    colorAndSize = colorAndSizes.get(0).get("value");
                    colorAndSize += " | " + colorAndSizes.get(1).get("value");
                } else {
                    colorAndSize = colorAndSizes.get(1).get("value");
                    colorAndSize += " | " + colorAndSizes.get(0).get("value");
                }
                textView.setText(colorAndSize);
            }
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public class OrderHolder {
        TextView orderNumber;
        TextView orderDate;
        TextView orderStatus;
        LinearLayout orderEvery;
    }

    public class SubOrderHolder {
        ImageView orderImage;
        TextView orderName;
        TextView orderCS;
        TextView orderPrice;
        TextView orderNum;
        TextView orderStatus;
        TextView orderNewStatus;
    }
}

