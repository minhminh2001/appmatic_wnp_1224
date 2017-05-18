package com.whitelabel.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.model.NotificationCell;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/9/24.
 */
public class HomeNotificationListAdapter extends BaseAdapter {

    private ArrayList<NotificationCell> list;
    private Context context;

    public HomeNotificationListAdapter(Context context, ArrayList<NotificationCell> list) {
        this.list = list;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_home_notification_list, null);
            viewHolder = new ViewHolder();
            viewHolder.ivDot = (ImageView) convertView.findViewById(R.id.iv_notification_dot);
            viewHolder.tvTile = (TextView) convertView.findViewById(R.id.tv_notification_title);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_notification_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NotificationCell notificationCell = list.get(position);
        String title = notificationCell.getTitle();
        title = title.trim();
        viewHolder.tvTile.setText(title);
        viewHolder.tvTime.setText(notificationCell.getCreated_at());

        if (notificationCell.getUnread() == 1) {//unRead
//            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
            convertView.setBackgroundResource(R.drawable.selector_helpcenterlist_and_notificationlist);
            viewHolder.ivDot.setVisibility(View.VISIBLE);

        } else {//Read
//            convertView.setBackgroundColor(context.getResources().getColor(R.color.greyF8F8F8));
            convertView.setBackgroundResource(R.drawable.selector_helpcenterlist_and_notificationlist2);
            viewHolder.ivDot.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView ivDot;
        private TextView tvTile, tvTime;
    }
}
