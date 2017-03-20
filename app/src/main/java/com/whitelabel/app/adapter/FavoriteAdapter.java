package com.whitelabel.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.model.FavoriteSonEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;

import java.util.List;

/**
 * Created by imaginato on 2015/8/19.
 */
public class FavoriteAdapter extends BaseAdapter {
    private Context context;
    private List<FavoriteSonEntity> list;
    private GridView mGridView;
    private Animation animation;
    private final ImageLoader mImageLoader;

    public FavoriteAdapter(Context context, List<FavoriteSonEntity> list, GridView mGridView, ImageLoader imageLoader) {
        this.context = context;
        this.list = list;
        this.mGridView = mGridView;
        mImageLoader = imageLoader;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_favorite_gridview, null);
            viewHolder.contentImg = (ImageView) convertView.findViewById(R.id.favoriteImg);
            viewHolder.contentText = (TextView) convertView.findViewById(R.id.favoriteText);
            viewHolder.heartImg = (ImageView) convertView.findViewById(R.id.heart);
            viewHolder.favoriteLayer = convertView.findViewById(R.id.favoriteLayer);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FavoriteSonEntity entity = (FavoriteSonEntity) getItem(position);
        if (entity != null) {
            viewHolder.contentText.setText(entity.getCategoryName());
            int screenWidth = GemfiveApplication.getPhoneConfiguration().getScreenWidth();
            ViewGroup.LayoutParams para = viewHolder.contentImg.getLayoutParams();
            para.width = (screenWidth - 80) / 2;
            para.height = (screenWidth - 80) / 2;
            viewHolder.favoriteLayer.setLayoutParams(para);
            viewHolder.contentImg.setLayoutParams(para);

            JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, viewHolder.contentImg, entity.getImage(), (screenWidth - 80) / 2, (screenWidth - 80) / 2);

            JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, viewHolder.heartImg, entity.getIconImage(), -1, -1);

            JLogUtils.i("Allen", "地址" + entity.getIconImage());
            boolean selected = entity.isSelected();
            if (selected) {
                viewHolder.favoriteLayer.setBackgroundColor(context.getResources().getColor(R.color.purple85));
            } else {
                viewHolder.favoriteLayer.setBackgroundColor(context.getResources().getColor(R.color.black75));
            }
//            switch (position){
//                case 0:
//                    viewHolder.heartImg.setImageResource(R.mipmap.favorite1);
//                    break;
//                case 1:
//                    viewHolder.heartImg.setImageResource(R.mipmap.favorite2);
//                    break;
//                case 2:
//                    viewHolder.heartImg.setImageResource(R.mipmap.favorite3);
//                    break;
//                case 3:
//                    viewHolder.heartImg.setImageResource(R.mipmap.favorite4);
//                    break;
//                case 4:
//                    viewHolder.heartImg.setImageResource(R.mipmap.favorite5);
//                    break;
//
//            }


        }
        return convertView;
    }

    //使用ViewHolder优化listView,将配置文件中的View缓存起来避免反复实例化
    private static class ViewHolder {
        TextView contentText;
        ImageView contentImg;
        ImageView heartImg;
        View favoriteLayer;
    }

    //局部刷新ListView
    public void updateView(int itemIndex) {
        //得到第一个可显示控件的位置，
        int visiblePosition = mGridView.getFirstVisiblePosition();
        //只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
        if (itemIndex - visiblePosition >= 0) {
            //得到要更新的item的view
            View view = mGridView.getChildAt(itemIndex - visiblePosition);
            //从view中取得holder
            ViewHolder holder = (ViewHolder) view.getTag();
            FavoriteSonEntity item = list.get(itemIndex);

            holder.contentImg = (ImageView) view.findViewById(R.id.favoriteImg);
            holder.contentText = (TextView) view.findViewById(R.id.favoriteText);
            holder.heartImg = (ImageView) view.findViewById(R.id.heart);

            boolean selected = item.isSelected();
            if (selected) {
                animation = AnimationUtils.loadAnimation(context, R.anim.anim_favorite_alpha);
                holder.favoriteLayer.startAnimation(animation);
                holder.favoriteLayer.setBackgroundColor(context.getResources().getColor(R.color.purple85));
            } else {
                animation = AnimationUtils.loadAnimation(context, R.anim.anim_favorite_alpha);
                holder.favoriteLayer.startAnimation(animation);
                holder.favoriteLayer.setBackgroundColor(context.getResources().getColor(R.color.black75));

            }
        }
    }
}
