package com.whitelabel.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchCategoryItemReturnEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.widget.ExpandableRecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by kevin on 2016/11/3.
 */

public class CategoryTreeExpandableAdapter extends ExpandableRecyclerAdapter<SVRAppserviceCatalogSearchCategoryItemReturnEntity> {
    public static final int TYPE_CHILD = 1001;
    private Context context;
    private final ImageLoader mImageLoader;
    private int mViewType;
    public final  static   int VIEW_HORIZONTAL=1;
    public final static  int VIEW_VERTICAL=2;
    private ChildOnClick childOnClick;

    public interface ChildOnClick {
        void childOnClick(int position, Object ob, String parentId);
    }

    public void setViewType(int viewType){
        mViewType=viewType;
    }
    public CategoryTreeExpandableAdapter(Activity activity, Context context,
                                         ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> groupList, ImageLoader imageLoader,
                                         ChildOnClick childOnClick) {
        super(context, activity);
        this.context = context;
        this.childOnClick = childOnClick;
        mImageLoader = imageLoader;
        setDataType(groupList);
    }
    public void setDataType(ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> groupList) {
        //将entity 排好序，并且设置itemType 和其父类Id
        ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> tempList = new ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity>();
        for (int i = 0; i < groupList.size(); i++) {
            groupList.get(i).setItemType(TYPE_HEADER);
            tempList.add(groupList.get(i));
            for (int j = 0; j < groupList.get(i).getChildren().size(); j++) {
                groupList.get(i).getChildren().get(j).setItemType(TYPE_CHILD);
                groupList.get(i).getChildren().get(j).setParentId(groupList.get(i).getId());
                tempList.add(groupList.get(i).getChildren().get(j));
            }
        }
        ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> groupList1 = tempList;
        //将数据更新到 ExpandableRecyclerAdapter
        setItems(groupList1);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                View convertView =null;
                if(mViewType==VIEW_HORIZONTAL){
                    convertView = LayoutInflater.from(context).inflate(R.layout.adapter_category_tree_group_item_hor, null);
                }else{
                    convertView = LayoutInflater.from(context).inflate(R.layout.adapter_category_tree_group_item, null);
                }
                return new GroupViewHolder(convertView);
            case TYPE_CHILD:
            default:
                View convertView2 = LayoutInflater.from(context).inflate(R.layout.adapter_category_tree_child_item, null);
                return new ChildViewHolder(convertView2);
        }
    }
    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position2) {
        final int position = position2;
        if (holder instanceof GroupViewHolder) {
            GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
            SVRAppserviceCatalogSearchCategoryItemReturnEntity entity = (SVRAppserviceCatalogSearchCategoryItemReturnEntity) getItem(position);
            groupViewHolder.tvCategoryTreeGroupName.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
            groupViewHolder.tvCategoryTreeGroupName.setText(entity.getName());
            if (groupViewHolder.ivCategoryTreeGroup.getTag() != null && groupViewHolder.ivCategoryTreeGroup.getTag().toString().equals(entity.getImage())) {
            } else {
                JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, groupViewHolder.ivCategoryTreeGroup, entity.getImage());
                groupViewHolder.ivCategoryTreeGroup.setTag(entity.getImage());
            }

            groupViewHolder.ivCategoryTreeGroup.setImageResource(R.mipmap.checkout_success_facebook_share);

//            if (position == 0) {
                groupViewHolder.tv_category_tree_divi.setVisibility(View.GONE);
//            } else {
//                groupViewHolder.tv_category_tree_divi.setVisibility(View.VISIBLE);
//            }
            if (entity.isExpaned()) {
                groupViewHolder.tvCategoryTreeLine.setVisibility(View.GONE);
            } else {
                groupViewHolder.tvCategoryTreeLine.setVisibility(View.VISIBLE);
            }
        } else {
            ChildViewHolder childViewHolder = (ChildViewHolder) holder;
            final SVRAppserviceCatalogSearchCategoryItemReturnEntity child = (SVRAppserviceCatalogSearchCategoryItemReturnEntity) getItem(position);
            childViewHolder.tvCategoryTreeChildName.setText(child.getName());
            childViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childOnClick.childOnClick(position, child, child.getParentId());
                }
            });
        }
    }
    public class GroupViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        public TextView tvCategoryTreeGroupName, tvCategoryTreeLine, tv_category_tree_divi;
        public ImageView ivCategoryTreeGroup;

        public GroupViewHolder(View view) {
            super(view);
            tvCategoryTreeGroupName = (TextView) view.findViewById(R.id.tv_category_tree_group_name);


            ivCategoryTreeGroup = (ImageView) view.findViewById(R.id.iv_category_tree_group);
            tvCategoryTreeLine = (TextView) view.findViewById(R.id.tv_category_tree_line);
            tv_category_tree_divi = (TextView) view.findViewById(R.id.tv_category_tree_divi);
            super.setArrowAndChangeListener(tvCategoryTreeLine, new ArrowChangeListener() {
                @Override
                public void arrowChange(final View arrow, boolean openGroup, int position) {
                    if (openGroup) {
                        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.anim_category_rotate);
                        arrow.startAnimation(operatingAnim);
                        operatingAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                arrow.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                    } else {
                        arrow.setVisibility(View.VISIBLE);
                        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.anim_category_rotate_tozero);
                        arrow.startAnimation(operatingAnim);
                        operatingAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                    }

                }
            });

        }
    }

    public class ChildViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
        public TextView tvCategoryTreeChildName;

        public ChildViewHolder(View view) {
            super(view);
            tvCategoryTreeChildName = (TextView) view.findViewById(R.id.tv_category_tree_child_name);
        }
    }

}
