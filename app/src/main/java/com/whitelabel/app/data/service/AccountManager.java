package com.whitelabel.app.data.service;

import com.whitelabel.app.data.model.RegisterRequest;
import com.whitelabel.app.data.preference.ICacheApi;
import com.whitelabel.app.data.retrofit.MyAccoutApi;
import com.whitelabel.app.data.retrofit.OneAllApi;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.AddresslistReslut;
import com.whitelabel.app.model.ApiException;
import com.whitelabel.app.model.NotificationUnReadResponse;
import com.whitelabel.app.model.ResponseConnection;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppServiceCustomerLoginReturnEntity;
import com.whitelabel.app.model.SVRAppserviceCustomerFbLoginReturnEntity;
import com.whitelabel.app.model.SubscriberResponse;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RxUtil;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

;

/**
 * Created by Administrator on 2017/7/5.
 */
public class AccountManager implements IAccountManager {

    private MyAccoutApi myAccoutApi;

    private ICacheApi iCacheApi;

    private OneAllApi oneAllApi;

    @Inject
    public AccountManager(MyAccoutApi myAccoutApi, ICacheApi iCacheApi, OneAllApi oneAllApi) {
        this.myAccoutApi = myAccoutApi;
        this.iCacheApi = iCacheApi;
        this.oneAllApi = oneAllApi;
    }

    @Override
    public Observable<ResponseConnection> getOneAllUser(String platform, String accessToken,
        String secret) {
        com.whitelabel.app.model.NativeLoginRequest request = new com.whitelabel.app.model
            .NativeLoginRequest(
            platform, accessToken, secret);
        return oneAllApi.info(request);
    }

    @Override
    public Observable<ResponseModel> deleteAddressById(final String sessionKey, String addressId) {
        return myAccoutApi.deleteAddressById(sessionKey, addressId)
            .compose(RxUtil.<ResponseModel>rxSchedulerHelper());
    }

    @Override
    public Observable<AddresslistReslut> getAddressList(final String sessionKey) {
        return myAccoutApi.getAddressList(sessionKey)
            .compose(RxUtil.<AddresslistReslut>rxSchedulerHelper())
            .onErrorResumeNext(new Func1<Throwable, Observable<AddresslistReslut>>() {
                @Override
                public Observable<AddresslistReslut> call(Throwable throwable) {
                    return Observable.error(throwable);
                }
            });
    }

    @Override
    public Observable<SVRAppserviceCustomerFbLoginReturnEntity> threePartLogin(String gavinName,
        String displayName,
        String formatted, String familyName, String email,
        String identityToken, String userToken, String provider, String boundEmail) {
        return myAccoutApi
            .threePartLogin(gavinName, displayName, formatted, familyName, email, identityToken,
                userToken, provider, boundEmail)
            .flatMap(
                new Func1<SVRAppserviceCustomerFbLoginReturnEntity,
                    Observable<SVRAppserviceCustomerFbLoginReturnEntity>>() {
                    @Override
                    public Observable<SVRAppserviceCustomerFbLoginReturnEntity> call(
                        SVRAppserviceCustomerFbLoginReturnEntity entity) {
                        if (entity.getStatus() == -1) {
                            return Observable.error(
                                new ApiException(entity.getErrorMessage(), entity.getCode()));
                        } else {
                            return Observable.just(entity);
                        }
                    }
                });
    }

    @Override
    public Observable<AddToWishlistEntity> addWishlist(String sessionKey, String productId) {
        return myAccoutApi.addWish(sessionKey, productId)
            .flatMap(new Func1<AddToWishlistEntity, Observable<AddToWishlistEntity>>() {
                @Override
                public Observable<AddToWishlistEntity> call(
                    AddToWishlistEntity addToWishlistEntity) {
                    if (addToWishlistEntity.getStatus() == -1) {
                        return Observable
                            .error(new ApiException(addToWishlistEntity.getErrorMessage()));
                    } else {
                        return Observable.just(addToWishlistEntity);
                    }
                }
            });
    }

