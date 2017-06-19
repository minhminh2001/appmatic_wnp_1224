package com.whitelabel.app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.ProductActivity;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.fragment.HomeBaseFragment;
import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.model.ProductListItemToProductDetailsEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomButton;
import com.whitelabel.app.widget.CustomSwipefreshLayout;
import com.whitelabel.app.widget.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HomeHomeFragmentV4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeHomeFragmentV4 extends HomeBaseFragment<HomeCategoryDetailContract.Presenter> implements HomeActivity.HomeFragmentCallback,SwipeRefreshLayout.OnRefreshListener,HomeCategoryDetailContract.View {
    public final static String ARG_CATEGORY_ID = "category_id";
    public final static String ARG_CATEGORY_INDEX = "category_index";
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
    public HomeCategoryDetailContract.Presenter getPresenter() {
        return new HomeCategoryDetailPresenterImpl();
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
            String sessionKey = "";
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())) {
                sessionKey = WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey();
            }
            mPresenter.getCategoryDetail(mCategoryId, sessionKey);
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
        if(mIndex==0) {
            showProgressDialog();
        }
        String sessionKey="";
        if(WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())){
            sessionKey=WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey();
        }
        mPresenter.getCategoryDetail(mCategoryId,sessionKey);
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
    public void loadData(final CategoryDetailModel categoryDetailModel) {
         if(getActivity()!=null){
             CategoryDetailHorizontalAdapter mAdapter=
                     new CategoryDetailHorizontalAdapter(categoryDetailModel,mImageLoader);
             mAdapter.setOnBestProductionItemClickListener(new CategoryDetailHorizontalAdapter.OnItemClickListener() {
                 @Override
                 public void onItemClick(RecyclerView.ViewHolder itemViewHolder, int position) {
                     startProductDetailActivity(categoryDetailModel.getBestSellerProducts().get(position));
                 }
             });
             mAdapter.setOnNewArrivalsItemClickListener(new CategoryDetailHorizontalAdapter.OnItemClickListener() {
                 @Override
                 public void onItemClick(RecyclerView.ViewHolder itemViewHolder, int position) {
                     startProductDetailActivity(categoryDetailModel.getNewArrivalProducts().get(position));
                 }
             });
             LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
             recyclerView1.setLayoutManager(linearLayoutManager);
             recyclerView1.setAdapter(mAdapter);
             recyclerView1.setOnTouchListener(new View.OnTouchListener() {
                 @Override
                 public boolean onTouch(View v, MotionEvent event) {
                     JLogUtils.i("ray","MotionEvent:"+event);
                     return false;
                 }
             });
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
        intent.setClass(getActivity(), ProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("productId",productEntity.getProductId());
        bundle.putString("from", "from_product_list");
        bundle.putSerializable("product_info", getProductListItemToProductDetailsEntity(productEntity));
        bundle.putString("imageurl", productEntity.getSmallImage());
        intent.putExtras(bundle);
        startActivityForResult(intent,1000);
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
}
