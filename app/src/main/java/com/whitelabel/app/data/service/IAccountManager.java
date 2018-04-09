package com.whitelabel.app.data.service;

        import com.whitelabel.app.data.model.RegisterRequest;
        import com.whitelabel.app.model.AddToWishlistEntity;
        import com.whitelabel.app.model.AddresslistReslut;
        import com.whitelabel.app.model.NotificationUnReadResponse;
        import com.whitelabel.app.model.ResponseConnection;
        import com.whitelabel.app.model.ResponseModel;
        import com.whitelabel.app.model.SVRAppServiceCustomerLoginReturnEntity;
        import com.whitelabel.app.model.SVRAppserviceCustomerFbLoginReturnEntity;
        import com.whitelabel.app.model.SubscriberResponse;
        import com.whitelabel.app.model.WishDelEntityResult;

        import rx.Observable;

/**
 * Created by Administrator on 2017/7/5.
 */
public interface IAccountManager {
     Observable<AddresslistReslut> getAddressList(final String sessionKey);
     Observable<ResponseModel> deleteAddressById(final String sessionKey, String addressId);
     Observable<AddToWishlistEntity> addWishlist(String sessionKey, String productId);
     Observable<WishDelEntityResult> deleteWishlist(String sessionKey, String productId);
     Observable<NotificationUnReadResponse> getNotificationUnReadCount(String userId);
     Observable<ResponseConnection>  getOneAllUser(String  platform, String accessToken, String secret);
     Observable<SVRAppserviceCustomerFbLoginReturnEntity>
    threePartLogin(String gavinName,String displayName,String formatted,String familyName,String email,
                   String identityToken,String userToken,String provider, String boundEmail );
     void saveGuideFlag(Boolean isFirst);
     boolean isGuide();
     Observable<ResponseModel> setUserAgreement(String sessionKey, String isAgree);
     Observable<SubscriberResponse> getUserAgreement(String sessionKey);

    Observable<ResponseModel>  register(RegisterRequest registerRequest);

    Observable<SVRAppServiceCustomerLoginReturnEntity> loginEmail(String email,String password,String deviceToken);
}
