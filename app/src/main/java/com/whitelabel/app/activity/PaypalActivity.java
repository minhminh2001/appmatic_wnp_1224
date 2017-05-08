package com.whitelabel.app.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ShippingAddress;
import com.whitelabel.app.R;
import org.json.JSONException;
import java.math.BigDecimal;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class PaypalActivity extends AppCompatActivity {
    @BindView(R.id.btn_buy)
    Button btnBuy;
    @BindView(R.id.activity_paypal)
    RelativeLayout activityPaypal;
    private static final String TAG = "paymentExample";
    private  final static  int REQUEST_CODE_PAYMENT=10000;
    private static  final String CONFIG_CLIENT_ID="AcMKriYdTV8vj8wCCpgcQfuOFHdeSxJO7KAYGLnZVgq_HP4zWTW99ZF2Gd7cA37uI9ozO1PA5oSEZHie";
    public static PayPalConfiguration  payPalConfiguration=new
            PayPalConfiguration().
            environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(CONFIG_CLIENT_ID);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);
        ButterKnife.bind(this);
        Intent intent=new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        startService(intent);
    }
    @OnClick(R.id.btn_buy)
    public void onClick() {
        buyOneThing();
    }

    public void buyOneThing(){
        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal("0.01"),"USD","item",PayPalPayment.PAYMENT_INTENT_SALE);
        ShippingAddress  shippingAddress=null;
        payPalPayment.providedShippingAddress(shippingAddress);
        shippingAddress.city("qingdao").countryCode("HK").postalCode("").state("").line1("");
        payPalPayment.invoiceNumber("ordernumber");
        Intent intent=new Intent(PaypalActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }

                }
            }
        }}}
