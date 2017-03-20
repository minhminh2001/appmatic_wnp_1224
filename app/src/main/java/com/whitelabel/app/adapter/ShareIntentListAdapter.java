
package com.whitelabel.app.adapter;
import android.app.Activity;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;


public class ShareIntentListAdapter extends ArrayAdapter{

    private final Activity context;
    Object[] items;


    public ShareIntentListAdapter(Activity context,Object[] items) {

        super(context, R.layout.layout_shared, items);
        this.context = context;
        this.items = items;

    }// end HomeListViewPrototype

    class ShareHolder{
        TextView shareName;
        ImageView imageShare;


    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ShareHolder hodler=null;
        if(view==null){
            LayoutInflater inflater = context.getLayoutInflater();
            view=  inflater.inflate(R.layout.layout_shared, null, true);
            hodler=new ShareHolder();
            hodler.imageShare= (ImageView) view.findViewById(R.id.shareImage);
            hodler.shareName= (TextView) view.findViewById(R.id.shareName);
            view.setTag(hodler);
        }else{
            hodler= (ShareHolder) view.getTag();
        }
        // set native name of App to share
        hodler.shareName.setText(((ResolveInfo) items[position]).activityInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());

        // share native image of the App to share
        hodler.imageShare.setImageDrawable(((ResolveInfo) items[position]).activityInfo.applicationInfo.loadIcon(context.getPackageManager()));
        return view;
    }// end getView

}// end main onCreate