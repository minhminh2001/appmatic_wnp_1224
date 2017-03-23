package com.whitelabel.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.CheckoutActivity;
import com.whitelabel.app.activity.ProductActivity;
import com.whitelabel.app.adapter.CheckoutReviewShoppingCartAdapter;
import com.whitelabel.app.model.CheckoutPaymentReturnShippingAddress;
import com.whitelabel.app.model.CheckoutPaymentSaveReturnEntity;
import com.whitelabel.app.model.SVRAppserviceSaveBillingEntity;
import com.whitelabel.app.model.ShoppingCarStoreCreditBean;
import com.whitelabel.app.model.ShoppingCartListEntityCell;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.FileUtils;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.widget.CustomWebView;

import java.util.ArrayList;

public class CheckoutReviewFragment extends BaseFragment {
    private String currTag = "CheckoutReviewFragment";
    private CheckoutActivity checkoutActivity;
    private ListView lvShoppingCart;
    private TextView tvSubtotal, tvVoucher, tvVoucherTitle;
    public TextView tvGrandTotal, tvShippingfee;
    public CustomWebView tvCreditCartTitleOnly;
    private RelativeLayout rlVoucherText;
    public TextView tvErrorMsg;
    private CheckoutReviewShoppingCartAdapter checkoutReviewShoppingCartAdapter;
    private LinearLayout llAddress;
    private CheckoutPaymentSaveReturnEntity paymentSaveReturnEntity;
    public CheckoutPaymentReturnShippingAddress address;
    private TextView tvWord;
    /**
     * ...
     * <p>
     * <p>
     * .
     * If it is null,means credit card payment,
     * else ,means molpay_type
     */
    public String payment_type;

