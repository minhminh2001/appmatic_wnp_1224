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
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsBrandItemReturnEntity;
import com.whitelabel.app.model.TMPProductListFilterSortPageEntity;
import com.whitelabel.app.model.TempCategoryBean;
import com.whitelabel.app.ui.brandstore.BrandStoreFontActivity;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.widget.CustomRadioButton;
import com.whitelabel.app.widget.CustomTextView;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/8/5.
 */
public class ProductListFilterSortFilterBrandAdapter extends ArrayAdapter<SVRAppserviceProductSearchFacetsBrandItemReturnEntity> {
    private Activity mActivity;
    private OnFilterSortFragmentListener filterSortFragmentListener;
    private ArrayList<SVRAppserviceProductSearchFacetsBrandItemReturnEntity> brandItemReturnEntityArrayList;
    private TMPProductListFilterSortPageEntity productListFilterSortPageEntity;
    private TempCategoryBean tempCategoryBean;

    public ProductListFilterSortFilterBrandAdapter(Activity activity, OnFilterSortFragmentListener listener, ArrayList<SVRAppserviceProductSearchFacetsBrandItemReturnEntity> array, TMPProductListFilterSortPageEntity filterSortPageEntity) {
        super(activity, R.layout.adapter_productlist_filter_brandlist_item, array);
        this.mActivity = activity;
        this.filterSortFragmentListener = listener;
        this.brandItemReturnEntityArrayList = array;
        this.productListFilterSortPageEntity = filterSortPageEntity;
        tempCategoryBean=TempCategoryBean.getInstance();
    }

    private int mCurrindex;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.adapter_productlist_filter_brandlist_item, null);
            viewHolder = new ViewHolder();
            viewHolder.ctvBrandTitle = (CustomTextView) convertView.findViewById(R.id.ctvBrandTitle);
            viewHolder.ivBrandTitle = (CustomRadioButton) convertView.findViewById(R.id.ivBrandTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (brandItemReturnEntityArrayList == null || position < 0 || brandItemReturnEntityArrayList.size() <= position) {
            if (convertView.getLayoutParams() != null) {
                convertView.getLayoutParams().height = 0;
            }
            return convertView;
        }

        final SVRAppserviceProductSearchFacetsBrandItemReturnEntity brandItemReturnEntity = brandItemReturnEntityArrayList.get(position);

        if (brandItemReturnEntity == null) {
            if (convertView.getLayoutParams() != null) {
                convertView.getLayoutParams().height = 0;
            }
            return convertView;
        }

        String brandLabel = brandItemReturnEntity.getLabel();
        long brandCount = brandItemReturnEntity.getCount();
        viewHolder.ctvBrandTitle.setText(brandLabel + " (" + brandCount + ") ");

        if (brandItemReturnEntity.isSelected()) {
            mCurrindex = position;
//            viewHolder.ivBrandTitle.setImageDrawable(JImageUtils.getDrawable(mActivity, R.mipmap.dot_checkout_address_selected));
            viewHolder.ivBrandTitle.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivBrandTitle.setVisibility(View.INVISIBLE);
//
//     viewHolder.ivBrandTitle.setImageDrawable(JImageUtils.getDrawable(mActivity, R.mipmap.dot_checkout_address_unselected));
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
                if (index < 0 || brandItemReturnEntityArrayList == null || brandItemReturnEntityArrayList.size() <= index) {
                    return;
                }
                final SVRAppserviceProductSearchFacetsBrandItemReturnEntity brandItemReturnEntity = brandItemReturnEntityArrayList.get(index);
//                Toast.makeText(getContext(),brandItemReturnEntity.getValue(),Toast.LENGTH_LONG).show();
                SVRAppserviceProductSearchFacetsBrandItemReturnEntity oldBrandItemReturnEntity = brandItemReturnEntityArrayList.get(mCurrindex);
                if (brandItemReturnEntity == null) {
                    return;
                }
                try {
                    if (oldBrandItemReturnEntity != null) {
                        oldBrandItemReturnEntity.setSelected(false);
                    }
                    brandItemReturnEntity.setSelected(true);
                    notifyDataSetChanged();
                } catch (Exception ex) {
                    ex.getStackTrace();
                }

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (productListFilterSortPageEntity.getPreviousFragmentType() == ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY) {
                            tempCategoryBean.setSVRAppserviceProductSearchParameterBrand(productListFilterSortPageEntity.getPreviousFragmentType(), productListFilterSortPageEntity.getCategoryFragmentPosition(), brandItemReturnEntity.getValue());
                            //TODO joyson old business code
//                        mActivity.setSVRAppserviceProductSearchParameterBrandName(productListFilterSortPageEntity.getCategoryFragmentPosition(), brandItemReturnEntity.getLabel());
                            if (filterSortFragmentListener != null) {
                                filterSortFragmentListener.onFilterSortListItemClick(ProductListActivity.TABBAR_INDEX_FILTER, null);
                            }
                        } else if (productListFilterSortPageEntity.getPreviousFragmentType() == ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS) {
                            tempCategoryBean.setSVRAppserviceProductSearchParameterBrand(productListFilterSortPageEntity.getPreviousFragmentType(), -1, brandItemReturnEntity.getValue());
                            if (filterSortFragmentListener != null) {
                                filterSortFragmentListener.onFilterSortListItemClick(ProductListActivity.TABBAR_INDEX_FILTER, null);
                            }
                        } else if (productListFilterSortPageEntity.getPreviousFragmentType() == MerchantStoreFrontActivity.ACTIVITY_TYPE_PRODUCTLIST_MERCHANT) {
                            ((MerchantStoreFrontActivity) mActivity).setSVRAppserviceProductSearchParameterBrand(brandItemReturnEntity.getValue());
                            if (filterSortFragmentListener != null) {
                                filterSortFragmentListener.onFilterSortListItemClick(MerchantStoreFrontActivity.TABBAR_INDEX_FILTER, null);
                            }
                        } else if (productListFilterSortPageEntity.getPreviousFragmentType() == CurationActivity.ACTIVITY_TYPE_PRODUCTLIST_CURATION) {
                            ((CurationActivity) mActivity).setSVRAppserviceProductSearchParameterBrand(brandItemReturnEntity.getValue());
                            if (filterSortFragmentListener != null) {
                                filterSortFragmentListener.onFilterSortListItemClick(CurationActivity.TABBAR_INDEX_FILTER, null);
                            }
                        }else if(productListFilterSortPageEntity.getPreviousFragmentType()== BrandStoreFontActivity.ACTIVITY_TYPE_PRODUCTLIST_BRANDSTORE){
                            ((BrandStoreFontActivity) mActivity).setSVRAppserviceProductSearchParameterBrand(brandItemReturnEntity.getValue());
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

    private Handler mHandler = new Handler();


    class ViewHolder {
        public CustomTextView ctvBrandTitle;
        public CustomRadioButton ivBrandTitle;
    }
}