    @Override
    public Observable<WishDelEntityResult> deleteWishlist(String sessionKey, String itemId) {
        return myAccoutApi.deleteWishById(sessionKey, itemId)
            .flatMap(new Func1<WishDelEntityResult, Observable<WishDelEntityResult>>() {
                @Override
                public Observable<WishDelEntityResult> call(
                    WishDelEntityResult wishDelEntityResult) {
                    if (wishDelEntityResult.getStatus() == -1) {
                        return Observable
                            .error(new ApiException(wishDelEntityResult.getErrorMessage()));
                    } else {
                        return Observable.just(wishDelEntityResult);
                    }
                }
            });
    }

    @Override
    public Observable<NotificationUnReadResponse> getNotificationUnReadCount(String userId) {
        return myAccoutApi.getNotificationUnReadResponse(userId)
            .flatMap(
                new Func1<ResponseModel<NotificationUnReadResponse>,
                    Observable<NotificationUnReadResponse>>() {
                    @Override
                    public Observable<NotificationUnReadResponse> call
                        (ResponseModel<NotificationUnReadResponse> bean) {
                        JLogUtils.i("ray", "Thread1:" + Thread.currentThread().getName());
                        if (bean.getCode() == 1) {
                            return Observable.just(bean.getData());
                        } else {
                            return Observable.error(new ApiException(bean.getErrorMessage()));
                        }
                    }
                });
    }

    @Override
    public void saveGuideFlag(Boolean isFirst) {
        iCacheApi.saveGuideFlag(isFirst);
    }

    @Override
    public boolean isGuide() {
        return iCacheApi.isGuide();
    }

    @Override
    public Observable<ResponseModel> setUserAgreement(String sessionKey, String isAgree) {
        return myAccoutApi.setUserAgreement(sessionKey, isAgree).flatMap(
            new Func1<ResponseModel, Observable<ResponseModel>>() {
                @Override
                public Observable<ResponseModel> call(ResponseModel responseModel) {
                    if (responseModel.getStatus() == 1) {
                        return Observable.just(responseModel);
                    } else {
                        return Observable.error(new ApiException(responseModel.getErrorMessage()));
                    }
                }
            });
    }

    @Override
    public Observable<SubscriberResponse> getUserAgreement(String sessionKey) {
        return myAccoutApi.getUserAgreement(sessionKey).flatMap(
            new Func1<SubscriberResponse, Observable<SubscriberResponse>>() {
                @Override
                public Observable<SubscriberResponse> call(SubscriberResponse subscriberResponse) {
                    if (subscriberResponse.getStatus() == 1) {
                        return Observable.just(subscriberResponse);
                    } else {
                        return Observable
                            .error(new ApiException(subscriberResponse.getErrorMessage()));
                    }
                }
            });
    }

    @Override
    public Observable<ResponseModel> register(RegisterRequest registerRequest) {
        Map<String, String> params = new HashMap<>();
        params.put("firstname", registerRequest.getFirstName());
        params.put("lastname", registerRequest.getLastName());
        params.put("email", registerRequest.getEmail());
        params.put("customer_telephone", registerRequest.getPhone());
        params.put("password", registerRequest.getPassword());
        params.put("is_subscribed", registerRequest.getSubscribed());
        params.put("device_token", registerRequest.getDeviceToken());
        return myAccoutApi.registerEmail(params).flatMap(
            new Func1<ResponseModel, Observable<ResponseModel>>() {
                @Override
                public Observable<ResponseModel> call(ResponseModel responseModel) {
                    if (responseModel.getStatus() == 1) {
                        return Observable.just(responseModel);
                    } else {
                        return Observable.error(new ApiException(responseModel.getErrorMessage()));
                    }
                }
            });
    }

    @Override
    public Observable<SVRAppServiceCustomerLoginReturnEntity> loginEmail(String email,
        String password, String deviceToken) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        if (!TextUtils.isEmpty(deviceToken)) {
            params.put("device_token", deviceToken);
        }
        return myAccoutApi.loginEmail(params);
    }
}
