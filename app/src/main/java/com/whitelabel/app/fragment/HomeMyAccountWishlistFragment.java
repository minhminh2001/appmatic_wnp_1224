package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.ProductActivity;
import com.whitelabel.app.adapter.MyAccountWishlistAdapter;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.Wishlist;
import com.whitelabel.app.model.WishlistEntityResult;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenu;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenuCreator;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenuItem;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenuListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by imaginato on 2015/7/28.
 */

public class HomeMyAccountWishlistFragment extends HomeBaseFragment implements View.OnClickListener, MyAccountFragmentRefresh, SwipeRefreshLayout.OnRefreshListener {
    private final int mMenuWidth = 50;
    private Activity homeActivity;
    private SwipeMenuListView lv;
    private MyAccountWishlistAdapter adapter;
    private View nogoods;
    private List<Wishlist> list = new ArrayList<>();
    private int currentPage = 1;
    private final int pageSize = 20;
    private int lastItem = 0;//记录最后一个Item
    private View layout;
    private Dialog mDialog;
    private RequestErrorHelper requestErrorHelper;
    private View connectionLayout;
    private LinearLayout tryAgain;
    //    private  OnMyAccountUserGuide MyAccountUserGuide;
    private MyAccountDao mAccountDao;
    private DataHandler dataHandler;
    private SwipeRefreshLayout swipeLayout;
    private String TAG = this.getClass().getSimpleName();
    private boolean isAddFoot;
    private boolean Loading = true;

    private ImageLoader mImageLoader;

