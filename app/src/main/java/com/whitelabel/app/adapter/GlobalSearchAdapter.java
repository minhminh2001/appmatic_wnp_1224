package com.whitelabel.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by imaginato on 2015/7/2.
 */
public class GlobalSearchAdapter extends BaseAdapter {
    private List<Map<String, Object>> list;
    private Context context;
    public GlobalSearchAdapter(Context context) {
        this.list = new ArrayList<Map<String,Object>>();
        this.context = context;
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
        ViewHolder holder=null;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.template_category_listing2,null);
            holder=new ViewHolder();
            holder.imageView=(ImageView) convertView.findViewById(R.id.img);
            holder.text=(TextView) convertView.findViewById(R.id.src1);
            holder.picuter= (TextView) convertView.findViewById(R.id.src2);
            holder.oldprice= (TextView) convertView.findViewById(R.id.src3);
            holder.price= (TextView) convertView.findViewById(R.id.src4);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        int resId=Integer.parseInt(list.get(position).get("image").toString());
        holder.imageView.setImageResource(resId);
        holder.text.setText(list.get(position).get("text").toString());
        holder.picuter.setText(list.get(position).get("picter").toString());
        holder.oldprice.setText(list.get(position).get("oldprice").toString());
        holder.oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        holder.price.setText(list.get(position).get("price").toString());
        return convertView;
    }
    static class ViewHolder{
        ImageView imageView;
        TextView text,picuter,oldprice,price;
    }
}
