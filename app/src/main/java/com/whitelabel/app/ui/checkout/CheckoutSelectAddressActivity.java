package com.whitelabel.app.ui.checkout;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.R;
import com.whitelabel.app.utils.JViewUtils;

import static com.whitelabel.app.ui.checkout.CheckoutDefaultAddressFragment.SELECTED_ADDRESS_ID;

public class CheckoutSelectAddressActivity extends BaseActivity {
    public static  final int RESULT_CODE=1001;

    private CheckoutSelectAddressFragment checkoutSelectAddressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_select_address);
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(this, R.drawable.ic_action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutSelectAddressFragment.onBackProcessor();
            }
        });
        setTitle(getResources().getString(R.string.address));

        String selectedAddressId = "";
        Intent intent = getIntent();
        if(intent != null){
            selectedAddressId = intent.getStringExtra(SELECTED_ADDRESS_ID);
        }

        checkoutSelectAddressFragment = CheckoutSelectAddressFragment.newInstance(false, selectedAddressId);
        FragmentTransaction  fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_content, checkoutSelectAddressFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        checkoutSelectAddressFragment.onBackProcessor();
    }
}