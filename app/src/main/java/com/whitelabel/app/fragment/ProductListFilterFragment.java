package com.whitelabel.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.CurationActivity;
import com.whitelabel.app.activity.IFilterSortActivity;
import com.whitelabel.app.activity.MerchantStoreFrontActivity;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.adapter.ProductListFilterSortFilterBrandAdapter;
import com.whitelabel.app.adapter.ProductListFilterSortFilterTypeAdapter;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.listener.OnFilterSortFragmentListener;
import com.whitelabel.app.listener.OnRangeSeekBarChangeListener;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsBrandItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsModelReturnEntity;
import com.whitelabel.app.model.TMPProductListFilterSortPageEntity;
import com.whitelabel.app.ui.brandstore.BrandStoreFontActivity;
import com.whitelabel.app.utils.AnimUtil;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.widget.CustomRangeSeekBar;
import com.whitelabel.app.widget.CustomTextView;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/8/10.
 */
public class ProductListFilterFragment extends com.whitelabel.app.BaseFragment implements View.OnClickListener {
    private final String TAG = "ProductListFilterFragment";
    private IFilterSortActivity filterSortActivity;
    private View contentView;
    private OnFilterSortFragmentListener fragmentListener;

    private CustomTextView ctvPriceRangeTitle, ctvPriceRangeMin, ctvPriceRangeMax, ctvAllBrandsTitle, ctvAllTypesTitle;
    private LinearLayout llFilter, llPriceBar;
    private RelativeLayout rlFilterAllbrandsTitle, rlFilterAlltypesTitle, rlHeaderbarCancel, rlAllBrandsPlus, rlAllTypesPlus;
    private TextView tvAllBrandsPlusAnimate, tvAllTypesPlusAnimate;
    private ListView lvAllBrandsContent, lvAllTypesContent;
    private View vAllBrandsContentAllTypesTitleDivider, vAllTypesTitleContentDivider;
    private  Boolean mCanUserBrand=true;
    private String FILTER_PRICERANGE;
    private String FILTER_ALLBRANDS;
    private String FILTER_ALLTYPES;
    private TMPProductListFilterSortPageEntity productListFilterSortPageEntity;

    private ArrayList<SVRAppserviceProductSearchFacetsBrandItemReturnEntity> brandItemReturnEntityArrayList;
    private ProductListFilterSortFilterBrandAdapter filterBrandAdapter;
    private ArrayList<SVRAppserviceProductSearchFacetsModelReturnEntity> modelTypeReturnEntityArrayList;
    private ProductListFilterSortFilterTypeAdapter filterTypeAdapter;

