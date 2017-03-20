package com.whitelabel.app.adapter;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.CurationActivity;
import com.whitelabel.app.activity.MerchantStoreFrontActivity;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.listener.OnFilterSortFragmentListener;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsSortItemReturnEntity;
import com.whitelabel.app.model.TMPProductListFilterSortPageEntity;
import com.whitelabel.app.ui.brandstore.BrandStoreFontActivity;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.widget.CustomTextView;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/8/6.
 */
public class ProductListFilterSortSortAdapter extends ArrayAdapter<SVRAppserviceProductSearchFacetsSortItemReturnEntity> {
    private Activity mActivity;
    private OnFilterSortFragmentListener filterSortFragmentListener;
    private ArrayList<SVRAppserviceProductSearchFacetsSortItemReturnEntity> facetsSortItemReturnEntityArrayList;
    private TMPProductListFilterSortPageEntity productListFilterSortPageEntity;

    public ProductListFilterSortSortAdapter(Activity activity, OnFilterSortFragmentListener listener, ArrayList<SVRAppserviceProductSearchFacetsSortItemReturnEntity> array, TMPProductListFilterSortPageEntity filterSortPageEntity) {
        super(activity, R.layout.adapter_productlist_sort_item, array);
        this.mActivity = activity;
        this.filterSortFragmentListener = listener;
        this.facetsSortItemReturnEntityArrayList = array;
        this.productListFilterSortPageEntity = filterSortPageEntity;
    }
   private int mCurrindex;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.adapter_productlist_sort_item, null);

            viewHolder = new ViewHolder();
            viewHolder.ctvSortTitle = (CustomTextView) convertView.findViewById(R.id.ctvSortTitle);
            viewHolder.ivSortTitle = (ImageView) convertView.findViewById(R.id.ivSortTitle);

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
        viewHolder.ctvSortTitle.setText(brandLabel);
       JLogUtils.i("zz",""+brandLabel);

        if (sortItemReturnEntity.isSelected()) {
            mCurrindex=position;
            viewHolder.ivSortTitle.setImageDrawable(JImageUtils.getDrawable(mActivity, R.mipmap.dot_checkout_address_selected));
        } else {
            viewHolder.ivSortTitle.setImageDrawable(JImageUtils.getDrawable(mActivity, R.mipmap.dot_checkout_address_unselected));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            private int index;
            public View.OnClickListener init(int i) {
                this.index = i;
                return this;
            }

            @Override
            public void onClick(View view) {
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
                            ((ProductListActivity)mActivity).setSVRAppserviceProductSearchParameterSort(productListFilterSortPageEntity.getPreviousFragmentType(), productListFilterSortPageEntity.getCategoryFragmentPosition(), sortItemReturnEntity.getValue());
                            if (filterSortFragmentListener != null) {
                                filterSortFragmentListener.onFilterSortListItemClick(ProductListActivity.TABBAR_INDEX_SORT, null);
                            }
                        } else if (productListFilterSortPageEntity.getPreviousFragmentType() == ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS) {
                            ((ProductListActivity)mActivity).setSVRAppserviceProductSearchParameterSort(productListFilterSortPageEntity.getPreviousFragmentType(), -1, sortItemReturnEntity.getValue());
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
        }.init(position));

        return convertView;
    }
    private Handler mHandler=new Handler();
    class ViewHolder {
        public CustomTextView ctvSortTitle;
        public ImageView ivSortTitle;
    }
}
