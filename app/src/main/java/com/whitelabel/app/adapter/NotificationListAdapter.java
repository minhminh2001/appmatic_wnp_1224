package com.whitelabel.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.model.NotificationInfo;

import java.util.ArrayList;

/**
 * Created by Aaron on 2018/3/22.
 */
public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    private ArrayList<NotificationInfo> notifications;
    private Context context;
    private OnItemClikListener itemClikListener;

    public NotificationListAdapter(Context context, ArrayList<NotificationInfo> list) {
        this.notifications = list;
        this.context = context;
    }

    public void setItemClickListener(OnItemClikListener listener){
        itemClikListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_home_notification_list, null);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        NotificationInfo notification = notifications.get(position);
        holder.itemView.setTag(notification);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClikListener == null){
                    return;
                }

                NotificationInfo notificationInfo = (NotificationInfo) view.getTag();
                itemClikListener.onItemClick(notificationInfo);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView time;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tv_notification_title);
            time = (TextView) itemView.findViewById(R.id.tv_notification_time);
        }
    }

    public interface OnItemClikListener{
        void onItemClick(NotificationInfo notificationInfo);
    }
}
