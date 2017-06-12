package com.whitelabel.app.ui.checkout;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.R;
import com.whitelabel.app.utils.JViewUtils;
public class CheckoutSelectAddressActivity extends BaseActivity {
    public static  final int RESULT_CODE=1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_select_address);
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(this, R.drawable.ic_action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setTitle(getResources().getString(R.string.address));
        FragmentTransaction  fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_content,CheckoutSelectAddressFragment.newInstance(false));
        fragmentTransaction.commit();
    }
}