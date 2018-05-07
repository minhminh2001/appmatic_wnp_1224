package com.whitelabel.app.adapter;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.activity.CurationActivity;
import com.whitelabel.app.activity.MerchantStoreFrontActivity;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.listener.OnFilterSortFragmentListener;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsSortItemReturnEntity;
import com.whitelabel.app.model.TMPProductListFilterSortPageEntity;
import com.whitelabel.app.model.TempCategoryBean;
import com.whitelabel.app.ui.brandstore.BrandStoreFontActivity;
import com.whitelabel.app.utils.JLogUtils;

import java.util.ArrayList;
/**
 * Created by imaginato on 2015/8/6.
 */
public class ProductListFilterSortSortAdapter extends ArrayAdapter<SVRAppserviceProductSearchFacetsSortItemReturnEntity> {
    private Activity mActivity;
    private OnFilterSortFragmentListener filterSortFragmentListener;
    private ArrayList<SVRAppserviceProductSearchFacetsSortItemReturnEntity> facetsSortItemReturnEntityArrayList;
    private TMPProductListFilterSortPageEntity productListFilterSortPageEntity;
    private TempCategoryBean tempCategoryBean;
    private MyClickListener clickListener;
    public ProductListFilterSortSortAdapter(Activity activity, OnFilterSortFragmentListener listener, ArrayList<SVRAppserviceProductSearchFacetsSortItemReturnEntity> array, TMPProductListFilterSortPageEntity filterSortPageEntity) {
        super(activity, R.layout.adapter_productlist_sort_item, array);
        this.mActivity = activity;
        this.filterSortFragmentListener = listener;
        this.facetsSortItemReturnEntityArrayList = array;
        this.productListFilterSortPageEntity = filterSortPageEntity;
        tempCategoryBean=TempCategoryBean.getInstance();
    }
   private int mCurrindex;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.adapter_productlist_sort_item, null);
            viewHolder = new ViewHolder();
            viewHolder.rbSortTitle = (RadioButton) convertView.findViewById(R.id.rbSortTitle);
            viewHolder.ivSortTitle = (ImageView) convertView.findViewById(R.id.ivSortTitle);

            viewHolder.rbSortTitle.setTextColor(getTextColorForSortItem());
            viewHolder.rbSortTitle.setOnClickListener(new MyClickListener(position));

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (facetsSortItemReturnEntityArrayList == null || position < 0 || facetsSortItemReturnEntityArrayList.size() <= position) {
            if (convertView.getLayoutParams() != null) {
                convertView.getLayoutParams().height = 0;
            }
            return convertView;
        }

        SVRAppserviceProductSearchFacetsSortItemReturnEntity sortItemReturnEntity = facetsSortItemReturnEntityArrayList.get(position);
        if (sortItemReturnEntity == null) {
            if (convertView.getLayoutParams() != null) {
                convertView.getLayoutParams().height = 0;
            }
            return convertView;
        }
        String brandLabel = sortItemReturnEntity.getLabel();
        viewHolder.rbSortTitle.setText(brandLabel);
        JLogUtils.i("zz",""+brandLabel+"---sortItemReturnEntity.isSelected():"+sortItemReturnEntity.isSelected());
        if (sortItemReturnEntity.isSelected()) {
            mCurrindex=position;
            viewHolder.rbSortTitle.setChecked(true);
            viewHolder.ivSortTitle.setBackgroundResource(R.drawable.ic_sort_item_selected);
        } else {
            viewHolder.rbSortTitle.setChecked(false);
            viewHolder.ivSortTitle.setBackground(null);
        }
        convertView.setOnClickListener(new MyClickListener(position));

        return convertView;
    }

    private Handler mHandler=new Handler();

    private ColorStateList getTextColorForSortItem(){
        int checkedColor = WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color();
        int unCheckedColor = ContextCompat.getColor(mActivity, R.color.black);
        int[] colors = new int[]{unCheckedColor, checkedColor, unCheckedColor};

        int [][]statelist = new int[3][];
        statelist[0] = new int[]{-android.R.attr.state_checked};
        statelist[1] = new int[]{android.R.attr.state_checked};
        statelist[2] = new int[]{};

        return new ColorStateList(statelist, colors);
    }

    private class MyClickListener implements View.OnClickListener{

        private int index;

        public MyClickListener(int position){
            this.index = position;
        }

        @Override
        public void onClick(View v) {
            if (productListFilterSortPageEntity == null) {
                return;
            }
            if (index < 0 || facetsSortItemReturnEntityArrayList == null || facetsSortItemReturnEntityArrayList.size() <= index) {
                return;
            }
            final   SVRAppserviceProductSearchFacetsSortItemReturnEntity sortItemReturnEntity = facetsSortItemReturnEntityArrayList.get(index);
            SVRAppserviceProductSearchFacetsSortItemReturnEntity oldSortItemReturnEntity=facetsSortItemReturnEntityArrayList.get(mCurrindex);
            if (sortItemReturnEntity == null) {
                return;
            }
            if(oldSortItemReturnEntity!=null) {
                oldSortItemReturnEntity.setSelected(false);
            }
            sortItemReturnEntity.setSelected(true);
            notifyDataSetChanged();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (productListFilterSortPageEntity.getPreviousFragmentType() == ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY) {
                        tempCategoryBean.setSVRAppserviceProductSearchParameterSort(productListFilterSortPageEntity.getPreviousFragmentType(), productListFilterSortPageEntity.getCategoryFragmentPosition(), sortItemReturnEntity.getValue());
                        if (filterSortFragmentListener != null) {
                            filterSortFragmentListener.onFilterSortListItemClick(ProductListActivity.TABBAR_INDEX_SORT, null);
                        }
                    } else if (productListFilterSortPageEntity.getPreviousFragmentType() == ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS) {
                        tempCategoryBean.setSVRAppserviceProductSearchParameterSort(productListFilterSortPageEntity.getPreviousFragmentType(), -1, sortItemReturnEntity.getValue());
                        if (filterSortFragmentListener != null) {
                            filterSortFragmentListener.onFilterSortListItemClick(ProductListActivity.TABBAR_INDEX_SORT, null);
                        }
                    }else  if (productListFilterSortPageEntity.getPreviousFragmentType() == MerchantStoreFrontActivity.ACTIVITY_TYPE_PRODUCTLIST_MERCHANT) {
                        ((MerchantStoreFrontActivity)mActivity).setSVRAppserviceProductSearchParameterSort(sortItemReturnEntity.getValue());
                        if (filterSortFragmentListener != null) {
                            filterSortFragmentListener.onFilterSortListItemClick(MerchantStoreFrontActivity.TABBAR_INDEX_FILTER, null);
                        }
                    }else  if (productListFilterSortPageEntity.getPreviousFragmentType() == CurationActivity.ACTIVITY_TYPE_PRODUCTLIST_CURATION) {
                        ((CurationActivity)mActivity).setSVRAppserviceProductSearchParameterSort(sortItemReturnEntity.getValue());
                        if (filterSortFragmentListener != null) {
                            filterSortFragmentListener.onFilterSortListItemClick(CurationActivity.TABBAR_INDEX_FILTER, null);
                        }
                    }else if(productListFilterSortPageEntity.getPreviousFragmentType()== BrandStoreFontActivity.ACTIVITY_TYPE_PRODUCTLIST_BRANDSTORE){
                        ((BrandStoreFontActivity)mActivity).setSVRAppserviceProductSearchParameterSort(sortItemReturnEntity.getValue());
                        if (filterSortFragmentListener != null) {
                            filterSortFragmentListener.onFilterSortListItemClick(CurationActivity.TABBAR_INDEX_FILTER, null);
                        }
                    }
                }
            }, 300);
        }
    }

    class ViewHolder {
        public RadioButton rbSortTitle;
        public ImageView ivSortTitle;
    }
}
