package com.whitelabel.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.bean.OrderBody;
import com.whitelabel.app.bean.OrderTip;
import com.whitelabel.app.model.MyAccountOrderInner;
import com.whitelabel.app.model.MyAccountOrderMiddle;
import com.whitelabel.app.model.MyAccountOrderOuter;
import com.whitelabel.app.model.MyAccountOrderTrackingInfo;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.RefreshLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/4/11.
 */
public class OrderListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 436874;
    private static final int TYPE_ITEM = 256478;
    private static final int TYPE_BODY_ITEM = 10000;
    private static final int TYPE_FOOTER = 9621147;
    private final Context context;
    private boolean loadMore;
    private final ArrayList dataList;
    private final ImageLoader mImageLoader;

    @SuppressWarnings("unchecked")
    public ArrayList getDataList(ArrayList dataList) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < dataList.size(); i++) {
            MyAccountOrderOuter myAccountOrderOuter = (MyAccountOrderOuter) dataList.get(i);
            OrderTip orderTip = new OrderTip();
            orderTip.setOrderNumber(myAccountOrderOuter.getOrderSn());
            orderTip.setOrderDate(myAccountOrderOuter.getDate());
            orderTip.setOrderSttus(myAccountOrderOuter.getStatus());
            orderTip.setOrderId(myAccountOrderOuter.getOrderId());
            arrayList.add(orderTip);
            MyAccountOrderMiddle[] orderlist = myAccountOrderOuter.getSuborders();
            for (int j = 0; j < orderlist.length; j++) {
                MyAccountOrderTrackingInfo trackingInfo = null;
                if (orderlist[j].getTrackingInfo() != null) {
                  trackingInfo = orderlist[j].getTrackingInfo();
                }
                for (int k = 0; k < orderlist[j].getItems().length; k++) {
                    MyAccountOrderInner orderInners = orderlist[j].getItems()[k];
                    OrderBody orderBody = new OrderBody();
                    orderBody.setOrderNumber(myAccountOrderOuter.getOrderSn());
                    orderBody.setAvailability(orderInners.getAvailability());
                    orderBody.setOrderCs(setCS(orderInners));
                    orderBody.setOrderImage(orderInners.getImage());
                    orderBody.setIsRPayment(myAccountOrderOuter.getIsRPayment());
                    orderBody.setOrderName(orderInners.getName());
                    orderBody.setOrderPrice(orderInners.getPrice());
                    orderBody.setOrderQuantity(orderInners.getQty());
                    orderBody.setOrderTextStatus(myAccountOrderOuter.getStatus());
                    orderBody.setOrderStatusCode(myAccountOrderOuter.getStatusCode());
                    orderBody.setMerchantName(orderInners.getVendorDisplayName());
                    orderBody.setVendor_id(orderInners.getVendor_id());
                    orderBody.setOrderId(myAccountOrderOuter.getOrderId());
                    if(trackingInfo!=null){
                        orderBody.setTrickingTitle(trackingInfo.getTitle());
                        orderBody.setTrickingUrl(trackingInfo.getUrl());
                    }
                    if(j==orderlist.length-1){
                        orderBody.setLast(true);
                    }
                    arrayList.add(orderBody);
                }
            }
        }
        return arrayList;
    }
    public OrderListRecyclerViewAdapter(Context context, ArrayList dataList, boolean pullEnable, ImageLoader imageLoader) {
        this.context = context;
        this.dataList = dataList;
        this.loadMore = pullEnable;
        mImageLoader = imageLoader;
    }

    private OnOrderViewItemClickListener onOrderViewItemClickListener;

    public interface OnOrderViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnOrderViewItemClickListener(OnOrderViewItemClickListener onOrderViewItemClickListener) {
        this.onOrderViewItemClickListener = onOrderViewItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(context).inflate(R.layout.order_list_item, null);
            return new OrderListholder(view);
        } else if (viewType == TYPE_BODY_ITEM) {
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_myorder_list_new_item1, null);
            return new SubOrderHolder(view);
        }
