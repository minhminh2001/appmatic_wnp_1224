package injection.components;

import android.app.Activity;

import com.whitelabel.app.activity.MyAccountOrderDetailActivity;
import com.whitelabel.app.fragment.HomeMyAccountOrdersFragment;
import com.whitelabel.app.fragment.HomeSettingCotentFragment;
import com.whitelabel.app.fragment.LoginRegisterEmailRegisterFragment;
import com.whitelabel.app.fragment.ProductListKeywordsSearchFragment;
import com.whitelabel.app.fragment.ShoppingCartVerticalFragment;
import com.whitelabel.app.ui.checkout.CheckoutActivity;
import com.whitelabel.app.activity.CheckoutPaymentStatusActivity;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.fragment.LoginRegisterEmailBoundFragment;
import com.whitelabel.app.fragment.LoginRegisterEmailLoginFragment;
import com.whitelabel.app.ui.checkout.CheckoutAddaddressFragment;
import com.whitelabel.app.ui.checkout.CheckoutDefaultAddressFragment;
import com.whitelabel.app.ui.common.BaseAddressFragment;
import com.whitelabel.app.ui.home.activity.ShopBrandActivity;
import com.whitelabel.app.ui.home.fragment.HomeFragmentV2;
import com.whitelabel.app.ui.home.fragment.HomeHomeFragmentShopBrand;
import com.whitelabel.app.ui.home.fragment.HomeHomeFragmentV1;
import com.whitelabel.app.ui.home.fragment.HomeHomeFragmentV2;
import com.whitelabel.app.ui.home.fragment.HomeHomeFragmentV3;
import com.whitelabel.app.ui.home.fragment.HomeHomeFragmentV4;
import com.whitelabel.app.ui.productdetail.BindProductActivity;
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
    void inject(HomeHomeFragmentShopBrand shopBrand);
    void inject(ShopBrandActivity shopBrandActivity);
    void inject(ProductDetailActivity productDetailActivity);
    void inject(BindProductActivity bindProductActivity);
    void inject(BaseAddressFragment baseAddressFragment);
    void inject(CheckoutDefaultAddressFragment checkoutDefaultAddressFragment);
    void inject(CheckoutAddaddressFragment checkoutAddaddressFragment);
    void inject(ShoppingCartVerticalFragment shoppingCartVerticalFragment);
    void inject(CheckoutActivity checkoutActivity);
    void inject(HomeActivity homeActivity);
    void inject(LoginRegisterEmailLoginFragment fragment);
    void inject(LoginRegisterEmailRegisterFragment fragment);
    void inject(CheckoutPaymentStatusActivity activity);
    void inject(LoginRegisterEmailBoundFragment fragment);
    void inject(ProductListKeywordsSearchFragment fragment);
    void inject(HomeSettingCotentFragment fragment);
    void inject(HomeMyAccountOrdersFragment fragment);
    void inject(MyAccountOrderDetailActivity activity);
    Activity getActivity();
}
