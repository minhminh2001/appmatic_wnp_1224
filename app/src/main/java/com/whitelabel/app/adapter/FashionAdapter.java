package com.whitelabel.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.model.Fashion;
import com.whitelabel.app.widget.HorizontalListView;

import java.util.LinkedList;

/**
 * Created by imaginato on 2015/6/18.
 */
public class FashionAdapter extends BaseAdapter {
    public LinkedList<Fashion> list;
    private HorizontalListView listView;
    private Context context;
    public FashionAdapter(Context context) {
        this.list = new LinkedList<Fashion>();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_fashion_horizontallistview,null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.fashion_imageview);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.fashion_textView);


        Fashion fh = (Fashion) getItem(position);
        tvTitle.setText(fh.getTitle());
        //图片资源，如果请求服务器失败应当显示默认图片，先这么写着
        imageView.setImageResource(fh.getImage());

        return convertView;
    }
}
