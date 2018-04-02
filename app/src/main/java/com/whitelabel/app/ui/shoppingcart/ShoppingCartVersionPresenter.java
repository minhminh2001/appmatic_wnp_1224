package com.whitelabel.app.ui.shoppingcart;

import com.whitelabel.app.data.preference.model.ShoppingItemLocalModel;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.IShoppingCartManager;
import com.whitelabel.app.model.GuestListResponse;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.RxUtil;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/12.
 */
public class ShoppingCartVersionPresenter extends RxPresenter<ShoppingCartVersionContract.View>
    implements ShoppingCartVersionContract.Presenter {

    private IBaseManager iBaseManager;

    private IShoppingCartManager iShoppingCartManager;

    public ShoppingCartVersionPresenter(IBaseManager iBaseManager,
        IShoppingCartManager iShoppingCartManager) {
        this.iBaseManager = iBaseManager;
        this.iShoppingCartManager = iShoppingCartManager;
    }

    @Override
    public void versionCheck() {
        Subscription subscription = iBaseManager.versionCheck()
            .compose(RxUtil.<ResponseModel>rxSchedulerHelper()).subscribe(
                new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (ExceptionParse.parseException(throwable)
                            .getErrorType() == ExceptionParse.ERROR.HTTP_ERROR) {
                            mView.showErrorMessage(
                                ExceptionParse.parseException(throwable).getErrorMsg());
                        }
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        if (responseModel.getStatus() == 1) {
                            mView.checkCartStock();
                        } else if (responseModel.getStatus() == -1) {
                            //need update
                            mView.showUpdateDialog();
                        }
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void getShoppingListFromLocal() {
        iShoppingCartManager.getProductListFromLocal().
            compose(RxUtil.<List<ShoppingItemLocalModel>>rxSchedulerHelper()).subscribe(
            new Subscriber<List<ShoppingItemLocalModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    mView.dimissDialog();
                }

                @Override
                public void onNext(List<ShoppingItemLocalModel> shoppingItemLocalModels) {
                    if (shoppingItemLocalModels != null && shoppingItemLocalModels.size() > 0) {
                        getGuestList(shoppingItemLocalModels);
                    } else {
                        mView.dimissDialog();
                        mView.setLayoutNotHaveProduct();
                    }
                }
            });
    }

    @Override
    public void deleteItem(String simpleId) {
        mView.showDialog();
        iShoppingCartManager.deleteLocalShoppingItem(simpleId)
            .compose(RxUtil.<Boolean>rxSchedulerHelper())
            .subscribe(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    mView.dimissDialog();
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    getShoppingListFromLocal();
                }
            });
    }

    @Override
    public void updateShoppingItemNumber(String simpleId, String s) {
        mView.showDialog();
        iShoppingCartManager.updateLocalShoppingItemNumber(simpleId, s)
            .compose(RxUtil.<Boolean>rxSchedulerHelper())
            .subscribe(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    mView.dimissDialog();
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    getShoppingListFromLocal();
                }
            });
        ;
    }

    public void getGuestList(final List<ShoppingItemLocalModel> shoppingItemLocalModels) {
        iShoppingCartManager.getGuestList(shoppingItemLocalModels)
            .compose(RxUtil.<GuestListResponse>rxSchedulerHelper())
            .subscribe(new Subscriber<GuestListResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    mView.dimissDialog();
                }

                @Override
                public void onNext(GuestListResponse shoppingCartListEntityCart) {
                    mView.dimissDialog();
                    mView.loadData(shoppingCartListEntityCart.getCart());
                }
            });

    }
}
