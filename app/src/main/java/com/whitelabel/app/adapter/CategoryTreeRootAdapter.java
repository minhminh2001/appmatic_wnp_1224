package com.whitelabel.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchCategoryItemReturnEntity;
import com.whitelabel.app.utils.JToolUtils;

import java.util.ArrayList;

/**
 * Created by kevin on 2016/11/3.
 */

public class CategoryTreeRootAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public int currentRootPosition=1;
    private ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> allData;
    public CategoryTreeRootAdapter(Context context,
                                   ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> allData,
                                   ItemClick itemClick) {
        super();
        Context context1 = context;
        this.allData=allData;
        this.itemClick=itemClick;
    }
    private ItemClick itemClick;
    public interface ItemClick{
        void onItemClick(ViewHolder holder, int position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_categorytree_root_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder=(ViewHolder)holder;
        SVRAppserviceCatalogSearchCategoryItemReturnEntity entity=allData.get(position);
        viewHolder.tvCategoryTreeRootName.setText(entity.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onItemClick(viewHolder,position);
            }
        });

        if(position==currentRootPosition){
            viewHolder.itemView.setBackgroundColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
            viewHolder.tvCategoryTreeRootName.setTextColor(JToolUtils.getColor(R.color.whiteFFFFFF));
        }else{
            viewHolder.itemView.setBackgroundColor(JToolUtils.getColor(R.color.greyF8F8F8));
            viewHolder.tvCategoryTreeRootName.setTextColor(JToolUtils.getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvCategoryTreeRootName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCategoryTreeRootName=(TextView)itemView.findViewById(R.id.tv_category_tree_root_name);
        }
    }
}
