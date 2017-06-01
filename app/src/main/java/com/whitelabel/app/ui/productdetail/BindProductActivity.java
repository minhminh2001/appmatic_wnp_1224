package com.whitelabel.app.ui.productdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.ProductActivity;
import com.whitelabel.app.adapter.BindProductAdapter;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.model.BindProductResponseModel;
import com.whitelabel.app.model.SVRAppserviceProductDetailResultPropertyReturnEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindProductActivity extends BaseActivity<BindProductContract.Presenter> implements BindProductContract.View {
    @BindView(R.id.rv_recycler)
    RecyclerView rvRecycler;
    @BindView(R.id.tv_add_to_cart)
    CustomTextView tvAddToCart;
    @BindView(R.id.ll_add_to_car)
    LinearLayout llAddToCar;
    @BindView(R.id.tv_total_title)
    CustomTextView tvTotalTitle;
    @BindView(R.id.tv_total_value)
    CustomTextView tvTotalValue;
    private String mProductId;
    private ImageLoader mImageLoader;
    private List<SVRAppserviceProductDetailResultPropertyReturnEntity> mProducts;
    public final static String EXTRA_PRODUCTID = "product_id";
    public final static String EXTRA_PRODUCT_DATA = "product";
    private SVRAppserviceProductDetailResultPropertyReturnEntity mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_product);
        ButterKnife.bind(this);
        setTitle(getResources().getString(R.string.productdetail_bind_title));
        mImageLoader = new ImageLoader(this);
        mProductId = getIntent().getStringExtra(EXTRA_PRODUCTID);
        if (getIntent().getExtras() != null) {
            mProduct = (SVRAppserviceProductDetailResultPropertyReturnEntity) getIntent().getExtras().getSerializable(EXTRA_PRODUCT_DATA);
        }
        JViewUtils.setSoildButtonGlobalStyle(this, tvAddToCart);
        tvTotalTitle.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
        tvTotalValue.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(this, R.drawable.ic_action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mPresenter.loadData(mProductId);
    }
    @Override
    public void showNetworkErrorView(String errorMsg) {
        JViewUtils.showErrorToast(this, errorMsg);
    }
    @Override
    public BindProductContract.Presenter getPresenter() {
        return new BindProductPresenterImpl();
    }
   private  List<SVRAppserviceProductDetailResultPropertyReturnEntity> mRelatedProducts;
    @Override
    public void showData(BindProductResponseModel products) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecycler.setLayoutManager(linearLayoutManager);
        mRelatedProducts.add(mProduct);
        mRelatedProducts.addAll(products.getRelatedProducts());
        BindProductAdapter mBindProductAdapter = new BindProductAdapter(mRelatedProducts, mImageLoader);
        rvRecycler.setAdapter(mBindProductAdapter);
        tvTotalValue.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + products.getTotalPrice());
    }
    @OnClick(R.id.ll_add_to_car)
    public void onClick() {
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(BindProductActivity.this)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (SVRAppserviceProductDetailResultPropertyReturnEntity bean : mRelatedProducts) {
                stringBuilder = stringBuilder.append(bean.getProductId()).append(",");
            }
            String ids = stringBuilder.substring(0, stringBuilder.length() - 1);
            mPresenter.addToCart(ids, WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey());
        }else{
            Intent intent = new Intent(this, LoginRegisterActivity.class);
            startActivityForResult(intent, 1000);
            overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
        }

    }
}
