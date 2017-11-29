package com.whitelabel.app.ui.productdetail;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.ShoppingCartActivity1;
import com.whitelabel.app.adapter.BindProductAdapter;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.model.BindProductResponseModel;
import com.whitelabel.app.model.ProductPropertyModel;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomTextView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;

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
    private ImageLoader mImageLoader;
    private  List<ProductPropertyModel> mRelatedProducts;
    public final static String EXTRA_PRODUCTID = "product_id";
    public final static String EXTRA_PRODUCT_DATA = "product";
    private Dialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_product);
        ButterKnife.bind(this);
        setTitle(getResources().getString(R.string.productdetail_bind_title));
        mImageLoader = new ImageLoader(this);
        String mProductId = getIntent().getStringExtra(EXTRA_PRODUCTID);
        mRelatedProducts=new ArrayList<>();
        JViewUtils.setSoildButtonGlobalStyle(this, tvAddToCart);
        tvTotalTitle.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        tvTotalValue.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(this, R.drawable.ic_action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initRecyclerView();
        openProgressDialog();
        mPresenter.loadData(mProductId);
    }

    @Override
    protected void initInject() {
        DaggerPresenterComponent1.builder().applicationComponent(WhiteLabelApplication.getApplicationComponent()).
                presenterModule(new PresenterModule(this)).build().inject(this);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecycler.setLayoutManager(linearLayoutManager);
    }
    public void  openProgressDialog(){
        mDialog=JViewUtils.showProgressDialog(this);
    }
    public void closeProgressDialog(){
        if(mDialog!=null){
            mDialog.dismiss();
        }
    }
    @Override
    public void showFaildErrorMsg(String errorMsg) {
        closeProgressDialog();
        JViewUtils.showErrorToast(this,errorMsg+"");
    }
    @Override
    public void addCartSuccess() {
        closeProgressDialog();
        finish();
        Intent intent = new Intent();
        intent.setClass(this, ShoppingCartActivity1.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
//        overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
    }
    @Override
    public void showNetworkErrorView(String errorMsg) {
        closeProgressDialog();
        JViewUtils.showErrorToast(this, errorMsg);
    }

    public void setAddToCartButtonEnable(boolean  enable){
        if(enable){
            tvAddToCart.setEnabled(true);
            tvAddToCart.setBackground(JImageUtils.getButtonBackgroudSolidDrawable(this));
        }else{
            tvAddToCart.setEnabled(false);
            tvAddToCart.setBackgroundResource(R.drawable.big_button_style_b8);
        }
    }
    @Override
    public void showData(BindProductResponseModel products) {
        llAddToCar.setVisibility(View.VISIBLE);
        closeProgressDialog();
//        mRelatedProducts.add(mProduct);
        mRelatedProducts.addAll(products.getRelatedProducts());
        final BindProductAdapter mBindProductAdapter = new BindProductAdapter(mRelatedProducts, mImageLoader);
        mBindProductAdapter.setOnItemClickListener(new BindProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClient(View view, int position) {
                mRelatedProducts.get(position).setSelected(!mRelatedProducts.get(position).isSelected());
                tvTotalValue.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + JDataUtils.formatDouble(mPresenter.computeSumPrice(mRelatedProducts)+""));
                mBindProductAdapter.notifyDataSetChanged();
                if(mPresenter.checkProductIsSelected(mRelatedProducts)){
                    setAddToCartButtonEnable(true);
                }else{
                    setAddToCartButtonEnable(false);
                }
            }
        });
        rvRecycler.setAdapter(mBindProductAdapter);
        tvTotalValue.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() +JDataUtils.formatDouble(mPresenter.computeSumPrice(mRelatedProducts)+""));
    }
    @OnClick(R.id.tv_add_to_cart)
    public void onClick() {
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(BindProductActivity.this)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ProductPropertyModel bean : mRelatedProducts) {
                if(bean.isSelected()) {
                    stringBuilder = stringBuilder.append(bean.getId()).append(",");
                }
            }
            if(!TextUtils.isEmpty(stringBuilder.toString())) {
                openProgressDialog();
                String ids = stringBuilder.substring(0, stringBuilder.length() - 1);
                mPresenter.addToCart(ids, WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey());
            }
        }else{
            Intent intent = new Intent(this, LoginRegisterActivity.class);
            startActivityForResult(intent, 1000);
            overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
        }
    }
}