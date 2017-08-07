package com.whitelabel.app.data.service;

        import com.whitelabel.app.model.AddToWishlistEntity;
        import com.whitelabel.app.model.AddresslistReslut;
        import com.whitelabel.app.model.NotificationUnReadResponse;
        import com.whitelabel.app.model.ResponseModel;
        import com.whitelabel.app.model.WishDelEntityResult;

        import rx.Observable;

/**
 * Created by Administrator on 2017/7/5.
 */
public interface IAccountManager {
    public Observable<AddresslistReslut> getAddressList(final String sessionKey);
    public Observable<ResponseModel> deleteAddressById(final String sessionKey, String addressId);
    public Observable<AddToWishlistEntity> addWishlist(String sessionKey, String productId);
    public Observable<WishDelEntityResult> deleteWishlist(String sessionKey, String productId);
    public Observable<NotificationUnReadResponse> getNotificationUnReadCount(String userId);
}
