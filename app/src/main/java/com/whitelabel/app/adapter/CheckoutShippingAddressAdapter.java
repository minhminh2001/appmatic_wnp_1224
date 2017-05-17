package com.whitelabel.app.adapter;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.model.CheckoutDefaultShippingAddress;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenuListView;

import java.util.LinkedList;

/**
 * Created by Administrator on 2015/6/25.
 */
public class CheckoutShippingAddressAdapter extends BaseAdapter{
    public LinkedList<CheckoutDefaultShippingAddress> list;
    private Context context;
    private SwipeMenuListView listView;
    public CheckoutShippingAddressAdapter(Context context,LinkedList<CheckoutDefaultShippingAddress> list,SwipeMenuListView listView) {
        this.context = context;
        this.list=list;
        this.listView=listView;
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
//    private boolean  bool;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            viewHolder=new ViewHolder();
            convertView =LayoutInflater.from(context).inflate(R.layout.fragment_checkout_shipping_selectaddress_cell,null);
            viewHolder. tvFirstname = (TextView) convertView.findViewById(R.id.tv_address_select_firstname);
            viewHolder. tvLastname = (TextView) convertView.findViewById(R.id.tv_address_select_lastname);
            viewHolder. tvAddress1 = (TextView) convertView.findViewById(R.id.tv_address_select_address1);
            viewHolder. tvAddress2 = (TextView) convertView.findViewById(R.id.tv_address_select_address2);
            viewHolder. tvCityStatePostcode = (TextView) convertView.findViewById(R.id.tv_address_select_citystatepostcode);
            viewHolder. tvCountry = (TextView) convertView.findViewById(R.id.tv_address_select_country);
            viewHolder. tvTelephone = (TextView) convertView.findViewById(R.id.tv_address_select_telephone);
            viewHolder. imageDot = (ImageView) convertView.findViewById(R.id.image_address_select_top);
            viewHolder. vSelected=convertView.findViewById(R.id.AddAddress_select_button);
            viewHolder. mParent=convertView.findViewById(R.id.mparent);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //Button buttonCover = (Button) convertView.findViewById(R.id.btn_address_select_cover);
        CheckoutDefaultShippingAddress address = list.get(position);
        viewHolder.tvFirstname.setText(address.getFirstName()+" "+address.getLastName());
//        tvLastname.setText(address.getLastName());
        viewHolder.tvAddress1.setText(address.getStreet().get(0));
        if(TextUtils.isEmpty(address.getStreet().get(1))){
            viewHolder. tvAddress2.setVisibility(View.GONE);
        }else{
            viewHolder.tvAddress2.setVisibility(View.VISIBLE);
            viewHolder.tvAddress2.setText(address.getStreet().get(1));
        }
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder=stringBuilder.append(address.getCity());
        if(!TextUtils.isEmpty(address.getRegion())) {
            stringBuilder= stringBuilder .append(", ").append( address.getRegion());
        }
        if(!TextUtils.isEmpty(address.getPostcode())){
           stringBuilder=stringBuilder.append(", ").append(address.getPostcode());
        }
        viewHolder.tvCityStatePostcode.setText(stringBuilder.toString());
        viewHolder. tvCountry.setText(address.getCountry());
        viewHolder.tvTelephone.setText(context.getResources().getString(R.string.t)+address.getTelephone());
        if ("1".equals(address.getPrimaryShipping())) {
            viewHolder. vSelected.setVisibility(View.VISIBLE);
            viewHolder. mParent.setBackgroundColor(context.getResources().getColor(R.color.greyC2C2C2));
        }else{
            viewHolder. vSelected.setVisibility(View.INVISIBLE);
            viewHolder.mParent.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }
    private static class ViewHolder {
        TextView tvFirstname, tvLastname, tvAddress1, tvAddress2, tvCityStatePostcode, tvCountry, tvTelephone;
        ImageView imageDot;
        View vSelected,mParent;
    }
//    //局部刷新ListView
    public void updateView(int itemIndex) {
        //得到第一个可显示控件的位置，
        int visiblePosition = listView.getFirstVisiblePosition();
        JLogUtils.i("Allen","visiblePosition="+visiblePosition+"  itemIndex=="+itemIndex);
        //只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
        if (itemIndex - visiblePosition >= 0) {
            //得到要更新的item的view
            View view = listView.getChildAt(itemIndex - visiblePosition);
            JLogUtils.i("Allen","view=="+view);
            //从view中取得holder
            ViewHolder holder = (ViewHolder) view.getTag();
            JLogUtils.i("Allen","holder=="+holder);
            holder.vSelected=view.findViewById(R.id.AddAddress_select_button);
            holder.mParent=view.findViewById(R.id.mparent);
            holder.vSelected.setVisibility(View.VISIBLE);
            holder.mParent.setBackgroundColor(context.getResources().getColor(R.color.greyC2C2C2));
        }
    }

}
