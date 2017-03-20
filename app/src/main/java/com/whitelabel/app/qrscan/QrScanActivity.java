package com.whitelabel.app.qrscan;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.google.zxing.ResultPoint;
import com.whitelabel.app.activity.CurationActivity;
import com.whitelabel.app.activity.ProductActivity;
import com.whitelabel.app.activity.ShoppingCartActivity1;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.model.SVRAppserviceLandingPagesDetailProductListItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceLandingPagesDetailReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductDetailResultPropertyReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductDetailResultReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductDetailReturnEntity;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductOptionEntity;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomDialog;
import com.whitelabel.app.widget.CustomTextView;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arman on 1/17/2017.
 */

public class QrScanActivity extends AppCompatActivity implements IQrScanView {

    private final int REQUESTCODE_LOGIN = 1000;
    private final int REQUESTCODE_ERROR = 1001;
    public static final int REQUESTCODE_SETTINGS = 1002;
    private static final String TAG = "QrScanActivity";
    private CustomCaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private IQrScanPresenter qrScanPresenter;
    private Dialog mDialog;
    private DataHandler mHandler;
    private ProductDao productDao;
    private ShoppingCarDao cartDao;
    private boolean isScanned = false;
    private String curationId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_qr_scan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.qr_title));
        }

        //set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.black000000));
        }

        mHandler = new DataHandler(this);
        productDao = new ProductDao(TAG, mHandler);
        cartDao = new ShoppingCarDao(TAG, mHandler);

        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);

        capture = new CustomCaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        qrScanPresenter = new QrScanPresenterImpl(this);

        barcodeScannerView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (!isScanned) {
                    showProgressBar();
                    qrScanPresenter.onQrScanDetected(result.getText());
                    isScanned = true;
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
        isScanned = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideProgressBar();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        if (productDao != null) {
            productDao.cancelHttpByTag(TAG);
        }
        if (cartDao != null) {
            cartDao.cancelHttpByTag(TAG);
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cancel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.enter_top_bottom, R.anim.exit_top_bottom);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_ERROR) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        } else if (requestCode == REQUESTCODE_SETTINGS) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                capture.dismissDialog();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void showProgressBar() {
        if (mDialog == null) {
            mDialog = new CustomDialog(this);
        }
        mDialog.show();
    }

    @Override
    public void hideProgressBar() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void showErrorLayout(ErrorType errorType, @Nullable Message message) {
        Intent intent = new Intent(this, QrScanErrorActivity.class);
        intent.putExtra("errorType", errorType);
        if (message != null) {
            intent.putExtra("errorMessage", message.arg2);
        }
        startActivityForResult(intent, REQUESTCODE_ERROR);
        overridePendingTransition(R.anim.activity_anim1_enter1, R.anim.activity_anim1_exit1);
    }

    @Override
    public void getProductDetails(String id) {
        String sessionKey = "";
        if (GemfiveApplication.getAppConfiguration().isSignIn(this)) {
            sessionKey = GemfiveApplication.getAppConfiguration().getUserInfo(this).getSessionKey();
        }
        showProgressBar();
        productDao.getProductDetail(id, sessionKey);
    }

    @Override
    public void getCurationDetails(String id) {
        curationId = id;
        String sessionKey = "";
        if (GemfiveApplication.getAppConfiguration().isSignIn(this)) {
            sessionKey = GemfiveApplication.getAppConfiguration().getUserInfo(this).getSessionKey();
        }
        showProgressBar();
        productDao.getCurationDetail("", "", "", "", "", curationId, "0", "2", sessionKey);
    }

    private static class DataHandler extends Handler {
        private final WeakReference<QrScanActivity> mActivity;

        public DataHandler(QrScanActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            switch (msg.what) {
                case ProductDao.REQUEST_PRODUCTDETAIL:
                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
                        SVRAppserviceProductDetailReturnEntity productentity = (SVRAppserviceProductDetailReturnEntity) msg.obj;
                        SVRAppserviceProductDetailResultReturnEntity bean = productentity.getResult();

                        if (bean == null || bean.getId() == null) {
                            mActivity.get().hideProgressBar();
                            mActivity.get().showErrorLayout(ErrorType.Qr, null);
                        } else if (bean.getCanViewPdp().equals("1")) {
                            if (bean.getType().equalsIgnoreCase(SVRAppserviceProductDetailResultReturnEntity.TYPE_SIMPLE) && bean.getStockQty() > 0) {
                                mActivity.get().addToCartSendRequest(bean);
                            } else {
                                //TODO we have the bean, we should just pass it on instead of refetching the same data in the acitivty
                                mActivity.get().goToProduct(bean.getId());
                            }
                        } else if (bean.getAvailability().equals("0") && bean.getVisibility().equals("1")) {
                            mActivity.get().goToProduct(bean.getId());
                        } else {
                            mActivity.get().hideProgressBar();
                            mActivity.get().showErrorLayout(ErrorType.Qr, null);
                        }
                    } else {
                        mActivity.get().hideProgressBar();
                        mActivity.get().showErrorLayout(ErrorType.Qr, null);
                    }
                    break;
                case ShoppingCarDao.REQUEST_ADDPRODUCT:
                    mActivity.get().hideProgressBar();
                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        mActivity.get().showToast(mActivity.get(), 1);
                        mActivity.get().goToCart();
                    } else {
                        String errorStr = (String) msg.obj;
                        if (!JToolUtils.expireHandler(mActivity.get(), errorStr, mActivity.get().REQUESTCODE_LOGIN)) {
                            Toast.makeText(mActivity.get(), errorStr + "", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case ProductDao.REQUEST_CURATIONDETAIL:
                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
                        SVRAppserviceLandingPagesDetailReturnEntity curationReturnEntity = (SVRAppserviceLandingPagesDetailReturnEntity) msg.obj;
                        ArrayList<SVRAppserviceLandingPagesDetailProductListItemReturnEntity> products = curationReturnEntity.getProductList();
                        if (!products.isEmpty()) {
                            mActivity.get().goToCuration(mActivity.get().curationId, curationReturnEntity.getTitle());
                        } else {
                            mActivity.get().hideProgressBar();
                            mActivity.get().showErrorLayout(ErrorType.Qr, null);
                        }
                    } else {
                        mActivity.get().hideProgressBar();
                        mActivity.get().showErrorLayout(ErrorType.Qr, null);
                    }
                    break;
                case ProductDao.REQUEST_ERROR:
                    mActivity.get().hideProgressBar();
                    mActivity.get().showErrorLayout(ErrorType.Connection, msg);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void addToCartSendRequest(SVRAppserviceProductDetailResultReturnEntity bean) {
        String productId = bean.getId();
        int addCount = 1;
        List<SVRAppserviceProductDetailResultPropertyReturnEntity> propertyReturnEntities = new ArrayList<>();
        if (GemfiveApplication.getAppConfiguration().isSignIn(this)) {
            cartDao.addProductToShoppingCart(GemfiveApplication.getAppConfiguration().getUserInfo(this).getSessionKey(), productId, addCount + "", propertyReturnEntities);
        } else {
            long localProductCount = JStorageUtils.getProductCountByAttribute(this, productId, propertyReturnEntities);
            if (bean.getMaxSaleQty() > 0) {
                if (bean.getMaxSaleQty() <= bean.getStockQty()) {
                    if ((localProductCount + addCount) > bean.getMaxSaleQty()) {
                        if (mDialog != null) {
                            mDialog.dismiss();
                        }
                        String message = getResources().getString(R.string.over_maxSale);
                        message = message.replace("x", bean.getMaxSaleQty() + "");
                        JViewUtils.showSingleToast(this, message);
                        isScanned = false;
                        return;
                    }
                }
            }
            if ((localProductCount + addCount) > bean.getStockQty()) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                JViewUtils.showToast(this, "", getResources().getString(R.string.insufficient_stock));
                isScanned = false;
                return;
            }
            SharedPreferences shared = getSharedPreferences("productInfo", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = shared.edit();
            editor2.putString("product_id", productId);
            editor2.putString("qty", "" + addCount);
            editor2.commit();
            if (bean != null && !JDataUtils.isEmpty(bean.getId())) {
                ArrayList<TMPLocalCartRepositoryProductOptionEntity> options = new ArrayList<>();
                // Color
                for (int i = 0; i < propertyReturnEntities.size(); i++) {
                    TMPLocalCartRepositoryProductOptionEntity option = new TMPLocalCartRepositoryProductOptionEntity();
                    option.setId(propertyReturnEntities.get(i).getId());
                    option.setSuperAttribute(propertyReturnEntities.get(i).getSuperAttribute());
                    option.setLabel(propertyReturnEntities.get(i).getLabel());
                    options.add(option);
                }

                TMPLocalCartRepositoryProductEntity cartRepositoryProductEntity = new TMPLocalCartRepositoryProductEntity();
                cartRepositoryProductEntity.setProductId(bean.getId());
                if (bean.getImages() != null && bean.getImages().size() > 0) {
                    cartRepositoryProductEntity.setImage(bean.getImages().get(0));
                }

                cartRepositoryProductEntity.setName(bean.getName());
                cartRepositoryProductEntity.setBrand(bean.getBrand());
                cartRepositoryProductEntity.setCategory(bean.getCategory());
                cartRepositoryProductEntity.setQty(bean.getStockQty());
                cartRepositoryProductEntity.setMaxSaleQty(bean.getMaxSaleQty() + "");
                cartRepositoryProductEntity.setType(bean.getType());
                cartRepositoryProductEntity.setInStock(bean.getInStock());
                cartRepositoryProductEntity.setPrice("" + bean.getPrice());
                cartRepositoryProductEntity.setFinalPrice("" + bean.getFinalPrice());
                cartRepositoryProductEntity.setOptions(options);
                cartRepositoryProductEntity.setSelectedQty(addCount);
                cartRepositoryProductEntity.setCanViewPdp(bean.getCanViewPdp());
                cartRepositoryProductEntity.setVisibility(bean.getVisibility());
                cartRepositoryProductEntity.setAvailability(bean.getAvailability());
                cartRepositoryProductEntity.setVendorDisplayName(bean.getVendorDisplayName());
                cartRepositoryProductEntity.setVendor_id(bean.getVendor_id());

                JStorageUtils.addProductToLocalCartRepository(this, cartRepositoryProductEntity);
            }
            hideProgressBar();
            showToast(this, 1);
            goToCart();
        }
    }

    private Toast mAddToCartToast;

    //TODO: remove to utils
    private void showToast(Context context, int type) {
        if (context == null) {
            return;
        }
        LinearLayout toastView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_prompt_productdetail_addtocart, null);
        ImageView ivPrompt = (ImageView) toastView.findViewById(R.id.ivPrompt);
        CustomTextView ctvPrompt = (CustomTextView) toastView.findViewById(R.id.ctvPrompt);
        if (1 == type) {
            ivPrompt.setImageDrawable(JImageUtils.getDrawable(this, R.mipmap.success));
            ctvPrompt.setText(R.string.product_detail_prompy_addtocart_success);
        } else if (2 == type) {
            ivPrompt.setImageDrawable(JImageUtils.getDrawable(this, R.mipmap.success));
            ctvPrompt.setText(R.string.added_to_wishlist);
        } else if (3 == type) {
            ivPrompt.setImageDrawable(JImageUtils.getDrawable(this, R.mipmap.error));
            ctvPrompt.setText(R.string.added_error);
        }
        if (mAddToCartToast == null) {
            mAddToCartToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
            if (GemfiveApplication.getPhoneConfiguration() != null && GemfiveApplication.getPhoneConfiguration().getScreenHeigth() != 0) {
                mAddToCartToast.setGravity(Gravity.BOTTOM, 0, (int) (GemfiveApplication.getPhoneConfiguration().getScreenHeigth() * 0.25));
            }
            mAddToCartToast.setView(toastView);
        } else {
            if (GemfiveApplication.getPhoneConfiguration() != null && GemfiveApplication.getPhoneConfiguration().getScreenHeigth() != 0) {
                mAddToCartToast.setGravity(Gravity.BOTTOM, 0, (int) (GemfiveApplication.getPhoneConfiguration().getScreenHeigth() * 0.25));
            }
            mAddToCartToast.setView(toastView);
        }
        mAddToCartToast.show();
    }

    private void goToProduct(String id) {
        Intent intent = new Intent(this, ProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("productId", id);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_anim1_enter1, R.anim.activity_anim1_exit1);
        finish();
    }

    private void goToCart() {
        Intent intent = new Intent(this, ShoppingCartActivity1.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_anim1_enter1, R.anim.activity_anim1_exit1);
        finish();
    }

    private void goToCuration(String id, String title) {
        Intent intent = new Intent(this, CurationActivity.class);
        intent.putExtra(CurationActivity.EXTRA_CURATION_ID, id);
        intent.putExtra(CurationActivity.EXTRA_CURATION_TITLE, title);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_anim1_enter1, R.anim.activity_anim1_exit1);
        finish();
    }
}