    @Override
    public void onRefresh() {
        currentPage = 1;
        sendRequest();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.try_again:
                connectionLayout.setVisibility(View.GONE);
                showRefreshLayout();
                currentPage = 1;
                sendRequest();
                break;
        }
    }

    public void showRefreshLayout() {
        dataHandler.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            homeActivity = activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
//    public void onAttachFragment(Fragment fragment){
//        //强转接口
//        MyAccountUserGuide= (OnMyAccountUserGuide) fragment;
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.onAttachFragment(getParentFragment());
    }

    private final static class DataHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        private final WeakReference<HomeMyAccountWishlistFragment> mFragment;

        public DataHandler(Activity activity, HomeMyAccountWishlistFragment fragment) {
            mActivity = new WeakReference<Activity>(activity);
            mFragment = new WeakReference<HomeMyAccountWishlistFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null || mFragment.get() == null) {
                return;
            }
            switch (msg.what) {
                case MyAccountDao.LOCAL_WISTLIST_GET:
                    if (mFragment.get().list != null && mFragment.get().list.size() > 0) {
                        return;
                    }
                    List<Wishlist> wishlists = (List<Wishlist>) msg.obj;
                    if (wishlists != null && wishlists.size() > 0) {
                        mFragment.get().nogoods.setVisibility(View.GONE);
                        mFragment.get().lv.setVisibility(View.VISIBLE);
                        mFragment.get().list.addAll(wishlists);
                        mFragment.get().adapter.notifyDataSetChanged();
                    }
                    mFragment.get().currentPage = 1;
                    mFragment.get().sendRequest();
                    break;
                case MyAccountDao.REQUEST_GETWISHLIST:
                    if (mFragment.get() == null) {
                        return;
                    }
                    if (mFragment.get().mDialog != null) {
                        mFragment.get().mDialog.dismiss();
                    }
                    mFragment.get().swipeLayout.setRefreshing(false);
                    if (msg.arg1 == MyAccountDao.RESPONSE_SUCCESS) {
//                        mFragment.get().MyAccountUserGuide.ShowMyAccountUserGuide(HomeMyAccountFragmentV2.TAG_WISHLIST);
                        mFragment.get().connectionLayout.setVisibility(View.GONE);
                        final WishlistEntityResult wishlistEntityResult = (WishlistEntityResult) msg.obj;
                        try {
                            GemfiveApplication.getAppConfiguration().updateWishlist(mActivity.get(), Integer.parseInt(wishlistEntityResult.getTotal()));
                        } catch (Exception ex) {
                            ex.getStackTrace();
                        }
                        if (mFragment.get().currentPage == 1) {
                            mFragment.get().list.clear();
                            mFragment.get().mAccountDao.saveLocalWishData(mActivity.get(), GemfiveApplication.getAppConfiguration().getUser().getId(), wishlistEntityResult.getResults());
                            if ((null == wishlistEntityResult.getResults() || wishlistEntityResult.getResults().size() == 0)) {
                                mFragment.get().adapter.notifyDataSetChanged();
                                mFragment.get().nogoods.setVisibility(View.VISIBLE);
//                                mFragment.get(). lv.setVisibility(View.GONE);
                                return;
                            }
                        }

                        if (wishlistEntityResult.getResults() != null) {
                            mFragment.get().nogoods.setVisibility(View.GONE);
                            mFragment.get().lv.setVisibility(View.VISIBLE);
                            if (wishlistEntityResult.getResults().size() >= mFragment.get().pageSize) {
                                if (mFragment.get().currentPage == 1 && !mFragment.get().isAddFoot) {
                                    mFragment.get().isAddFoot = true;
                                    mFragment.get().addMFooterView();
                                }
                                mFragment.get().currentPage++;
                                mFragment.get().Loading = true;
                            } else {
                                if (mFragment.get().layout != null) {
                                    mFragment.get().layout.setVisibility(View.GONE);
                                }
                                mFragment.get().Loading = false;
                            }

                            mFragment.get().list.addAll(wishlistEntityResult.getResults());
                            mFragment.get().adapter.notifyDataSetChanged();
                        } else {
                            if (mFragment.get().layout != null) {
                                mFragment.get().layout.setVisibility(View.GONE);
                            }
                            mFragment.get().Loading = false;
                        }
                    } else {
                        String errormsg = (String) msg.obj;
                        JToolUtils.expireHandler(mActivity.get(), errormsg, 1000);
                    }
                    break;
                case MyAccountDao.REQUEST_DELETEWISHLIST:
                    if (msg.arg1 == MyAccountDao.RESPONSE_SUCCESS) {
                        GemfiveApplication.getAppConfiguration().deleteWishlist(mActivity.get());
                        //如果更换listview,这里可被放开
//                      mFragment.get().mAccountDao.saveLocalWishData(mActivity.get(), GemfiveApplication.getAppConfiguration().getUser().getId(),mFragment.get(). list);
                        mFragment.get().currentPage = 1;
                        mFragment.get().sendRequest();
                        if (mFragment.get().list.size() == 0) {
                            mFragment.get().nogoods.setVisibility(View.VISIBLE);
                        }
                    } else {
                        try {
                            ErrorMsgBean bean = (ErrorMsgBean) msg.obj;
                            if (!TextUtils.isEmpty(bean.getErrorMessage())) {
                                JToolUtils.expireHandler(mActivity.get(), bean.getErrorMessage(), 1000);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case MyAccountDao.ERROR:
                    if (mFragment.get().mDialog != null) {
                        mFragment.get().mDialog.cancel();
                    }
                    mFragment.get().swipeLayout.setRefreshing(false);
                    if (msg.arg1 == MyAccountDao.REQUEST_GETWISHLIST) {
                        if (mFragment.get().list == null || mFragment.get().list.size() == 0) {
                            mFragment.get().requestErrorHelper.showConnectionBreaks(msg);
                            return;
                        }
                    }
                    mFragment.get().requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mCommonCallback != null) {
            mCommonCallback.switchMenu(HomeCommonCallback.MENU_WISHLIST);
        }
        dataHandler = new DataHandler(homeActivity, this);
        mAccountDao = new MyAccountDao(TAG, dataHandler);
        initLisener();
        setSwipeListView();

        mAccountDao.getLocalWishData(getActivity(), GemfiveApplication.getAppConfiguration().getUser().getId());
        showRefreshDialog();
        setHasOptionsMenu(true);

    }

    private void initLisener() {
        tryAgain.setOnClickListener(this);

    }

    public void sendRequest() {
        Loading = false;
        mAccountDao.getWishList(GemfiveApplication.getAppConfiguration().getUserInfo(homeActivity).getSessionKey(), currentPage, pageSize);
    }

    public void showRefreshDialog() {
//        mDialog = JViewUtils.showProgressDialog(homeActivity);
        showRefreshLayout();

    }

    //    public void reLoad(){
//        showRefreshDialog();
//        currentPage=1;
//        sendRequest();
//    }
    public void setSwipeListView() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu, int position) {
                menu.addMenuItem(createDeleteSwipeItem());
            }
        };
        lv.setMenuCreator(creator);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            lv.setFooterDividersEnabled(false);
        }
        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Wishlist wishlish1 = list.get(position);
                        sendRequestToDeteleteCell(wishlish1.getItemId(), position);
                        break;
                }
                return false;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String availability = list.get(position).getAvailability();
                String visibility = list.get(position).getVisibility();
                if ((("1").equals(visibility))) {

                    Intent it = new Intent(getActivity(), ProductActivity.class);
                    it.putExtra("productId", list.get(position).getProductId());
                    homeActivity.startActivity(it);
                    homeActivity.overridePendingTransition(R.anim.enter_righttoleft,
                            R.anim.exit_righttoleft);
                }
            }
        });
        adapter = new MyAccountWishlistAdapter(homeActivity, list, mImageLoader);
        lv.setAdapter(adapter);
        lv.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                swipeLayout.setEnabled(false);
            }

            @Override
            public void onSwipeEnd(int position) {
                swipeLayout.setEnabled(true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //调用删除接口
    private void sendRequestToDeteleteCell(String itemId, int position) {
        mDialog = JViewUtils.showProgressDialog(homeActivity);
        mAccountDao.deleteWishListById(GemfiveApplication.getAppConfiguration().getUserInfo(homeActivity).getSessionKey(), itemId, position);
    }

    public final SwipeMenuItem createDeleteSwipeItem() {
        SwipeMenuItem deleteItem = new SwipeMenuItem(
                getActivity());
        deleteItem.setBackground(getResources().getDrawable(R.color.white));
        deleteItem.setWidth(JToolUtils.dip2px(getActivity(), mMenuWidth));
        deleteItem.setIcon(getResources().getDrawable(R.drawable.draw_dele));
        return deleteItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_wishlist, null);
        mImageLoader = new ImageLoader(homeActivity);
        connectionLayout = contentView.findViewById(R.id.connectionBreaks);
        requestErrorHelper = new RequestErrorHelper(getContext(), connectionLayout);
        tryAgain = (LinearLayout) contentView.findViewById(R.id.try_again);
        nogoods = contentView.findViewById(R.id.wishlist_no_goods);
        lv = (SwipeMenuListView) contentView.findViewById(R.id.whistlist_lv);
        swipeLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(R.color.purple660070);
        swipeLayout.setOnRefreshListener(this);
        return contentView;
    }

//    private void initWithWebServiceDatas(LinkedList<Wishlist> results) {
//        if(list!=null){
//            list.clear();
//        }
//        if(results!=null&&results.size()>0) {
//            lv.setVisibility(View.VISIBLE);
//            nogoods.setVisibility(View.GONE);
//            list.addAll(results);
//            adapter.notifyDataSetChanged();
//        }else{
//            lv.setVisibility(View.GONE);
//            nogoods.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void datasPage(LinkedList<Wishlist> results) {
//        list.addAll(results);//追加数据
//        adapter.notifyDataSetChanged();
//    }

    public void addMFooterView() {
        //获得布局设置页脚
        LayoutInflater inflater = LayoutInflater.from(homeActivity);
        layout = inflater.inflate(R.layout.layout_my_footer, null);
        lv.addFooterView(layout);//设置页脚
        layout.setVisibility(View.GONE);//隐藏页脚
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //滑动到底部并且屏幕不在滚动
                if (adapter.getCount() >= pageSize && adapter.getCount() - lastItem <= pageSize - 10 && scrollState == SCROLL_STATE_IDLE && Loading) {
                    JLogUtils.i("Allen", "adapter.getCount()=" + adapter.getCount());
                    if (currentPage > 1) {
                        layout.setVisibility(View.VISIBLE);
                        sendRequest();
                    } else {
                        layout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;
            }
        });
    }

    @Override
    public void refresh(boolean bool) {
        if (bool) {
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(homeActivity, true);
        GaTrackHelper.getInstance().googleAnalytics("Sign In screen", homeActivity);
        JLogUtils.i("googleGA_screen", "My Wishlist Screen");
    }

    @Override
    public void onStop() {
        super.onStop();
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(homeActivity, false);
    }
}
