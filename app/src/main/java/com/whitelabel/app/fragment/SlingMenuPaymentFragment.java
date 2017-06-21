package com.whitelabel.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.CheckoutPaymentStatusActivity;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.dao.NotificationDao;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JStorageUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * A simple {@link } subclass.
 * Activities that contain this fragment must implement the
 * {@link SlingMenuPaymentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SlingMenuPaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlingMenuPaymentFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private NotificationDao mDao;
    private DataHandler dataHandler;
//    private String mParam1;
//    private String mParam2;
    private CheckoutPaymentStatusActivity paymentStatusActivity;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SlingMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SlingMenuPaymentFragment newInstance(String param1, String param2) {
        SlingMenuPaymentFragment fragment = new SlingMenuPaymentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SlingMenuPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_home_RelativeLayout: {
                Intent intent = new Intent(paymentStatusActivity, HomeActivity.class);
                startActivity(intent);
//                paymentStatusActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                paymentStatusActivity.finish();
                break;
            }
            case R.id.signin_ImageView:
                break;
            case R.id.signin_textview: {
                Intent intent = new Intent(paymentStatusActivity, LoginRegisterActivity.class);
                startActivityForResult(intent, 1000);
                paymentStatusActivity.overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
                paymentStatusActivity.finish();
                break;
            }

            case R.id.hassingin_textview: {
                Intent intent = new Intent(paymentStatusActivity, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("from_checkout_to_wishlist", "from_checkout_to_wishlist");
                intent.putExtras(bundle);
                startActivity(intent);
//                paymentStatusActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                paymentStatusActivity.finish();
                break;
            }
            case R.id.signin_whishlist_RelativeLayout: {
                if (WhiteLabelApplication.getAppConfiguration().isSignIn(paymentStatusActivity)) {
                    Intent intent = new Intent(paymentStatusActivity, HomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("from_checkout_to_wishlist", "from_checkout_to_wishlist");
                    intent.putExtras(bundle);
                    startActivity(intent);
//                    paymentStatusActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                } else {
                    Intent intent = new Intent(paymentStatusActivity, LoginRegisterActivity.class);
                    startActivityForResult(intent, 1000);
                    paymentStatusActivity.overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
                }
                paymentStatusActivity.finish();
                break;
            }
            case R.id.signin_orders_RelativeLayout: {
                Intent i = new Intent(paymentStatusActivity, HomeActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_ORDER);
                i.putExtras(bundle1);
                startActivity(i);
//                paymentStatusActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                paymentStatusActivity.finish();
                break;
            }
            case R.id.signin_notification_RelativeLayout: {
                Intent intent = new Intent(paymentStatusActivity, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_NOTIFICATION);
                intent.putExtras(bundle);
                startActivity(intent);
//                paymentStatusActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                paymentStatusActivity.finish();
                break;
            }
            case R.id.signin_address_RelativeLayout:{
                Intent intent2 = new Intent(paymentStatusActivity, HomeActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE,HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_ADDRESS);
                intent2.putExtras(bundle2);
                startActivity(intent2);
//                paymentStatusActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                paymentStatusActivity.finish();
                break;
            }
            case R.id.signin_setting_RelativeLayout: {
                Intent intent = new Intent(paymentStatusActivity, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_SETTING);
                intent.putExtras(bundle);
                startActivity(intent);
//                paymentStatusActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                paymentStatusActivity.finish();
                break;
            }
            case R.id.signin_helpcenter_RelativeLayout: {
                Intent intent = new Intent(paymentStatusActivity, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_HELPCENTER);
                intent.putExtras(bundle);
                startActivity(intent);
//                paymentStatusActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                paymentStatusActivity.finish();
                break;
            }
            case R.id.signin_shoppingCart_RelativeLayout:
                Intent intent = new Intent(paymentStatusActivity, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ShoppingCart", "ShoppingCart");
                intent.putExtras(bundle);
                startActivity(intent);
//                paymentStatusActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                paymentStatusActivity.finish();
                break;
            case R.id.signin_category_RelativeLayout:
                Intent intent_category_tree = new Intent(paymentStatusActivity, HomeActivity.class);
                Bundle bundle_category_tree = new Bundle();
                bundle_category_tree.putString("CategoryTree", "CategoryTree");
                intent_category_tree.putExtras(bundle_category_tree);
                startActivity(intent_category_tree);
//                paymentStatusActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                paymentStatusActivity.finish();
                break;
            case R.id.signin_customerService_RelativeLayout:
                Intent intent1 = new Intent(paymentStatusActivity, HomeActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_HELPCENTERCUSTOMSERVICE);
                intent1.putExtras(bundle1);
                startActivity(intent1);
//                paymentStatusActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                paymentStatusActivity.finish();
                break;
             case R.id.signin_shippingReturns_RelativeLayout:
                 Intent intent2 = new Intent(paymentStatusActivity, HomeActivity.class);
                 Bundle bundle2 = new Bundle();
                 bundle2.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_HELPCENTERSHIPPINGDELIVERY);
                 intent2.putExtras(bundle2);
                 startActivity(intent2);
//                 paymentStatusActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                 paymentStatusActivity.finish();
                break;
            case R.id.signin_storeCredit_RelativeLayout:
                Intent storeCreditIntent = new Intent(paymentStatusActivity, HomeActivity.class);
                Bundle storeCreditBundle = new Bundle();
                storeCreditBundle.putString(HomeActivity.BUNDLE_START_FRAGMENT, HomeActivity.BUNDLE_FRAGMENT_STORECREDIT);
                storeCreditIntent.putExtras(storeCreditBundle);
                startActivity(storeCreditIntent);
//                paymentStatusActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                paymentStatusActivity.finish();
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    private ImageView signin_ImageView;
    private TextView signin_textview, hassingin_textview, tvNotificationNum,tvWhishlistNum,tvOrsersNum,tvShoppingCartNum;
//    private CustomCircularImageView ccivUserAvater;
    private View signin_category_RelativeLayout,signin_home_RelativeLayout, signin_whishlist_RelativeLayout,signin_orders_RelativeLayout,signin_storeCredit_RelativeLayout,
            signin_shippingReturns_RelativeLayout,signin_address_RelativeLayout,signin_customerService_RelativeLayout, signin_setting_RelativeLayout, signin_helpcenter_RelativeLayout, signin_notification_RelativeLayout,shoppingcartLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_sling_menu, container, false);
        signin_ImageView = (ImageView) view.findViewById(R.id.signin_ImageView);
        signin_textview = (TextView) view.findViewById(R.id.signin_textview);
//        ccivUserAvater = (CustomCircularImageView) view.findViewById(R.id.ccivUserAvater);
        hassingin_textview = (TextView) view.findViewById(R.id.hassingin_textview);
        tvNotificationNum = (TextView) view.findViewById(R.id.tv_notification_num);
        signin_storeCredit_RelativeLayout=view.findViewById(R.id.signin_storeCredit_RelativeLayout);
        signin_storeCredit_RelativeLayout.setOnClickListener(this);
        signin_category_RelativeLayout= view.findViewById(R.id.signin_category_RelativeLayout);
        signin_category_RelativeLayout.setOnClickListener(this);
        signin_home_RelativeLayout = view.findViewById(R.id.signin_home_RelativeLayout);
        signin_whishlist_RelativeLayout = view.findViewById(R.id.signin_whishlist_RelativeLayout);
        signin_orders_RelativeLayout=view.findViewById(R.id.signin_orders_RelativeLayout);
        signin_address_RelativeLayout=view.findViewById(R.id.signin_address_RelativeLayout);
        signin_setting_RelativeLayout = view.findViewById(R.id.signin_setting_RelativeLayout);
        signin_helpcenter_RelativeLayout = view.findViewById(R.id.signin_helpcenter_RelativeLayout);
        signin_notification_RelativeLayout = view.findViewById(R.id.signin_notification_RelativeLayout);
        signin_customerService_RelativeLayout=view.findViewById(R.id.signin_customerService_RelativeLayout);
        signin_shippingReturns_RelativeLayout=view.findViewById(R.id.signin_shippingReturns_RelativeLayout);
        shoppingcartLayout=view.findViewById(R.id.signin_shoppingCart_RelativeLayout);
        signin_storeCredit_RelativeLayout.setVisibility(View.VISIBLE);
        shoppingcartLayout.setOnClickListener(this);
        tvWhishlistNum= (TextView) view.findViewById(R.id.tv_whishlist_num);
        tvOrsersNum= (TextView) view.findViewById(R.id.tv_orsers_num);
        tvShoppingCartNum= (TextView) view.findViewById(R.id.tv_shoppingCart_num);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateProfile();
        updateNotificationCount();
        updateMenuShowNumber();
    }

    public void updateMenuShowNumber(){
        if(WhiteLabelApplication.getAppConfiguration().isSignIn(paymentStatusActivity)) {
            long wishlistItem = WhiteLabelApplication.getAppConfiguration().getUserInfo(paymentStatusActivity).getWishListItemCount();
            if (wishlistItem > 0) {
                if(wishlistItem>99){
                    tvWhishlistNum.setText(getResources().getString(R.string.nine));
                }else {
                    tvWhishlistNum.setText("" + wishlistItem);
                }
                tvWhishlistNum.setVisibility(View.VISIBLE);
            }else{
                tvWhishlistNum.setVisibility(View.INVISIBLE);
            }
            long orgerItem= WhiteLabelApplication.getAppConfiguration().getUserInfo(paymentStatusActivity).getOrderCount();
            if(orgerItem>0){
                if(orgerItem>99){
                    tvOrsersNum.setText(getResources().getString(R.string.nine));
                }else {
                    tvOrsersNum.setText("" + orgerItem);
                }
                tvOrsersNum.setVisibility(View.VISIBLE);
            }else{
                tvOrsersNum.setVisibility(View.INVISIBLE);
            }
            long cartItemCount= WhiteLabelApplication.getAppConfiguration().getUserInfo(paymentStatusActivity).getCartItemCount();
            ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(paymentStatusActivity);
            if (list.size() > 0) {
                for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                    cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                }
            }
            if(cartItemCount>0){
                if(cartItemCount>99){
                    tvShoppingCartNum.setText(getResources().getString(R.string.nine));
                }else {
                    tvShoppingCartNum.setText("" + cartItemCount);
                }
                tvShoppingCartNum.setVisibility(View.VISIBLE);
            }else{
                tvShoppingCartNum.setVisibility(View.INVISIBLE);
            }
        }else{
            tvWhishlistNum.setVisibility(View.INVISIBLE);
            tvOrsersNum.setVisibility(View.INVISIBLE);
            tvShoppingCartNum.setVisibility(View.INVISIBLE);

            long cartItemCount=0;
            ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(paymentStatusActivity);
            if (list.size() > 0) {
                for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                    cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                }
                if(cartItemCount>0) {
                    tvShoppingCartNum.setVisibility(View.VISIBLE);
                    tvShoppingCartNum.setText("" + cartItemCount);
                }
            }

        }
    }
        private void updateProfile() {
//        SharedPreferences shared = paymentStatusActivity.getSharedPreferences("session_key", Activity.MODE_PRIVATE);
//            sessionKey = shared.getString("session_key", "");

        if (WhiteLabelApplication.getAppConfiguration().isSignIn(paymentStatusActivity)) {
            String username = "";
            String firstname = WhiteLabelApplication.getAppConfiguration().getUser().getFirstName();
            String lastname = WhiteLabelApplication.getAppConfiguration().getUser().getLastName();

            String avater = WhiteLabelApplication.getAppConfiguration().getUser().getHeadImage();
//            JImageUtils.downloadImageFromServerByUrl(homeActivity, ccivUserAvater, avater, JDataUtils.dp2Px(50), JDataUtils.dp2Px(50));

            if (!JDataUtils.isEmpty(firstname)) {
                username = username + firstname;
            }
            if (!JDataUtils.isEmpty(lastname)) {
                if (!JDataUtils.isEmpty(username)) {
                    username = username + " " + lastname;
                } else {
                    username = username + lastname;
                }
            }
            if (hassingin_textview != null) {
                hassingin_textview.setText(username);
//                signin_ImageView.setVisibility(View.GONE);
                signin_textview.setVisibility(View.GONE);
//                ccivUserAvater.setVisibility(View.VISIBLE);
                hassingin_textview.setVisibility(View.VISIBLE);
            }
        } else {
            if (hassingin_textview != null) {
//                ccivUserAvater.setVisibility(View.GONE);
                hassingin_textview.setVisibility(View.GONE);
                signin_ImageView.setVisibility(View.VISIBLE);
                signin_textview.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateNotificationCount() {
       String  sessionKey="";
        if (WhiteLabelApplication.getAppConfiguration().getUser() != null && WhiteLabelApplication.getAppConfiguration() != null) {
            sessionKey = WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey();
        }
        mDao.getNotificationDetailCount(sessionKey, WhiteLabelApplication.getPhoneConfiguration().getRegistrationToken());

//        final String SESSION_EXPIRED = "session expired,login again please";
//        SVRParameters parameters = new SVRParameters();
//        parameters.put("session_key", WhiteLabelApplication.getAppConfiguration().getUser() == null ? null : WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey());
//        parameters.put("device_token", WhiteLabelApplication.getPhoneConfiguration().getRegistrationToken());
//        //parameters.put("device_token", "dXHj4TRWf20:APA91bGJ-7eqohReIDBYu4GPy9FomqMsBbTYanmDjAFhdltrq6KDX9z2QcmDU8V0qU4PrsORk3J8j3c0LWmRwTysCOXV1xfGNTtY04QmMF6Xae8-J-SRPIvQ6d0mLu8aI31W6HjSOU7d");
//
//        final SVRNotificationCount notificationCountHandler = new SVRNotificationCount(getActivity(), parameters);
//        notificationCountHandler.loadDatasFromServer(new SVRCallback() {
//            @Override
//            public void onSuccess(int resultCode, SVRReturnEntity result) {
//                NotificationCountReturnEntity notificationCountReturnEntity = (NotificationCountReturnEntity) result;
//                if (notificationCountReturnEntity.getStatus() == 1) {
//                    //update count
//                    if (notificationCountReturnEntity.getCount() > 0) {
//                        tvNotificationNum.setText(String.valueOf(notificationCountReturnEntity.getCount()));
//                        tvNotificationNum.setVisibility(View.VISIBLE);
//                    } else {
//                        tvNotificationNum.setVisibility(View.INVISIBLE);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int resultCode, String errorMsg) {
//                if (!JDataUtils.errorMsgHandler(paymentStatusActivity, errorMsg)) {
//                    if ((!JDataUtils.isEmpty(errorMsg)) && (errorMsg.contains(SESSION_EXPIRED))) {
//                    } else {
//                    }
//                }
//            }
//        });
    }
    private static final class DataHandler extends Handler {
        private final WeakReference<SlingMenuPaymentFragment> mFragment;
        public DataHandler( SlingMenuPaymentFragment fragment) {
            mFragment = new WeakReference<SlingMenuPaymentFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            if(mFragment.get()==null){
                return;
            }
            switch (msg.what) {
                case NotificationDao.REQUEST_NOTIFICATION_COUNT:
                    if(msg.arg1==NotificationDao.RESPONSE_SUCCESS){
                        Integer integer= (Integer) msg.obj;
                        if (integer > 0) {
                            if(integer>99){
                                mFragment.get().tvNotificationNum.setText("99+");
                            }else {
                                mFragment.get().tvNotificationNum.setText(String.valueOf(integer));
                                mFragment.get().tvNotificationNum.setVisibility(View.VISIBLE);
                            }
                        } else {
                            mFragment.get().tvNotificationNum.setVisibility(View.INVISIBLE);
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        signin_ImageView.setOnClickListener(this);
        signin_textview.setOnClickListener(this);
//        ccivUserAvater.setOnClickListener(this);
        hassingin_textview.setOnClickListener(this);
        signin_home_RelativeLayout.setOnClickListener(this);
        signin_whishlist_RelativeLayout.setOnClickListener(this);
        signin_setting_RelativeLayout.setOnClickListener(this);
        signin_helpcenter_RelativeLayout.setOnClickListener(this);
        signin_notification_RelativeLayout.setOnClickListener(this);
        signin_orders_RelativeLayout.setOnClickListener(this);
        signin_address_RelativeLayout.setOnClickListener(this);
        signin_customerService_RelativeLayout.setOnClickListener(this);
        signin_shippingReturns_RelativeLayout.setOnClickListener(this);
        dataHandler=new DataHandler(this);
        mDao=new NotificationDao("NotificationReceiver",dataHandler);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            paymentStatusActivity = (CheckoutPaymentStatusActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        paymentStatusActivity = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
