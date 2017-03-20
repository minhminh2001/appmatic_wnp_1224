package com.whitelabel.app.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.model.StoreCreditItemBean;
import com.whitelabel.app.utils.JDataUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class StoreCreditAdapter extends ArrayAdapter<StoreCreditItemBean> {
    private List<StoreCreditItemBean> beans;
    public StoreCreditAdapter(Context context,List<StoreCreditItemBean> beans){
        super(context,R.layout.item_store_credit,beans);
        this.beans=beans;
    }
    class Holder{
        TextView tvName;
        TextView tvDate;
        TextView tvPrice;
        TextView tvCredit;
        TextView tvType;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        Holder holder=null;
        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.item_store_credit,null);
            holder=new Holder();
            holder.tvName= (TextView) view.findViewById(R.id.tv_name);
            holder.tvDate= (TextView) view.findViewById(R.id.tv_date);
            holder.tvPrice= (TextView) view.findViewById(R.id.tv_price);
            holder.tvCredit= (TextView) view.findViewById(R.id.tv_credit);
            holder.tvType= (TextView) view.findViewById(R.id.tv_type);
            view.setTag(holder);
        }else{
            holder= (Holder) view.getTag();
        }
        StoreCreditItemBean bean=beans.get(position);
        if(TextUtils.isEmpty(bean.getReference())){
            holder.tvName.setVisibility(View.GONE);
        }else {
            holder.tvName.setVisibility(View.VISIBLE);
            holder.tvName.setText(bean.getReference());
        }
        holder.tvCredit.setText(JDataUtils.formatThousand(bean.getBalance()));
        if(Double.parseDouble(bean.getPoints())>0){
            holder.tvPrice.setTextColor(getContext().getResources().getColor(R.color.black));
        }else{
            holder.tvPrice.setTextColor(getContext().getResources().getColor(R.color.red));
        }
        holder.tvPrice.setText(JDataUtils.formatThousand(bean.getPoints()));
        holder.tvDate.setText(bean.getDate());
        holder.tvType.setText(bean.getType());

        return view;
    }
}