    private boolean allBrandsListViewIsShow = true;
    private boolean allTypesListViewIsShow = false;
    public ProductListFilterFragment(){

    }
    public final static String EXTRA_CAN_USE_BRANDFILTER="can_use_brandfilter";
    public static ProductListFilterFragment newInstance(String canUserBrand){
        ProductListFilterFragment fragment=new ProductListFilterFragment();
        Bundle bundle=new Bundle();
        bundle.putString(EXTRA_CAN_USE_BRANDFILTER,canUserBrand);
        fragment.setArguments(bundle);
        return fragment;
    }
    public void setCanUseBrand(boolean  canUseBrand){
        this.mCanUserBrand=canUseBrand;

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
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
            filterSortActivity= (IFilterSortActivity) context;
        }
        if (context instanceof Activity) {
            brandItemReturnEntityArrayList = new ArrayList<>();
            modelTypeReturnEntityArrayList = new ArrayList<>();
        }
    }

    public void setFragmentListener(OnFilterSortFragmentListener listener) {
        this.fragmentListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_productlist_filter, null);
        return contentView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FILTER_PRICERANGE = getString(R.string.productlist_filtersort_filter_pricerange);
        FILTER_ALLBRANDS = getString(R.string.productlist_filtersort_filter_allbrands);
        FILTER_ALLTYPES = getString(R.string.productlist_filtersort_filter_alltypes);

        Bundle bundle = getArguments();
        if (bundle != null) {
            productListFilterSortPageEntity = (TMPProductListFilterSortPageEntity) bundle.getSerializable("data");
        }

        rlHeaderbarCancel = (RelativeLayout) contentView.findViewById(R.id.rl_headerbar_cancel);
        rlHeaderbarCancel.setOnClickListener(this);

        llFilter = (LinearLayout) contentView.findViewById(R.id.llFilter);
        ctvPriceRangeTitle = (CustomTextView) contentView.findViewById(R.id.ctvPriceRangeTitle);
        ctvPriceRangeMin = (CustomTextView) contentView.findViewById(R.id.ctvPriceRangeMin);
        llPriceBar = (LinearLayout) contentView.findViewById(R.id.llPriceBar);
        ctvPriceRangeMax = (CustomTextView) contentView.findViewById(R.id.ctvPriceRangeMax);
        rlFilterAllbrandsTitle = (RelativeLayout) contentView.findViewById(R.id.rlFilterAllbrandsTitle);
        rlFilterAllbrandsTitle.setOnClickListener(this);
        ctvAllBrandsTitle = (CustomTextView) contentView.findViewById(R.id.ctvAllBrandsTitle);
        lvAllBrandsContent = (ListView) contentView.findViewById(R.id.lvAllBrandsContent);
        vAllBrandsContentAllTypesTitleDivider = contentView.findViewById(R.id.vAllBrandsContentAllTypesTitleDivider);
        vAllTypesTitleContentDivider = contentView.findViewById(R.id.vAllTypesTitleContentDivider);
        rlFilterAlltypesTitle = (RelativeLayout) contentView.findViewById(R.id.rlFilterAlltypesTitle);
        rlFilterAlltypesTitle.setOnClickListener(this);
        ctvAllTypesTitle = (CustomTextView) contentView.findViewById(R.id.ctvAllTypesTitle);
        lvAllTypesContent = (ListView) contentView.findViewById(R.id.lvAllTypesContent);
        tvAllBrandsPlusAnimate = (TextView) contentView.findViewById(R.id.tv_all_brands_plus_animate);
        rlAllBrandsPlus = (RelativeLayout) contentView.findViewById(R.id.rl_all_brands_plus);
        tvAllTypesPlusAnimate = (TextView) contentView.findViewById(R.id.tv_all_types_plus_animate);
        rlAllTypesPlus = (RelativeLayout) contentView.findViewById(R.id.rl_all_types_plus);
        View vPriceRangeBrandsDivider  =contentView.findViewById(R.id.vPriceRangeBrandsDivider);
        ctvPriceRangeTitle.setText(String.format(FILTER_PRICERANGE, WhiteLabelApplication.getAppConfiguration().getCurrency().getName()));
        addFilterPriceRangeBar();
        JLogUtils.i("ProductListFilterFragment","mCanUserBrand:"+mCanUserBrand);
        if(mCanUserBrand) {
            addFilterAllBrands();
            addFilterAllTypes(false);
        }else{
            rlFilterAllbrandsTitle.setVisibility(View.GONE);
            vPriceRangeBrandsDivider.setVisibility(View.GONE);
            addFilterAllTypes(true);
        }

    }
    protected void onAnimationEnded() {
        fragmentListener.onAnimationFinished(this);
    }
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                productListFilterSortPageEntity = (TMPProductListFilterSortPageEntity) bundle.getSerializable("data");
            }
            addFilterPriceRangeBar();
            if(mCanUserBrand) {
                addFilterAllBrands();
                addFilterAllTypes(false);
            }else {
                addFilterAllTypes(true);
            }
        }
    }

    private void addFilterPriceRangeBar() {
        if (filterSortActivity == null || productListFilterSortPageEntity == null) {
            return;
        }

        llPriceBar.removeAllViews();
        int minPrice = 1;
        int minSelectedPrice = minPrice;
        int maxPrice = 10000;
        int maxSelectedPrice = maxPrice;
        if (productListFilterSortPageEntity.getFacets() != null && productListFilterSortPageEntity.getFacets().getPrice_filter() != null) {
            minPrice = (int) productListFilterSortPageEntity.getFacets().getPrice_filter().getMin_price();
            minSelectedPrice = (int) productListFilterSortPageEntity.getFacets().getPrice_filter().getFrom();
            maxPrice = (int) productListFilterSortPageEntity.getFacets().getPrice_filter().getMax_price();
            maxSelectedPrice = (int) productListFilterSortPageEntity.getFacets().getPrice_filter().getTo();
        }

        // if the is no range, just display a text
        if (maxPrice - minPrice < 1) {
            llPriceBar.addView(addPriceLabel(minPrice));
            ctvPriceRangeMin.setVisibility(View.INVISIBLE);
            ctvPriceRangeMax.setVisibility(View.INVISIBLE);
        } else {
            llPriceBar.addView(addSortBar(minSelectedPrice, maxSelectedPrice, minPrice, maxPrice));
            ctvPriceRangeMin.setVisibility(View.VISIBLE);
            ctvPriceRangeMax.setVisibility(View.VISIBLE);
        }
    }
    private View addPriceLabel(int price) {
        CustomTextView textView = new CustomTextView(getActivity());
        textView.setText(JDataUtils.formatPrice(price));
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setSingleLine();
        textView.setFont("fonts/Lato-Regular.ttf", getActivity());
        return textView;
    }
    private View addSortBar(int minSelectedPrice, int maxSelectedPrice, int minPrice, int maxPrice) {
        ctvPriceRangeMin.setText("" + minSelectedPrice);
        ctvPriceRangeMax.setText("" + maxSelectedPrice);
        CustomRangeSeekBar priceSeekBar = new CustomRangeSeekBar<>(minPrice, maxPrice, getActivity());
        priceSeekBar.setSelectedMinValue(minSelectedPrice);
        priceSeekBar.setSelectedMaxValue(maxSelectedPrice);
        priceSeekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(CustomRangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                JLogUtils.i(TAG, "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
                ctvPriceRangeMin.setText("" + minValue);
                ctvPriceRangeMax.setText("" + maxValue);
            }
            @Override
            public void onRangeSeekBarTouchActionUp(CustomRangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // This is done so that the result will include the products with maxValue price
                // service will fix this bug in the future,so app don't need to do additional processing
//                maxValue++;
                if (filterSortActivity != null && productListFilterSortPageEntity != null) {
                    if (productListFilterSortPageEntity.getPreviousFragmentType() == ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY) {
                        filterSortActivity.setSVRAppserviceProductSearchParameterMinPriceMaxPrice(productListFilterSortPageEntity.getPreviousFragmentType(), productListFilterSortPageEntity.getCategoryFragmentPosition(), minValue, maxValue);
                        if (fragmentListener != null) {
                            fragmentListener.onFilterSortListItemClick(ProductListActivity.TABBAR_INDEX_FILTER, null);
                        }
                    } else if (productListFilterSortPageEntity.getPreviousFragmentType() == ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS) {
                        filterSortActivity.setSVRAppserviceProductSearchParameterMinPriceMaxPrice(productListFilterSortPageEntity.getPreviousFragmentType(), -1, minValue, maxValue);
                        if (fragmentListener != null) {
                            fragmentListener.onFilterSortListItemClick(ProductListActivity.TABBAR_INDEX_FILTER, null);
                        }
                    } else if (productListFilterSortPageEntity.getPreviousFragmentType() == MerchantStoreFrontActivity.ACTIVITY_TYPE_PRODUCTLIST_MERCHANT) {
                        filterSortActivity.setSVRAppserviceProductSearchParameterMinPriceMaxPrice(productListFilterSortPageEntity.getPreviousFragmentType(), -1, minValue, maxValue);
                        if (fragmentListener != null) {
                            fragmentListener.onFilterSortListItemClick(MerchantStoreFrontActivity.TABBAR_INDEX_FILTER, null);
                        }
                    }else if (productListFilterSortPageEntity.getPreviousFragmentType() == CurationActivity.ACTIVITY_TYPE_PRODUCTLIST_CURATION) {
                        filterSortActivity.setSVRAppserviceProductSearchParameterMinPriceMaxPrice(productListFilterSortPageEntity.getPreviousFragmentType(), -1, minValue, maxValue);
                        if (fragmentListener != null) {
                            fragmentListener.onFilterSortListItemClick(CurationActivity.TABBAR_INDEX_FILTER, null);
                        }
                    }else if(productListFilterSortPageEntity.getPreviousFragmentType()==BrandStoreFontActivity.ACTIVITY_TYPE_PRODUCTLIST_BRANDSTORE){
                        filterSortActivity.setSVRAppserviceProductSearchParameterMinPriceMaxPrice(productListFilterSortPageEntity.getPreviousFragmentType(), -1, minValue, maxValue);
                        if (fragmentListener != null) {
                            fragmentListener.onFilterSortListItemClick(CurationActivity.TABBAR_INDEX_FILTER, null);
                        }
                    }
                }
            }
        });
        return priceSeekBar;
    }

    private void addFilterAllBrands() {
        if (brandItemReturnEntityArrayList != null) {
            brandItemReturnEntityArrayList.clear();

            if (filterBrandAdapter != null) {
                filterBrandAdapter.notifyDataSetChanged();
            }
        }

        if (filterSortActivity == null) {
            ctvAllBrandsTitle.setText(FILTER_ALLBRANDS + " (0) ");

            ctvAllBrandsTitle.setTextColor(getResources().getColor(R.color.black000000));
            rlAllBrandsPlus.setVisibility(View.GONE);

            LinearLayout.LayoutParams dividerlp = (LinearLayout.LayoutParams) vAllBrandsContentAllTypesTitleDivider.getLayoutParams();
            if (dividerlp != null) {
                dividerlp.height = 0;
                vAllBrandsContentAllTypesTitleDivider.setLayoutParams(dividerlp);
            }

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lvAllBrandsContent.getLayoutParams();
            if (lp != null) {
                lp.weight = 0;
                lvAllBrandsContent.setLayoutParams(lp);
            }
            return;
        }

        SVRAppserviceProductSearchFacetsBrandItemReturnEntity allBrandsEntity = null;
        if (productListFilterSortPageEntity == null || productListFilterSortPageEntity.getFacets() == null) {
            allBrandsEntity = new SVRAppserviceProductSearchFacetsBrandItemReturnEntity();
            allBrandsEntity.setLabel(FILTER_ALLBRANDS);
            allBrandsEntity.setSelected(true);
            allBrandsEntity.setValue(null);
            allBrandsEntity.setCount(0l);
            allBrandsEntity.setUrl(null);

            brandItemReturnEntityArrayList.add(allBrandsEntity);
        } else if (productListFilterSortPageEntity.getFacets().getBrand_filter() == null || productListFilterSortPageEntity.getFacets().getBrand_filter().size() <= 0) {
            allBrandsEntity = new SVRAppserviceProductSearchFacetsBrandItemReturnEntity();
            allBrandsEntity.setLabel(FILTER_ALLBRANDS);
            allBrandsEntity.setSelected(true);
            allBrandsEntity.setValue(null);
            allBrandsEntity.setCount(0l);
            allBrandsEntity.setUrl(null);

            brandItemReturnEntityArrayList.add(allBrandsEntity);
        } else {
            allBrandsEntity = productListFilterSortPageEntity.getFacets().getBrand_filter().get(0);
            boolean isSelect = false;
            for (int i = 0; i < productListFilterSortPageEntity.getFacets().getBrand_filter().size(); i++) {
                if (productListFilterSortPageEntity.getFacets().getBrand_filter().get(i).isSelected()) {
                    isSelect = true;
                }
            }
            if (!isSelect) {
                allBrandsEntity.setSelected(true);
            }
            brandItemReturnEntityArrayList.addAll(productListFilterSortPageEntity.getFacets().getBrand_filter());
        }

        long allBrandsCount = 0l;
        if (allBrandsEntity != null) {
            allBrandsCount = allBrandsEntity.getCount();
        }
        ctvAllBrandsTitle.setText(FILTER_ALLBRANDS + " (" + allBrandsCount + ") ");

        if (brandItemReturnEntityArrayList == null || brandItemReturnEntityArrayList.size() <= 0) {
            ctvAllBrandsTitle.setTextColor(getResources().getColor(R.color.black000000));
            rlAllBrandsPlus.setVisibility(View.GONE);

            LinearLayout.LayoutParams dividerlp = (LinearLayout.LayoutParams) vAllBrandsContentAllTypesTitleDivider.getLayoutParams();
            if (dividerlp != null) {
                dividerlp.height = 0;
                vAllBrandsContentAllTypesTitleDivider.setLayoutParams(dividerlp);
            }

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lvAllBrandsContent.getLayoutParams();
            if (lp != null) {
                lp.weight = 0;
                lvAllBrandsContent.setLayoutParams(lp);
            }

            allBrandsListViewIsShow = false;
        } else {
            ctvAllBrandsTitle.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
            AnimUtil.animatePlusSign(tvAllBrandsPlusAnimate, true, getContext());

            LinearLayout.LayoutParams dividerlp = (LinearLayout.LayoutParams) vAllBrandsContentAllTypesTitleDivider.getLayoutParams();
            if (dividerlp != null) {
                dividerlp.height = JDataUtils.dp2Px(1);
                vAllBrandsContentAllTypesTitleDivider.setLayoutParams(dividerlp);
            }

            filterBrandAdapter = new ProductListFilterSortFilterBrandAdapter((Activity) filterSortActivity, fragmentListener, brandItemReturnEntityArrayList, productListFilterSortPageEntity);
            lvAllBrandsContent.setAdapter(filterBrandAdapter);

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lvAllBrandsContent.getLayoutParams();
            if (lp != null) {
                lp.weight = 1.0f;
                lvAllBrandsContent.setLayoutParams(lp);
            }

            allBrandsListViewIsShow = true;
            rlFilterAllbrandsTitle.setEnabled(true);
        }
    }

    private void addFilterAllTypes( boolean expand) {
        if (modelTypeReturnEntityArrayList != null) {
            modelTypeReturnEntityArrayList.clear();

            if (filterTypeAdapter != null) {
                filterTypeAdapter.notifyDataSetChanged();
            }
        }

        if (filterSortActivity == null) {
            ctvAllTypesTitle.setText(FILTER_ALLTYPES + " (0) ");

            ctvAllTypesTitle.setTextColor(getResources().getColor(R.color.black000000));
            rlAllTypesPlus.setVisibility(View.GONE);

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lvAllTypesContent.getLayoutParams();
            if (lp != null) {
                lp.weight = 0;
                lvAllTypesContent.setLayoutParams(lp);
            }
            return;
        }

        SVRAppserviceProductSearchFacetsModelReturnEntity allTypesEntity = null;
        if (productListFilterSortPageEntity == null || productListFilterSortPageEntity.getFacets() == null) {
            allTypesEntity = new SVRAppserviceProductSearchFacetsModelReturnEntity();
            allTypesEntity.setLabel(FILTER_ALLTYPES);
            allTypesEntity.setSelected(false);
            allTypesEntity.setCount(0l);
            allTypesEntity.setValue(null);
            allTypesEntity.setUrl(null);

            modelTypeReturnEntityArrayList.add(allTypesEntity);
        } else if (productListFilterSortPageEntity.getFacets().getModel_type_filter() == null || productListFilterSortPageEntity.getFacets().getModel_type_filter().size() <= 0) {
            allTypesEntity = new SVRAppserviceProductSearchFacetsModelReturnEntity();
            allTypesEntity.setLabel(FILTER_ALLTYPES);
            allTypesEntity.setSelected(false);
            allTypesEntity.setCount(0l);
            allTypesEntity.setValue(null);
            allTypesEntity.setUrl(null);

            modelTypeReturnEntityArrayList.add(allTypesEntity);
        } else {
            allTypesEntity = productListFilterSortPageEntity.getFacets().getModel_type_filter().get(0);
            boolean isSelect = false;
            for (int i = 0; i < productListFilterSortPageEntity.getFacets().getModel_type_filter().size(); i++) {
                if (productListFilterSortPageEntity.getFacets().getModel_type_filter().get(i).isSelected()) {
                    isSelect = true;
                }
            }
            if (!isSelect) {
                allTypesEntity.setSelected(true);
            }
            modelTypeReturnEntityArrayList.addAll(productListFilterSortPageEntity.getFacets().getModel_type_filter());
        }

        long allTypesCount = 0l;
        if (allTypesEntity != null) {
            allTypesCount = allTypesEntity.getCount();
        }
        ctvAllTypesTitle.setText(FILTER_ALLTYPES + " (" + allTypesCount + ") ");

        if (modelTypeReturnEntityArrayList == null || modelTypeReturnEntityArrayList.size() <= 0) {
            ctvAllTypesTitle.setTextColor(getResources().getColor(R.color.black000000));
            rlAllTypesPlus.setVisibility(View.GONE);

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lvAllTypesContent.getLayoutParams();
            if (lp != null) {
                lp.weight = 0;
                lvAllTypesContent.setLayoutParams(lp);
            }
            allTypesListViewIsShow = false;
        } else {
            ctvAllTypesTitle.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
            rlAllTypesPlus.setVisibility(View.VISIBLE);

            filterTypeAdapter = new ProductListFilterSortFilterTypeAdapter((Activity) filterSortActivity, fragmentListener, modelTypeReturnEntityArrayList, productListFilterSortPageEntity);
            lvAllTypesContent.setAdapter(filterTypeAdapter);

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lvAllTypesContent.getLayoutParams();
            if (lp != null) {
                if (expand) {
                    lp.weight = 1;
                } else{
                    lp.weight = 0;
                }
                lvAllTypesContent.setLayoutParams(lp);
            }
            if(expand) {
                AnimUtil.animatePlusSign(tvAllTypesPlusAnimate, true, getContext());
                allTypesListViewIsShow = true;
            }else {
                allTypesListViewIsShow = false;
            }
            rlFilterAlltypesTitle.setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_headerbar_cancel: {
                if (fragmentListener != null) {
                    fragmentListener.onCancelClick(view);
                }
                break;
            }
            case R.id.rlFilterAllbrandsTitle: {
                // Brands
                if (allBrandsListViewIsShow) {
                    LinearLayout.LayoutParams dividerlp = (LinearLayout.LayoutParams) vAllBrandsContentAllTypesTitleDivider.getLayoutParams();
                    if (dividerlp != null) {
                        dividerlp.height = 0;
                        vAllBrandsContentAllTypesTitleDivider.setLayoutParams(dividerlp);
                        AnimUtil.animateLayoutChange(vAllBrandsContentAllTypesTitleDivider);
                    }

                    LinearLayout.LayoutParams contentlp = (LinearLayout.LayoutParams) lvAllBrandsContent.getLayoutParams();
                    if (contentlp != null) {
                        contentlp.weight = 0;
                        lvAllBrandsContent.setLayoutParams(contentlp);
                        AnimUtil.animateLayoutChange(lvAllBrandsContent);
                    }

                    AnimUtil.animatePlusSign(tvAllBrandsPlusAnimate, false, getContext());
                    ctvAllBrandsTitle.setTextColor(getResources().getColor(R.color.black000000));
                } else {
                    LinearLayout.LayoutParams dividerlp = (LinearLayout.LayoutParams) vAllBrandsContentAllTypesTitleDivider.getLayoutParams();
                    if (dividerlp != null) {
                        dividerlp.height = JDataUtils.dp2Px(1);
                        vAllBrandsContentAllTypesTitleDivider.setLayoutParams(dividerlp);
                        AnimUtil.animateLayoutChange(vAllBrandsContentAllTypesTitleDivider);
                    }

                    LinearLayout.LayoutParams contentlp = (LinearLayout.LayoutParams) lvAllBrandsContent.getLayoutParams();
                    if (contentlp != null) {
                        contentlp.weight = 1.0f;
                        lvAllBrandsContent.setLayoutParams(contentlp);
                        AnimUtil.animateLayoutChange(lvAllBrandsContent);
                    }

                    AnimUtil.animatePlusSign(tvAllBrandsPlusAnimate, true, getContext());
                    ctvAllBrandsTitle.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
                }

                // Types
                LinearLayout.LayoutParams contentlp = (LinearLayout.LayoutParams) lvAllTypesContent.getLayoutParams();
                if (contentlp != null) {
                    contentlp.weight = 0;
                    lvAllTypesContent.setLayoutParams(contentlp);
                    AnimUtil.animateLayoutChange(lvAllTypesContent);
                }
                if (allTypesListViewIsShow) {
                    AnimUtil.animatePlusSign(tvAllTypesPlusAnimate, false, getContext());
                    allTypesListViewIsShow = false;
                }

                AnimUtil.animateLayoutChange(rlFilterAlltypesTitle);
                AnimUtil.animateLayoutChange(vAllTypesTitleContentDivider);

                ctvAllTypesTitle.setTextColor(getResources().getColor(R.color.black000000));
                allBrandsListViewIsShow = !allBrandsListViewIsShow;

                break;
            }
            case R.id.rlFilterAlltypesTitle: {
                // Types
                if (allTypesListViewIsShow) {
                    LinearLayout.LayoutParams contentlp = (LinearLayout.LayoutParams) lvAllTypesContent.getLayoutParams();
                    if (contentlp != null) {
                        contentlp.weight = 0;
                        lvAllTypesContent.setLayoutParams(contentlp);
                        AnimUtil.animateLayoutChange(lvAllTypesContent);
                    }

                    AnimUtil.animatePlusSign(tvAllTypesPlusAnimate, false, getContext());
                    ctvAllTypesTitle.setTextColor(getResources().getColor(R.color.black000000));
                } else {
                    LinearLayout.LayoutParams contentlp = (LinearLayout.LayoutParams) lvAllTypesContent.getLayoutParams();
                    if (contentlp != null) {
                        contentlp.weight = 1.0f;
                        lvAllTypesContent.setLayoutParams(contentlp);
                        AnimUtil.animateLayoutChange(lvAllTypesContent);
                    }

                    AnimUtil.animatePlusSign(tvAllTypesPlusAnimate, true, getContext());
                    ctvAllTypesTitle.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
                }

                // Brands
                LinearLayout.LayoutParams dividerlp = (LinearLayout.LayoutParams) vAllBrandsContentAllTypesTitleDivider.getLayoutParams();
                if (dividerlp != null) {
                    dividerlp.height = 0;
                    vAllBrandsContentAllTypesTitleDivider.setLayoutParams(dividerlp);
                    AnimUtil.animateLayoutChange(vAllBrandsContentAllTypesTitleDivider);
                }

                LinearLayout.LayoutParams contentlp = (LinearLayout.LayoutParams) lvAllBrandsContent.getLayoutParams();
                if (contentlp != null) {
                    contentlp.weight = 0;
                    lvAllBrandsContent.setLayoutParams(contentlp);
                    AnimUtil.animateLayoutChange(lvAllBrandsContent);
                }

                if (allBrandsListViewIsShow) {
                    AnimUtil.animatePlusSign(tvAllBrandsPlusAnimate, false, getContext());

                    allBrandsListViewIsShow = false;
                }

                AnimUtil.animateLayoutChange(rlFilterAlltypesTitle);
                AnimUtil.animateLayoutChange(vAllTypesTitleContentDivider);

                ctvAllBrandsTitle.setTextColor(getResources().getColor(R.color.black000000));
                allTypesListViewIsShow = !allTypesListViewIsShow;

                break;
            }
        }
    }
}
