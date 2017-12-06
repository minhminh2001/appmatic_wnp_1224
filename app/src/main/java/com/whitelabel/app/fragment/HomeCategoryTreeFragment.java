package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.Const;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.DrawerLayoutActivity;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.adapter.CategoryTreeExpandableAdapter1;
import com.whitelabel.app.adapter.CategoryTreeRootAdapter;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchCategoryItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.PageIntentUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.utils.logger.Logger;
import com.whitelabel.app.widget.CustomSpeedLayoutManager;
import com.whitelabel.app.widget.ExpandableRecyclerAdapter;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/14.
 */
public class HomeCategoryTreeFragment extends HomeBaseFragment implements View.OnClickListener {
    private Dialog mDialog;
    private ProductDao dao;
    public static String TAG;
    private HomeCommonCallback homeCommonCallback;
    private RequestErrorHelper requestErrorHelper;

    private View view, connectionLayout;
    private LinearLayout ll_category_tree_main;
    private RecyclerView rvRootCategory, rlCategoryTree;

    private CategoryTreeRootAdapter categoryTreeRootAdapter;
    private CategoryTreeExpandableAdapter1 categoryTreeExpandableAdapter;
    private ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> categoryList = new ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity>();
    private ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> allData = new ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity>();
    private ImageLoader mImageLoader;
    DrawerLayoutActivity drawerLayoutActivity;
    private String leftMenuTitle;
    private String rightSubTitle;
    private String rightTopTitle;
    public HomeCategoryTreeFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category_tree_layout, null);
        TAG = this.getClass().getSimpleName();
        DataHandler mHandler = new DataHandler(getActivity(), this);
        dao = new ProductDao(TAG, mHandler);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mImageLoader = new ImageLoader(getContext());
        connectionLayout = view.findViewById(R.id.connectionBreaks);
        requestErrorHelper = new RequestErrorHelper(getContext(), connectionLayout);
        LinearLayout tryAgain = (LinearLayout) connectionLayout.findViewById(R.id.try_again);
        ll_category_tree_main = (LinearLayout) view.findViewById(R.id.ll_category_tree_main);
        ll_category_tree_main.setVisibility(View.GONE);
        tryAgain.setOnClickListener(this);
        rvRootCategory = (RecyclerView) view.findViewById(R.id.rvRootCategory);
        rlCategoryTree = (RecyclerView) view.findViewById(R.id.rl_category_tree);
        connectionLayout.setVisibility(View.GONE);
        initData();
    }

    public void initData() {
        //得到数据
        SVRAppserviceCatalogSearchReturnEntity categoryEntity = WhiteLabelApplication.getAppConfiguration().getCategoryArrayList();
        allData.clear();
        if (categoryEntity != null && categoryEntity.getCategory() != null && categoryEntity.getCategory().size() > 0) {
            for (SVRAppserviceCatalogSearchCategoryItemReturnEntity entity : categoryEntity.getCategory()) {
                //last position result children is empty,lead last postion don't visible
//                if (!entity.getChildren().isEmpty()) {
                allData.add((SVRAppserviceCatalogSearchCategoryItemReturnEntity) JToolUtils.cloneObject(entity));
//                }
            }
            ll_category_tree_main.setVisibility(View.VISIBLE);
        } else {
            refresh();
            return;
        }

        String gemBradns = getString(R.string.gembrand);
        String allBradns = getString(R.string.productlist_categorylanding_allcategory);
        for (SVRAppserviceCatalogSearchCategoryItemReturnEntity oneItem : allData) {
            for (SVRAppserviceCatalogSearchCategoryItemReturnEntity twoItem : oneItem.getChildren()) {
                //需求隐藏 Gem Brands 的ALL  GEM Brands 的
                boolean isGemBrands = false;
                if (!TextUtils.isEmpty(twoItem.getMenuTitle()) && gemBradns.equals(twoItem.getMenuTitle().replace(" ", "").toUpperCase())) {
                    isGemBrands = true;
                }
                if (!isGemBrands) {
                    //添加category "ALL"
                    SVRAppserviceCatalogSearchCategoryItemReturnEntity allCategory = new SVRAppserviceCatalogSearchCategoryItemReturnEntity();
                    allCategory.setId(twoItem.getId());
                    allCategory.setName(allBradns);
                    allCategory.setLevel(twoItem.getLevel() + 1);
                    twoItem.getChildren().add(0, allCategory);
                }
            }
        }
        //初始化左侧根目录
        initRootTreeData();
        //初始化 setCategoryTreeData
        initCategoryTreeData();

    }

    public void refresh() {
        mDialog = JViewUtils.showProgressDialog(getActivity());
        dao.getBaseCategory();
    }

    private void initCategoryTreeData() {
        CustomSpeedLayoutManager linearLayoutManager = new CustomSpeedLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlCategoryTree.setLayoutManager(linearLayoutManager);

        categoryList.clear();
        if (categoryTreeRootAdapter.currentRootPosition < allData.size()) {
            categoryList.addAll(allData.get(categoryTreeRootAdapter.currentRootPosition).getChildren());
        }
        categoryTreeExpandableAdapter = new CategoryTreeExpandableAdapter1(getActivity(), getContext(), categoryList, mImageLoader, childOnClick);
        //点击一个，关闭其他所有item
        categoryTreeExpandableAdapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        rlCategoryTree.setAdapter(categoryTreeExpandableAdapter);
        categoryTreeExpandableAdapter.setRecycleView(rlCategoryTree);
    }

    private CategoryTreeExpandableAdapter1.ChildOnClick childOnClick = new CategoryTreeExpandableAdapter1.ChildOnClick() {
        @Override
        public void childOnClick(int position, Object ob, String parentId) {
            //   GO TO  ProductListActivity
            SVRAppserviceCatalogSearchCategoryItemReturnEntity entity = (SVRAppserviceCatalogSearchCategoryItemReturnEntity) ob;
            rightSubTitle=entity.getMenuTitle();

            Intent intent = new Intent(getContext(), ProductListActivity.class);
            intent.putExtra(ProductListActivity.INTENT_DATA_PREVTYPE, ProductListActivity.INTENT_DATA_PREVTYPE_VALUE_MAINCATEGOTY);
            intent.putExtra(ProductListActivity.INTENT_DATA_FRAGMENTTYPE, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY);
            // Get Parent data
            for (SVRAppserviceCatalogSearchCategoryItemReturnEntity en : categoryList) {
                if (en.getId() != null && en.getId().equals(parentId)) {
                    intent.putExtra(ProductListActivity.INTENT_DATA_CATEGORYID, en);
                    rightTopTitle =en.getMenuTitle();
                    continue;
                }
            }
            //第三级bran选择的位置
            intent.putExtra("categoryId", entity.getId());
            getContext().startActivity(intent);
            ((BaseActivity) getContext()).startActivityTransitionAnim();
            gaScreenName();
        }
    };

    private void gaScreenName(){
        StringBuilder builder=new StringBuilder();
        builder.append("Category_");
        builder.append(leftMenuTitle);
        builder.append("_");
        builder.append(rightTopTitle);
        builder.append("_");
        builder.append(rightSubTitle);
        GaTrackHelper.getInstance().googleAnalytics(builder.toString(),getActivity());
    }

    private void switchCategoryList(int posotion) {
        //关闭所有的组
        categoryTreeExpandableAdapter.collapseAll();
        //重新加载数据
        categoryList.clear();
        categoryList.addAll(allData.get(posotion).getChildren());
        categoryTreeExpandableAdapter = new CategoryTreeExpandableAdapter1(getActivity(), getContext(), categoryList, mImageLoader, childOnClick);
        categoryTreeExpandableAdapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        rlCategoryTree.setAdapter(categoryTreeExpandableAdapter);
        categoryTreeExpandableAdapter.setRecycleView(rlCategoryTree);
    }


    private void initRootTreeData() {
        //  初始化 rvRootCategory
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRootCategory.setLayoutManager(linearLayoutManager);
        leftMenuTitle = allData.get(0).getMenuTitle();
        categoryTreeRootAdapter = new CategoryTreeRootAdapter(getContext(), allData,
                new CategoryTreeRootAdapter.ItemClick() {
                    @Override
                    public void onItemClick(CategoryTreeRootAdapter.ViewHolder holder, int position) {
                        categoryTreeRootAdapter.currentRootPosition = position;
                        categoryTreeRootAdapter.notifyDataSetChanged();
                        SVRAppserviceCatalogSearchCategoryItemReturnEntity entity = allData.get(position);
                        leftMenuTitle = entity.getMenuTitle();
                        if (position==allData.size()-1){//last one
                            SVRAppserviceCatalogSearchCategoryItemReturnEntity returnEntity = allData.get(position);
                            GaTrackHelper.getInstance().googleAnalytics(returnEntity.getMenuTitle(),getActivity());
                            PageIntentUtils.skipToBrandListPage(getActivity(),returnEntity.getMenuId(),returnEntity.getMenuTitle());
                        }else {
                            if (!HomeHomeFragment.isCategory(entity)) {
                                //点击 GEMdeals去主页
                                startHomeHomeFragment(entity.getId());
                            } else {
                                switchCategoryList(categoryTreeRootAdapter.currentRootPosition);
                            }
                        }

                    }
                });
        //默认选中第一个category entity
        for (int i = 0; i < allData.size(); i++) {
            SVRAppserviceCatalogSearchCategoryItemReturnEntity entity = allData.get(i);
            if (HomeHomeFragment.isCategory(entity)) {
                categoryTreeRootAdapter.currentRootPosition = i;
                break;
            }
        }
        rvRootCategory.setAdapter(categoryTreeRootAdapter);

    }




    public void startHomeHomeFragment(String categoryId) {
        Serializable serializable = categoryId;
        drawerLayoutActivity.jumpHomePage(serializable);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        homeCommonCallback.setTitle(getString(R.string.category));
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        homeCommonCallback = (HomeCommonCallback) activity;
        drawerLayoutActivity = (DrawerLayoutActivity) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        GaTrackHelper.getInstance().googleAnalytics(Const.GA.CATEGORY_ALL_LIST_SCREEN,getActivity());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.try_again:
                connectionLayout.setVisibility(View.GONE);
                refresh();
                break;
        }
    }

    private static class DataHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        private final WeakReference<HomeCategoryTreeFragment> mFragment;

        public DataHandler(Activity activity, HomeCategoryTreeFragment fragment) {
            mActivity = new WeakReference<Activity>(activity);
            mFragment = new WeakReference<HomeCategoryTreeFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null || mFragment.get() == null) {
                return;
            }
            HomeCategoryTreeFragment fragment = mFragment.get();
            switch (msg.what) {
                case ProductDao.REQUEST_CATALOGSEARCH:
                    if (fragment.mDialog != null && fragment.mDialog.isShowing()) {
                        fragment.mDialog.dismiss();
                    }
                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
                        SVRAppserviceCatalogSearchReturnEntity searchCatalog = (SVRAppserviceCatalogSearchReturnEntity) msg.obj;
                        WhiteLabelApplication.getAppConfiguration().setCategoryArrayList(searchCatalog);
                        JStorageUtils.saveCategoryArrayList(mActivity.get(), searchCatalog);
                        fragment.initData();
                    }
                    break;
                case ProductDao.REQUEST_ERROR:
                    if (fragment.mDialog != null) {
                        fragment.mDialog.cancel();
                    }
                    fragment.requestErrorHelper.showConnectionBreaks(msg);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void onStop() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (dao != null) {
            dao.cancelHttpByTag(TAG);
        }
        super.onDestroyView();
    }
}