//        else if (viewType == TYPE_HEADER) {
//            View headerView = new AlxRefreshLoadMoreRecyclerView.CustomDragHeaderView(parent.getContext());
//            return new VHHeader(headerView);
//        }
        else if (viewType == TYPE_FOOTER) {
            RefreshLoadMoreRecyclerView.CustomDragRecyclerFooterView footerView = new RefreshLoadMoreRecyclerView.CustomDragRecyclerFooterView(parent.getContext());
            return new VHFooter(footerView);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    public void setPullLoadMoreEnable(boolean enable) {
        this.loadMore = enable;
    }

    public boolean getPullLoadMoreEnable() {
        return loadMore;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {//相当于getView
        ArrayList arrayList = getDataList(dataList);
        if (holder instanceof OrderListholder) {
            OrderTip orderTip = (OrderTip) arrayList.get(position);
            final OrderListholder orderListholder = (OrderListholder) holder;
            orderListholder.orderNumber.setText("Order No. " + orderTip.getOrderNumber());
            orderListholder.orderDate.setText(orderTip.getOrderDate());
            orderListholder.orderStatus.setText(orderTip.getOrderSttus());
            orderListholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOrderViewItemClickListener.onItemClick(orderListholder.itemView, position);
                }
            });
        }  else if (holder instanceof VHFooter) {
//            if(!loadMore){
            ((VHFooter) holder).footerView.setState(RefreshLoadMoreRecyclerView.CustomDragRecyclerFooterView.STATE_READY);
//            }
        } else if (holder instanceof SubOrderHolder) {
            final OrderBody orderBody = (OrderBody) arrayList.get(position);
            final SubOrderHolder subOrderHolder = (SubOrderHolder) holder;
//            JLogUtils.i("OrderListRecyclerViewAdapter", "=============" + String.valueOf(subOrderHolder.orderImage.getTag()));
            if (orderBody.getOrderImage() == null || !orderBody.getOrderImage().equals(String.valueOf(subOrderHolder.orderImage.getTag()))) {
                JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, subOrderHolder.orderImage, orderBody.getOrderImage(), JToolUtils.dip2px(context, 85), JToolUtils.dip2px(context, 85));
            }
            subOrderHolder.orderImage.setTag(orderBody.getOrderImage());
            subOrderHolder.rmTop.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+"");
            subOrderHolder.orderName.setText(orderBody.getOrderName());
            //add to cart button
            if (orderBody.isLast()){
                subOrderHolder.btnOrderListItemAddtocart.setVisibility(View.VISIBLE);
                GradientDrawable myGrad = (GradientDrawable)subOrderHolder.btnOrderListItemAddtocart.getBackground();
                myGrad.setColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
            }else {
                subOrderHolder.btnOrderListItemAddtocart.setVisibility(View.GONE);
            }
            subOrderHolder.btnOrderListItemAddtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOrderViewItemClickListener.onItemClick(v, position);
                }
            });
            if (orderBody.getIsRPayment()==0||!orderBody.isLast()) {
                subOrderHolder.llOrderRePayment.setVisibility(View.GONE);
            } else {
                subOrderHolder.llOrderRePayment.setVisibility(View.VISIBLE);
                JViewUtils.setSoildButtonGlobalStyle(subOrderHolder.itemView.getContext(),subOrderHolder.tvRepayment);
                subOrderHolder.tvRepayment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onOrderViewItemClickListener!=null) {
                            onOrderViewItemClickListener.onItemClick(v, position);
                        }
                    }
                });
            }
            if (("").equals(orderBody.getOrderCs())) {
                subOrderHolder.orderCS.setVisibility(View.GONE);
            } else {
                subOrderHolder.orderCS.setVisibility(View.VISIBLE);
                subOrderHolder.orderCS.setText(orderBody.getOrderCs());
            }


            GradientDrawable myGrad = (GradientDrawable)subOrderHolder.btnOrderListItemAddtocart.getBackground();
            if(!TextUtils.isEmpty(orderBody.getAvailability())&&!"1".equals(orderBody.getAvailability())){
                subOrderHolder.tvUnavailable.setVisibility(View.VISIBLE);
                subOrderHolder.tvTrans.setVisibility(View.VISIBLE);
                myGrad.setColor(ContextCompat.getColor(context,R.color.greyDCDCDC));
                subOrderHolder.btnOrderListItemAddtocart.setEnabled(false);
            }else{
                subOrderHolder.tvUnavailable.setVisibility(View.GONE);
                subOrderHolder.tvTrans.setVisibility(View.GONE);
                myGrad.setColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
                subOrderHolder.btnOrderListItemAddtocart.setEnabled(true);
            }
            subOrderHolder.orderPrice.setText(JDataUtils.formatDouble(orderBody.getOrderPrice()));
            subOrderHolder.orderNum.setText("Quantity: " + orderBody.getOrderQuantity());
            subOrderHolder.orderNewStatus.setText(orderBody.getOrderTextStatus());

            JViewUtils.setStatus(subOrderHolder.orderStatus, orderBody.getOrderStatusCode());
            if (!TextUtils.isEmpty(orderBody.getMerchantName())) {
                String soldBy = subOrderHolder.orderMerName.getContext().getResources().getString(R.string.soldby);
                    subOrderHolder.orderMerName.setText(soldBy + " " + orderBody.getMerchantName());
                    subOrderHolder.orderMerName.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                subOrderHolder.orderMerName.setText("");
            }
            subOrderHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOrderViewItemClickListener.onItemClick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (getDataList(dataList) == null || getDataList(dataList).size() == 0) ? 1 : getDataList(dataList).size() + 1;//如果有header,若list不存在或大小为0就没有footView，反之则有
    }//这里要考虑到头尾部，多以要加2

    /**
     * @param position 根据位置判断这里该用哪个ViewHolder
     * @return
     */
    @Override
    public int getItemViewType(int position) {
//        if (position == 0){
//            return TYPE_HEADER;
//        } else
        if (isPositonFooter(position)) {
            return TYPE_FOOTER;
        } else {
            if (getDataList(dataList).get(position) instanceof OrderTip)
                return TYPE_ITEM;
            else
                return TYPE_BODY_ITEM;
        }
    }

    private boolean isPositonFooter(int position) {//这里的position从0算起
        //如果没有item
        return getDataList(dataList) == null && position == 1 || position == getDataList(dataList).size();
    }

    class VHFooter extends RecyclerView.ViewHolder {
        public final RefreshLoadMoreRecyclerView.CustomDragRecyclerFooterView
                footerView;

        public VHFooter(View itemView) {
            super(itemView);
            footerView = (RefreshLoadMoreRecyclerView.CustomDragRecyclerFooterView) itemView;
        }
    }

    public static class OrderListholder extends RecyclerView.ViewHolder {
        private final TextView orderNumber;
        private final TextView orderDate;
        private final TextView orderStatus;

        public OrderListholder(View view) {
            super(view);
            orderNumber = (TextView) view.findViewById(R.id.tv_myaccount_orderlist_ordernumber);
            orderDate = (TextView) view.findViewById(R.id.tv_myaccount_orderlist_date);
            orderStatus = (TextView) view.findViewById(R.id.tv_myaccount_orderlist_status);
        }
    }

    public static class SubOrderHolder extends RecyclerView.ViewHolder {
        final ImageView orderImage;
        final LinearLayout llOrderRePayment;
        final TextView orderName;
        final TextView orderCS;
        final TextView orderPrice;
        final TextView orderNum;
        final TextView orderStatus;
        final TextView orderNewStatus;
        final TextView orderMerName;
        final TextView tvRepayment;
        final TextView rmTop;
        final TextView tvUnavailable;
        final TextView tvTrans;
        final Button btnOrderListItemAddtocart;

        public SubOrderHolder(View view) {
            super(view);
            tvUnavailable= (TextView) view.findViewById(R.id.order_detail_unavailable);
            orderImage = (ImageView) view.findViewById(R.id.iv_orderlist_new);
            orderName = (TextView) view.findViewById(R.id.tv_orderlist_new_name);
            orderCS = (TextView) view.findViewById(R.id.tv_orderlist_new_det);
            orderPrice = (TextView) view.findViewById(R.id.tv_orderlist_new_pri);
            orderNum = (TextView) view.findViewById(R.id.tv_orderlist_new_ext);
            orderStatus = (TextView) view.findViewById(R.id.order_status);
            orderNewStatus = (TextView) view.findViewById(R.id.tv_orderlist_new_status);
            orderMerName = (TextView) view.findViewById(R.id.tv_orderlist_new_mername);
            tvRepayment = (TextView) view.findViewById(R.id.tv_order_repayment);
            llOrderRePayment= (LinearLayout) view.findViewById(R.id.ll_order_repayment);
            rmTop= (TextView) view.findViewById(R.id.rm_top);
            tvTrans= (TextView) view.findViewById(R.id.order_detail_trans);
            btnOrderListItemAddtocart= (Button) view.findViewById(R.id.btn_order_list_item_addtocart);

        }
    }

    private String setCS(MyAccountOrderInner orderInners) {
        StringBuilder str = new StringBuilder();
        ArrayList<HashMap<String, String>> colorAndSizes = orderInners.getOptions();
        if (colorAndSizes != null && colorAndSizes.size() > 0) {
            for (int i = 0; i < colorAndSizes.size(); i++) {
                str.append(colorAndSizes.get(i).get("value")).append(" | ");
            }
            str = new StringBuilder(str.substring(0, str.length() - 2));
        } else {
            //textView.setVisibility(View.GONE);
            str = new StringBuilder();
        }
        return str.toString();
    }

