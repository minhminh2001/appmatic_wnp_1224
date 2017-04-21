package com.whitelabel.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;

/**
 * Created by ray on 2017/4/21.
 */

public class PaypalHelper{
    private  static String  CONFIG_CLIENT_ID="AbzokPpKiJUMsKciCC8I6n2tAqPjvdByz_WVbgQt_lyL5yD3v8HVCwctXmxyysPGGx6flc84eDcyinOg";

    private  static PayPalConfiguration payPalConfiguration=new
            PayPalConfiguration().
            environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(CONFIG_CLIENT_ID);
    public PaypalHelper(){
        CONFIG_CLIENT_ID="AbzokPpKiJUMsKciCC8I6n2tAqPjvdByz_WVbgQt_lyL5yD3v8HVCwctXmxyysPGGx6flc84eDcyinOg";
    }




    public void startPaypalService(Context context){
        Intent intent=new Intent(context,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        context.startService(intent);
    }


    public static  final   int REQUEST_CODE_PAYMENT=10000;

    public void startPaypalPayment(Activity activity,String price, String unit, String productName, String orderNumber){
        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(price),unit,productName,PayPalPayment.PAYMENT_INTENT_SALE);
        payPalPayment.invoiceNumber(orderNumber);
//        payPalPayment.softDescriptor(productName+", Shipping Fee " + shippingFee);
//        ShippingAddress shippingAddress=null;
//        payPalPayment.providedShippingAddress(shippingAddress);
//        shippingAddress.city("qingdao").countryCode("HK").postalCode("").state("").line1("");
        Intent intent=new Intent(activity, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        activity.startActivityForResult(intent, REQUEST_CODE_PAYMENT);

    }
}
