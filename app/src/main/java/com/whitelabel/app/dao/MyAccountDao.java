package com.whitelabel.app.dao;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.AddressDeleteCellEntity;
import com.whitelabel.app.model.AddresslistReslut;
import com.whitelabel.app.model.CheckoutSelectShippingAddressEntity;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.MyAccountOrderDetailEntityResult;
import com.whitelabel.app.model.MyAccountOrderListEntityResult;
import com.whitelabel.app.model.MyAccountOrderOuter;
import com.whitelabel.app.model.SVRAddAddress;
import com.whitelabel.app.model.SVRAppServiceCustomerCountry;
import com.whitelabel.app.model.SVRAppServiceCustomerLoginReturnEntity;
import com.whitelabel.app.model.SVRAppServiceCustomerMonthlyIncom;
import com.whitelabel.app.model.SVRAppServiceCustomerMyAccount;
import com.whitelabel.app.model.SVRAppServiceCustomerMyAccountUpdate;
import com.whitelabel.app.model.SVRAppServiceCustomerchangepass;
import com.whitelabel.app.model.SVRAppserviceCustomerFbLoginReturnEntity;
import com.whitelabel.app.model.SVREditAddress;
import com.whitelabel.app.model.SVRGetCityANdStateByPostCodeEntity;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.model.Wishlist;
import com.whitelabel.app.model.WishlistEntityResult;
import com.whitelabel.app.network.BaseHttp;
import com.whitelabel.app.utils.JJsonUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/3/30.
 */
public class MyAccountDao extends BaseHttp {
    public static final int REQUEST_GETADDRESS = 10001;
    public static final int ERROR = 1111;
    public static final int REQUEST_GETWISHLIST = 20000;
    public static final int REQUEST_DELETEWISHLIST = 30000;
    public static final int REQUEST_DELETEWISH = 30001;
    public static final int REQUEST_GETADDRESLIST = 10002;
    public static final int REQUEST_DELETEADDRESS = 10003;
    public static final int REQUEST_GETCOUNTRY_REGIONS = 10004;
    public static final int REQUEST_ADDRESS_SAVE = 10005;
    public static final int REQUEST_CITY_STATE_BYPOSTCODE = 10006;
    public static final int REQUEST_EDIT_SAVE = 10007;
    public static final int REQUEST_SENDREQUEST = 10008;
    public static final int REQUEST_CHANGEPASS = 100009;
    public static final int REQUEST_ACCOUNTUPDATEA = 1000010;
    public static final int REQUEST_MONTHLYINCOM = 1000011;
    public static final int REQUEST_SENDREQUESTTOGET = 1000012;
    public static final int REQUEST_SAVE = 1000013;

    public static final int REQUEST_CHECKEMAIL = 10009;
    public static final int REQUEST_EMAILLOGIN = 10010;
    public static final int REQUEST_FACEBOOKLOGIN = 10011;

    public static final int REQUEST_REGISTERCODE = 10012;
    public static final int REQUEST_ORDERLIST = 10013;
    public static final int REQUEST_GOOGLELOGIN = 10014;
    public static final int LOCAL_WISTLIST_GET = 100020;
    public static final int LOCAL_WISTLIST_SAVE = 100021;
    public static final int LOCAL_ORDER_GET = 100022;
    public static final int LOCAL_ORDER_SAVE = 100023;
    public static final int LOCAL_ADDRESS_GET = 100024;
    public static final int LOCAL_ADDRESS_SAVE = 100025;


    public MyAccountDao(String TAG, Handler handler) {
        super(TAG, handler);
    }

    public void getAddressBySession(String sessionkey) {
        params = new TreeMap<>();
        params.put("session_key", sessionkey);
        requestHttp(BaseHttp.HTTP_METHOD.GET, "appservice/customer/customerAddressInfo", params, REQUEST_GETADDRESS);
    }

