package com.whitelabel.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.CurationActivity;
import com.whitelabel.app.activity.IFilterSortActivity;
import com.whitelabel.app.activity.MerchantStoreFrontActivity;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.adapter.ProductListFilterSortSortAdapter;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.listener.OnFilterSortFragmentListener;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsSortItemReturnEntity;
import com.whitelabel.app.model.TMPProductListFilterSortPageEntity;
import com.whitelabel.app.ui.brandstore.BrandStoreFontActivity;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/8/10.
 */
public class ProductListSortFragment extends com.whitelabel.app.BaseFragment implements View.OnClickListener {
    private final String TAG = "ProductListSortFragment";
    private IFilterSortActivity filterSortActivity;
    private View contentView;
    private OnFilterSortFragmentListener fragmentListener;

    private ListView lvSortConditions;
    private RelativeLayout rlHeaderbarCancel;

    private TMPProductListFilterSortPageEntity productListFilterSortPageEntity;

    private ArrayList<SVRAppserviceProductSearchFacetsSortItemReturnEntity> facetsSortItemReturnEntityArrayList;
    private ProductListFilterSortSortAdapter sortAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProductListActivity) {
            filterSortActivity = (ProductListActivity) context;
        } else if (context instanceof MerchantStoreFrontActivity) {
            filterSortActivity = (MerchantStoreFrontActivity) context;
        }else if (context instanceof CurationActivity) {
            filterSortActivity = (CurationActivity) context;
        }else if(context instanceof BrandStoreFontActivity){
            filterSortActivity=(BrandStoreFontActivity)context;
        }
        if (context instanceof Activity) {
            facetsSortItemReturnEntityArrayList = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_productlist_sort, null);
        return contentView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            productListFilterSortPageEntity = (TMPProductListFilterSortPageEntity) bundle.getSerializable("data");
        }
        contentView.findViewById(R.id.rlHeaderBar).setBackgroundColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getSecondaryColor());
        rlHeaderbarCancel = (RelativeLayout) contentView.findViewById(R.id.rl_headerbar_cancel);
        rlHeaderbarCancel.setOnClickListener(this);
        lvSortConditions = (ListView) contentView.findViewById(R.id.lvSortConditions);

        addSortSortConditions();
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                productListFilterSortPageEntity = (TMPProductListFilterSortPageEntity) bundle.getSerializable("data");
            }
        }
    }

    @Override
    protected void onAnimationEnded() {
        fragmentListener.onAnimationFinished(this);
    }

    private void addSortSortConditions() {
        if (facetsSortItemReturnEntityArrayList != null) {
            facetsSortItemReturnEntityArrayList.clear();

            if (sortAdapter != null) {
                sortAdapter.notifyDataSetChanged();
            }
        }

        boolean defaultItem = true;
        if (productListFilterSortPageEntity != null && productListFilterSortPageEntity.getFacets() != null && productListFilterSortPageEntity.getFacets().getSort_filter() != null) {
            for (int index = 0; index < productListFilterSortPageEntity.getFacets().getSort_filter().size(); ++index) {
                SVRAppserviceProductSearchFacetsSortItemReturnEntity sortItem = productListFilterSortPageEntity.getFacets().getSort_filter().get(index);
                if (sortItem.isSelected()) {
                    defaultItem = false;
                }
            }
            for (int index = 0; index < productListFilterSortPageEntity.getFacets().getSort_filter().size(); ++index) {
                SVRAppserviceProductSearchFacetsSortItemReturnEntity sortItem = productListFilterSortPageEntity.getFacets().getSort_filter().get(index);
                if (index == 0 && defaultItem) {
                    sortItem.setSelected(true);
                }
                facetsSortItemReturnEntityArrayList.add(sortItem);
            }
        }
        sortAdapter = new ProductListFilterSortSortAdapter((Activity) filterSortActivity, fragmentListener, facetsSortItemReturnEntityArrayList, productListFilterSortPageEntity);
        lvSortConditions.setAdapter(sortAdapter);
    }

    public void setFragmentListener(OnFilterSortFragmentListener listener) {
        this.fragmentListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_headerbar_cancel) {
            if (fragmentListener != null) {
                fragmentListener.onCancelClick(view);
            }
        }
    }
}
