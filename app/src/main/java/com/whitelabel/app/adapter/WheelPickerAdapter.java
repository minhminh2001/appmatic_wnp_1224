package com.whitelabel.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.whitelabel.app.R;
import com.whitelabel.app.model.WheelPickerEntity;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.wheel.adapters.AbstractWheelTextAdapter;

import java.util.ArrayList;


/**
 * Created by imaginato on 2015/7/1.
 */
public class WheelPickerAdapter extends AbstractWheelTextAdapter {
    private ArrayList<WheelPickerEntity> arrayList;
    private Context mContext;
    public WheelPickerAdapter(Context context, ArrayList<WheelPickerEntity> array) {
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
            viewHolder.ctvContent = (CustomTextView) convertView.findViewById(R.id.ctvContent);
            AbsListView.LayoutParams params= (AbsListView.LayoutParams) convertView.getLayoutParams();
            if(params==null){
                params=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,JDataUtils.dp2px(mContext,45));
            }
            params.height= JDataUtils.dp2px(mContext,45);

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
        public CustomTextView ctvContent;
    }
}
