package com.whitelabel.app.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.GlobalData;
import com.whitelabel.app.R;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.callback.CustomFocusChangeCallBack;
import com.whitelabel.app.callback.GlobalCallBack;
import com.whitelabel.app.callback.MyResultCallback;
import com.whitelabel.app.callback.WheelPickerCallback;
import com.whitelabel.app.dao.OtherDao;
import com.whitelabel.app.model.BanktransferBean;
import com.whitelabel.app.model.KeyValueBean;
import com.whitelabel.app.model.WheelPickerConfigEntity;
import com.whitelabel.app.model.WheelPickerEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.network.OkHttpClientManager;
import com.whitelabel.app.utils.AnimUtil;
import com.whitelabel.app.utils.FileUtils;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomButtomLineRelativeLayout;
import com.whitelabel.app.widget.CustomEdit;
import com.whitelabel.app.widget.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by ray on 2015/10/19.
 */

@RuntimePermissions
public class BankTransaferActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    public static final int REQUESTCODE_STORAGE = 0;
    public static final int REQUESTCODE_CAMERA = 1;
    private TextView tvTrasferFrom;
    private TextView tvDate;
    private TextView tvDateHint;
    private TextView tvBankHint;
    private View bottomBlack;
    private LinearLayout llSubmit;
    private CustomEdit etEmailAddress, etCustomer, etTransferAmount, etOrderNum;
    private BanktransferBean mBean;
    private ImageView ivImg, iv_bank_from_arrow, iv_trans_date_arrow;
    private OtherDao mDao;
    private String TAG;
    private String mImagePath, url;
    private Dialog mDialog;
    private PopupWindow mSelectImgPop;
    private Bitmap mBitMap;
    public static final int RESULTCODE = 100;
    private String emailErrorString;
    private String imageSelectError;
    private String intentnetErrorString;
    private boolean isEdit;
    private ImageLoader mImageLoader;
    private MaterialDialog permissionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transafer);
        TAG = BankTransaferActivity.this.getClass().getSimpleName();
        initView();
        initData();
        initToolBar();
    }

    private void initToolBar() {
        setTitle(getResources().getString(R.string.bank_transafer_confirm));
        setLeftMenuIcon(JToolUtils.getDrawable(R.drawable.action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        DataHandler mHandler = new DataHandler(this);
        try {
            GaTrackHelper.getInstance().googleAnalytics("Submit Bank Transfer Screen", BankTransaferActivity.this);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        emailErrorString = getResources().getString(R.string.email_error);
        imageSelectError = getResources().getString(R.string.select_img);
        intentnetErrorString = getResources().getString(R.string.Global_Error_Internet);
        String amount = getResources().getString(R.string.transfer_amount);
        String date = getResources().getString(R.string.transfer_date);
        if (getIntent().getExtras() != null) {
            mBean = (BanktransferBean) getIntent().getExtras().getSerializable("bean");
        }
        etTransferAmount.getEditText().setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etTransferAmount.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        if (!TextUtils.isEmpty(mBean.getTransferId())) {//查看
            findViewById(R.id.ll_confirm).setVisibility(View.GONE);
            tvTrasferFrom.setTag(mBean.getBankFrom());
            etEmailAddress.setText(mBean.getEmail());

            tvBankHint.setVisibility(View.VISIBLE);
            tvBankHint.setText(amount);
            tvBankHint.setTextColor(getResources().getColor(R.color.label_saved));
            tvDateHint.setVisibility(View.VISIBLE);
            tvDateHint.setText(date);
            tvDateHint.setTextColor(getResources().getColor(R.color.label_saved));


            etOrderNum.setText(mBean.getOrderNo());
            etCustomer.setText(mBean.getTransferee());
            etTransferAmount.setText(mBean.getTransferred());
            tvDate.setText(mBean.getTransferDate());
            ivImg.setVisibility(View.VISIBLE);
            String bankFrom = "";
            for (int i = 0; i < mBean.getBankFromList().size(); i++) {
                if (mBean.getBankFrom().equals(mBean.getBankFromList().get(i).getValue())) {
                    bankFrom = mBean.getBankFromList().get(i).getLabel();
                }
            }
            tvTrasferFrom.setText(bankFrom);
            JImageUtils.downloadImageFromServerByUrl(BankTransaferActivity.this, mImageLoader, ivImg, mBean.getProofFile(), JDataUtils.dp2Px(100), JDataUtils.dp2Px(100));
            if (mBean.getCanSubmit() == 1) {
                isEdit = true;
                etOrderNum.setEnabled(false);
                llSubmit.setVisibility(View.VISIBLE);
                bottomBlack.setVisibility(View.VISIBLE);
            } else {
                tvTrasferFrom.setFocusable(true);
                tvTrasferFrom.setFocusableInTouchMode(true);
                tvTrasferFrom.requestFocus();
                llSubmit.setVisibility(View.GONE);
                bottomBlack.setVisibility(View.GONE);
                etCustomer.setEnabled(false);
                etEmailAddress.setEnabled(false);
                etTransferAmount.setEnabled(false);
            }
        } else {//提交
            if (mBean.getCanSubmit() == 1) {
                llSubmit.setVisibility(View.VISIBLE);
                bottomBlack.setVisibility(View.VISIBLE);
            } else {
                llSubmit.setVisibility(View.GONE);
                bottomBlack.setVisibility(View.GONE);
            }
            etEmailAddress.setText(WhiteLabelApplication.getAppConfiguration().getUser().getEmail());
            etOrderNum.setText(mBean.getOrderNo());
            etOrderNum.setEnabled(false);
            etCustomer.setText(WhiteLabelApplication.getAppConfiguration().getUser().getFirstName() + " " + WhiteLabelApplication.getAppConfiguration().getUser().getLastName());
        }

        mDao = new OtherDao(TAG, mHandler);
    }

    private String rankFrom, email, orderNumber, transferee, transferDate, transferred;

    private static class DataHandler extends Handler {
        private final WeakReference<BankTransaferActivity> mActivity;

        public DataHandler(BankTransaferActivity activity) {
            mActivity = new WeakReference<BankTransaferActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            mActivity.get().closeDialog();
            switch (msg.what) {
                case OtherDao.REQUEST_SAVEBANK:
                    if (msg.arg1 == OtherDao.RESPONSE_SUCCESS) {
                        KeyValueBean desc = (KeyValueBean) msg.obj;
                        String id = desc.getKey();
                        String cansubmit = desc.getLabel();
                        Intent intent = new Intent(mActivity.get(), BankTransaferSuccessActivity.class);
                        intent.putExtra("desc", desc.getValue());
                        mActivity.get().startActivity(intent);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("id", id);
                        resultIntent.putExtra("rankFrom", mActivity.get().rankFrom);
                        resultIntent.putExtra("email", mActivity.get().email);
                        resultIntent.putExtra("orderNumber", mActivity.get().orderNumber);
                        resultIntent.putExtra("transferee", mActivity.get().transferee);
                        try {
                            mActivity.get().transferDate = mActivity.get().tvDate.getText().toString();
                        } catch (Exception ex) {
                            ex.getStackTrace();
                        }
                        resultIntent.putExtra("transferDate", mActivity.get().transferDate);
                        resultIntent.putExtra("transferred", mActivity.get().transferred);
                        resultIntent.putExtra("url", mActivity.get().url);
                        resultIntent.putExtra("cansubmit", cansubmit);
                        mActivity.get().setResult(RESULTCODE, resultIntent);
                        mActivity.get().finish();
                    } else {
                        String msgStr = String.valueOf(msg.obj);
                        if (!JToolUtils.expireHandler(mActivity.get(), msgStr, 1000)) {
                            Toast.makeText(mActivity.get(), msgStr, Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case OtherDao.REQUEST_ERROR:
                    RequestErrorHelper requestErrorHelper = new RequestErrorHelper(mActivity.get());
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;

            }
            super.handleMessage(msg);
        }
    }

    private void closeDateDialog() {
        CustomButtomLineRelativeLayout.setBottomLineActive(view_backtransafer_date_line, false);
        AnimUtil.rotateArrow(iv_trans_date_arrow, false);
        if (tvDate.getText().length() != 0) {
            tvDateHint.setText(getResources().getString(R.string.order_detail_date));
            tvDateHint.setTextColor(getResources().getColor(R.color.label_saved));
            tvDateHint.setVisibility(View.VISIBLE);
        }
    }
    private static final int SELECT_PHOTO = 100;
    private static final int TAKE_PHOTO = 200;
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_bankFrom:
                JViewUtils.cleanCurrentViewFocus(BankTransaferActivity.this);
                if (!TextUtils.isEmpty(tvTrasferFrom.getText().toString())) {
                    tvBankHint.startAnimation(getHintAnimation(tvBankHint, getResources().getString(R.string.bank_from)));
                }
                tvTrasferFrom.setFocusable(true);
                tvTrasferFrom.requestFocus();
                showBankFrom();
                break;
            case R.id.tv_date:
                CustomButtomLineRelativeLayout.setBottomLineActive(view_backtransafer_date_line, true);
                AnimUtil.rotateArrow(iv_trans_date_arrow, true);
                JViewUtils.cleanCurrentViewFocus(BankTransaferActivity.this);
                String date = tvDate.getText().toString();
                Dialog dialog = JViewUtils.onCreateDateDialog(BankTransaferActivity.this, tvDate, date, new GlobalCallBack() {
                    @Override
                    public void callBack() {
                        closeDateDialog();
                    }
                }, new GlobalCallBack() {
                    @Override
                    public void callBack() {
                        closeDateDialog();

                    }
                });
                dialog.show();
                break;
            case R.id.btn_choose:
                JViewUtils.cleanCurrentViewFocus(BankTransaferActivity.this);
                showPopWindowSelectPhoto();
                break;
            case R.id.tv_confirm:
                JViewUtils.cleanCurrentViewFocus(BankTransaferActivity.this);
                if (checkData()) {
                    closeKeyBoard();
                    mDialog = JViewUtils.showProgressDialog(BankTransaferActivity.this);
                    if (!TextUtils.isEmpty(mImagePath)) {
                        File file = new File(mImagePath);
                        uploadFile(file);
                    } else {
                        saveBrankConfirm(mBean.getProofFile());
                    }
                }
                break;
            case R.id.album:
                //launchAlbum();
                BankTransaferActivityPermissionsDispatcher.launchAlbumWithCheck(this);
                break;
            case R.id.photograph:
                BankTransaferActivityPermissionsDispatcher.launchCameraWithCheck(this);
                break;
            case R.id.cancleButton:
                if (mSelectImgPop != null) {
                    mSelectImgPop.dismiss();
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUESTCODE_CAMERA || requestCode == REQUESTCODE_STORAGE) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                    if (!showRationale) {
                        if (Manifest.permission.CAMERA.equals(permission)) {
                            showCameraPermissionDialog();
                            return;
                        } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
                            showAlbumPermissionDialog();
                            return;
                        }
                    }
                }
            }
        }
        // NOTE: delegate the permission handling to generated method
        BankTransaferActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void launchAlbum() {
        if (mSelectImgPop != null) {
            mSelectImgPop.dismiss();
        }
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForAlbum(PermissionRequest request) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                REQUESTCODE_STORAGE);
    }

    private void showAlbumPermissionDialog() {
        permissionDialog = JViewUtils.showPermissionDialog(this, getString(R.string.permission_error_title), getString(R.string.permission_error_storage_content), REQUESTCODE_STORAGE, false);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void launchCamera() {
        if (mSelectImgPop != null) {
            mSelectImgPop.dismiss();
        }
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, TAKE_PHOTO);
        } catch (Exception e) {
            JLogUtils.e(TAG, e.getMessage());
        }
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(PermissionRequest request) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                REQUESTCODE_CAMERA);
    }
    private void showCameraPermissionDialog() {
        permissionDialog = JViewUtils.showPermissionDialog(this, getString(R.string.permission_error_title), getString(R.string.permission_error_camera_content), REQUESTCODE_CAMERA, false);
    }
    @Override
    protected void onDestroy() {
        mDao.cancelHttpByTag(TAG);
        super.onDestroy();
    }
    public void showPopWindowSelectPhoto() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupWindowView = inflater.inflate(R.layout.popup_window, null);
        mSelectImgPop = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mSelectImgPop.setBackgroundDrawable(new BitmapDrawable());
        //设置PopupWindow的弹出和消失效果
        mSelectImgPop.setAnimationStyle(R.style.popupAnimation);
        TextView album = (TextView) popupWindowView.findViewById(R.id.album);
        album.setOnClickListener(this);
        TextView photograph = (TextView) popupWindowView.findViewById(R.id.photograph);
        photograph.setOnClickListener(this);
        TextView cancleButton = (TextView) popupWindowView.findViewById(R.id.cancleButton);
        cancleButton.setOnClickListener(this);
        mSelectImgPop.showAtLocation(album, Gravity.CENTER, 0, 0);//以album为主容器,gravity为对齐参照点,定义偏移
    }
    public void saveBrankConfirm(String profFile) {
        url = profFile;
        rankFrom = String.valueOf(tvTrasferFrom.getTag());
        email = etEmailAddress.getText().toString();
        orderNumber = etOrderNum.getText().toString();
        transferee = etCustomer.getText().toString();
        transferred = etTransferAmount.getText().toString();
        transferDate = tvDate.getText().toString();
        String[] date = transferDate.split("-");
        if (date.length == 3) {
            transferDate = date[2] + "/" + date[1] + "/" + date[0];
        }
        String id = mBean.getTransferId();
        mDao.saveBankTransferConfirm(id, WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey(),
                rankFrom,
                email,
                orderNumber,
                transferee,
                transferred,
                transferDate,
                profFile);
    }
    public void closeKeyBoard() {
        JToolUtils.closeKeyBoard(BankTransaferActivity.this, etCustomer.getEditText());
        JToolUtils.closeKeyBoard(BankTransaferActivity.this, etOrderNum.getEditText());
        JToolUtils.closeKeyBoard(BankTransaferActivity.this, etTransferAmount.getEditText());
        JToolUtils.closeKeyBoard(BankTransaferActivity.this, etEmailAddress.getEditText());
    }

    public void showBankFrom() {
        CustomButtomLineRelativeLayout.setBottomLineActive(view_backtransafer_bankfrom_line, true);
        AnimUtil.rotateArrow(iv_bank_from_arrow, true);
        List<KeyValueBean> mBeans = mBean.getBankFromList();
        ArrayList<WheelPickerEntity> entitys = new ArrayList<>();
//        entitys.add(new WheelPickerEntity(pleaseStr, ""));
        String rankFrom = String.valueOf(tvTrasferFrom.getTag());
        int currIndex = 0;
        for (int i = 0; i < mBeans.size(); i++) {
            WheelPickerEntity entity = new WheelPickerEntity();
            if (rankFrom.equals(mBeans.get(i).getValue())) {
                currIndex = i;
            }
            entity.setDisplay(mBeans.get(i).getLabel());
            entity.setValue(mBeans.get(i).getValue());
            entitys.add(entity);
        }

        WheelPickerEntity oldEntity = new WheelPickerEntity();
        oldEntity.setIndex(currIndex);
        createStatueDialogPicker(entitys, oldEntity, tvTrasferFrom);
    }

    private void createStatueDialogPicker(ArrayList<WheelPickerEntity> list, WheelPickerEntity oldEntity, final TextView view) {

        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(list);
        configEntity.setOldValue(oldEntity);
        configEntity.setIndex(oldEntity.getIndex());
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                CustomButtomLineRelativeLayout.setBottomLineActive(view_backtransafer_bankfrom_line, false);
                AnimUtil.rotateArrow(iv_bank_from_arrow, false);
                if (TextUtils.isEmpty(tvTrasferFrom.getText().toString())) {
                    return;
                }
                tvBankHint.setText(getResources().getString(R.string.bank_from));
                tvBankHint.setTextColor(getResources().getColor(R.color.label_saved));
                tvBankHint.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                CustomButtomLineRelativeLayout.setBottomLineActive(view_backtransafer_bankfrom_line, false);
                AnimUtil.rotateArrow(iv_bank_from_arrow, false);
                tvBankHint.setText(getResources().getString(R.string.bank_from));
                tvBankHint.setTextColor(getResources().getColor(R.color.label_saved));
                if (newValue == null || TextUtils.isEmpty(newValue.getDisplay()) || TextUtils.isEmpty(newValue.getValue())) {
                    return;
                }
                tvTrasferFrom.setText(newValue.getDisplay());
                tvTrasferFrom.setTag(newValue.getValue());
                if (TextUtils.isEmpty(tvTrasferFrom.getText())) {
                    tvBankHint.setVisibility(View.INVISIBLE);
                } else {
                    tvBankHint.setVisibility(View.VISIBLE);
                }
            }
        });
        JViewUtils.showWheelPickerOneDialog(BankTransaferActivity.this, configEntity);
    }

    private Animation getHintAnimation(final TextView tv, final String hintText) {

        Animation animation = AnimationUtils.loadAnimation(BankTransaferActivity.this, R.anim.anim_checkout_hint);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv.setVisibility(View.VISIBLE);
                    }
                }, 100);

                tv.setText(hintText);
                tv.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        return animation;
    }

    public boolean checkData() {
        String tvBankFrom = tvTrasferFrom.getText().toString();
        if (TextUtils.isEmpty(tvBankFrom)) {
            tvBankHint.setVisibility(View.VISIBLE);
            tvBankHint.setText(getResources().getString(R.string.apply_hint_red));
            tvBankHint.setTextColor(getResources().getColor(R.color.redC2060A));
            return false;
        }
        String email = etEmailAddress.getText().toString();
        if (etEmailAddress.isEmpty()) {
            return false;
        }

        if (etCustomer.isEmpty()) {
            return false;
        }

        if (etTransferAmount.isEmpty()) {
            return false;
        }
        String date = tvDate.getText().toString();
        if (TextUtils.isEmpty(date)) {
            tvDateHint.setVisibility(View.VISIBLE);
            tvDateHint.setText(getResources().getString(R.string.apply_hint_red));
            tvDateHint.setTextColor(getResources().getColor(R.color.redC2060A));
            return false;
        }
        if (!JDataUtils.isEmail(etEmailAddress.getEditText().getText().toString())) {
            etEmailAddress.setErrorMsg(emailErrorString);
            return false;
        } else {
            etEmailAddress.setErrorMsg("");
        }

        if (TextUtils.isEmpty(mImagePath) && !isEdit) {
            Toast.makeText(BankTransaferActivity.this, imageSelectError, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public static Bitmap resizeImage2(String path,
                                      int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//不加载bitmap到内存中
        BitmapFactory.decodeFile(path, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 1;
        if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
            int sampleSize = (outWidth / width + outHeight / height) / 2;
            options.inSampleSize = sampleSize;
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_PHOTO && data != null) {
            selectPhotoHander(data);
        } else if (requestCode == TAKE_PHOTO && data != null && data.getExtras() != null) {
            takePhotoHander(data);
        } else if (requestCode == REQUESTCODE_STORAGE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                dismissPermissionDialog();
            }
        } else if (requestCode == REQUESTCODE_CAMERA) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dismissPermissionDialog();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void dismissPermissionDialog() {
        if (permissionDialog != null && permissionDialog.isShowing()) {
            permissionDialog.dismiss();
        }
    }

    private void selectPhotoHander(Intent data) {
        if (data == null) {
            return;
        }
        Uri selectedImage = data.getData();
        mImagePath = uriToRealUrl(selectedImage);
        if (mBitMap != null && !mBitMap.isRecycled()) {
            mBitMap.recycle();
        }
        if (selectedImage != null) {
            ivImg.setVisibility(View.VISIBLE);
            mBitMap = resizeImage2(mImagePath, JDataUtils.dp2Px(120), JDataUtils.dp2Px(100));
            ivImg.setImageBitmap(mBitMap);
        }
    }

    private void takePhotoHander(Intent data) {
        if (mBitMap != null && !mBitMap.isRecycled()) {
            mBitMap.recycle();
        }
        mBitMap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mBitMap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File file = FileUtils.createFile(this, System.currentTimeMillis() + ".jpg", Environment.DIRECTORY_PICTURES);
        mImagePath = file.getAbsolutePath();

        FileOutputStream fo = null;
        try {
            file.createNewFile();
            fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivImg.setVisibility(View.VISIBLE);
        ivImg.setImageBitmap(mBitMap);
    }

    public String uriToRealUrl(Uri selectImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectImage,
                filePathColumn, null, null, null);
        String imgDecodableString = "";
        if (cursor == null) {
            imgDecodableString = selectImage.getPath();
        } else {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
        }
        return imgDecodableString;
    }

    public void uploadFile(File file) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
        OkHttpClientManager.Param param = new OkHttpClientManager.Param("session_key", WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey());
        params[0] = param;
        OkHttpClientManager.getUploadDelegate().postAsyn(GlobalData.upLoadFileUrl, "proof_file", file, params, new MyResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

                if (mDialog != null) {
                    mDialog.cancel();
                }
                JViewUtils.showErrorToast(BankTransaferActivity.this, intentnetErrorString);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String u) {
                JLogUtils.e("TAG", "onResponse" + u);
                int status = 0;
                JSONObject jsonObject = null;
                if (u != null) {
                    try {
                        jsonObject = new JSONObject(u);
                        status = jsonObject.getInt("status");
                        if (status == 1) {
                            String url = jsonObject.getString("proofFile");
                            saveBrankConfirm(url);
                        } else {
                            closeDialog();
                            try {
                                String errorMessage = jsonObject.getString("errorMessage");
                                if (!TextUtils.isEmpty(errorMessage)) {
                                    JViewUtils.showErrorToast(BankTransaferActivity.this, errorMessage);
                                }
                            } catch (Exception ex) {
                                ex.getStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    closeDialog();
                }
            }
        }, BankTransaferActivity.this);
    }

    public void closeDialog() {
        if (mDialog != null) {
            mDialog.cancel();
        }
    }

    private View view_backtransafer_email_line, view_backtransafer_amount_line, view_backtransafer_name_line, view_backtransafer_bankfrom_line, view_backtransafer_date_line;

    private void initView() {
        mImageLoader = new ImageLoader(this);
        tvDateHint = (TextView) findViewById(R.id.tv_date_hint);
        tvBankHint = (TextView) findViewById(R.id.tv_bankFrom_hint);

        etTransferAmount = (CustomEdit) findViewById(R.id.et_transferAmount);
        tvTrasferFrom = (TextView) findViewById(R.id.tv_bankFrom);
        tvDate = (TextView) findViewById(R.id.tv_date);
        etEmailAddress = (CustomEdit) findViewById(R.id.et_email);
        etOrderNum = (CustomEdit) findViewById(R.id.et_order_number);
        etCustomer = (CustomEdit) findViewById(R.id.et_customer);
        TextView btnChoose = (TextView) findViewById(R.id.btn_choose);
        TextView btnSubmit = (TextView) findViewById(R.id.tv_confirm);
        llSubmit = (LinearLayout) findViewById(R.id.ll_confirm);
        bottomBlack = (View) findViewById(R.id.bottom_black);
        ivImg = (ImageView) findViewById(R.id.iv_image);
        iv_bank_from_arrow = (ImageView) findViewById(R.id.iv_bank_from_arrow);
        iv_trans_date_arrow = (ImageView) findViewById(R.id.iv_trans_date_arrow);

        llSubmit.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tvTrasferFrom.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        btnChoose.setOnClickListener(this);
        view_backtransafer_email_line = findViewById(R.id.view_backtransafer_email_line);
        view_backtransafer_amount_line = findViewById(R.id.view_backtransafer_amount_line);
        view_backtransafer_name_line = findViewById(R.id.view_backtransafer_name_line);
        view_backtransafer_bankfrom_line = findViewById(R.id.view_backtransafer_bankfrom_line);
        view_backtransafer_date_line = findViewById(R.id.view_backtransafer_date_line);
        tvTrasferFrom.setOnFocusChangeListener(this);
        etEmailAddress.setCustomFocusChangeCallBack(new CustomFocusChangeCallBack() {
            @Override
            public void focusChangeCalBack(View v, boolean hasFocus) {
                if (hasFocus) {
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_backtransafer_email_line, true);
                } else {
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_backtransafer_email_line, false);
                }
            }
        });
        etCustomer.setCustomFocusChangeCallBack(new CustomFocusChangeCallBack() {
            @Override
            public void focusChangeCalBack(View v, boolean hasFocus) {
                if (hasFocus) {
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_backtransafer_name_line, true);
                } else {
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_backtransafer_name_line, false);
                }
            }
        });
        etTransferAmount.setCustomFocusChangeCallBack(new CustomFocusChangeCallBack() {
            @Override
            public void focusChangeCalBack(View v, boolean hasFocus) {
                if (hasFocus) {
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_backtransafer_amount_line, true);
                } else {
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_backtransafer_amount_line, false);
                }
            }
        });
        JViewUtils.setSoildButtonGlobalStyle(this, btnSubmit);
//        btnSubmit.setBackground(JImageUtils.getButtonBackgroudSolidDrawable(this));
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == tvTrasferFrom.getId()) {
            if (!hasFocus) {
                tvBankHint.setTextColor(JToolUtils.getColor(R.color.label_saved));
            }
        }
    }

}