//    public void setStatus(TextView textView, String statusCode) {
//        if ("pending".equalsIgnoreCase(statusCode)) {
//            //viewHolder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_pending));
//            textView.setBackgroundResource(R.drawable.order_item_status_pending);
//        }
//        /*else  if ("pendingCD".equalsIgnoreCase(orderListCellNew.getStatusCode())) {
//            tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_pending_cd));
//        }*/
//        else if ("processing".equalsIgnoreCase(statusCode)) {
//            //viewHolder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_proccessing));
//            textView.setBackgroundResource(R.drawable.order_item_status_process);
//        } else if (statusCode.contains("verified")) {
//            //viewHolder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_verified));
//            textView.setBackgroundResource(R.drawable.order_item_status_verified);
//        } else if ("shipped".equalsIgnoreCase(statusCode)) {
//            //viewHolder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_shipped));
//            textView.setBackgroundResource(R.drawable.order_item_status_ship);
//        } else if (statusCode.contains("delivered")) {
//            //viewHolder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_delivered));
//            textView.setBackgroundResource(R.drawable.order_item_status_delivered);
//        } else if ("deliveryFailed".equalsIgnoreCase(statusCode)) {
//            //viewHolder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_delivery_failed));
//            textView.setBackgroundResource(R.drawable.order_item_status_failed);
//        } else if (statusCode.contains("canceled")) {
//            //viewHolder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.order_status_canceled));
//            textView.setBackgroundResource(R.drawable.order_item_status_cancel);
//        }
//    }
}
