package com.whitelabel.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.whitelabel.app.R;
import com.whitelabel.app.model.NotificationInfo;

import java.util.ArrayList;

/**
 * Created by Aaron on 2018/3/22.
 */
public class NotificationListAdapter extends BaseAdapter {

    private ArrayList<NotificationInfo> notifications;
    private Context context;

    public NotificationListAdapter(Context context, ArrayList<NotificationInfo> list) {
        this.notifications = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        NotificationInfo notification = notifications.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_home_notification_list, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder {

    }
}
