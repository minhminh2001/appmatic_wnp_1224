package com.whitelabel.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.model.SelectServiceEntity;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/12/3.
 */
public class SelectServiceAdapter extends BaseAdapter {
    public ArrayList<SelectServiceEntity> list;
    private Context context;
    public SelectServiceAdapter(Context context,ArrayList<SelectServiceEntity> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_select_service,null);
        TextView textView= (TextView) view.findViewById(R.id.serviceName);
        View selectView=view.findViewById(R.id.select);
        textView.setText(list.get(position).getServiceName());
        if(list.get(position).isCurrent()){
            selectView.setVisibility(View.VISIBLE);
        }
        return view;
    }
}
