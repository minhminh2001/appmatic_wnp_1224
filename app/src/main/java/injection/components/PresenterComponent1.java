package injection.components;

import android.app.Activity;
import android.app.Fragment;


import com.whitelabel.app.activity.CheckoutActivity;
import com.whitelabel.app.activity.CheckoutPaymentStatusActivity;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.fragment.LoginRegisterEmailLoginFragment;
import com.whitelabel.app.ui.checkout.CheckoutDefaultAddressFragment;
import com.whitelabel.app.ui.common.BaseAddressFragment;
import com.whitelabel.app.ui.home.fragment.HomeFragmentV2;
import com.whitelabel.app.ui.home.fragment.HomeHomeFragmentV1;
import com.whitelabel.app.ui.home.fragment.HomeHomeFragmentV2;
import com.whitelabel.app.ui.home.fragment.HomeHomeFragmentV3;
import com.whitelabel.app.ui.home.fragment.HomeHomeFragmentV4;
import com.whitelabel.app.ui.productdetail.ProductDetailActivity;
import com.whitelabel.app.ui.start.StartActivityV2;

import dagger.Component;
import injection.ActivityScope;
import injection.modules.PresenterModule;

/**
 * Created by ray on 2017/5/5.
 */
@ActivityScope
@Component(modules = PresenterModule.class,dependencies = ApplicationComponent.class)
public interface PresenterComponent1 {
    void inject(StartActivityV2 homeActivity);
    void inject(HomeFragmentV2 fragmentV2);
    void inject(HomeHomeFragmentV1 homeHomeFragmentV1);
    void inject(HomeHomeFragmentV2 homeHomeFragmentV2);
    void inject(HomeHomeFragmentV3 homeHomeFragmentV3);
    void inject(HomeHomeFragmentV4 homeHomeFragmentV4);
    void inject(ProductDetailActivity productDetailActivity);
    void inject(BaseAddressFragment baseAddressFragment);
    void inject(CheckoutDefaultAddressFragment checkoutDefaultAddressFragment);
    void inject(CheckoutActivity checkoutActivity);
    void inject(HomeActivity homeActivity);
    void inject(LoginRegisterEmailLoginFragment fragment);
    void inject(CheckoutPaymentStatusActivity activity);
    Activity getActivity();
}
