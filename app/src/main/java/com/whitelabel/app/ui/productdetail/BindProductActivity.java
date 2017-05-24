package com.whitelabel.app.ui.productdetail;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import com.whitelabel.app.R;
import com.whitelabel.app.adapter.BindProductAdapter;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
public class BindProductActivity extends com.whitelabel.app.BaseActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_product);
        ButterKnife.bind(this);
        setTitle(getResources().getString(R.string.productdetail_bind_title));
        JViewUtils.setSoildButtonGlobalStyle(this,tvAddToCart);
        tvTotalTitle.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
        tvTotalValue.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(this,R.drawable.ic_action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initRecyclerView();
    }
    private void initRecyclerView() {
        LinearLayoutManager  linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecycler.setLayoutManager(linearLayoutManager);
        BindProductAdapter mBindProductAdapter=new BindProductAdapter();
        rvRecycler.setAdapter(mBindProductAdapter);
    }
}
