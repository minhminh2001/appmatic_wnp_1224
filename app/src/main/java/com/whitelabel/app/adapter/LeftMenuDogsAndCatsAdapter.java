package com.whitelabel.app.adapter;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.whitelabel.app.Const;
import com.whitelabel.app.R;
import com.whitelabel.app.model.CategoryBaseBean;
import com.whitelabel.app.utils.AnimUtil;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.logger.Logger;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by img on 2017/12/27.
 */

public class LeftMenuDogsAndCatsAdapter  extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    private List<MultiItemEntity> data;
    private Map<Integer,TextView> tvMaps=new HashMap<>();

    private String lv0Title ;
    CategoryBaseBean.CategoryBean.ChildrenBeanX lv0parent;
    public interface ITreeClick{
        void onChildClick(CategoryBaseBean.CategoryBean.ChildrenBeanX parentBean,CategoryBaseBean.CategoryBean.ChildrenBeanX.ChildrenBean childrenBean,String lv0Title);
    }
    private ITreeClick iTreeClick;
    public LeftMenuDogsAndCatsAdapter(List<MultiItemEntity> data,ITreeClick iTreeClick) {
        super(data);
        this.data=data;
        this.iTreeClick=iTreeClick;
        addItemType(Const.TYPE_TREE_LEVEL_0, R.layout.item_left_menu_expandable_lv0);
        addItemType(Const.TYPE_TREE_LEVEL_1, R.layout.item_left_menu_expandable_lv1);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MultiItemEntity item) {
        switch (helper.getItemViewType()){
            case Const.TYPE_TREE_LEVEL_0:
                final CategoryBaseBean.CategoryBean.ChildrenBeanX lv0 = (CategoryBaseBean.CategoryBean.ChildrenBeanX) item;
                helper.setText(R.id.tv_expand_lv0,lv0.getMenuTitle())
                        .setImageResource(R.id.iv_arrow_lv0, lv0.isExpanded() ? R.drawable.ic_arrow_up_expand_item : R.drawable.ic_arrow_down_expand_item);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i=0;i<data.size();i++){
                            if (i!=helper.getAdapterPosition()){
                                collapse(i);
                            }
                        }
                        lv0Title = lv0.getMenuTitle();
                        lv0parent=lv0;
                        if (lv0.isExpanded()) {
                            collapse(helper.getAdapterPosition());
                        } else {
                            expand(helper.getAdapterPosition());
                        }
                    }
                });
                break;
            case Const.TYPE_TREE_LEVEL_1:
                final TextView tvExpandLv1 = helper.getView(R.id.tv_expand_lv1);
                JViewUtils.setSlideMenuTextStyle(tvExpandLv1,false);
                tvExpandLv1.setSelected(false);
                tvMaps.put(helper.getAdapterPosition(),tvExpandLv1);
                final CategoryBaseBean.CategoryBean.ChildrenBeanX.ChildrenBean lv1 = (CategoryBaseBean.CategoryBean.ChildrenBeanX.ChildrenBean) item;
                tvExpandLv1.setText(lv1.getMenuTitle());
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iTreeClick.onChildClick(lv0parent,lv1,lv0Title);
                        for (Map.Entry<Integer, TextView> entry : tvMaps.entrySet()) {
                            TextView tv=entry.getValue();
                            tv.setSelected(false);
                        }
                        tvExpandLv1.setSelected(true);
                    }
                });
                break;
        }
    }
}
