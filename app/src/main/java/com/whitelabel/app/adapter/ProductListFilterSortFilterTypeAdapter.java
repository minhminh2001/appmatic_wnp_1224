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
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsModelReturnEntity;
import com.whitelabel.app.model.TMPProductListFilterSortPageEntity;
import com.whitelabel.app.model.TempCategoryBean;
import com.whitelabel.app.ui.brandstore.BrandStoreFontActivity;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.widget.CustomTextView;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/8/5.
 */
public class ProductListFilterSortFilterTypeAdapter extends ArrayAdapter<SVRAppserviceProductSearchFacetsModelReturnEntity> {
    private Activity activity;
    private OnFilterSortFragmentListener filterSortFragmentListener;
    private ArrayList<SVRAppserviceProductSearchFacetsModelReturnEntity> modelTypeReturnEntityArrayList;
    private TMPProductListFilterSortPageEntity productListFilterSortPageEntity;
    private TempCategoryBean tempCategoryBean;
    public ProductListFilterSortFilterTypeAdapter(Activity activity, OnFilterSortFragmentListener listener, ArrayList<SVRAppserviceProductSearchFacetsModelReturnEntity> array, TMPProductListFilterSortPageEntity filterSortPageEntity) {
        super(activity, R.layout.adapter_productlist_filter_typelist_item, array);
        this.activity = activity;
        this.filterSortFragmentListener = listener;
        this.modelTypeReturnEntityArrayList = array;
        this.productListFilterSortPageEntity = filterSortPageEntity;
        tempCategoryBean=TempCategoryBean.getInstance();
    }
    private int mCurrindex;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.adapter_productlist_filter_typelist_item, null);
            viewHolder = new ViewHolder();
            viewHolder.ctvTypeTitle = (CustomTextView) convertView.findViewById(R.id.ctvTypeTitle);
            viewHolder.ivTypeTitle =  convertView.findViewById(R.id.ivTypeTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position < 0 || modelTypeReturnEntityArrayList == null || modelTypeReturnEntityArrayList.size() <= position) {
            if (convertView.getLayoutParams() != null) {
                convertView.getLayoutParams().height = 0;
            }
            return convertView;
        }

        SVRAppserviceProductSearchFacetsModelReturnEntity modelTypeReturnEntity = modelTypeReturnEntityArrayList.get(position);
        if (modelTypeReturnEntity == null) {
            if (convertView.getLayoutParams() != null) {
                convertView.getLayoutParams().height = 0;
            }
            return convertView;
        }

        String typeLabel = modelTypeReturnEntity.getLabel();
        long typeCount = modelTypeReturnEntity.getCount();
        viewHolder.ctvTypeTitle.setText(typeLabel + " (" + typeCount + ") ");

        if (modelTypeReturnEntity.isSelected()) {
            mCurrindex=position;
            viewHolder.ivTypeTitle.setVisibility(View.VISIBLE);
//            viewHolder.ivTypeTitle.setImageDrawable(JImageUtils.getDrawable(activity, R.mipmap.dot_checkout_address_selected));
        } else {
            viewHolder.ivTypeTitle.setVisibility(View.INVISIBLE);
//            viewHolder.ivTypeTitle.setImageDrawable(JImageUtils.getDrawable(activity, R.mipmap.dot_checkout_address_unselected));
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

                if (index < 0 || modelTypeReturnEntityArrayList == null || modelTypeReturnEntityArrayList.size() <= index) {
                    return;
                }

             final   SVRAppserviceProductSearchFacetsModelReturnEntity modelTypeReturnEntity = modelTypeReturnEntityArrayList.get(index);
                SVRAppserviceProductSearchFacetsModelReturnEntity oldBrandItemReturnEntity=modelTypeReturnEntityArrayList.get(mCurrindex);
                if (modelTypeReturnEntity == null) {
                    return;
                }



                try {

                    if (oldBrandItemReturnEntity != null) {
                        oldBrandItemReturnEntity.setSelected(false);
                    }

                    modelTypeReturnEntity.setSelected(true);
                    notifyDataSetChanged();
                }catch(Exception ex){
                    ex.getStackTrace();
                }

                JLogUtils.i("Martin", "OnClickListener --> modelTypeReturnEntity.getValue() =>" + modelTypeReturnEntity.getValue());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (productListFilterSortPageEntity.getPreviousFragmentType() == ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY) {
                            tempCategoryBean.setSVRAppserviceProductSearchParameterType(productListFilterSortPageEntity.getPreviousFragmentType(), productListFilterSortPageEntity.getCategoryFragmentPosition(), modelTypeReturnEntity.getValue());
                            if (filterSortFragmentListener != null) {
                                filterSortFragmentListener.onFilterSortListItemClick(ProductListActivity.TABBAR_INDEX_FILTER, null);
                            }
                        } else if (productListFilterSortPageEntity.getPreviousFragmentType() == ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS) {
                            tempCategoryBean.setSVRAppserviceProductSearchParameterType(productListFilterSortPageEntity.getPreviousFragmentType(), -1, modelTypeReturnEntity.getValue());
                            if (filterSortFragmentListener != null) {
                                filterSortFragmentListener.onFilterSortListItemClick(ProductListActivity.TABBAR_INDEX_FILTER, null);
                            }
                        }
                        else if (productListFilterSortPageEntity.getPreviousFragmentType() == MerchantStoreFrontActivity.ACTIVITY_TYPE_PRODUCTLIST_MERCHANT) {
                            ((MerchantStoreFrontActivity)activity).setSVRAppserviceProductSearchParameterType(modelTypeReturnEntity.getValue());
                            if (filterSortFragmentListener != null) {
                                filterSortFragmentListener.onFilterSortListItemClick(MerchantStoreFrontActivity.TABBAR_INDEX_FILTER, null);
                            }
                        } else if (productListFilterSortPageEntity.getPreviousFragmentType() == CurationActivity.ACTIVITY_TYPE_PRODUCTLIST_CURATION) {
                            ((CurationActivity)activity).setSVRAppserviceProductSearchParameterType(modelTypeReturnEntity.getValue());
                            if (filterSortFragmentListener != null) {
                                filterSortFragmentListener.onFilterSortListItemClick(CurationActivity.TABBAR_INDEX_FILTER, null);
                            }
                        }else if(productListFilterSortPageEntity.getPreviousFragmentType()== BrandStoreFontActivity.ACTIVITY_TYPE_PRODUCTLIST_BRANDSTORE){
                            ((BrandStoreFontActivity) activity).setSVRAppserviceProductSearchParameterType(modelTypeReturnEntity.getValue());
                            if (filterSortFragmentListener != null) {
                                filterSortFragmentListener.onFilterSortListItemClick(CurationActivity.TABBAR_INDEX_FILTER, null);
                            }
                        }
                    }
                },300);

            }
        }.init(position));

        return convertView;
    }
   private Handler mHandler=new Handler();
    class ViewHolder {
        public CustomTextView ctvTypeTitle;
        public View ivTypeTitle;
    }
}
