package com.whitelabel.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.CurationActivity;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.adapter.CurationAdapter;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.callback.ZoomImageViewCallBack;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.model.MarketingLayersEntity;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchCategoryItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceLandingPagesListLandingPageItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceLandingPagesListReturnEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RequestErrorHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by imaginato on 2015/7/17.
 */
public class HomeHomeCategoryFragment extends HomeBaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    public Long mGATrackTimeStart = 0L;
    public boolean mGATrackTimeEnable = false;
    private HomeActivity homeActivity;
    public SVRAppserviceCatalogSearchCategoryItemReturnEntity categoryEntity;
    private ArrayList<SVRAppserviceLandingPagesListLandingPageItemReturnEntity> curationList;
    private boolean mMarketShow = false;
    private RecyclerView mRecyclerView;
    private int mIndex;
    private boolean mCurrDisplayed = true;
    private int mIdentification;
    private MarketingLayersEntity mCurationListReturnEntity;
    private boolean firstCategory = false;
    private RequestErrorHelper requestErrorHelper;

    private View connectionLayout;
    private LinearLayout tryAgain;
    private boolean isinit = false;
    private static String TAG;
    private ProductDao mDao;
    private boolean isPrepared, isVisible, mHasLoadedOnce;
    //    private GetServiceListDao mDao;
    private int showItAfter;
    private int duration;
    private Handler mHandler = new Handler();
    private CurationAdapter adapter;
    private DataHandler mHandler1;
    private SwipeRefreshLayout refreshLayout;
    private ImageLoader mImageLoader;

    private static class DataHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        private final WeakReference<HomeHomeCategoryFragment> mFragment;

        public DataHandler(Activity activity, HomeHomeCategoryFragment fragment) {
            mActivity = new WeakReference<Activity>(activity);
            mFragment = new WeakReference<HomeHomeCategoryFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null || mFragment.get() == null) {
                return;
            }
            switch (msg.what) {
                case ProductDao.REQUEST_CURATION:
                    mFragment.get().refreshLayout.setRefreshing(false);
                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
//                        if (mFragment.get().firstCategory) {
//                            ((HomeHomeFragment) mFragment.get().getParentFragment()).showAppRate();
//                        }
                        SVRAppserviceLandingPagesListReturnEntity curationListReturnEntity = (SVRAppserviceLandingPagesListReturnEntity) msg.obj;
                        if ((curationListReturnEntity != null) && (curationListReturnEntity.getLandingList() != null) && (curationListReturnEntity.getLandingList().size() > 0)) {
                            mFragment.get().curationList.clear();
                            mFragment.get().mDao.saveMainCategoryLocal(mActivity.get(), mFragment.get().categoryEntity.getId(), curationListReturnEntity.getLandingList());
                            mFragment.get().curationList.addAll(curationListReturnEntity.getLandingList());
                            mFragment.get().adapter.notifyDataSetChanged();
                        }
                    }
//                    if (mFragment.get().mGATrackTimeEnable) {
//                        GaTrackHelper.getInstance().googleAnalyticsTimeStop(
//                                GaTrackHelper.GA_TIME_CATEGORY_IMPRESSION, mFragment.get().mGATrackTimeStart, "Main Category Loading"
//                        );
//                        mFragment.get().mGATrackTimeEnable = false;
//                    }
                    break;
                case ProductDao.REQUEST_MARKETING:
