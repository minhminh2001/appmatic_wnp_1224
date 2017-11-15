package com.whitelabel.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.GlobalData;
import com.whitelabel.app.R;
import com.whitelabel.app.model.MyAccountOrderOuter;
import com.whitelabel.app.widget.RefreshLoadMoreRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by img on 2017/11/10.
 */

@SuppressWarnings("ALL")
public class OrderListRecyclerViewTextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final Context mContext;
    private final List<MyAccountOrderOuter> data;
    public OrderListRecyclerViewTextAdapter(Context context,@Nullable List<MyAccountOrderOuter> data) {
        this.data=data;
        this.mContext=context;
    }

    private OnOrderTextViewItemClickListener onOrderTextViewItemClickListener;

    public interface OnOrderTextViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnOrderTextViewItemClickListener(OnOrderTextViewItemClickListener onOrderTextViewItemClickListener) {
        this.onOrderTextViewItemClickListener = onOrderTextViewItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==GlobalData.ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_myorder_list_new_item_text, parent,false);
            return new OrderListholder(view);
        }else if (viewType==GlobalData.FOOTER){
            RefreshLoadMoreRecyclerView.CustomDragRecyclerFooterView footerView = new RefreshLoadMoreRecyclerView.CustomDragRecyclerFooterView(parent.getContext());
            return new VHFooter(footerView);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof OrderListholder){
            OrderListholder orderHolder= (OrderListholder) holder;
            MyAccountOrderOuter myAccountOrderOuter = data.get(position);
            orderHolder.tvOrderNo.setText(this.mContext.getString(R.string.Order_Number)+":"+myAccountOrderOuter.getOrderSn());
            orderHolder.tvOrderDate.setText(this.mContext.getString(R.string.Order_Date)+myAccountOrderOuter.getDate());
            orderHolder.tvOrderTotal.setText(this.mContext.getString(R.string.product_detail_total)+myAccountOrderOuter.getTotalFormatted());
            orderHolder.tvOrderStatus.setText(this.mContext.getString(R.string.Order_Status)+myAccountOrderOuter.getStatus());
            orderHolder.rlParentInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOrderTextViewItemClickListener!=null){
                        onOrderTextViewItemClickListener.onItemClick(v,holder.getAdapterPosition());
                    }
                }
            });
        }else if (holder instanceof VHFooter){
            ((VHFooter) holder).footerView.setState(RefreshLoadMoreRecyclerView.CustomDragRecyclerFooterView.STATE_READY);
        }
    }

    @Override
    public int getItemCount() {
        return this.data.isEmpty()?0:this.data.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositonFooter(position)){
            return GlobalData.FOOTER;
        }else {
            return GlobalData.ITEM;
        }
    }

    private boolean isPositonFooter(int position) {
        return this.data.isEmpty() && position == 1 || position == data.size();
    }

    private class VHFooter extends RecyclerView.ViewHolder {
        public final RefreshLoadMoreRecyclerView.CustomDragRecyclerFooterView
                footerView;

        public VHFooter(View itemView) {
            super(itemView);
            footerView = (RefreshLoadMoreRecyclerView.CustomDragRecyclerFooterView) itemView;
        }
    }

    public static class OrderListholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_order_no)
        TextView tvOrderNo;
        @BindView(R.id.tv_order_date)
        TextView tvOrderDate;
        @BindView(R.id.tv_order_total)
        TextView tvOrderTotal;
        @BindView(R.id.tv_order_status)
        TextView tvOrderStatus;
        @BindView(R.id.rl_parent_info)
        RelativeLayout rlParentInfo;
        public OrderListholder(View view) {
            super(view);
            ButterKnife.bind(this,view);

        }
    }
}
