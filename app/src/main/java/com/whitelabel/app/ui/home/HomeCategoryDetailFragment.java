package com.whitelabel.app.ui.home;

import android.content.Intent;

import android.os.Bundle;
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
import com.whitelabel.app.R;
import com.whitelabel.app.activity.ProductActivity;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.fragment.HomeBaseFragment;
import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.model.ProductListItemToProductDetailsEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.network.ImageLoader;
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
 * Use the {@link HomeCategoryDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeCategoryDetailFragment extends HomeBaseFragment<HomeCategoryDetailContract.Presenter>implements HomeCategoryDetailContract.View,SwipeRefreshLayout.OnRefreshListener {
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
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeCategoryDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeCategoryDetailFragment newInstance(int index,String id) {
        HomeCategoryDetailFragment fragment = new HomeCategoryDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putString(ARG_CATEGORY_ID,id);
        bundle.putInt(ARG_CATEGORY_INDEX,index);
        fragment.setArguments(bundle);
        return fragment;
    }
    private String mCategoryId;
    public final static  String ARG_CATEGORY_ID="category_id";
    public final static  String ARG_CATEGORY_INDEX="category_index";
    private int mIndex;
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
        View view = inflater.inflate(R.layout.fragment_home_category, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void closeRefreshLaout() {
        if(swipeContainer!=null){
            swipeContainer.setRefreshing(false);
        }
    }

    private ImageLoader mImageLoader;
    private CategoryDetailAdapter mAdapter;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImageLoader=new ImageLoader(getActivity());
        swipeContainer.setColorSchemeColors(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        swipeContainer.setOnRefreshListener(this);
        if(mIndex==0) {
            showProgressDialog();
        }
        mPresenter.getCategoryDetail(mCategoryId);
    }
    @Override
    public void onRefresh() {
        mPresenter.getCategoryDetail(mCategoryId);
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
//    public CategoryDetailModel  getData(){
//        String  data="{\n" +
//                "    \"category_id\": \"1111\",\n" +
//                "    \"category_name\": \"category1\",\n" +
//                "    \"category_img\": [\n" +
//                "      \"image.jpg\",\n" +
//                "      \"icon\"\n" +
//                "    ],\n" +
//                "    \"newArrivalProducts\": [\n" +
//                "      {\n" +
//                "        \"productId\": \"243008\",\n" +
//                "        \"name\": \"Jh 12045 Banquet Table White (70844151) (70844151)\",\n" +
//                "        \"brand\": \"Tesco\",\n" +
//                "        \"brandId\": \"10677\",\n" +
//                "        \"inStock\": \"1\",\n" +
//                "        \"maxSaleQty\": 10,\n" +
//                "        \"price\": \"84.90\",\n" +
//                "        \"final_price\": \"55.90\",\n" +
//                "        \"smallImage\": \"catalog/product/1076/7/0/70844151-(1).jpg\",\n" +
//                "        \"is_like\": 0,\n" +
//                "        \"item_id\": 0,\n" +
//                "        \"vendorDisplayName\": \"Tesco\",\n" +
//                "        \"vendor_id\": \"1076\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"productId\": \"243008\",\n" +
//                "        \"name\": \"Jh 12045 Banquet Table White (70844151) (70844151)\",\n" +
//                "        \"brand\": \"Tesco\",\n" +
//                "        \"brandId\": \"10677\",\n" +
//                "        \"inStock\": \"1\",\n" +
//                "        \"maxSaleQty\": 10,\n" +
//                "        \"price\": \"84.90\",\n" +
//                "        \"final_price\": \"55.90\",\n" +
//                "        \"smallImage\": \"catalog/product/1076/7/0/70844151-(1).jpg\",\n" +
//                "        \"is_like\": 0,\n" +
//                "        \"item_id\": 0,\n" +
//                "        \"vendorDisplayName\": \"Tesco\",\n" +
//                "        \"vendor_id\": \"1076\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"productId\": \"243008\",\n" +
//                "        \"name\": \"Jh 12045 Banquet Table White (70844151) (70844151)\",\n" +
//                "        \"brand\": \"Tesco\",\n" +
//                "        \"brandId\": \"10677\",\n" +
//                "        \"inStock\": \"1\",\n" +
//                "        \"maxSaleQty\": 10,\n" +
//                "        \"price\": \"84.90\",\n" +
//                "        \"final_price\": \"55.90\",\n" +
//                "        \"smallImage\": \"catalog/product/1076/7/0/70844151-(1).jpg\",\n" +
//                "        \"is_like\": 0,\n" +
//                "        \"item_id\": 0,\n" +
//                "        \"vendorDisplayName\": \"Tesco\",\n" +
//                "        \"vendor_id\": \"1076\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"productId\": \"243008\",\n" +
//                "        \"name\": \"Jh 12045 Banquet Table White (70844151) (70844151)\",\n" +
//                "        \"brand\": \"Tesco\",\n" +
//                "        \"brandId\": \"10677\",\n" +
//                "        \"inStock\": \"1\",\n" +
//                "        \"maxSaleQty\": 10,\n" +
//                "        \"price\": \"84.90\",\n" +
//                "        \"final_price\": \"55.90\",\n" +
//                "        \"smallImage\": \"catalog/product/1076/7/0/70844151-(1).jpg\",\n" +
//                "        \"is_like\": 0,\n" +
//                "        \"item_id\": 0,\n" +
//                "        \"vendorDisplayName\": \"Tesco\",\n" +
//                "        \"vendor_id\": \"1076\"\n" +
//                "      }\n" +
//                "    ],\n" +
//                "    \"bestSellerProducts\": [\n" +
//                "      {\n" +
//                "        \"productId\": \"243008\",\n" +
//                "        \"name\": \"Jh 12045 Banquet Table White (70844151) (70844151)\",\n" +
//                "        \"brand\": \"Tesco\",\n" +
//                "        \"brandId\": \"10677\",\n" +
//                "        \"inStock\": \"1\",\n" +
//                "        \"maxSaleQty\": 10,\n" +
//                "        \"price\": \"84.90\",\n" +
//                "        \"final_price\": \"55.90\",\n" +
//                "        \"smallImage\": \"catalog/product/1076/7/0/70844151-(1).jpg\",\n" +
//                "        \"is_like\": 0,\n" +
//                "        \"item_id\": 0,\n" +
//                "        \"vendorDisplayName\": \"Tesco\",\n" +
//                "        \"vendor_id\": \"1076\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"productId\": \"243008\",\n" +
//                "        \"name\": \"Jh 12045 Banquet Table White (70844151) (70844151)\",\n" +
//                "        \"brand\": \"Tesco\",\n" +
//                "        \"brandId\": \"10677\",\n" +
//                "        \"inStock\": \"1\",\n" +
//                "        \"maxSaleQty\": 10,\n" +
//                "        \"price\": \"84.90\",\n" +
//                "        \"final_price\": \"55.90\",\n" +
//                "        \"smallImage\": \"catalog/product/1076/7/0/70844151-(1).jpg\",\n" +
//                "        \"is_like\": 0,\n" +
//                "        \"item_id\": 0,\n" +
//                "        \"vendorDisplayName\": \"Tesco\",\n" +
//                "        \"vendor_id\": \"1076\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"productId\": \"243008\",\n" +
//                "        \"name\": \"Jh 12045 Banquet Table White (70844151) (70844151)\",\n" +
//                "        \"brand\": \"Tesco\",\n" +
//                "        \"brandId\": \"10677\",\n" +
//                "        \"inStock\": \"1\",\n" +
//                "        \"maxSaleQty\": 10,\n" +
//                "        \"price\": \"84.90\",\n" +
//                "        \"final_price\": \"55.90\",\n" +
//                "        \"smallImage\": \"catalog/product/1076/7/0/70844151-(1).jpg\",\n" +
//                "        \"is_like\": 0,\n" +
//                "        \"item_id\": 0,\n" +
//                "        \"vendorDisplayName\": \"Tesco\",\n" +
//                "        \"vendor_id\": \"1076\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"productId\": \"243008\",\n" +
//                "        \"name\": \"Jh 12045 Banquet Table White (70844151) (70844151)\",\n" +
//                "        \"brand\": \"Tesco\",\n" +
//                "        \"brandId\": \"10677\",\n" +
//                "        \"inStock\": \"1\",\n" +
//                "        \"maxSaleQty\": 10,\n" +
//                "        \"price\": \"84.90\",\n" +
//                "        \"final_price\": \"55.90\",\n" +
//                "        \"smallImage\": \"catalog/product/1076/7/0/70844151-(1).jpg\",\n" +
//                "        \"is_like\": 0,\n" +
//                "        \"item_id\": 0,\n" +
//                "        \"vendorDisplayName\": \"Tesco\",\n" +
//                "        \"vendor_id\": \"1076\"\n" +
//                "      }\n" +
//                "    ]\n" +
//                "  }";
//        CategoryDetailModel categoryDetailModel=JJsonUtils.parseJsonObj(data,CategoryDetailModel.class);
//        return categoryDetailModel;
//    }

    @Override
    public void showErrorMsg(String errorMsg) {
        if(getActivity()!=null)
        JViewUtils.showErrorToast(getActivity(),errorMsg);
    }
    @Override
    public void loadData(CategoryDetailModel categoryDetailModel) {
        if(getActivity()!=null) {
            mAdapter = new CategoryDetailAdapter(getActivity(), categoryDetailModel, mImageLoader);
            mAdapter.setOnItemClickLitener(new CategoryDetailAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(CategoryDetailAdapter.ItemViewHolder itemViewHolder, int position) {
                    SVRAppserviceProductSearchResultsItemReturnEntity productEntity =null;
                    if(position>mAdapter.getData().getNewArrivalProducts().size()){
                        productEntity=mAdapter.getData().getBestSellerProducts().get((position-mAdapter.getData().getNewArrivalProducts().size()-2));
                    }else{
                        productEntity=mAdapter.getData().getNewArrivalProducts().get((position-1));
                    }
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
            });
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView1.setLayoutManager(layoutManager);
            layoutManager.setSpanSizeLookup(mTwoRowSpan);
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
    @Override
    public HomeCategoryDetailContract.Presenter getPresenter() {
        return  new HomeCategoryDetailPresenterImpl();
    }
}