//                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
//                        mFragment.get().mCurationListReturnEntity = (MarketingLayersEntity) msg.obj;
//
//                        if (mFragment.get().mCurationListReturnEntity != null && !"0".equalsIgnoreCase(mFragment.get().mCurationListReturnEntity.getDuration()) && mFragment.get().mCurationListReturnEntity.getIndex() == mFragment.get().mIdentification) {
//                            int count = mFragment.get().getMarketCount(mFragment.get().categoryEntity.getId());
//                            String image = mFragment.get().getMarketImage(mFragment.get().categoryEntity.getId());
//
//                            if (count == -1 || !image.equals(mFragment.get().mCurationListReturnEntity.getImage())) {
//                                mFragment.get().saveMarketing(mFragment.get().categoryEntity.getId(), mFragment.get().mCurationListReturnEntity.getImage(), mFragment.get().mCurationListReturnEntity.getRepeats());
//                                count = mFragment.get().getMarketCount(mFragment.get().categoryEntity.getId());
//                            }
//                            if (count > 0) {
//                                mFragment.get().showMarketLayers(mFragment.get().mCurationListReturnEntity);
//                            }
//                        }
//                    }
                    break;
                case ProductDao.REQUEST_ERROR:
                    mFragment.get().refreshLayout.setRefreshing(false);

                    if (msg.arg1 == ProductDao.REQUEST_CURATION) {
                        if (mFragment.get().curationList != null && mFragment.get().curationList.size() > 0) {
                            mFragment.get().requestErrorHelper.showNetWorkErrorToast(msg);
                        } else {
                            mFragment.get().requestErrorHelper.showConnectionBreaks(msg);
                        }
                    }
                    break;
                case ProductDao.LOCAL_GETMAINCATEGORY:
                    List<SVRAppserviceLandingPagesListLandingPageItemReturnEntity> beans = (List<SVRAppserviceLandingPagesListLandingPageItemReturnEntity>) msg.obj;
                    if (beans != null && beans.size() > 0) {
                        mFragment.get().curationList.addAll(beans);
                        mFragment.get().adapter.notifyDataSetChanged();
                    }
                    mFragment.get().getCurationList();
                    break;

            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onRefresh() {
        getCurationList();
    }

    public void getCurationList() {
//        boolean showGuide = JStorageUtils.showAppGuide1(homeActivity);//是否显示新手指南
        //显示loading条件不和新手引导重叠，只显示第一个category的loading，并且刷新时显示loading
//        HomeHomeFragment object = (HomeHomeFragment) getParentFragment();
//        firstCategory = object.isCategory(categoryEntity);//第一个Category
//        if (firstCategory && showGuide) {
//            homeActivity.showMarketLayers = false;//禁止弹出广告层
//            if (mCommonCallback.getRlMarketLayer().getVisibility() == View.VISIBLE) {
//                mCommonCallback.marketLayerClose();//关闭广告弹出层
//            }
//        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
        if (categoryEntity == null || JDataUtils.isEmpty(categoryEntity.getId())) {
            return;
        }
        mDao.getCurationList(categoryEntity.getId());
    }

    public void initData() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }

        mHasLoadedOnce = true;
        JLogUtils.d("data1", "1");
        beforeInit();
        isinit = true;
//        initMaretingLayers();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            initData();
        } else {
            isVisible = false;
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.try_again:
                connectionLayout.setVisibility(View.GONE);
                getCurationList();
                break;
        }
    }

    /**
     * when advertisement is requesting,
     * just right now user switch to current fragment,
     * we should not permit to show it again in this condition which often occur at app initializing home page.
     * And set this boolean value true when the first (same as the only) request comes back.
     */
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        homeActivity = (HomeActivity) activity;
    }

    public static HomeHomeCategoryFragment newInstance(int position, SVRAppserviceCatalogSearchCategoryItemReturnEntity data) {
        HomeHomeCategoryFragment fragment = new HomeHomeCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("categoryEntity", data);
        bundle.putInt("index", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGATrackTimeEnable = true;
//        setScrollToolBarEnable(true);
        mImageLoader = new ImageLoader(homeActivity);
        View mContentView = inflater.inflate(R.layout.fragment_home_home_category, null);
        connectionLayout = mContentView.findViewById(R.id.connectionBreaks);
        requestErrorHelper = new RequestErrorHelper(getContext(), connectionLayout);
        tryAgain = (LinearLayout) mContentView.findViewById(R.id.try_again);
        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.recyclerView1);
        refreshLayout = (SwipeRefreshLayout) mContentView.findViewById(R.id.swipe_container);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(this);
        tryAgain.setOnClickListener(this);
        isPrepared = true;
        initData();
        return mContentView;
    }

    private void beforeInit() {
        mHandler1 = new DataHandler(getActivity(), this);
        JLogUtils.d("data2", "2");
        initIntent();
        if (mDao == null) {
            mDao = new ProductDao(TAG, mHandler1);
        }
        initRecyclerView();
        mDao.getMainCategoryLocal(getActivity(), categoryEntity.getId());
    }

    public void setmCurrDisplayedAndData(int position, SVRAppserviceCatalogSearchCategoryItemReturnEntity data) {
        mCurrDisplayed = false;
        categoryEntity = data;
        this.mIndex = position;
//        if(adapter != null) {
//            adapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onResume() {
        JLogUtils.i("russell", "*****" + mIndex + "**********onResume==" + isinit);
//        mGATrackTimeStart = GaTrackHelper.getInstance().googleAnalyticsTimeStart();
//        mMarketShow = true;
//        if (isinit) {
//            initMaretingLayers();
//        }
        super.onResume();
    }

    public void initMaretingLayers() {
//        if (!mCurrDisplayed) {
//            mIdentification++;
//            getMarketingLayers(mIdentification);
//        }
    }

    @Override
    public void onPause() {
        mMarketShow = false;
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onPause();
    }

