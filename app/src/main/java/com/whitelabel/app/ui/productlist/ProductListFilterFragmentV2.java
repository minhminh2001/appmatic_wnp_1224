package com.whitelabel.app.ui.productlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.whitelabel.app.R;
import com.whitelabel.app.fragment.FilterModel;
import com.whitelabel.app.model.FilterItemModel;
import com.whitelabel.app.model.FilterItemValueModel;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsPriceReturnEntity;
import com.whitelabel.app.widget.CustomTextView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListFilterFragmentV2 extends Fragment {
    @BindView(R.id.ctvHeaderBarTitle)
    CustomTextView ctvHeaderBarTitle;
    @BindView(R.id.tv_headerbar_cancel)
    ImageView tvHeaderbarCancel;
    @BindView(R.id.rl_headerbar_cancel)
    RelativeLayout rlHeaderbarCancel;
    @BindView(R.id.rlHeaderBar)
    RelativeLayout rlHeaderBar;
    @BindView(R.id.vHeaderBarContentDivider)
    View vHeaderBarContentDivider;
    @BindView(R.id.rv_recycler)
    RecyclerView rvRecycler;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public ProductListFilterFragmentV2() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProductListFilterFragmentV2.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductListFilterFragmentV2 newInstance() {
        ProductListFilterFragmentV2 fragment = new ProductListFilterFragmentV2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
    }
    private void initRecyclerView() {
        LinearLayoutManager  linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecycler.setHasFixedSize(true);
        rvRecycler.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator)rvRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        FilterModel filterModel=getFilterData();
        ProductListFilterExpandAdapter  adapter=new ProductListFilterExpandAdapter(getActivity(),filterModel);
        adapter.setRecycleView(rvRecycler);
        rvRecycler.setAdapter(adapter);
    }
    public FilterModel  getFilterData(){
        FilterModel filterModel=new FilterModel();
        List<FilterItemModel> filterList=new ArrayList<>();
        SVRAppserviceProductSearchFacetsPriceReturnEntity price_filter=new SVRAppserviceProductSearchFacetsPriceReturnEntity();
        price_filter.setMax_price(1000);
        price_filter.setMin_price(100);
        filterModel.setPrice_filter(price_filter);
        filterModel.setFilterList(filterList);
        FilterItemModel filterItemModel=new FilterItemModel();
        filterItemModel.setId("10000");
        filterItemModel.setLabel("Color");
        List<FilterItemValueModel> filterItemValueModels=new ArrayList<>();
        filterItemValueModels.add(new FilterItemValueModel("green","12"));
        filterItemValueModels.add(new FilterItemValueModel("green1","13"));
        filterItemModel.setValues(filterItemValueModels);
        filterList.add(filterItemModel);


        FilterItemModel filterItemModel1=new FilterItemModel();
        filterItemModel1.setId("10001");
        filterItemModel1.setLabel("Size");
        List<FilterItemValueModel> filterItemValueModels1=new ArrayList<>();
        filterItemValueModels1.add(new FilterItemValueModel("45","12"));
        filterItemValueModels1.add(new FilterItemValueModel("55","13"));
        filterItemModel1.setValues(filterItemValueModels1);
        filterList.add(filterItemModel1);
        return filterModel;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_list_filter_fragment_v2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
