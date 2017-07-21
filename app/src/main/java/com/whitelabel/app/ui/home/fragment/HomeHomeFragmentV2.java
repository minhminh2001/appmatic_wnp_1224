package com.whitelabel.app.ui.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.adapter.CategoryTreeExpandableAdapter;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.fragment.HomeBaseFragment;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchCategoryItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.home.HomeHomeContract;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomSpeedLayoutManager;
import com.whitelabel.app.widget.ExpandableRecyclerAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;


public class HomeHomeFragmentV2 extends HomeBaseFragment<HomeHomeContract.Presenter> implements HomeHomeContract.View,HomeActivity.HomeFragmentCallback{

    @BindView(R.id.rl_category_tree)
    RecyclerView rlCategoryTree;
    private CategoryTreeExpandableAdapter mAdapter;
    private ImageLoader mImageLoader;
    public HomeHomeFragmentV2() {
        // Required empty public constructor
    }
    @Override
    public void inject() {
        DaggerPresenterComponent1.builder().applicationComponent(WhiteLabelApplication.getApplicationComponent()).
                presenterModule(new PresenterModule(getActivity())).build().inject(this);
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeHomeFragmentV1.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeHomeFragmentV2 newInstance() {
        HomeHomeFragmentV2 fragment = new HomeHomeFragmentV2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void requestData() {
        showProgressDialog();
        mPresenter.attachView(this);
        mPresenter.getSearchCategory();
    }

    public void initRecyclerView() {
        CustomSpeedLayoutManager linearLayoutManager = new CustomSpeedLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlCategoryTree.setLayoutManager(linearLayoutManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_home_fragment_v3, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mCommonCallback.setHomeSearchBarAndOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ProductListActivity.class);
                intent.putExtra(ProductListActivity.INTENT_DATA_PREVTYPE, ProductListActivity.INTENT_DATA_PREVTYPE_VALUE_HOME);
                intent.putExtra(ProductListActivity.INTENT_DATA_FRAGMENTTYPE, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS);
                ((BaseActivity)getActivity()).startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
            }
        });
        inflater.inflate(R.menu.menu_home, menu);
        MenuItem cartItem = menu.findItem(R.id.action_shopping_cart);
        MenuItemCompat.setActionView(cartItem, R.layout.item_count);
        View view = cartItem.getActionView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_SHOPPINGCART);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        TextView textView = (TextView) view.findViewById(R.id.ctv_home_shoppingcart_num);
        textView.setBackground(JImageUtils.getThemeCircle(getActivity()));
        JViewUtils.updateCartCount(textView, getCartItemCount());
    }
    public long getCartItemCount() {
        long cartItemCount = 0;
        try {
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())) {
                cartItemCount = WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()).getCartItemCount();
                ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(getActivity());
                if (list.size() > 0) {
                    for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                        cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                    }
                }
            } else {
                ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(getActivity());
                if (list.size() > 0) {
                    for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                        cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                    }
                }
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return cartItemCount;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showErrorMsg(String errormsg) {
        Toast.makeText(getActivity(), errormsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommonCallback.switchMenu(HomeCommonCallback.MENU_HOME);
        mImageLoader=new ImageLoader(getActivity());
        initRecyclerView();
//        mPresenter = getPresenter();
        requestData();
        setHasOptionsMenu(true);
    }

    @Override
    public void loadRecyclerViewData(SVRAppserviceCatalogSearchReturnEntity svrAppserviceCatalogSearchReturnEntity) {
        mAdapter = new CategoryTreeExpandableAdapter(getActivity(), getContext(),
                svrAppserviceCatalogSearchReturnEntity.getCategory(),
                mImageLoader, childOnClick);
        //点击一个，关闭其他所有item
        mAdapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        mAdapter.setViewType(CategoryTreeExpandableAdapter.VIEW_HORIZONTAL);
        rlCategoryTree.setAdapter(mAdapter);
        mAdapter.setRecycleView(rlCategoryTree);
    }
    private CategoryTreeExpandableAdapter.ChildOnClick childOnClick = new CategoryTreeExpandableAdapter.ChildOnClick() {
        @Override
        public void childOnClick(int position, Object ob, String parentId) {
            SVRAppserviceCatalogSearchCategoryItemReturnEntity entity = (SVRAppserviceCatalogSearchCategoryItemReturnEntity) ob;
            Intent intent = new Intent(getContext(), ProductListActivity.class);
            intent.putExtra(ProductListActivity.INTENT_DATA_PREVTYPE, ProductListActivity.INTENT_DATA_PREVTYPE_VALUE_MAINCATEGOTY);
            intent.putExtra(ProductListActivity.INTENT_DATA_FRAGMENTTYPE, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY);
            // Get Parent data
            for (SVRAppserviceCatalogSearchCategoryItemReturnEntity en : mAdapter.getVisibleData()) {
                if (en.getId() != null && en.getId().equals(parentId)) {
                    intent.putExtra(ProductListActivity.INTENT_DATA_CATEGORYID, en);
                    continue;
                }
            }
            intent.putExtra("categoryId", entity.getId());
            ((BaseActivity)getActivity()).startActivity(intent);
        }
    };



}
