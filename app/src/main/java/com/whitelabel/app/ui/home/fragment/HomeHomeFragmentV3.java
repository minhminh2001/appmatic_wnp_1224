package com.whitelabel.app.ui.home.fragment;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.fragment.HomeBaseFragment;
import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.model.ProductListItemToProductDetailsEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.home.adapter.CategoryDetailVerticalAdapter;
import com.whitelabel.app.ui.home.HomeCategoryDetailContract;
import com.whitelabel.app.ui.productdetail.ProductDetailActivity;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomButton;
import com.whitelabel.app.widget.CustomSwipefreshLayout;
import com.whitelabel.app.widget.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HomeHomeFragmentV3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeHomeFragmentV3 extends HomeBaseFragment<HomeCategoryDetailContract.Presenter>implements HomeCategoryDetailContract.View,SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView1;
    @BindView(R.id.v_view)
    View vView;
    @BindView(R.id.swipe_container)
    CustomSwipefreshLayout swipeContainer;
    @BindView(R.id.iv_error)
    ImageButton ivError;
    @BindView(R.id.ctv_error_header)
    CustomTextView ctvErrorHeader;
    @BindView(R.id.ctv_error_subheader)
    CustomTextView ctvErrorSubheader;
    @BindView(R.id.ll_error_message)
    LinearLayout llErrorMessage;
    @BindView(R.id.imageButtonServer)
    ImageButton imageButtonServer;
    @BindView(R.id.customTextViewServer)
    CustomTextView customTextViewServer;
    @BindView(R.id.iv_try_again)
    ImageView ivTryAgain;
    @BindView(R.id.btn_try_again)
    CustomButton btnTryAgain;
    @BindView(R.id.try_again)
    LinearLayout tryAgain;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.connectionBreaks)
    RelativeLayout connectionBreaks;
    private ImageLoader mImageLoader;
    private CategoryDetailVerticalAdapter mAdapter;
    private String mCategoryId;
    public final static  String ARG_CATEGORY_ID="category_id";
    public final static  String ARG_CATEGORY_INDEX="category_index";
    private int mIndex;
    private boolean isPrepared, isVisible, mHasLoadedOnce;
    /**
     * Use this factory method to creaøte a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeHomeFragmentV3.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeHomeFragmentV3 newInstance(int index, String id) {
        HomeHomeFragmentV3 fragment = new HomeHomeFragmentV3();
        Bundle bundle=new Bundle();
        bundle.putString(ARG_CATEGORY_ID,id);
        bundle.putInt(ARG_CATEGORY_INDEX,index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void dissmissProgressDialog() {
        closeProgressDialog();
    }

    @Override
    public void showSwipeLayout() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);
            }
        });
    }

    @Override
    public void closeSwipeLayout() {
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void inject() {
        super.inject();
        DaggerPresenterComponent1.builder().applicationComponent(WhiteLabelApplication.getApplicationComponent()).
                presenterModule(new PresenterModule(getActivity())).build().inject(this);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mCategoryId= (String) getArguments().get(ARG_CATEGORY_ID);
            mIndex=getArguments().getInt(ARG_CATEGORY_INDEX);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setScrollToolBarEnable(true);
        View view = inflater.inflate(R.layout.fragment_home_category, container, false);
        ButterKnife.bind(this, view);
        setRetryTheme(view);
        isPrepared = true;
        init();
        return view;
    }
    @Override
    public void closeRefreshLaout() {
        if(swipeContainer!=null){
            swipeContainer.setRefreshing(false);
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImageLoader=new ImageLoader(getActivity());
        swipeContainer.setColorSchemeColors(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        swipeContainer.setOnRefreshListener(this);
    }
    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView1.setLayoutManager(layoutManager);
        layoutManager.setSpanSizeLookup(mTwoRowSpan);
//        recyclerView1.addItemDecoration(new SpacesItemDecoration(JDataUtils.dp2Px(5)));
    }
    @Override
    public void onRefresh() {
        if (getActivity()!=null&&!getActivity().isFinishing()&&isAdded()) {
            mPresenter.getCategoryDetail(mCategoryId);
        }
    }
    private final GridLayoutManager.SpanSizeLookup mTwoRowSpan = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            if (position == 0 ||position==mAdapter.getNewArrivalProductSize()+1) {
                return 2;
            }
            return 1;
        }
    };
    @Override
    public void showErrorMsg(String errorMsg) {
        if(getActivity()!=null)
        JViewUtils.showErrorToast(getActivity(),errorMsg);
    }
    public void init() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        mHasLoadedOnce = true;
        initRecyclerView();
        mPresenter.getCategoryDetail(mCategoryId);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            init();
        } else {
            isVisible = false;
        }
    }
    @Override
    public void loadData(CategoryDetailModel categoryDetailModel) {
        if(getActivity()!=null) {
            mAdapter = new CategoryDetailVerticalAdapter(getActivity(), categoryDetailModel, mImageLoader);
            mAdapter.setOnItemClickLitener(new CategoryDetailVerticalAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(CategoryDetailVerticalAdapter.ItemViewHolder itemViewHolder, int position) {
                    SVRAppserviceProductSearchResultsItemReturnEntity productEntity =null;
                    if(position>mAdapter.getData().getNewArrivalProducts().size()){
                        productEntity=mAdapter.getData().getBestSellerProducts().get((position-mAdapter.getData().getNewArrivalProducts().size()-2));
                    }else{
                        productEntity=mAdapter.getData().getNewArrivalProducts().get((position-1));
                    }
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ProductDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("productId",productEntity.getProductId());
                    bundle.putString("from", "from_product_list");
                    bundle.putSerializable("product_info", getProductListItemToProductDetailsEntity(productEntity));
                    bundle.putString("imageurl", productEntity.getSmallImage());
                    intent.putExtras(bundle);
                     getContext().startActivity(intent);
                    ((BaseActivity)getActivity()).startActivityTransitionAnim();
                }
            });
            recyclerView1.setAdapter(mAdapter);
        }
    }
    private ProductListItemToProductDetailsEntity getProductListItemToProductDetailsEntity(SVRAppserviceProductSearchResultsItemReturnEntity e) {
        ProductListItemToProductDetailsEntity entity = new ProductListItemToProductDetailsEntity();
        entity.setBrand(e.getBrand());
        entity.setCategory(e.getCategory());
        entity.setFinalPrice(e.getFinal_price());
        entity.setInStock(Integer.parseInt(e.getInStock()));
        entity.setName(e.getName());
        entity.setPrice(e.getPrice());
        entity.setVendorDisplayName(e.getVendorDisplayName());
        return entity;
    }

}