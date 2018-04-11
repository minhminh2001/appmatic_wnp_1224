package com.whitelabel.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.logger.Logger;
import com.whitelabel.app.widget.swipe.SwipeLayout;
import com.whitelabel.app.widget.swipe.SwipeLayoutCallBack;
import com.whitelabel.app.widget.swipe.SwipeableAdapter;

import java.util.List;

public class AddressBookAdapterV2 extends SwipeableAdapter {
    public List<AddressBook> mAddressBook;
    private Context context;

    public interface ISwipeClick{
        void onEditClick(int position);
        void onDelClick(int position);
    }
    public interface IOnItemClick{
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }
    IOnItemClick iOnItemClick;
    private ISwipeClick iSwipeClick;
    private boolean isSwipeDelVisible;

    public void setiSwipeClick(ISwipeClick iSwipeClick) {
        this.iSwipeClick = iSwipeClick;
    }

    public void setiOnItemClick(IOnItemClick iOnItemClick) {
        this.iOnItemClick = iOnItemClick;
    }

    public AddressBookAdapterV2(Context context, List<AddressBook> list, boolean isSwipeDelVisible) {
        this.mAddressBook =list;
        this.context = context;
        this.isSwipeDelVisible=isSwipeDelVisible;
    }

    public  List<AddressBook> getData(){
        return mAddressBook;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.fragment_addressbook_item_v2,null);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final AddressViewHolder holder= (AddressViewHolder) viewHolder;
        setDotDefault(holder);
        final   AddressBook addreddBean = mAddressBook.get(position);
        if("1".equals(addreddBean.getPrimaryBilling())){
            holder.tvDefault.setVisibility(View.VISIBLE);
            holder.tvDefault.setText(context.getResources().getString(R.string.address_default_billing));
            holder.ivAddressDelete.setVisibility(View.GONE);
            holder.ivAddressEdit.setVisibility(View.VISIBLE);
        }else if("1".equals(addreddBean.getPrimaryShipping())){
            holder.tvDefault.setVisibility(View.VISIBLE);
            holder.tvDefault.setText(context.getResources().getString(R.string.address_default_shipping));
            holder.ivAddressDelete.setVisibility(View.GONE);
            holder.ivAddressEdit.setVisibility(View.VISIBLE);
        }else if (isSwipeDelVisible){
            holder.tvDefault.setVisibility(View.GONE);
            holder.ivAddressDelete.setVisibility(View.VISIBLE);
            holder.ivAddressEdit.setVisibility(View.VISIBLE);
        }else {
            holder.tvDefault.setVisibility(View.GONE);
            holder.ivAddressDelete.setVisibility(View.GONE);
            holder.ivAddressEdit.setVisibility(View.VISIBLE);
        }
        holder.recyclerviewSwipe.setDragEdge(SwipeLayout.DragEdge.Right);
        holder.recyclerviewSwipe.setShowMode(SwipeLayout.ShowMode.PullOut);
        //用于管理所有item开闭状态的
        holder.recyclerviewSwipe.setCallBack(this);
        holder.recyclerviewSwipe.close();
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
        holder.rlAddressItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iOnItemClick!=null){
                    iOnItemClick.onItemClick(null,holder.itemView,position,v.getId());
                }
                String swipeOpenStatus = "1";
                if (swipeOpenStatus.equals(holder.recyclerviewSwipe.getTag())) {
                    holder.recyclerviewSwipe.toggle();
                }
            }
        });
        holder.ivAddressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iSwipeClick!=null){
                    holder.recyclerviewSwipe.close();
                    iSwipeClick.onEditClick(position);
                }
            }
        });
        holder.ivAddressDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iSwipeClick!=null){
                    holder.recyclerviewSwipe.close();
                    iSwipeClick.onDelClick(position);
                }
            }
        });
        holder.llAddressbookCellPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            holder.recyclerviewSwipe.toggle();
            }
        });
        holder.recyclerviewSwipe.setStatuCallBack(new SwipeLayoutCallBack() {
            @Override
            public void swipeCallback(SwipeLayout v, int type) {
                boolean drakColor = false;
                if (type == SwipeLayoutCallBack.type_add) {
                    drakColor = false;
                } else if (type == SwipeLayoutCallBack.type_re) {
                    drakColor = true;
                }
                setPonitColor(holder, drakColor);
            }
        });
        holder.tvaddress.setText(stringBuilder.toString());
        holder.tvmalaysia.setText(addreddBean.getCountry());
        holder.tvtel.setText(addreddBean.getTelephone());
        holder.tvDayPhoneValue.setText(addreddBean.getFax());
    }

    private void setPonitColor(AddressViewHolder viewHolder, boolean darkColor) {
        if (!darkColor) {
            setDotSelected(viewHolder);
        } else {
            setDotDefault(viewHolder);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (mAddressBook!=null&& !mAddressBook.isEmpty()){
            return mAddressBook.size();
        }else {
            return 0;
        }

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
        SwipeLayout recyclerviewSwipe;
        ImageView ivAddressEdit;
        ImageView ivAddressDelete;


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
            recyclerviewSwipe= (SwipeLayout) view.findViewById(R.id.recyclerview_swipe);
            ivAddressEdit= (ImageView) view.findViewById(R.id.iv_address_edit);
            ivAddressDelete= (ImageView) view.findViewById(R.id.iv_address_delete);
        }
    }

}