    public void emailLogin(String email, String password, String deviceToken) {
        params = new TreeMap<>();
        params.put("email", email);
        params.put("password", password);
        if (TextUtils.isEmpty(deviceToken)) {
            params.put("device_token", "");
        } else {
            params.put("device_token", deviceToken);
        }
        requestHttp(HTTP_METHOD.POST, "appservice/customer/login", params, REQUEST_EMAILLOGIN);
    }


    class MyAccountThread extends Thread {
        public static final int DATA_WIST_SAVE = 1;
        public static final int DATA_WIST_GET = 2;
        public static final int DATA_ORDER_SAVE = 3;
        public static final int DATA_ORDER_GET = 4;
        public static final int DATA_ADDRESS_SAVE = 5;
        public static final int DATA_ADDRESS_GET = 6;
        private Object data;
        private WeakReference<Context> mContext;
        private WeakReference<Handler> mHandler;
        private int mType;
        private String mUserId;
        private int mRequestCode;

        public MyAccountThread(int currType, String userId, Object data, Context context, Handler handler, int requestCode) {
            this.mType = currType;
            this.mUserId = userId;
            this.data = data;
            mContext = new WeakReference<Context>(context);
            mHandler = new WeakReference<Handler>(handler);
            mRequestCode = requestCode;
        }

        @Override
        public void run() {
            if (mContext.get() == null || mHandler.get() == null) {
                return;
            }
            if (mType == DATA_WIST_GET) {
                data = JStorageUtils.getWistListData(mContext.get(), mUserId);
            } else if (mType == DATA_WIST_SAVE) {
                JStorageUtils.saveWistListData(mContext.get(), mUserId, (List<com.whitelabel.app.bean.Wishlist>) data);
            } else if (mType == DATA_ORDER_GET) {
                data = JStorageUtils.getOrderListData(mContext.get(), mUserId);
            } else if (mType == DATA_ORDER_SAVE) {
                JStorageUtils.saveOrderListData(mContext.get(), mUserId, (List<MyAccountOrderOuter>) data);

            } else if (mType == DATA_ADDRESS_GET) {
                data = JStorageUtils.getAddressData(mContext.get(), mUserId);

            } else if (mType == DATA_ADDRESS_SAVE) {
                JStorageUtils.saveAddressData(mContext.get(), mUserId, (List<AddressBook>) data);
            }
            Message msg = new Message();
            msg.what = mRequestCode;
            msg.obj = data;
            msg.arg1 = RESPONSE_SUCCESS;
            mHandler.get().sendMessage(msg);
            super.run();
        }
    }

    public void getLocalWishData(Context context, String userId) {
        new MyAccountThread(MyAccountThread.DATA_WIST_GET, userId, null, context, mHandler, LOCAL_WISTLIST_GET).start();
    }

    public void saveLocalWishData(Context context, String userId, List<Wishlist> wishlist) {
        new MyAccountThread(MyAccountThread.DATA_WIST_SAVE, userId, wishlist, context, mHandler, LOCAL_WISTLIST_SAVE).start();
    }

    public void getLocalOrderData(Context context, String userId) {
        new MyAccountThread(MyAccountThread.DATA_ORDER_GET, userId, null, context, mHandler, LOCAL_ORDER_GET).start();
    }

    public void saveLocalOrderData(Context context, String userId, List<MyAccountOrderOuter> beans) {
        new MyAccountThread(MyAccountThread.DATA_ORDER_SAVE, userId, beans, context, mHandler, LOCAL_ORDER_SAVE).start();
    }

    public void getLocalAddressData(Context context, String userId) {
        new MyAccountThread(MyAccountThread.DATA_ADDRESS_GET, userId, null, context, mHandler, LOCAL_ADDRESS_GET).start();
    }

    public void saveLocalAddressData(Context context, String userId, List<AddressBook> beans) {
        new MyAccountThread(MyAccountThread.DATA_ADDRESS_SAVE, userId, beans, context, mHandler, LOCAL_ADDRESS_SAVE).start();
    }


    public void getOrderList(String sessionKey, String offset, String max) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("offset", offset);
        params.put("max", max);