//    public void getMarketingLayers(final int identification) {
//        if (categoryEntity == null || JDataUtils.isEmpty(categoryEntity.getId())) {
//            return;
//        }
//        mDao.getMarketingLayers(categoryEntity.getId(), identification);
//    }

    public void saveMarketing(String id, String image, int count) {
        SharedPreferences shared = homeActivity.getSharedPreferences("market", 0);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt(id + "count", count);
        editor.putString(id + "image", image);
        editor.commit();
    }

    public int getMarketCount(String id) {
        SharedPreferences shared = homeActivity.getSharedPreferences("market", 0);
        int count = shared.getInt(id + "count", -1);
        return count;
    }

    public String getMarketImage(String id) {
        SharedPreferences shared = homeActivity.getSharedPreferences("market", 0);
        String image = shared.getString(id + "image", "");
        return image;
    }

    private Runnable openMarketRun = new Runnable() {
        @Override
        public void run() {
            JLogUtils.i("russell", "****" + mIndex + "******openMarketRun");
            if (homeActivity.showMarketLayers) {
                if (!mCurrDisplayed) {
                    int count = getMarketCount(categoryEntity.getId());
                    saveMarketing(categoryEntity.getId(), mCurationListReturnEntity.getImage(), count - 1);
                    mCommonCallback.showMarketLayers();
                    mCurrDisplayed = true;
                    try {
                        duration = Integer.parseInt(mCurationListReturnEntity.getDuration()) * 1000;
                        mHandler.postDelayed(closeMarketRun, duration);
                    } catch (Exception ex) {
                        ex.getStackTrace();
                    }
                }
            }
        }

    };
    private Runnable closeMarketRun = new Runnable() {
        @Override
        public void run() {
            mCommonCallback.closeMarketLayers();
        }
    };

    private void showMarketLayers(final MarketingLayersEntity curationListReturnEntity) {
        if (!mMarketShow) {
            return;
        }
        if (getParentFragment() instanceof HomeHomeFragment) {
            HomeHomeFragment fragment = (HomeHomeFragment) getParentFragment();
            int currIndex = fragment.getCurrentFragmentIndex();
            if (currIndex == mIndex) {
                mCommonCallback.initMarketingLayers(curationListReturnEntity);
                showItAfter = curationListReturnEntity.getShowItAfter() * 1000;
                //loadImage
                //String imageUrl = JImageUtils.getImageServerUrlByWidthHeight(homeActivity, curationListReturnEntity.getImage(), GemfiveApplication.getPhoneConfiguration().getScreenWidth(homeActivity), GemfiveApplication.getPhoneConfiguration().getScreenHeigth(homeActivity) - JDataUtils.dp2Px(170));
                //JLogUtils.i("Allen","url== "+imageUrl);
                //Add callback listener
                int marketLayerWidth = GemfiveApplication.getPhoneConfiguration().getScreenWidth(homeActivity);
                int marketLayerHeight = GemfiveApplication.getPhoneConfiguration().getScreenHeigth(homeActivity) - JDataUtils.dp2Px(170);
//                JImageUtils.downloadImageFromServerListener(homeActivity, curationListReturnEntity.getImage(), marketLayerWidth, marketLayerHeight, new CategoryLoadImageListener(this));
//               JImageUtils.getInstance(homeActivity).display(((HomeHomeCallback)(getParentFragment())).getIvMarketLayer(), imageUrl, defaultBitmapLoadCallBack);
            }
        }
    }

    private class CategoryLoadImageListener extends SimpleTarget<Bitmap> {
        WeakReference<HomeHomeCategoryFragment> mFragment;

        public CategoryLoadImageListener(HomeHomeCategoryFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            if (mFragment.get() == null) return;
            if (homeActivity == null) return;
            try {
                homeActivity.getIvMarketLayer().setImageBitmap(resource);
                mFragment.get().mHandler.postDelayed(mFragment.get().openMarketRun, mFragment.get().showItAfter);
            } catch (Exception e) {
                e.printStackTrace();
                JLogUtils.e(TAG, e.getStackTrace()+"");
            }
        }

        @Override
        public void onLoadFailed(Exception ex, Drawable errorDrawable) {
            JLogUtils.e(TAG, ex.getMessage()+"");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initRecyclerView() {
        LinearLayoutManager fullyLayout = new LinearLayoutManager(homeActivity);
        fullyLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(fullyLayout);
        mRecyclerView.setHasFixedSize(true);
        curationList = new ArrayList<>();
        JLogUtils.d("data4", "4");
        initCurationList();
    }


    private void initIntent() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            try {
                categoryEntity = (SVRAppserviceCatalogSearchCategoryItemReturnEntity) bundle.getSerializable("categoryEntity");
                mIndex = bundle.getInt("index");
                TAG = this.getClass().getSimpleName() + categoryEntity.getId();
            } catch (Exception ex) {
                JLogUtils.e(TAG, "onActivityCreated", ex);
            }
        }
    }


    public void removeOpenMarketRun() {
        if (mHandler != null) {
            mHandler.removeCallbacks(openMarketRun);
        }
    }

    public void removeCloseMarketRun() {
        if (isAdded() && mHandler != null) {
            mHandler.removeCallbacks(closeMarketRun);
        }
    }

    @Override
    public void onDestroyView() {
        if (mDao != null) {
            mDao.cancelHttpByTag(TAG);
        }
        if (mHandler1 != null) {
            mHandler1.removeCallbacksAndMessages(null);
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        GemfiveApplication.getRefWatcher(getActivity()).watch(this);
    }

    private void initCurationList() {
        adapter = new CurationAdapter(curationList, homeActivity, categoryEntity.getChildren(), mImageLoader);
        adapter.setOnItemClickLitener(new CurationAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(final CurationAdapter.ItemViewHolder itemHolder, int position) {
                String standalone = curationList.get(position).getStandalone();
                if (!"1".equals(standalone)) {
                    try {
                        Intent intent = new Intent();
                        intent.setClass(homeActivity, CurationActivity.class);
                        intent.putExtra(CurationActivity.EXTRA_CURATION_ID, curationList.get(position).getPageId());
                        intent.putExtra(CurationActivity.EXTRA_CURATION_TITLE, curationList.get(position).getHeading());
                        if (categoryEntity != null && !TextUtils.isEmpty(categoryEntity.getName())) {
                            intent.putExtra(CurationActivity.EXTRA_CATEGORY_NAME, categoryEntity.getName());
                        }
                        homeActivity.startActivity(intent);
                        homeActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                        //跳转结束后记得将curationheading  复原
                        if (itemHolder.img != null) {
                            itemHolder.img.wait(new ZoomImageViewCallBack() {
                                @Override
                                public void imageAniCallBack() {
                                    if (itemHolder != null && itemHolder.tvDesc != null) {
                                        itemHolder.tvDesc.setBackgroundColor(itemHolder.img.getContext().getResources().getColor(R.color.whiteEAFFFFFF));
                                        itemHolder.tvDesc.setTextColor(itemHolder.tvDesc.getContext().getResources().getColor(R.color.black));
                                    }
                                }
                            }, 550);
                        }

                    } catch (Exception ex) {
                        ex.getStackTrace();
                    }
                }
//                GaTrackHelper.getInstance().googleAnalytics("Curation Landing Screen", homeActivity);
//                JLogUtils.i("googleGA_screen", "Curation Landing Screen");
            }
        });
        JLogUtils.d("data5", "5");
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void setmMarketShow(boolean mMarketShow) {
        this.mMarketShow = mMarketShow;
    }

}
