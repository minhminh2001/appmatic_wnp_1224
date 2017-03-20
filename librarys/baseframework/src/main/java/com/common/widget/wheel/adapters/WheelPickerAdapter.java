package com.common.widget.wheel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.common.R;
import com.common.utils.ScreenHelper;
import com.common.widget.wheel.model.WheelPickerEntity;

import java.util.List;


/**
 * Created by imaginato on 2015/7/1.
 */
public class WheelPickerAdapter extends AbstractWheelTextAdapter {
    private List<WheelPickerEntity> arrayList;
    private Context mContext;
    public WheelPickerAdapter(Context context, List<WheelPickerEntity> array) {
        super(context, R.layout.layout_wheelpicker_item);
        arrayList = array;
        mContext=context;
    }

    @Override
    protected CharSequence getItemText(int index) {
        return "";
    }

    @Override
    public int getItemsCount() {
        int itemCount = 0;
        if (arrayList != null) {
            itemCount = arrayList.size();
        }
        return itemCount;
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_wheelpicker_item, null);
            viewHolder = new ViewHolder();
            viewHolder.ctvContent = (TextView) convertView.findViewById(R.id.ctvContent);
            AbsListView.LayoutParams params= (AbsListView.LayoutParams) convertView.getLayoutParams();
            if(params==null){
                params=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ScreenHelper.dip2px(mContext,45));
            }
            params.height= ScreenHelper.dip2px(mContext,45);

            viewHolder.ctvContent.setLayoutParams(params);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String displaytext = null;
        if (arrayList != null && arrayList.size() > 0 && index >= 0 && index < arrayList.size()) {
            WheelPickerEntity entity = arrayList.get(index);
            if (entity != null) {
                displaytext = entity.getDisplay();
            }
        }
        viewHolder.ctvContent.setText(displaytext);

        return convertView;
    }

    class ViewHolder {
        public TextView ctvContent;
    }
}