        requestHttp(BaseHttp.HTTP_METHOD.GET, "appservice/order/history", params, REQUEST_ORDERLIST);

    }


    public void registerUser(String firstname, String lastname, String email, String password, String subscribed, String deviceToken) {
        params = new TreeMap<>();
        if (TextUtils.isEmpty(firstname)) {
            params.put("firstname", "");
        } else {
            params.put("firstname", firstname);
        }

        if (TextUtils.isEmpty(lastname)) {
            params.put("lastname", "");
        } else {
            params.put("lastname", lastname);
        }

        if (TextUtils.isEmpty(email)) {
            params.put("email", "");
        } else {
            params.put("email", email);
        }
        if (TextUtils.isEmpty(password)) {
            params.put("password", "");
        } else {
            params.put("password", password);
        }

        if (TextUtils.isEmpty(subscribed)) {
            params.put("is_subscribed", "");
        } else {
            params.put("is_subscribed", subscribed);
        }

        if (TextUtils.isEmpty(deviceToken)) {
            params.put("device_token", "");
        } else {
            params.put("device_token", deviceToken);
        }
        requestHttp(BaseHttp.HTTP_METHOD.POST, "appservice/customer/create", params, REQUEST_REGISTERCODE);

    }


    public void facebookLogin(String email, String fbHasEmail, String firstname, String lastname, String fbId, String deviceToken) {
        params = new TreeMap<>();
        if (TextUtils.isEmpty(email)) {
            params.put("email", "");
        } else {
            params.put("email", email);
        }
        params.put("fb_has_email", fbHasEmail);
        if (TextUtils.isEmpty(firstname)) {
            params.put("firstname", "");
        } else {
            params.put("firstname", firstname);
        }
        if (TextUtils.isEmpty(lastname)) {
            params.put("lastname", "");
        } else {
            params.put("lastname", lastname);
        }
        params.put("fb_id", fbId);
        params.put("store_id", "1");
        if (TextUtils.isEmpty(deviceToken)) {
            params.put("device_token", "");
        } else {
            params.put("device_token", deviceToken);
        }
        requestHttp(BaseHttp.HTTP_METHOD.POST, "appservice/customer/fbLogin", params, REQUEST_FACEBOOKLOGIN);
    }


    public void googleLogin(String email, String firstname, String lastname, String fbId, String deviceToken) {
        params = new TreeMap<>();
        if (TextUtils.isEmpty(email)) {
            params.put("email", "");
        } else {
            params.put("email", email);
        }
        if (TextUtils.isEmpty(firstname)) {
            params.put("firstname", "");
        } else {
            params.put("firstname", firstname);
        }
        if (TextUtils.isEmpty(lastname)) {
            params.put("lastname", "");
        } else {
            params.put("lastname", lastname);
        }
        params.put("gg_id", fbId);
        params.put("store_id", "1");
        if (TextUtils.isEmpty(deviceToken)) {
            params.put("device_token", "");
        } else {
            params.put("device_token", deviceToken);
        }
        requestHttp(BaseHttp.HTTP_METHOD.POST, "appservice/customer/gglogin", params, REQUEST_GOOGLELOGIN);
    }


    public void checkEmail(String email) {
        params = new TreeMap<>();
        params.put("email", email);
        requestHttp(BaseHttp.HTTP_METHOD.POST, "appservice/customer/confirmation", params, REQUEST_CHECKEMAIL);

    }

    public void getAddresslist(String sessionkey) {
        params = new TreeMap<>();
        params.put("session_key", sessionkey);
        requestHttp(BaseHttp.HTTP_METHOD.GET, "appservice/customer/customerAddressInfo", params, REQUEST_GETADDRESLIST);
    }

    public void deleteAddress(String sessionKey, String addressId) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("address_id", addressId);
        requestHttp(BaseHttp.HTTP_METHOD.GET, "appservice/customer/removeAddress", params, REQUEST_DELETEADDRESS);
    }

    public void getCountryAndRegions(String sessionKey) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        requestHttp(BaseHttp.HTTP_METHOD.GET, "appservice/directory/getCountryAndRegionList", params, REQUEST_GETCOUNTRY_REGIONS);
    }

    public void getCityAndStateByPostCodet(String session_key, String postcode, String country_id) {
        params = new TreeMap<>();
        params.put("session_key", session_key);
        params.put("postcode", postcode);
        params.put("country_id", country_id);
        requestHttp(BaseHttp.HTTP_METHOD.POST, "appservice/customer/getCityRegionByPostcode", params, REQUEST_CITY_STATE_BYPOSTCODE);
    }

    public void addressSave(String sessionKey, String firstname, String lastname,
                            String country_id, String telephone, String street0, String street1,
                            String postcode, String city, String region, String region_id, String default_shipping) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("country_id", country_id);
        params.put("telephone", telephone);
        params.put("street[0]", street0);
        params.put("street[1]", street1);
        params.put("postcode", postcode);
        params.put("city", city);
        params.put("region", region);
        params.put("region_id", region_id);
        params.put("default_shipping", default_shipping);
        requestHttp(BaseHttp.HTTP_METHOD.GET, "appservice/customer/createCustomerAddress", params, REQUEST_ADDRESS_SAVE);
    }

    public void EditSave(String addressId, String sessionKey, String firstname, String lastname,
                         String country_id, String telephone, String street0, String street1,
                         String postcode, String city, String region, String region_id, String default_shipping) {
        params = new TreeMap<>();
        params.put("address_id", addressId);
        params.put("session_key", sessionKey);
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("country_id", country_id);
        params.put("telephone", telephone);
        params.put("street[0]", street0);
        params.put("street[1]", street1);
        params.put("postcode", postcode);
        params.put("city", city);
        params.put("region", region);
        params.put("region_id", region_id);
        if (default_shipping != null) {
            params.put("default_shipping", default_shipping);
        }
        requestHttp(HTTP_METHOD.POST, "appservice/customer/editCustomerAddress", params, REQUEST_EDIT_SAVE);
    }

    public void sendRequest(String storeId, String session_key, String orderId) {
        params = new TreeMap<>();
        params.put("storeId", storeId);
        params.put("session_key", session_key);
        params.put("orderId", orderId);
        requestHttp(BaseHttp.HTTP_METHOD.GET, "appservice/order/detail", params, REQUEST_SENDREQUEST);
    }

    public void changePass(String session_key, String current_password, String password, String confirmation) {
        params = new TreeMap<>();
        params.put("session_key", session_key);
        params.put("current_password", current_password);
        params.put("password", password);
        params.put("confirmation", confirmation);
        requestHttp(BaseHttp.HTTP_METHOD.POST, "appservice/customer/changePassword", params, REQUEST_CHANGEPASS);
    }

    public void CustomerInfo(String session_key) {
        params = new TreeMap<>();
        params.put("session_key", session_key);
        requestHttp(BaseHttp.HTTP_METHOD.GET, "appservice/customer/customerInfo", params, REQUEST_ACCOUNTUPDATEA);
    }

    public void MonthlyIncom(String session_key) {
        params = new TreeMap<>();
        params.put("session_key", session_key);
        requestHttp(BaseHttp.HTTP_METHOD.GET, "appservice/directory/getMonthlyIncomeList", params, REQUEST_MONTHLYINCOM);
    }

    public void sendRequestToGetCityAndStateByPostCode(String session_key, String postcode, String country_id) {
        params = new TreeMap<>();
        params.put("session_key", session_key);
        params.put("postcode", postcode);
        params.put("country_id", country_id);
        requestHttp(HTTP_METHOD.POST, "appservice/customer/getCityRegionByPostcode", params, REQUEST_SENDREQUESTTOGET);
    }

    public void save(String session_key, String emailStr, String firstNameStr, String lastNameStr, String genderStr, String birthdayStr, String countryIdStr, String regionIdStr,
                     String cityStr, String incomeIdStr, String postcodeStr, String telephoneCodeStr, String telephoneStr) {
        params = new TreeMap<>();
        params.put("session_key", session_key);
        params.put("email", emailStr);
        params.put("firstName", firstNameStr);
        params.put("lastName", lastNameStr);
        params.put("gender", genderStr);
        params.put("birthday", birthdayStr);
        params.put("countryId", countryIdStr);
        params.put("regionId", regionIdStr);
        params.put("city", cityStr);
        params.put("incomeId", incomeIdStr);
        params.put("postcode", postcodeStr);
        params.put("telephoneCode", telephoneCodeStr);
        params.put("telephone", telephoneStr);
        requestHttp(HTTP_METHOD.POST, "appservice/customer/update", params, REQUEST_SAVE);
    }


    @Override
    public void onSuccess(int requestCode, String response, Object object) {
        JLogUtils.json("response", requestCode, response);
        switch (requestCode) {
            case REQUEST_SAVE:
                if (isOk(response)) {
                    SVRAppServiceCustomerMyAccount bean = JJsonUtils.parseJsonObj(response, SVRAppServiceCustomerMyAccount.class);
                    postHandler(requestCode, bean, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(REQUEST_SAVE, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_SENDREQUESTTOGET:
                if (isOk(response)) {
                    SVRGetCityANdStateByPostCodeEntity bean = JJsonUtils.parseJsonObj(response, SVRGetCityANdStateByPostCodeEntity.class);
                    postHandler(requestCode, bean, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(REQUEST_SENDREQUESTTOGET, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_MONTHLYINCOM:
                if (isOk(response)) {
                    SVRAppServiceCustomerMonthlyIncom bean = JJsonUtils.parseJsonObj(response, SVRAppServiceCustomerMonthlyIncom.class);
                    postHandler(requestCode, bean, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(REQUEST_MONTHLYINCOM, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_ACCOUNTUPDATEA:
                if (isOk(response)) {
                    SVRAppServiceCustomerMyAccountUpdate bean = JJsonUtils.parseJsonObj(response, SVRAppServiceCustomerMyAccountUpdate.class);
                    postHandler(requestCode, bean, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(REQUEST_ACCOUNTUPDATEA, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_CHANGEPASS:
                if (isOk(response)) {
                    SVRAppServiceCustomerchangepass bean = JJsonUtils.parseJsonObj(response, SVRAppServiceCustomerchangepass.class);
                    postHandler(requestCode, bean, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(REQUEST_CHANGEPASS, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_SENDREQUEST:
                if (isOk(response)) {
                    MyAccountOrderDetailEntityResult bean = JJsonUtils.parseJsonObj(response, MyAccountOrderDetailEntityResult.class);
                    postHandler(requestCode, bean, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(REQUEST_SENDREQUEST, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_EDIT_SAVE:
                if (isOk(response)) {
                    SVREditAddress bean = JJsonUtils.parseJsonObj(response, SVREditAddress.class);
                    postHandler(requestCode, bean, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(REQUEST_EDIT_SAVE, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_CITY_STATE_BYPOSTCODE:
                if (isOk(response)) {
                    SVRGetCityANdStateByPostCodeEntity bean = JJsonUtils.parseJsonObj(response, SVRGetCityANdStateByPostCodeEntity.class);
                    postHandler(requestCode, bean, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(REQUEST_CITY_STATE_BYPOSTCODE, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_ADDRESS_SAVE:
                if (isOk(response)) {
                    SVRAddAddress bean = JJsonUtils.parseJsonObj(response, SVRAddAddress.class);
                    postHandler(requestCode, bean, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(REQUEST_ADDRESS_SAVE, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_GETCOUNTRY_REGIONS:
                if (isOk(response)) {
                    SVRAppServiceCustomerCountry bean = JJsonUtils.parseJsonObj(response, SVRAppServiceCustomerCountry.class);
                    postHandler(requestCode, bean, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(REQUEST_GETCOUNTRY_REGIONS, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_DELETEADDRESS:
                if (isOk(response)) {
                    AddressDeleteCellEntity bean = JJsonUtils.parseJsonObj(response, AddressDeleteCellEntity.class);
                    postHandler(requestCode, bean, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(REQUEST_DELETEADDRESS, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_GETADDRESLIST:
                if (isOk(response)) {
                    AddresslistReslut bean = JJsonUtils.parseJsonObj(response, AddresslistReslut.class);
                    postHandler(requestCode, bean, RESPONSE_SUCCESS);

                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(REQUEST_GETADDRESLIST, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_GETADDRESS:
                if (isOk(response)) {
                    CheckoutSelectShippingAddressEntity bean = JJsonUtils.parseJsonObj(response, CheckoutSelectShippingAddressEntity.class);
                    postHandler(requestCode, bean, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(REQUEST_GETADDRESS, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_GETWISHLIST:
                if (isOk(response)) {
                    WishlistEntityResult wishlistEntityResult = JJsonUtils.parseJsonObj(response, WishlistEntityResult.class);
                    postHandler(requestCode, wishlistEntityResult, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(requestCode, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_DELETEWISHLIST:
                if (isOk(response)) {
                    WishDelEntityResult wishDelEntityResult = JJsonUtils.parseJsonObj(response, WishDelEntityResult.class);
                    wishDelEntityResult.setParams(object);
                    postHandler(requestCode, wishDelEntityResult, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    errorBean.setParams(object);
                    postHandler(requestCode, errorBean, RESPONSE_FAILED);
                }
                break;
            case REQUEST_DELETEWISH:
                if (isOk(response)) {
                    WishDelEntityResult wishDelEntityResult = JJsonUtils.parseJsonObj(response, WishDelEntityResult.class);
                    postHandler(requestCode, wishDelEntityResult, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(requestCode, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_CHECKEMAIL:
                if (isOk(response)) {
                    postHandler(requestCode, "", RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(requestCode, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_EMAILLOGIN:
                if (isOk(response)) {
                    SVRAppServiceCustomerLoginReturnEntity loginReturnEntity = JJsonUtils.parseJsonObj(response, SVRAppServiceCustomerLoginReturnEntity.class);
                    postHandler(requestCode, loginReturnEntity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(requestCode, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_FACEBOOKLOGIN:
                if (isOk(response)) {
                    SVRAppserviceCustomerFbLoginReturnEntity entity = JJsonUtils.parseJsonObj(response, SVRAppserviceCustomerFbLoginReturnEntity.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(requestCode, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_REGISTERCODE:
                if (isOk(response)) {
                    postHandler(requestCode, "", RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(requestCode, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_ORDERLIST:

                if (isOk(response)) {
                    MyAccountOrderListEntityResult orderListEntityResult = JJsonUtils.parseJsonObj(response, MyAccountOrderListEntityResult.class);
                    JLogUtils.d("response", response);
                    postHandler(requestCode, orderListEntityResult, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(requestCode, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_GOOGLELOGIN:
                if (isOk(response)) {
                    SVRAppserviceCustomerFbLoginReturnEntity entity = JJsonUtils.parseJsonObj(response, SVRAppserviceCustomerFbLoginReturnEntity.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorBean = getErrorMsgBean(response);
                    postHandler(requestCode, errorBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
        }
    }

    @Override
    public void onFalied(int requestCode, VolleyError volleyError, Object object, int errorType) {
        postErrorHandler(ERROR, requestCode, volleyError.getMessage(),errorType);
    }

    public void getWishList(String sessionKey, int offset, int size) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("offset", offset + "");
        params.put("max", size + "");
        requestHttp(HTTP_METHOD.POST, "appservice/wishlist", params, REQUEST_GETWISHLIST);
    }

    public void deleteWishListById(String sessionKey, String itemId, int position) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("item_id", itemId);
        requestHttp(HTTP_METHOD.POST, "appservice/wishlist/remove", params, REQUEST_DELETEWISHLIST, position);
    }

    public void deleteWishById(String sessionKey, String itemId) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("item_id", itemId);
        requestHttp(HTTP_METHOD.POST, "appservice/wishlist/remove", params, REQUEST_DELETEWISH);
    }

}
