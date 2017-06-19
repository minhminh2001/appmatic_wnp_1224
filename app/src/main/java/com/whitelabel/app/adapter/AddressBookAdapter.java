package com.whitelabel.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.utils.JLogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imaginato on 2015/6/29.
 */
public class AddressBookAdapter extends ArrayAdapter<AddressBook> {
    public List<AddressBook> list;
    private Context context;

    public AddressBookAdapter(Context context,List<AddressBook> list) {
        super(context,0,list);
        this.list=list;
        this.context = context;
        AddressBookAdapter adapter = this;
    }


    public  List<AddressBook> getData(){
        return list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        AddressViewHolder holder=null;
        if(view==null){
            view=LayoutInflater.from(context).inflate(R.layout.fragment_addressbook_item,null);
            holder=new AddressViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (AddressViewHolder) view.getTag();
        }
        final   AddressBook addreddBean = list.get(position);
        if("1".equals(addreddBean.getPrimaryBilling())){
            holder.tvDefault.setVisibility(View.VISIBLE);
            holder.tvDefault.setText(context.getResources().getString(R.string.address_default_billing));
        }else if("1".equals(addreddBean.getPrimaryShipping())){
            holder.tvDefault.setVisibility(View.VISIBLE);
            holder.tvDefault.setText(context.getResources().getString(R.string.address_default_shipping));
        }else{
            holder.tvDefault.setVisibility(View.GONE);
        }
        holder.tvname.setText(addreddBean.getFirstName()+" "+addreddBean.getLastName());
        holder.tvsecond.setText(addreddBean.getStreet().get(0));
        if(TextUtils.isEmpty(addreddBean.getStreet().get(1))){
            holder.tvthird.setVisibility(View.GONE);
        }else{
            holder.tvthird.setText(addreddBean.getStreet().get(1));
            holder.tvthird.setVisibility(View.VISIBLE);
        }
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder= stringBuilder.append(addreddBean.getCity());
        if(!TextUtils.isEmpty(addreddBean.getRegion())){
            stringBuilder=stringBuilder.append(", ").append(addreddBean.getRegion());
        }
        if(!TextUtils.isEmpty(addreddBean.getPostcode())){
            stringBuilder=stringBuilder.append(", ").append(addreddBean.getPostcode());
        }
        holder.tvaddress.setText(stringBuilder.toString());
        holder.tvmalaysia.setText(addreddBean.getCountry());
        holder.tvtel.setText(" : "+addreddBean.getTelephone());
        holder.tvDayPhoneValue.setText(" : "+addreddBean.getFax());
        return view;
    }
    private static class AddressViewHolder extends RecyclerView.ViewHolder{
        TextView tvname;
        TextView tvsecond;
        TextView tvthird;
        TextView tvaddress ;
        TextView tvmalaysia ;
        TextView tvtel ;
        TextView tvLine;
        View ivDefault;
        TextView tvDefault;
        // item 主要内容
        TextView tvDayPhoneValue;
        RelativeLayout rlAddressItem;

        public AddressViewHolder(View view) {
            super(view);
            tvDayPhoneValue= (TextView) view.findViewById(R.id.tv_day_phone_value);
            tvname = (TextView) view.findViewById(R.id.AddAddress_name_textview);
            tvsecond = (TextView) view.findViewById(R.id.AddAddress_second_textview);
            tvthird= (TextView) view.findViewById(R.id.AddAddress_third_textview);
            tvaddress = (TextView) view.findViewById(R.id.AddAddress_address_textview);
            tvDefault= (TextView) view.findViewById(R.id.tv_default_address);
            tvmalaysia = (TextView) view.findViewById(R.id.AddAddress_malaysia_textview);
            tvtel = (TextView) view.findViewById(R.id.AddAddress_tel_textview);

            ivDefault=  view.findViewById(R.id.iv_address_default);
            rlAddressItem=(RelativeLayout) view.findViewById(R.id.rl_address_item);
        }
    }

}