    /**
     * used as param in molpay page
     */
    public String productName;
    private String code;
    private ImageLoader mImageLoader;
    private TextView mTvInstruction;
    private TextView mGst;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            checkoutActivity = (CheckoutActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View rlStoreCredit;
    private TextView tvStoreCreditTitle;
    private TextView tvStoreCreditValue;
    private View llRoot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_review, container, false);
        mImageLoader = new ImageLoader(checkoutActivity);
        tvCreditCartTitleOnly = (CustomWebView) view.findViewById(R.id.tv_checkout_review_pay_onlyCreditCard_title);
//        tvCardType = (TextView) view.findViewById(R.id.tv_checkout_review_pay_cardtype);
//        tvCardNumber = (TextView) view.findViewById(R.id.tv_checkout_review_pay_cardnubmer);
//        tvCardBank = (TextView) view.findViewById(R.id.tv_checkout_review_pay_cardBank);
        tvWord = (TextView) view.findViewById(R.id.tv_word);
        llRoot = view.findViewById(R.id.ll_root);
        tvSubtotal = (TextView) view.findViewById(R.id.tv_checkout_review_subtotal);
        tvShippingfee = (TextView) view.findViewById(R.id.tv_checkout_review_shippingfee);
        rlVoucherText = (RelativeLayout) view.findViewById(R.id.ll_checkout_review_voucher_text);
        tvVoucher = (TextView) view.findViewById(R.id.tv_checkout_review_voucher);
        tvVoucherTitle = (TextView) view.findViewById(R.id.tv_checkout_review_voucher_title);
        tvGrandTotal = (TextView) view.findViewById(R.id.tv_checkout_review_grandtotal);
        mTvInstruction = (TextView) view.findViewById(R.id.tv_checkout_review_instruction);
        mGst = (TextView) view.findViewById(R.id.tv_checkout_review_gst);
        tvErrorMsg = (TextView) view.findViewById(R.id.tv_checkout_errormsg_review);
        rlStoreCredit = view.findViewById(R.id.ll_checkout_review_storeCredit_text);
        tvStoreCreditTitle = (TextView) view.findViewById(R.id.tv_checkout_review_storeCredi_title);
        tvStoreCreditValue = (TextView) view.findViewById(R.id.tv_checkout_review_storeCredit);
        //shoppingcart container
        lvShoppingCart = (ListView) view.findViewById(R.id.lv_checkout_review_shoppingcart);
        checkoutReviewShoppingCartAdapter = new CheckoutReviewShoppingCartAdapter(checkoutActivity, mImageLoader);
        lvShoppingCart.setAdapter(checkoutReviewShoppingCartAdapter);
        //address container
        llAddress = (LinearLayout) view.findViewById(R.id.ll_checkout_review_address);
        initData();
        tvWord.setFocusable(true);
        tvWord.setFocusableInTouchMode(true);
        tvWord.requestFocus();
//        AnimUtil.alpha_0_1_500(llRoot);
        return view;
    }

    public void initStoreCredit(ShoppingCarStoreCreditBean bean) {
        //ray修改
        if (bean == null) {
            rlStoreCredit.setVisibility(View.GONE);
        } else {
            rlStoreCredit.setVisibility(View.VISIBLE);
            tvStoreCreditTitle.setText(bean.getTitle());
            try {
                tvStoreCreditValue.setText("-RM " + JDataUtils.formatDouble(bean.getValue()));
            } catch (Exception ex) {
                ex.getMessage();
            }
        }
    }


    public void webViewFont(String str) {
        JLogUtils.i("WebViewFont", str);//service带有样式就会冲突
        String html = FileUtils.readAssest(checkoutActivity, "html/order_web.html");
//        html = html.replace("@fontName0", "LatoRegular");
//        html = html.replace("@fontPath0", "../fonts/Lato-Regular.ttf");// assets相对路径
        html = html.replace("@mytext", str);
        String baseurl = "file:///android_asset/html/";
        tvCreditCartTitleOnly.loadDataWithBaseURL(baseurl, html, "text/html", "UTF-8", null);
    }

    private void initData() {

        //Get datas from CheckoutActivity
/*<<<<<<< HEAD
        paymentSaveReturnEntity = (CheckoutPaymentSaveReturnEntity) getArguments().getSerializable("paymentSaveReturnEntity");

        //Set Datas to ShippingAddress and inflate llAddress with an address cell
        address = paymentSaveReturnEntity.getShippingAddress();
        View view = LayoutInflater.from(checkoutActivity).inflate(R.layout.fragment_checkout_shipping_selectaddress_cell_for_review,null);
        view.findViewById(R.id.image_address_select_top).setVisibility(View.GONE);
        view.findViewById(R.id.image_address_select_end).setVisibility(View.GONE);
        //view.findViewById(R.id.btn_address_select_cover).setVisibility(View.GONE);

        TextView tvFirstname = (TextView) view.findViewById(R.id.tv_address_select_firstname);
        //TextView tvLastname = (TextView) view.findViewById(R.id.tv_address_select_lastname);
        TextView tvAddress1 = (TextView) view.findViewById(R.id.tv_address_select_address1);
        TextView tvAddress2 = (TextView) view.findViewById(R.id.tv_address_select_address2);
        TextView tvCityStatePostcode = (TextView) view.findViewById(R.id.tv_address_select_citystatepostcode);
        TextView tvCountry = (TextView) view.findViewById(R.id.tv_address_select_country);
        TextView tvTelephone = (TextView) view.findViewById(R.id.tv_address_select_telephone);

        tvFirstname.setText(address.getFirstname() + " " + address.getLastname());
        //tvLastname.setText(address.getLastname());
        tvAddress1.setText(address.getStreet());
        tvAddress2.setVisibility(View.GONE);

        *//**
         * Constructor city,state,postcode
         *//*
        String cityStatePostcode = address.getCity() + ", ";
        if (!JDataUtils.isEmpty(address.getRegion()) && !address.getRegion().equalsIgnoreCase("null")) {

            cityStatePostcode += address.getRegion() + ", ";
        }
        cityStatePostcode += address.getPostcode();
        tvCityStatePostcode.setText(cityStatePostcode);
=======*/
        try {
            /**
             * Train of thought:
             * First to cast to CheckoutPaymentSaveReturnEntity, if failed, means should catch exception...
             */
            paymentSaveReturnEntity = (CheckoutPaymentSaveReturnEntity) getArguments().getSerializable("paymentSaveReturnEntity");
            if (!TextUtils.isEmpty(paymentSaveReturnEntity.getGst().trim())) {
                mGst.setText(paymentSaveReturnEntity.getGst());
            }else{
                mGst.setText("");
            }
            if (!TextUtils.isEmpty(paymentSaveReturnEntity.getOrders_notice().trim())) {
                mTvInstruction.setText(paymentSaveReturnEntity.getOrders_notice());
            }else{
                mTvInstruction.setText("");
            }
            //Set Datas to ShippingAddress and inflate llAddress with an address cell
            address = paymentSaveReturnEntity.getShippingAddress();
            View view = LayoutInflater.from(checkoutActivity).inflate(R.layout.fragment_checkout_shipping_selectaddress_cell_for_review, null);
            view.findViewById(R.id.image_address_select_top).setVisibility(View.GONE);
            view.findViewById(R.id.image_address_select_end).setVisibility(View.GONE);
            //view.findViewById(R.id.btn_address_select_cover).setVisibility(View.GONE);

            TextView tvFirstname = (TextView) view.findViewById(R.id.tv_address_select_firstname);
            //TextView tvLastname = (TextView) view.findViewById(R.id.tv_address_select_lastname);
            TextView tvAddress1 = (TextView) view.findViewById(R.id.tv_address_select_address1);
            TextView tvAddress2 = (TextView) view.findViewById(R.id.tv_address_select_address2);
            TextView tvCityStatePostcode = (TextView) view.findViewById(R.id.tv_address_select_citystatepostcode);
            TextView tvCountry = (TextView) view.findViewById(R.id.tv_address_select_country);
            TextView tvTelephone = (TextView) view.findViewById(R.id.tv_address_select_telephone);

            tvFirstname.setText(address.getFirstname() + " " + address.getLastname());
            //tvLastname.setText(address.getLastname());
            tvAddress1.setText(address.getStreet());
            tvAddress2.setVisibility(View.GONE);
            //initstoreCredit
            initStoreCredit(paymentSaveReturnEntity.getStoreCredit());


            /**
             * Constructor city,state,postcode
             */
            String cityStatePostcode = address.getCity() + ", ";
            if (!JDataUtils.isEmpty(address.getRegion()) && !address.getRegion().equalsIgnoreCase("null")) {

                cityStatePostcode += address.getRegion() + ", ";
            }
            cityStatePostcode += address.getPostcode();
            tvCityStatePostcode.setText(cityStatePostcode);

            tvCountry.setText(address.getCountry());
            tvTelephone.setText(getResources().getString(R.string.t) + address.getTelephone());

            llAddress.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            //Set Datas to Payment Method
            payment_type = getArguments().getString("payment[molpay_type]");
            code = getArguments().getString("code");

            String paymentInfo = paymentSaveReturnEntity.getPaymentinfo();
            if (!TextUtils.isEmpty(paymentInfo)) {
                //   JToolUtils.setWebViewText(checkoutActivity, paymentInfo, tvCreditCartTitleOnly);
                webViewFont(paymentInfo);
            }


//
//            if(!TextUtils.isEmpty(checkoutActivity.paymentMethodCode)) {
//                tvCardType.setText(paymentInfo.get("title"));
//                String content=paymentInfo.get("instructions");
//                if(!TextUtils.isEmpty(content)){
//                    tvCardBank.setText(content);
//                    tvCardBank.setVisibility(View.VISIBLE);
//                }else {
//                    tvCardBank.setVisibility(View.INVISIBLE);
//                }
//                tvCardNumber.setVisibility(View.GONE);
//            } else if (payment_type != null) {
//                /**
//                 * Online Banking
//                 */
//                tvCardType.setText(getResources().getString(R.string.payment_method_ONLINE_BANKING));
//                tvCardBank.setText(paymentInfo.get("bank"));
//                tvCardNumber.setVisibility(View.GONE);
//            } else {
//                tvCreditCartTitleOnly.setVisibility(View.VISIBLE);
//                tvCardType.setText(paymentInfo.get("Credit Card Type"));
//                tvCardNumber.setText(paymentInfo.get("Credit Card Number"));
//                tvCardBank.setText(paymentInfo.get("Issuer Bank"));
//            }

            final ArrayList<ShoppingCartListEntityCell> list = paymentSaveReturnEntity.getReviewOrder();
            //Set Datas to ShoppingCartCell
            checkoutReviewShoppingCartAdapter.list = list;
            productName = paymentSaveReturnEntity.getReviewOrder().get(0).getName();
            checkoutReviewShoppingCartAdapter.notifyDataSetChanged();

            // click not jump
//            lvShoppingCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent it = new Intent(getActivity(), ProductActivity.class);
//                    it.putExtra("productId", list.get(position).getProductId());
//                    checkoutActivity.startActivity(it);
//                }
//            });

            //Set Datas to last four RM number
            tvSubtotal.setText(paymentSaveReturnEntity.getSubtotal());
            tvShippingfee.setText(paymentSaveReturnEntity.getShipping() == null ? "RM 0.00" : paymentSaveReturnEntity.getShipping().get("value"));
            //Voucher or Discount
            if (null != paymentSaveReturnEntity.getDiscount() && paymentSaveReturnEntity.getDiscount().size() > 0) {
                if (JDataUtils.isEmpty(paymentSaveReturnEntity.getDiscount().get("title"))) {
                    tvVoucherTitle.setText(getResources().getString(R.string.Discount));
                } else {
                    tvVoucherTitle.setText(paymentSaveReturnEntity.getDiscount().get("title"));
                }
                tvVoucher.setText(paymentSaveReturnEntity.getDiscount().get("value"));
                rlVoucherText.setVisibility(View.VISIBLE);
            }
            tvGrandTotal.setText(" " + paymentSaveReturnEntity.getGrandtotal());

        } catch (Exception e) {
            /**
             * Train of thought:
             * if throughs exception, means grand total is 0, should cast to SVRAppserviceSaveBillingEntity.
             */
            try {
                SVRAppserviceSaveBillingEntity paymentSaveReturnEntity = (SVRAppserviceSaveBillingEntity) getArguments().getSerializable("paymentSaveReturnEntity");
                if (paymentSaveReturnEntity.getGst() != null) {
                    mGst.setText(paymentSaveReturnEntity.getGst());
                }
                if (paymentSaveReturnEntity.getOrders_notice() != null) {
                    mTvInstruction.setText(paymentSaveReturnEntity.getOrders_notice());
                }
                //Set Datas to ShippingAddress and inflate llAddress with an address cell
                address = paymentSaveReturnEntity.getShippingAddress();
                View view = LayoutInflater.from(checkoutActivity).inflate(R.layout.fragment_checkout_shipping_selectaddress_cell_for_review, null);
                view.findViewById(R.id.image_address_select_top).setVisibility(View.GONE);
                view.findViewById(R.id.image_address_select_end).setVisibility(View.GONE);
                //view.findViewById(R.id.btn_address_select_cover).setVisibility(View.GONE);

                TextView tvFirstname = (TextView) view.findViewById(R.id.tv_address_select_firstname);
                //TextView tvLastname = (TextView) view.findViewById(R.id.tv_address_select_lastname);
                TextView tvAddress1 = (TextView) view.findViewById(R.id.tv_address_select_address1);
                TextView tvAddress2 = (TextView) view.findViewById(R.id.tv_address_select_address2);
                TextView tvCityStatePostcode = (TextView) view.findViewById(R.id.tv_address_select_citystatepostcode);
                TextView tvCountry = (TextView) view.findViewById(R.id.tv_address_select_country);
                TextView tvTelephone = (TextView) view.findViewById(R.id.tv_address_select_telephone);

                tvFirstname.setText(address.getFirstname() + " " + address.getLastname());
                //tvLastname.setText(address.getLastname());
                tvAddress1.setText(address.getStreet());
                tvAddress2.setVisibility(View.GONE);

                /**
                 * Constructor city,state,postcode
                 */
                String cityStatePostcode = address.getCity() + ", ";
                if (!JDataUtils.isEmpty(address.getRegion()) && !address.getRegion().equalsIgnoreCase("null")) {

                    cityStatePostcode += address.getRegion() + ", ";
                }
                cityStatePostcode += address.getPostcode();
                tvCityStatePostcode.setText(cityStatePostcode);

                tvCountry.setText(address.getCountry());
                tvTelephone.setText(getResources().getString(R.string.t) + address.getTelephone());

                llAddress.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                //Set Datas to Payment Method
                payment_type = getArguments().getString("payment[molpay_type]");

//                tvCardNumber.setVisibility(View.GONE);
//                tvCardType.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(paymentSaveReturnEntity.getPaymentinfo())) {
                    JToolUtils.setWebViewText(checkoutActivity, paymentSaveReturnEntity.getPaymentinfo(), tvCreditCartTitleOnly);
                }

                final ArrayList<ShoppingCartListEntityCell> list = paymentSaveReturnEntity.getReviewOrder();
                //Set Datas to ShoppingCartCell
                checkoutReviewShoppingCartAdapter.list = list;
                productName = paymentSaveReturnEntity.getReviewOrder().get(0).getName();
                checkoutReviewShoppingCartAdapter.notifyDataSetChanged();

                lvShoppingCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent it = new Intent(getActivity(), ProductActivity.class);
                        it.putExtra("productId", list.get(position).getProductId());
                        checkoutActivity.startActivity(it);
                    }
                });

                //Set Datas to last four RM number
                tvSubtotal.setText(paymentSaveReturnEntity.getSubtotal());
                tvShippingfee.setText(paymentSaveReturnEntity.getShipping() == null ? "RM 0.00" : paymentSaveReturnEntity.getShipping().get("value"));
                //Voucher or Discount
                if (null != paymentSaveReturnEntity.getDiscount() && paymentSaveReturnEntity.getDiscount().size() > 0) {
                    if (JDataUtils.isEmpty(paymentSaveReturnEntity.getDiscount().get("title"))) {
                        tvVoucherTitle.setText(getResources().getString(R.string.Discount));
                    } else {
                        tvVoucherTitle.setText(paymentSaveReturnEntity.getDiscount().get("title"));
                    }
                    tvVoucher.setText(paymentSaveReturnEntity.getDiscount().get("value"));
                    rlVoucherText.setVisibility(View.VISIBLE);
                }
                tvGrandTotal.setText(paymentSaveReturnEntity.getGrandtotal());

                initStoreCredit(paymentSaveReturnEntity.getStoreCredit());
            } catch (Exception e_inner) {
                e_inner.printStackTrace();
                JLogUtils.e("revieworder", e_inner.getMessage());
            }

        }


        //To fix measurement on page
        int totalHeight = 0;
        if (getActivity() != null) {
            totalHeight = JDataUtils.dp2px(getActivity(), 20);
        }
        for (int i = 0; i < checkoutReviewShoppingCartAdapter.getCount(); i++) {
            View listItem = checkoutReviewShoppingCartAdapter.getView(i, null, lvShoppingCart);
            listItem.measure(0, 0);
            JLogUtils.i(currTag, "height : " + listItem.getMeasuredHeight() + ", heigh" + JDataUtils.dp2px(getActivity(), 130));
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lvShoppingCart.getLayoutParams();
        params.height = totalHeight + (lvShoppingCart.getDividerHeight() * (checkoutReviewShoppingCartAdapter.getCount() - 1));
        //((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        lvShoppingCart.setLayoutParams(params);

        try {
            String shippingState = address.getRegion();
            GaTrackHelper.getInstance().googleAnalyticsEvent("Checkout Action",
                    "Place Order",
                    shippingState,
                    null);
            JLogUtils.i("googleGA", "checkout shipping  点击 continue" + "shippingState=" + shippingState);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (checkoutActivity.mGATrackPaymentToReviewTimeEnable) {
//            GaTrackHelper.getInstance().googleAnalyticsTimeStop(
//                    GaTrackHelper.GA_TIME_CATEGORY_CHECKOUT,
//                    checkoutActivity.mGATrackPaymentToReviewTimeStart,
//                    "Checkout - Review Order"
//            );
//            checkoutActivity.mGATrackPaymentToReviewTimeEnable = false;
//        }
    }

    /**
     * This method will be called when it'fragment is delete from activity
     * for example activity remove or replace fragment
     */
    @Override
    public void onDetach() {
        super.onDetach();
        //Toast.makeText(checkoutActivity,"review fragment is over",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {

    }

    public CheckoutPaymentSaveReturnEntity getPaymentSaveReturnEntity() {
        return paymentSaveReturnEntity;
    }
}
