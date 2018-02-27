package com.whitelabel.app.ui.home.fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whitelabel.app.Const;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.callback.IHomeItemClickListener;
import com.whitelabel.app.callback.WheelPickerCallback;
import com.whitelabel.app.fragment.HomeBaseFragment;
import com.whitelabel.app.model.CategoryDetailNewModel;
import com.whitelabel.app.model.ProductListItemToProductDetailsEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.model.ShopBrandResponse;
import com.whitelabel.app.model.WheelPickerConfigEntity;
import com.whitelabel.app.model.WheelPickerEntity;
import com.whitelabel.app.network.BaseHttp;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.home.HomeCategoryDetailContract;
import com.whitelabel.app.ui.home.ShopBrandContract;
import com.whitelabel.app.ui.home.adapter.CategoryDetailHorizontalAdapter;
import com.whitelabel.app.ui.home.adapter.ShopBrandDetailAdapter;
import com.whitelabel.app.ui.productdetail.ProductDetailActivity;
import com.whitelabel.app.utils.AnimUtil;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.PageIntentUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.utils.logger.Logger;
import com.whitelabel.app.widget.CustomButton;
import com.whitelabel.app.widget.CustomSwipefreshLayout;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.GridSpacingItemDecoration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;

/**
 * replace the original ShopBrandActivity
 */
public class HomeHomeFragmentShopBrand extends HomeBaseFragment<ShopBrandContract.Presenter> implements SwipeRefreshLayout.OnRefreshListener, ShopBrandContract.View{
    @BindView(R.id.tv_start_with)
    CustomTextView tvStartWith;
    @BindView(R.id.rcv_brand_list)
    RecyclerView rcvBrandList;
    @BindView(R.id.iv_arrow_down_black)
    ImageView ivArrowDownBlack;
    @BindView(R.id.swipe_container)
    CustomSwipefreshLayout swipeContainer;
    @BindView(R.id.connectionBreaks)
    RelativeLayout rlNetError;

    public final static String ARG_CATEGORY_ID = "category_id";
    public final static String ARG_CATEGORY_INDEX = "category_index";

    private HomeActivity homeActivity;
    private Context mContext;
    private String menuId;
    private String menuIndex;
    private static final int COLUMN=3;
    private static final String DEFALUT_STRING="0-9";
    private String currentWheelTitle="";
    private int currentWheelIndex=0;
    private GridLayoutManager gridLayoutManager;
    private ShopBrandDetailAdapter recycViewAdapter;
    private List<ShopBrandResponse.BrandsBean.ItemsBean> titles=new ArrayList<>();
    private List<ShopBrandResponse.BrandsBean.ItemsBean> titlesAnditems=new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeHomeFragmentV4.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeHomeFragmentShopBrand newInstance(int index, String id) {
        HomeHomeFragmentShopBrand fragment = new HomeHomeFragmentShopBrand();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_ID, id);
        args.putInt(ARG_CATEGORY_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showSwipeLayout() {
        if(swipeContainer!=null){
            swipeContainer.setRefreshing(true);
        }
    }
    @Override
    public void closeSwipeLayout() {
        if(swipeContainer!=null) {
            swipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void showOnlineErrorLayout() {
        if (getActivity() != null) {
            rlNetError.setVisibility(View.VISIBLE);
            RequestErrorHelper requestErrorHelper = new RequestErrorHelper(getContext(), rlNetError);
            requestErrorHelper.showConnectionBreaks(BaseHttp.ERROR_TYPE_NET);
            requestErrorHelper.setResponseListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.getOnlineCategoryDetail(false,menuId);
                }
            });
        }
    }

    @Override
    public void hideOnlineErrorLayout() {
        rlNetError.setVisibility(View.GONE);
    }

