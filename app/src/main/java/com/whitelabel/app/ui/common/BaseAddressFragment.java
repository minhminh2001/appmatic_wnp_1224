package com.whitelabel.app.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.whitelabel.app.BaseFragment;
import com.whitelabel.app.R;
import com.whitelabel.app.adapter.AddressBookAdapter;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.data.service.AccountManager;
import com.whitelabel.app.data.service.CommodityManager;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.network.BaseHttp;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomButton;
import com.whitelabel.app.widget.CustomSwipefreshLayout;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenu;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenuCreator;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenuItem;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenuListView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public abstract class BaseAddressFragment extends BaseFragment<BaseAddressContract.Presenter> implements BaseAddressContract.View, SwipeMenuListView.OnMenuItemClickListener, SwipeMenuListView.OnSwipeListener, AdapterView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.mListView)
    SwipeMenuListView mListView;
    @BindView(R.id.swipe_container)
    CustomSwipefreshLayout swipeContainer;
    @BindView(R.id.addressbook_add_textview)
    CustomTextView addressbookAddTextview;
    @BindView(R.id.addressbook_add_RelativeLayout)
    RelativeLayout addressbookAddRelativeLayout;
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
    Unbinder unbinder;
    private boolean useCache;
    private int mMenuWidth = 50;
    public final static  int REQUEST_EDIT_ADDRESS=1000;
    public final static  int REQUEST_ADD_ADDRESS=2000;
    public abstract List<Integer> getDeleteFuntionPostions();
    public abstract  void  addAddressBtnOnClick();
    public abstract List<AddressBook> handlerAddressData(List<AddressBook> addressBooks);
    public abstract   void onEditButtonClick(int postion);
    private AddressBookAdapter mAddressBookAdapter;
    protected final static String EXTRA_USE_CACHE = "use_cache";
    private RequestErrorHelper requestErrorHelper;
    // TODO: Rename parameter arguments, choose names that match
    public BaseAddressFragment() {
        // Required empty public constructor
    }
    @Override
    public void openSwipeLayout() {
         swipeContainer.setRefreshing(true);
    }

    public AddressBookAdapter getAdapter() {
        return mAddressBookAdapter;
    }
    @Override
    public void onSwipeStart(int position) {
        swipeContainer.setEnabled(false);
    }
    @Override
    public void onSwipeEnd(int position) {
        swipeContainer.setEnabled(true);
    }
    @Override
    public void showNetworkErrorView(String errorMsg) {
        if(mAddressBookAdapter==null||mAddressBookAdapter.getData().size()==0){
           addressbookAddTextview.setVisibility(View.INVISIBLE);
           requestErrorHelper.showConnectionBreaks(BaseHttp.ERROR_TYPE_NET);
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
    @Override
    public void closeSwipeLayout() {
            swipeContainer.setRefreshing(false);
    }
    @Override
    public void loadData(List<AddressBook> addressBooks) {
        if(addressBooks!=null) {
            addressbookAddTextview.setVisibility(View.VISIBLE);
            addressBooks = handlerAddressData(addressBooks);
            mAddressBookAdapter = new AddressBookAdapter(getContext(), addressBooks);
            mListView.setAdapter(mAddressBookAdapter);
        }
    }
    @Override
    public void onRefresh() {
        String sessionKey = WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey();
        mPresenter.getAddressListOnLine(sessionKey,WhiteLabelApplication.getAppConfiguration().getUser().getId());
    }
    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index) {
            case 0:
                onEditButtonClick(position);
                break;
            case 1:
                showProgressDialog();
                String sessionKey = WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey();
                mPresenter.deleteAddressById(sessionKey,
                        mAddressBookAdapter.getData().get(position).getAddressId(),
                WhiteLabelApplication.getAppConfiguration().getUser().getId());
                break;
        }
        return false;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base_address, container, false);
        unbinder = ButterKnife.bind(this, view);
        setRetryTheme(view);
        return view;
    }

    @Override
    public void inject() {
        if (getArguments() != null) {
            useCache = getArguments().getBoolean(EXTRA_USE_CACHE);
        }
        DaggerPresenterComponent1.builder().applicationComponent(WhiteLabelApplication.getApplicationComponent()).
                presenterModule(new PresenterModule(getActivity())).build().inject(this);
        mPresenter.setUseCache(useCache);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeContainer.setColorSchemeColors(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        swipeContainer.setOnRefreshListener(this);
        requestErrorHelper=new RequestErrorHelper(getActivity(),connectionBreaks);
        setSwipeListView();
        if(useCache){
            requestCacheData();
        }else {
            requestData();
        }
    }
    @Override
    public void loadCachaData(List<AddressBook> addressBooks) {
            String sessionKey = WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey();
            mPresenter.getAddressListOnLine(sessionKey,WhiteLabelApplication.getAppConfiguration().getUser().getId());
            loadData(addressBooks);
    }
    public void requestCacheData(){
        String sessionKey = WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey();
        mPresenter.getAddressListCache(sessionKey,WhiteLabelApplication.getAppConfiguration().getUser().getId());
    }
    public void  requestData(){
        String sessionKey = WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey();
        if (useCache) {
            swipeContainer.setRefreshing(true);
        }else{
            showProgressDialog();
        }
        mPresenter.getAddressListOnLine(sessionKey,WhiteLabelApplication.getAppConfiguration().getUser().getId());
    }
    public void setSwipeListView() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu, int position) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                openItem.setBackground(getResources().getDrawable(R.color.white));
                // set item width
                openItem.setWidth(JToolUtils.dip2px(getActivity(), mMenuWidth));
                openItem.setIcon(getResources().getDrawable(R.drawable.draw_edit));
                // add to menu
                menu.addMenuItem(openItem);
              if (getDeleteFuntionPostions() != null) {
                    for(int  index: getDeleteFuntionPostions()){
                         if(index==position)menu.addMenuItem(createDeleteSwipeItem());
                    }
                }
            }
        };
        // set creator
        mListView.setMenuCreator(creator);
        mListView.setOnItemClickListener(this);
        mListView.setOnMenuItemClickListener(this);
        mListView.setOnSwipeListener(this);
    }
    public final SwipeMenuItem createDeleteSwipeItem() {
        SwipeMenuItem deleteItem = new SwipeMenuItem(
                getActivity());
        // set item background
        deleteItem.setBackground(getResources().getDrawable(R.color.white));
        // set item width
        deleteItem.setWidth(JToolUtils.dip2px(getActivity(), mMenuWidth));
        // set a icon
        deleteItem.setIcon(getResources().getDrawable(R.drawable.draw_dele));
        // add to menu
        return deleteItem;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.addressbook_add_textview,R.id.try_again})
    public void onViewClicked(View view) {
           switch(view.getId()){
               case R.id.addressbook_add_textview:
                   addAddressBtnOnClick();
                   break;
               case R.id.try_again:
                   connectionBreaks.setVisibility(View.GONE);
                   requestData();
                   break;
           }
    }

}
