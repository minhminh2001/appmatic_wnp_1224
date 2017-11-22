package com.whitelabel.app.ui.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.GlobalData;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.callback.WheelPickerCallback;
import com.whitelabel.app.fragment.ProductListKeywordsSearchFragment;
import com.whitelabel.app.model.ShopBrandResponse;
import com.whitelabel.app.model.WheelPickerConfigEntity;
import com.whitelabel.app.model.WheelPickerEntity;
import com.whitelabel.app.ui.home.ShopBrandContract;
import com.whitelabel.app.ui.home.adapter.ShopBrandDetailAdapter;
import com.whitelabel.app.ui.home.fragment.HomeHomeFragmentV4;
import com.whitelabel.app.utils.AnimUtil;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.PageIntentUtils;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;

/**
 * Created by img on 2017/11/20.
 */

public class ShopBrandActivity extends BaseActivity<ShopBrandContract.Presenter> implements ShopBrandContract.View {
    @BindView(R.id.tv_start_with)
    CustomTextView tvStartWith;
    @BindView(R.id.rcv_brand_list)
    RecyclerView rcvBrandList;
    @BindView(R.id.iv_arrow_down_black)
    ImageView ivArrowDownBlack;

    private Context mContext;
    private String menuId;
    private String menuTitle;
    private static final int COLUMN=3;
    private static final String DEFALUT_STRING="0-9";
    private String currentWheelTitle="";
    private int currentWheelIndex=0;
    private GridLayoutManager gridLayoutManager;
    private ShopBrandDetailAdapter recycViewAdapter;
    private List<ShopBrandResponse.BrandsBean.ItemsBean> titles=new ArrayList<>();
    private List<ShopBrandResponse.BrandsBean.ItemsBean> titlesAnditems=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_brand);
        ButterKnife.bind(this);
        initGetIntent();
        initTitleBar();
        initRecyclerView();
        initTopBrandSelect();
    }

    private void initGetIntent() {
        Intent intent = getIntent();
        mContext = this;
        if (intent != null) {
            menuId = intent.getStringExtra(HomeHomeFragmentV4.ARG_CATEGORY_ID);
            menuTitle = intent.getStringExtra(HomeHomeFragmentV4.ARG_CATEGORY_NAME);
            mPresenter.getOnlineCategoryDetail(false,menuId);
        }
    }

    private void initTitleBar() {
        setTitle(menuTitle);
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(this, R.drawable.ic_action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initRecyclerView() {
        gridLayoutManager = new GridLayoutManager(mContext, COLUMN,GridLayoutManager.VERTICAL,false);

        rcvBrandList.setLayoutManager(gridLayoutManager);
        rcvBrandList.addItemDecoration(new GridSpacingItemDecoration(COLUMN,JToolUtils.dip2px(mContext,4),true));
        recycViewAdapter = new ShopBrandDetailAdapter(mContext, titlesAnditems);
        recycViewAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                int type = rcvBrandList.getAdapter().getItemViewType(position);
                if ( GlobalData.HEADER == type) {
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
                    if (GlobalData.ITEM==itemsBean.getItemType()){
//                        skipToSearchPage(position);
                        PageIntentUtils.skipToSerachPage(mContext,itemsBean);
                    }
                }
            }
        });
    }

    @Override
    protected void initInject() {
        DaggerPresenterComponent1.builder().applicationComponent(WhiteLabelApplication.getApplicationComponent()).
                presenterModule(new PresenterModule(this)).build().inject(this);
    }

    @Override
    public void loadData(List<ShopBrandResponse.BrandsBean.ItemsBean> itemsBean) {
        this.titlesAnditems.clear();
        this.titlesAnditems=itemsBean;
        recycViewAdapter.setNewData(this.titlesAnditems);
        recycViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadTitleData(List<ShopBrandResponse.BrandsBean.ItemsBean> itemsBean) {
        titles.clear();
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

    @Override
    public void showErrorMsg(String errorMsg) {
        JViewUtils.showErrorToast(mContext, errorMsg);
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