    @Override
    public void inject() {
        super.inject();
        DaggerPresenterComponent1.builder().applicationComponent(WhiteLabelApplication.getApplicationComponent()).
                presenterModule(new PresenterModule(getActivity())).build().inject(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivity= (HomeActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            menuId = (String) getArguments().get(ARG_CATEGORY_ID);
            menuIndex = (String) getArguments().get(ARG_CATEGORY_INDEX);
        }
    }

    @Override
    public void onRefresh() {
        if (getActivity()!=null&&!getActivity().isFinishing()&&isAdded()) {
            mPresenter.getOnlineCategoryDetail(false,menuId);
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        swipeContainer.setColorSchemeColors(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        swipeContainer.setColorSchemeColors(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        swipeContainer.setOnRefreshListener(this);
        mContext=homeActivity;
        mPresenter.getOnlineCategoryDetail(false,menuId);
        initRecyclerView();
        initTopBrandSelect();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setScrollToolBarEnable(false);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_shop_brand, container, false);
        ButterKnife.bind(this, view);
        setRetryTheme(view);
        return view;
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        if(getActivity()!=null)
            JViewUtils.showErrorToast(getActivity(),errorMsg);
    }

    private void initRecyclerView() {
        gridLayoutManager = new GridLayoutManager(mContext, COLUMN,GridLayoutManager.VERTICAL,false);

        rcvBrandList.setLayoutManager(gridLayoutManager);
        rcvBrandList.addItemDecoration(new GridSpacingItemDecoration(COLUMN,
            JToolUtils.dip2px(mContext,4),true));
        recycViewAdapter = new ShopBrandDetailAdapter(mContext, titlesAnditems);
        recycViewAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                int type = rcvBrandList.getAdapter().getItemViewType(position);
                if ( Const.HEADER == type) {
                    return 3;
                } else{
                    return 1;
                }
            }
        });
        rcvBrandList.setAdapter(recycViewAdapter);
        recycViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!titlesAnditems.isEmpty() && titlesAnditems.size()>position){
                    ShopBrandResponse.BrandsBean.ItemsBean itemsBean = titlesAnditems.get(position);
                    if (Const.ITEM==itemsBean.getItemType()){
//                        skipToSearchPage(position);
                        PageIntentUtils.skipToSerachPage(mContext,itemsBean);
                    }
                }
            }
        });
    }

    @Override
    public void loadData(List<ShopBrandResponse.BrandsBean.ItemsBean> itemsBean) {
        this.titlesAnditems=itemsBean;
        recycViewAdapter.setNewData(this.titlesAnditems);
        recycViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadTitleData(List<ShopBrandResponse.BrandsBean.ItemsBean> itemsBean) {
        titles=itemsBean;
        initTopBrandSelect();
    }

    private void initTopBrandSelect() {
        String brandStartWith= getResources().getString(R.string.shop_by_brand_start_with);
        if (titles!=null&& !titles.isEmpty()){
            currentWheelTitle=titles.get(0).getTitle();
            currentWheelIndex=0;
            String finalString = String.format(brandStartWith,currentWheelTitle);
            tvStartWith.setText(finalString);
        }else {
            currentWheelTitle=DEFALUT_STRING;
            currentWheelIndex=0;
            tvStartWith.setText(String.format(brandStartWith,currentWheelTitle ));
        }
    }

    @OnClick(R.id.tv_start_with)
    public void onViewClicked() {
        wheelPickerBrandTitle();
    }

    private void wheelPickerBrandTitle() {
        AnimUtil.rotateArrow(ivArrowDownBlack, true);
        ArrayList<WheelPickerEntity> wheel = new ArrayList<WheelPickerEntity>();
        if (titles!=null && !titles.isEmpty()){
            for (int i=0;i<titles.size();i++){
                ShopBrandResponse.BrandsBean.ItemsBean itemsBean = titles.get(i);
                WheelPickerEntity entity = new WheelPickerEntity();
                entity.setDisplay(itemsBean.getTitle());
                entity.setIndex(i);
                entity.setValue(itemsBean.getTitle());
                wheel.add(entity);
            }
        }
        WheelPickerEntity oldwheel = new WheelPickerEntity();
        oldwheel.setValue(currentWheelTitle);
        oldwheel.setIndex(currentWheelIndex);

        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(wheel);
        configEntity.setOldValue(oldwheel);
        configEntity.setIndex(oldwheel.getIndex());
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                AnimUtil.rotateArrow(ivArrowDownBlack, false);
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                AnimUtil.rotateArrow(ivArrowDownBlack, false);
                String brandStartWith= getResources().getString(R.string.shop_by_brand_start_with);
                if (newValue.getDisplay() == null) {
                    tvStartWith.setText(String.format(brandStartWith, DEFALUT_STRING));
                    currentWheelTitle=DEFALUT_STRING;
                    currentWheelIndex=0;
                } else {
                    tvStartWith.setText(String.format(brandStartWith, newValue.getDisplay()));
                    currentWheelTitle=newValue.getValue();
                    currentWheelIndex=newValue.getIndex();
                }
                if (!titles.isEmpty()){
                    int currentTitlePos= titles.get(currentWheelIndex).getPosition();
                    gridLayoutManager.scrollToPositionWithOffset(currentTitlePos, 0);
                }
            }
        });
        JViewUtils.showWheelPickerOneDialog(mContext, configEntity);    }



}
