package com.whitelabel.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.ui.common.BaseAddressFragment;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.logger.Logger;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenuLayout;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenuListView;

import java.util.List;

/**
 * Created by imaginato on 2015/6/29.
 */
public class AddressBookAdapter extends ArrayAdapter<AddressBook> {
    public List<AddressBook> mAddressBook;
    private Context context;
    public interface IChangeMenuDot{
        void updateDot(int position);
    }
    IChangeMenuDot iChangeMenuDot;

    public void setiChangeMenuDot(IChangeMenuDot iChangeMenuDot) {
        this.iChangeMenuDot = iChangeMenuDot;
    }

    public AddressBookAdapter(Context context, List<AddressBook> list) {
        super(context,0,list);
        this.mAddressBook =list;
        this.context = context;
    }

    public  List<AddressBook> getData(){
        return mAddressBook;
    }

    public void updataView(int position, SwipeMenuListView listView, boolean isSelected) {
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        if (position >= visibleFirstPosi && position <= visibleLastPosi) {
            SwipeMenuLayout layout = (SwipeMenuLayout) listView.getChildAt(position - visibleFirstPosi);
            View view= layout.getChildAt(0);
            AddressViewHolder holder = (AddressViewHolder) view.getTag();
            if (isSelected){
                setDotSelected(holder);
            }else {
                setDotDefault(holder);
            }
        }
    }

    public void updateAllViewToDef(SwipeMenuListView listView){
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        for (int i=visibleFirstPosi;i<=visibleLastPosi;i++){
            SwipeMenuLayout layout = (SwipeMenuLayout) listView.getChildAt(i);
            View view= layout.getChildAt(0);
            AddressViewHolder holder = (AddressViewHolder) view.getTag();
            setDotDefault(holder);
        }
    }

    private  void setDotSelected(AddressViewHolder viewHolder){
        viewHolder.llAddressbookCellPoint.setTag("1");
        viewHolder.tvAddressbookCellPoint1.setBackgroundResource(R.drawable.button_oval_grey);
        viewHolder.tvAddressbookCellPoint2.setBackgroundResource(R.drawable.button_oval_grey);
        viewHolder.tvAddressbookCellPoint3.setBackgroundResource(R.drawable.button_oval_grey);
    }

    private void setDotDefault(AddressViewHolder viewHolder){
        viewHolder.llAddressbookCellPoint.setTag("0");
        viewHolder.tvAddressbookCellPoint1.setBackground(JImageUtils.getThemeCircle(context));
        viewHolder.tvAddressbookCellPoint2.setBackground(JImageUtils.getThemeCircle(context));
        viewHolder.tvAddressbookCellPoint3.setBackground(JImageUtils.getThemeCircle(context));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view=convertView;
        final AddressViewHolder holder;
        if(view==null){
            view=LayoutInflater.from(context).inflate(R.layout.fragment_addressbook_item,null);
            holder=new AddressViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (AddressViewHolder) view.getTag();
        }
        setDotDefault(holder);
        final   AddressBook addreddBean = mAddressBook.get(position);
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

        holder.llAddressbookCellPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iChangeMenuDot!=null){
                    iChangeMenuDot.updateDot(position);
                }
            }
        });
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
        LinearLayout llAddressbookCellPoint;
        TextView tvAddressbookCellPoint1;
        TextView tvAddressbookCellPoint2;
        TextView tvAddressbookCellPoint3;

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
            llAddressbookCellPoint= (LinearLayout) view.findViewById(R.id.ll_addressbook_cell_point);
            tvAddressbookCellPoint1= (TextView) view.findViewById(R.id.tv_addressbook_cell_point1);
            tvAddressbookCellPoint2= (TextView) view.findViewById(R.id.tv_addressbook_cell_point2);
            tvAddressbookCellPoint3= (TextView) view.findViewById(R.id.tv_addressbook_cell_point3);
        }
    }

}
