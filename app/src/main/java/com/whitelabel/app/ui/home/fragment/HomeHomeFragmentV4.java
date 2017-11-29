package com.whitelabel.app.ui.home.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.bean.OperateProductIdPrecache;
import com.whitelabel.app.callback.IHomeItemClickListener;
import com.whitelabel.app.data.service.BaseManager;
import com.whitelabel.app.fragment.HomeBaseFragment;
import com.whitelabel.app.model.CategoryDetailNewModel;
import com.whitelabel.app.model.ProductListItemToProductDetailsEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.checkout.CheckoutPresenterImpl;
import com.whitelabel.app.ui.home.adapter.CategoryDetailHorizontalAdapter;
import com.whitelabel.app.ui.home.HomeCategoryDetailContract;
import com.whitelabel.app.ui.productdetail.ProductDetailActivity;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.PageIntentUtils;
import com.whitelabel.app.utils.logger.Logger;
import com.whitelabel.app.widget.CustomButton;
import com.whitelabel.app.widget.CustomSwipefreshLayout;
import com.whitelabel.app.widget.CustomTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HomeHomeFragmentV4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeHomeFragmentV4 extends HomeBaseFragment<HomeCategoryDetailContract.Presenter> implements HomeActivity.HomeFragmentCallback,SwipeRefreshLayout.OnRefreshListener,HomeCategoryDetailContract.View,HomeActivity.ICommunHomeActivity {
    public final static String ARG_CATEGORY_ID = "category_id";
    public final static String ARG_CATEGORY_INDEX = "category_index";
    public final static String SHOP_BRAND_MENU_ID = "816";
    public final static String LAST_TAB_INDICATOR_INDEX = "2";
    public final static String ARG_CATEGORY_NAME = "SHOP BY BRAND";
    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView1;
//    @BindView(R.id.v_view)
//    View vView;
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
    private String mCategoryId;
    private int mIndex;
    private ImageLoader mImageLoader;
    private CategoryDetailNewModel categoryDetailModel;
    CategoryDetailHorizontalAdapter mAdapter;
    private HomeActivity homeActivity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeHomeFragmentV4.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeHomeFragmentV4 newInstance(int index, String id) {
        HomeHomeFragmentV4 fragment = new HomeHomeFragmentV4();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_ID, id);
        args.putInt(ARG_CATEGORY_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void dissmissProgressDialog() {
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
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivity= (HomeActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategoryId = (String) getArguments().get(ARG_CATEGORY_ID);
            mIndex = getArguments().getInt(ARG_CATEGORY_INDEX);
        }
    }

    @Override
    public void onRefresh() {
        if (getActivity()!=null&&!getActivity().isFinishing()&&isAdded()) {
            mPresenter.getOnlineCategoryDetail(mCategoryId);
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        swipeContainer.setColorSchemeColors(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        mImageLoader=new ImageLoader(getActivity());
        swipeContainer.setColorSchemeColors(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        swipeContainer.setOnRefreshListener(this);
        requestData();
    }
    @Override
    public void requestData() {
        mPresenter.getCategoryDetail(mCategoryId);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setScrollToolBarEnable(false);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_home_fragment_v4, container, false);
        ButterKnife.bind(this, view);
        setRetryTheme(view);
        return view;
    }
    @Override
    public void showErrorMsg(String errorMsg) {
        if(getActivity()!=null)
            JViewUtils.showErrorToast(getActivity(),errorMsg);
    }
    @Override
    public void loadData(final CategoryDetailNewModel categoryDetailModel) {
         if(getActivity()!=null){
             this.categoryDetailModel =categoryDetailModel;
//             this.categoryDetailModel=categoryDetailModel;
             mAdapter=
                     new CategoryDetailHorizontalAdapter(homeActivity,this.categoryDetailModel,mImageLoader);
             mAdapter.setiCommunHomeActivity(this);
             mAdapter.setOnProductItemClickListener(new IHomeItemClickListener.IHorizontalItemClickListener() {
                 @Override
                 public void onItemClick(RecyclerView.ViewHolder itemViewHolder, int parentPosition, int childPosition) {
                     startProductDetailActivity(categoryDetailModel.getCarousels().get(parentPosition).getItems().get(childPosition));
                 }
             });
            mAdapter.setOnHeaderClick(new IHomeItemClickListener.IHeaderItemClickListener() {
                @Override
                public void onItemClick(RecyclerView.ViewHolder headerViewHolder, int position) {
                    CategoryDetailNewModel.BannersBean bannersBean = categoryDetailModel.getBanners().get(position);
                    PageIntentUtils.skipToSerachPage(getActivity(),bannersBean);
                }
            });
             LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
             recyclerView1.setLayoutManager(linearLayoutManager);
             recyclerView1.setAdapter(mAdapter);
             recyclerView1.setOnTouchListener(new View.OnTouchListener() {
                 @Override
                 public boolean onTouch(View v, MotionEvent event) {
                     return false;
                 }
             });
         }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("onActivityResult");
        if (requestCode==HomeHomeFragmentV3.REQUEST_PRODUCT_DETAIL_CODE && resultCode== Activity.RESULT_OK){
            Logger.e("onActivityResult 2");
            if (data != null) {
                if (!data.getBooleanExtra("needRefreshWhenBackPressed", false)) {
                    String productId = data.getStringExtra("productId");
                    String itemId = data.getStringExtra("itemId");
                    int isLike = data.getIntExtra("isLike", -1);
                    Logger.e("peoductList productId :"+productId+" isLike:"+isLike);
                    if (!TextUtils.isEmpty(productId) && isLike != -1) {
                        notifyBackThisPageChangeWishIconStatus(productId, isLike, itemId);
                    }
                } else {
                    onRefresh();
                }
            }
        }
    }

    private void notifyBackThisPageChangeWishIconStatus(String productId,int isLike,String itemId) {
        if (categoryDetailModel !=null && categoryDetailModel.getCarousels()!=null ){
            List<CategoryDetailNewModel.CarouselsBean> carousels = categoryDetailModel.getCarousels();
            for (CategoryDetailNewModel.CarouselsBean carouselsBean:carousels ) {
                if (carouselsBean.getItems()!=null){
                    List<SVRAppserviceProductSearchResultsItemReturnEntity> items = carouselsBean.getItems();
                    for (SVRAppserviceProductSearchResultsItemReturnEntity entity:items){
                        if (productId!=null && productId.equals(entity.getProductId())){
                            entity.setIsLike(isLike);
                            entity.setItemId(itemId);
                            if (mAdapter!=null){
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
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
    public void startProductDetailActivity( SVRAppserviceProductSearchResultsItemReturnEntity productEntity){
        Intent intent = new Intent();
        intent.setClass(getActivity(), ProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("productId",productEntity.getProductId());
        bundle.putString("from", HomeHomeFragmentV3.FROM_HOME_LIST);
        bundle.putInt("isLike", productEntity.getIsLike());
        bundle.putSerializable("product_info", getProductListItemToProductDetailsEntity(productEntity));
        bundle.putString("imageurl", productEntity.getSmallImage());
        intent.putExtras(bundle);
        startActivityForResult(intent,HomeHomeFragmentV3.REQUEST_PRODUCT_DETAIL_CODE);
    }
    @Override
    public void closeRefreshLaout() {
        if(swipeContainer!=null){
            swipeContainer.setRefreshing(false);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void saveProductIdWhenCheckPage(String productId, int isLike, boolean isUnLogin) {
        homeActivity.saveProductIdWhenCheckPage(productId,isLike,isUnLogin);
    }

    @Override
    public boolean isUnLoginCanWishIconRefresh(String productId) {
        return homeActivity.isUnLoginCanWishIconRefresh(productId);
    }

}
