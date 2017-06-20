package com.whitelabel.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.adapter.WheelPickerAdapter;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.callback.GlobalCallBack;
import com.whitelabel.app.callback.MaterialDialogCallback;
import com.whitelabel.app.callback.WheelPickerCallback;
import com.whitelabel.app.listener.OnMessageDialogListener;
import com.whitelabel.app.model.WheelPickerConfigEntity;
import com.whitelabel.app.model.WheelPickerEntity;
import com.whitelabel.app.widget.CustomButton;
import com.whitelabel.app.widget.CustomDialog;
import com.whitelabel.app.widget.CustomMyDialog;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.MaterialDialog;
import com.whitelabel.app.widget.wheel.OnWheelChangedListener;
import com.whitelabel.app.widget.wheel.WheelView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by imaginato on 2015/6/10.
 */
public class JViewUtils {
    private static final String TAG = "JViewUtils";
    private static Dialog dialog;

    public static int getToolBarHeight(Context context) {
        int[] attrs = new int[]{R.attr.actionBarSize};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int toolBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
        return toolBarHeight;
    }

    public static void setStatus(TextView textView, String statusCode) {
        JLogUtils.d(TAG, "statusCode==" + statusCode);
        if ("pending".equalsIgnoreCase(statusCode)) {
            textView.setBackgroundResource(R.drawable.order_item_status_pending);
        } else if ("processing".equalsIgnoreCase(statusCode)) {
            textView.setBackgroundResource(R.drawable.order_item_status_process);
        } else if (statusCode.contains("verified")) {
            textView.setBackgroundResource(R.drawable.order_item_status_verified);
        } else if ("shipped".equalsIgnoreCase(statusCode)) {
            textView.setBackgroundResource(R.drawable.order_item_status_ship);
        } else if (statusCode.contains("delivered")) {
            textView.setBackgroundResource(R.drawable.order_item_status_delivered);
        } else if ("deliveryFailed".equalsIgnoreCase(statusCode)) {
            textView.setBackgroundResource(R.drawable.order_item_status_failed);
        } else if (statusCode.contains("canceled")) {
            textView.setBackgroundResource(R.drawable.order_item_status_cancel);
        } else if (statusCode.contains("holded")) {
            textView.setBackgroundResource(R.drawable.order_item_status_hold);
        }
    }



    public  static   void  setNavBarTextColor(TextView textView){
         textView.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getNavigation_bar_text_icon_default_color());
    }


    public static  void setNavBarIconColor(Context context , ImageView ivImg,int icon){
        ivImg.setImageDrawable(JImageUtils.
                getThemeIconTapping(ContextCompat.getDrawable(context,icon),
                        WhiteLabelApplication.getAppConfiguration().
                                getThemeConfig().getNavigation_bar_text_icon_default_color(),
                        WhiteLabelApplication.getAppConfiguration().getThemeConfig().getNavigation_bar_text_icon_tapping_color()));
    }

    public static  void setSlideMenuTextStyle(TextView tvText,boolean defaultIsGray){
        int defaultColor=defaultIsGray? ContextCompat.getColor(tvText.getContext(),R.color.blackD0):
                ContextCompat.getColor(tvText.getContext(),R.color.black);
        int[] colors = new int[] {WhiteLabelApplication.getAppConfiguration().getThemeConfig().getSide_menu_background_default_color()
                , WhiteLabelApplication.getAppConfiguration().getThemeConfig().getSide_menu_text_icon_tapping_color(),
                defaultColor};
        int[][] states = new int[3][];
        states[0] = new int[] { android.R.attr.state_enabled, android.R.attr.state_selected };
        states[1] = new int[] { android.R.attr.state_enabled,android.R.attr.state_pressed};
        states[2] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
        tvText.setTextColor(colorList);
    }


    public static Drawable getNavBarIconDrawable(Context context, int icon){
     return   JImageUtils.
                getThemeIconTapping(ContextCompat.getDrawable(context,icon),
                        WhiteLabelApplication.getAppConfiguration().
                                getThemeConfig().getNavigation_bar_text_icon_default_color(),
                        WhiteLabelApplication.getAppConfiguration().getThemeConfig().getNavigation_bar_text_icon_tapping_color());
    }
    public static void setStrokeButtonGlobalStyle(Context context,TextView textView){
        textView.setBackground(JImageUtils.getbuttonBakcgroundStrokeDrawable(context));
        textView.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
    }


    public static   void setSoildButtonGlobalStyle(Context context,TextView textView){
        textView.setBackground(JImageUtils.getButtonBackgroudSolidDrawable(context));
        textView.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getButton_text_color());
    }
    public static void showErrorToast(Context context, String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyboard(Activity activity) {
        try {
            View decorView = activity.getWindow().peekDecorView();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(decorView.getWindowToken(), 0);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "hideKeyboard", ex);
        }
    }



    public static  void showNoInventoryToast(Context context,String str){
        if (context == null) {
            return;
        }
        LinearLayout toastView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_prompt_productdetail_notenoughinventory, null);
        TextView message = (TextView) toastView.findViewById(R.id.tv_text);
           message.setText(str);
           Toast mToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
            if (WhiteLabelApplication.getPhoneConfiguration() != null && WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() != 0) {
                mToast.setGravity(Gravity.BOTTOM, 0, (int) (WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() * 0.25));
            }
            mToast.setView(toastView);
        mToast.show();
    }


    public static Dialog showExitDialog(Context context, String message, String btnLeft, String btnRight, DialogInterface.OnClickListener btnLeftListener, DialogInterface.OnClickListener btnRightListener) {
        Dialog dialog = null;
        CustomMyDialog.Builder builder = new CustomMyDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(btnLeft, btnLeftListener);
        builder.setNegativeButton(btnRight, btnRightListener);
        dialog = builder.create();
        Window win = dialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        dialog.setCancelable(false);
        win.setWindowAnimations(R.style.dialogAnim);
        dialog.show();
        return dialog;
    }

    public static void showKeyboard(EditText et) {
        if (et != null) {
            InputMethodManager inputManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(et, 0);
            et.setFocusable(true);
            et.setFocusableInTouchMode(true);
            et.requestFocus();
        }
    }


    public static Dialog showProgressDialog(Context context) {
        Dialog dialog = new CustomDialog(context);
        dialog.show();
        return dialog;
    }

    public static Dialog showProgressDialog(Context context, String gravity) {
        Dialog dialog = new CustomDialog(context, gravity);
        dialog.show();
        return dialog;
    }


    public static MaterialDialog showMaterialDialogV2(Context context, String title, String message, final MaterialDialogCallback materialDialogCallback) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
        mMaterialDialog.setTitle(title);
        mMaterialDialog.setMessage(message);
        mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
                if (materialDialogCallback != null) {
                    materialDialogCallback.callBack();
                }
            }
        });
        mMaterialDialog.show();
//        mMaterialDialog.getPositiveButton().setTextColor(context.getResources().getColor(R.color.purple92018d));
        return mMaterialDialog;
    }

    public static MaterialDialog showMaterialDialog(Context context, String title, String message, final View.OnClickListener okListener) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
        mMaterialDialog.setTitle(title);
        mMaterialDialog.setMessage(message);
        if (okListener != null) {
            mMaterialDialog.setPositiveButton("OK", okListener);
        } else {
            mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMaterialDialog.dismiss();
                }
            });
        }
        mMaterialDialog.show();
//        mMaterialDialog.getPositiveButton().setTextColor(context.getResources().getColor(R.color.purple92018d));
        return mMaterialDialog;
    }


    public static MaterialDialog showMaterialDialog(Context context, String title, String message, String button, final View.OnClickListener okListener, boolean isCanncel) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
        mMaterialDialog.setTitle(title);
        mMaterialDialog.setMessage(message);

        mMaterialDialog.setCancelable(isCanncel);
        mMaterialDialog.setCanceledOnTouchOutside(isCanncel);
        if (okListener != null) {
            mMaterialDialog.setPositiveButton(button, okListener);
        } else {
            mMaterialDialog.setPositiveButton(button, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMaterialDialog.dismiss();
                }
            });
        }
        mMaterialDialog.show();
        mMaterialDialog.getPositiveButton().setTextColor(context.getResources().getColor(R.color.purple92018d));
        return mMaterialDialog;
    }

    public static MaterialDialog showPermissionDialog(final Activity activity, String title, String message, final int requestCode, final boolean finishActivity) {
        final MaterialDialog dialog = new MaterialDialog(activity);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setNegativeButton(activity.getString(R.string.messagedialog_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finishActivity) {
                    activity.finish();
                } else {
                    dialog.dismiss();
                }

            }
        });
        dialog.setPositiveButton(activity.getString(R.string.messagedialog_settings), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), requestCode);
            }
        });
        dialog.show();
        dialog.getPositiveButton().setTextColor(activity.getResources().getColor(R.color.purple92018d));
        return dialog;
    }

    public static MaterialDialog showMaterialDialog(Context context, String title, String message, String button, final View.OnClickListener okListener) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
        mMaterialDialog.setTitle(title);
        mMaterialDialog.setMessage(message);
        if (okListener != null) {
            mMaterialDialog.setPositiveButton(button, okListener);
        } else {
            mMaterialDialog.setPositiveButton(button, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMaterialDialog.dismiss();
                }
            });
        }
        mMaterialDialog.show();
        mMaterialDialog.getPositiveButton().setTextColor(context.getResources().getColor(R.color.purple92018d));
        return mMaterialDialog;
    }


//    public static void showProgressBar(Activity activity) {
////        if (activity != null) {
////            CustomProgressBar.createProgressBar(activity).show();
////        }
//        dialog=new CustomDialog(activity);
//        dialog.show();
//    }

//    public static void dismissProgressBar(Activity activity) {
////        if (activity != null) {
////            CustomProgressBar.createProgressBar(activity).dismiss();
////        }
//        dialog.dismiss();
//    }

    public static void showToast(Context context, String title, String message) {
        if (context == null) {
            return;
        }

        Toast toast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
        if (WhiteLabelApplication.getPhoneConfiguration() != null && WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() != 0) {
            toast.setGravity(Gravity.BOTTOM, 0, (int) (WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() * 0.25));
        }

        LinearLayout toastView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_customtoast, null);
        CustomTextView ctvTitle = (CustomTextView) toastView.findViewById(R.id.vTextViewTitle);
        CustomTextView ctvContent = (CustomTextView) toastView.findViewById(R.id.vTextViewMessage);

        if (JDataUtils.isEmpty(title)) {
            ctvTitle.setVisibility(View.GONE);
        } else {
            ctvTitle.setText(title);
            ctvTitle.setVisibility(View.VISIBLE);
        }

        ctvContent.setText(message);
        ctvContent.setVisibility(View.VISIBLE);

        toast.setView(toastView);
        toast.show();
    }

    private static Toast mSingleToast;

    public static void showSingleToast(Context context, String message) {
        if (mSingleToast == null) {
            mSingleToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            mSingleToast.setText(message);
        }
        mSingleToast.show();
    }


    public static void setLandingBannerFontSize(String types, String value, TextView textView) {
        if (types.length() != 1) {
            return;
        }
        char[] ch = types.toCharArray();
        char type = ch[0];
        int size = value.length();
        switch (type) {
            case '8':
            case '7':
            case '4':
                setLandingAtt(size, textView);
                break;
        }
    }

    private static void setLandingAtt(int size, TextView textView) {
        if (size < 4) {
            textView.setTextSize(textView.getContext().getResources().getInteger(R.integer.landing_banner_font_size_low4));
            RelativeLayout.LayoutParams tv_lp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
            if (tv_lp != null) {
                float left = textView.getContext().getResources().getDimension(R.dimen.landing_banner_font_size_low4_add_marginleft);
                float top = textView.getContext().getResources().getDimension(R.dimen.landing_banner_font_size_low4_add_margintop);
                tv_lp.setMargins((int) left, (int) top, 0, 0);
                textView.setLayoutParams(tv_lp);
            }
        } else {
            if (size == 4) {
                textView.setTextSize(textView.getContext().getResources().getInteger(R.integer.landing_banner_font_size_4));
            } else if (size == 5) {
                textView.setTextSize(textView.getContext().getResources().getInteger(R.integer.landing_banner_font_size_5));
            }
            RelativeLayout.LayoutParams tv_lp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
            if (tv_lp != null) {
                float left = textView.getContext().getResources().getDimension(R.dimen.landing_banner_font_size_4_add_marginleft);
                float top = textView.getContext().getResources().getDimension(R.dimen.landing_banner_font_size_low4_add_margintop);
                tv_lp.setMargins((int) left, (int) top, 0, 0);
                textView.setLayoutParams(tv_lp);
            }
        }
    }

    public static void showMessageDialog(Context context, String message) {
        showMessageDialog(context, message, null);
    }

    public static void showMessageDialog(Context context, String message, OnMessageDialogListener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_message, null);
        window.setContentView(dialogView);

        CustomTextView messageView = (CustomTextView) dialogView.findViewById(R.id.ctvMessage);
        messageView.setText(message);

        CustomButton okView = (CustomButton) dialogView.findViewById(R.id.cbtOK);
        okView.setOnClickListener(new View.OnClickListener() {
            private AlertDialog dialog;
            private OnMessageDialogListener listener;

            public View.OnClickListener init(AlertDialog dialog, OnMessageDialogListener l) {
                this.dialog = dialog;
                this.listener = l;
                return this;
            }

            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (listener != null) {
                    listener.onOKClickListener(null);
                }
            }
        }.init(dialog, listener));
    }


    public static void showNewWheelPickerOneDialog(Context context, WheelPickerConfigEntity pickerConfigEntity) {
        if (pickerConfigEntity == null || pickerConfigEntity.getArrayList() == null || pickerConfigEntity.getCallBack() == null || pickerConfigEntity.getOldValue() == null) {
            return;
        }
        pickerConfigEntity.getCallBack().getOldValue().setIndex(pickerConfigEntity.getOldValue().getIndex());
        pickerConfigEntity.getCallBack().getOldValue().setValue(pickerConfigEntity.getOldValue().getValue());
        pickerConfigEntity.getCallBack().getOldValue().setDisplay(pickerConfigEntity.getOldValue().getDisplay());

        pickerConfigEntity.getCallBack().getNewValue().setIndex(-1);
        pickerConfigEntity.getCallBack().getNewValue().setValue(null);
        pickerConfigEntity.getCallBack().getNewValue().setDisplay(null);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_wheelpicker_one, null);
        final RelativeLayout rlContainer = (RelativeLayout) dialogView.findViewById(R.id.rlContainer);
        final CustomTextView ctvCancel = (CustomTextView) dialogView.findViewById(R.id.ctvCancel);
        final CustomTextView ctvSet = (CustomTextView) dialogView.findViewById(R.id.ctvSet);
        final WheelView wheelView = (WheelView) dialogView.findViewById(R.id.wheelView);

        WheelPickerAdapter wheelPickerAdapter = new WheelPickerAdapter(context, pickerConfigEntity.getArrayList());
        wheelView.setViewAdapter(wheelPickerAdapter);
        wheelView.setVisibleItems(5);
        wheelView.setCurrentItem(pickerConfigEntity.getIndex());

        final Dialog customDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        Window dialogWindow = customDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);

        rlContainer.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallback;

            public View.OnClickListener init(Dialog d, WheelPickerCallback c) {
                this.dialog = d;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onCancel();
                }
            }
        }.init(customDialog, pickerConfigEntity.getCallBack()));
        ctvCancel.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallback;

            public View.OnClickListener init(Dialog d, WheelPickerCallback c) {
                this.dialog = d;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onCancel();
                }
            }
        }.init(customDialog, pickerConfigEntity.getCallBack()));
        ctvSet.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallback;

            public View.OnClickListener init(Dialog d, WheelPickerCallback c) {
                this.dialog = d;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onDone(wheelPickerCallback.getOldValue(), wheelPickerCallback.getNewValue());
                }
            }
        }.init(customDialog, pickerConfigEntity.getCallBack()));
        wheelView.addChangingListener(new OnWheelChangedListener() {
            private ArrayList<WheelPickerEntity> array;
            private WheelPickerCallback wheelPickerCallback;

            public OnWheelChangedListener init(ArrayList<WheelPickerEntity> a, WheelPickerCallback c) {
                this.array = a;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (this.array != null && (newValue >= 0 && newValue < this.array.size()) && this.array.get(newValue) != null) {
                    if (wheelPickerCallback != null) {
                        final WheelPickerEntity newPickerEntity = array.get(newValue);
                        if (newPickerEntity != null) {
                            wheelPickerCallback.getNewValue().setIndex(newPickerEntity.getIndex());
                            wheelPickerCallback.getNewValue().setValue(newPickerEntity.getValue());
                            wheelPickerCallback.getNewValue().setDisplay(newPickerEntity.getDisplay());
                        }
                    }
                }

                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onScrolling(wheelPickerCallback.getOldValue(), wheelPickerCallback.getNewValue());
                }
            }
        }.init(pickerConfigEntity.getArrayList(), pickerConfigEntity.getCallBack()));
        customDialog.setContentView(dialogView);
        customDialog.show();
    }

    public static void cleanCurrentViewFocus(Activity activity) {
        View currFocuView = activity.getCurrentFocus();
        if (currFocuView != null) {
            currFocuView.clearFocus();
        }
    }

    public static Dialog onCreateDateDialog(final Context context, final TextView tvDate, String currDate) {
        Dialog dialog = null;
        final Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(currDate)) {
            try {
                Date currDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(currDate);
                c.setTimeInMillis(currDate1.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        final StringBuffer buffer = new StringBuffer();

        dialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                        if (buffer.length() == 0) {
                            String monthstr = (month + 1) < 10 ? "0" + (month + 1) : (month + 1) + "";
                            String dayOfMonthStr = dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "";
                            buffer.append(year).append("-").append(monthstr).append("-").append(dayOfMonthStr);
                            tvDate.setText(buffer.toString());
                        }
                    }
                }, c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );

        return dialog;
    }

    public static Dialog onCreateDateDialog(final Context context, final TextView tvDate, String currDate, final GlobalCallBack callback, final GlobalCallBack dissCallback) {
        Dialog dialog = null;
        final Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(currDate)) {
            try {
                Date currDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(currDate);
                c.setTimeInMillis(currDate1.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        final StringBuilder buffer = new StringBuilder();

        dialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                        if (buffer.length() == 0) {
                            String monthstr = (month + 1) < 10 ? "0" + (month + 1) : (month + 1) + "";
                            String dayOfMonthStr = dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "";
                            buffer.append(year).append("-").append(monthstr).append("-").append(dayOfMonthStr);
                            tvDate.setText(buffer.toString());
                            callback.callBack();
                        }
                    }
                }, c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dissCallback.callBack();
            }
        });
        return dialog;
    }

    public static void showWheelPickerOneDialog(Context context, WheelPickerConfigEntity pickerConfigEntity) {
        if (pickerConfigEntity == null || pickerConfigEntity.getArrayList() == null || pickerConfigEntity.getCallBack() == null || pickerConfigEntity.getOldValue() == null) {
            return;
        }
        pickerConfigEntity.getCallBack().getOldValue().setIndex(pickerConfigEntity.getOldValue().getIndex());
        pickerConfigEntity.getCallBack().getOldValue().setValue(pickerConfigEntity.getOldValue().getValue());
        pickerConfigEntity.getCallBack().getOldValue().setDisplay(pickerConfigEntity.getOldValue().getDisplay());

        pickerConfigEntity.getCallBack().getNewValue().setIndex(-1);
        pickerConfigEntity.getCallBack().getNewValue().setValue(null);
        pickerConfigEntity.getCallBack().getNewValue().setDisplay(null);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_wheelpicker_one, null);
        final RelativeLayout rlContainer = (RelativeLayout) dialogView.findViewById(R.id.rlContainer);
        final CustomTextView ctvCancel = (CustomTextView) dialogView.findViewById(R.id.ctvCancel);
        final CustomTextView ctvSet = (CustomTextView) dialogView.findViewById(R.id.ctvSet);
        ctvSet.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        final WheelView wheelView = (WheelView) dialogView.findViewById(R.id.wheelView);

        WheelPickerAdapter wheelPickerAdapter = new WheelPickerAdapter(context, pickerConfigEntity.getArrayList());
        wheelView.setViewAdapter(wheelPickerAdapter);
        wheelView.setVisibleItems(5);
        wheelView.setCurrentItem(pickerConfigEntity.getIndex());

        final Dialog customDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        Window dialogWindow = customDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);

        rlContainer.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallback;

            public View.OnClickListener init(Dialog d, WheelPickerCallback c) {
                this.dialog = d;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onCancel();
                }
            }
        }.init(customDialog, pickerConfigEntity.getCallBack()));
        ctvCancel.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallback;

            public View.OnClickListener init(Dialog d, WheelPickerCallback c) {
                this.dialog = d;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onCancel();
                }
            }
        }.init(customDialog, pickerConfigEntity.getCallBack()));
        ctvSet.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallback;

            public View.OnClickListener init(Dialog d, WheelPickerCallback c) {
                this.dialog = d;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onDone(wheelPickerCallback.getOldValue(), wheelPickerCallback.getNewValue());
                }
            }
        }.init(customDialog, pickerConfigEntity.getCallBack()));
        wheelView.addChangingListener(new OnWheelChangedListener() {
            private ArrayList<WheelPickerEntity> array;
            private WheelPickerCallback wheelPickerCallback;

            public OnWheelChangedListener init(ArrayList<WheelPickerEntity> a, WheelPickerCallback c) {
                this.array = a;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (this.array != null && (newValue >= 0 && newValue < this.array.size()) && this.array.get(newValue) != null) {
                    if (wheelPickerCallback != null) {
                        final WheelPickerEntity newPickerEntity = array.get(newValue);
                        if (newPickerEntity != null) {
                            wheelPickerCallback.getNewValue().setIndex(newPickerEntity.getIndex());
                            wheelPickerCallback.getNewValue().setValue(newPickerEntity.getValue());
                            wheelPickerCallback.getNewValue().setDisplay(newPickerEntity.getDisplay());
                        }
                    }
                }

                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onScrolling(wheelPickerCallback.getOldValue(), wheelPickerCallback.getNewValue());
                }
            }
        }.init(pickerConfigEntity.getArrayList(), pickerConfigEntity.getCallBack()));

        customDialog.setContentView(dialogView);
        customDialog.show();
    }

    public static void showWheelPickerThreeDialog2(Context context, WheelPickerAdapter wpaLeft, WheelPickerConfigEntity pickerConfigEntityLeft, int currentItem1, WheelPickerConfigEntity pickerConfigEntityMiddle, int currentItem2, WheelPickerConfigEntity pickerConfigEntityRight, int currentItem3) {
        if (context == null || pickerConfigEntityLeft == null || pickerConfigEntityMiddle == null || pickerConfigEntityRight == null) {
            return;
        }

        if (pickerConfigEntityLeft.getCallBack() == null || pickerConfigEntityLeft.getArrayList() == null || pickerConfigEntityLeft.getOldValue() == null) {
            return;
        }
        pickerConfigEntityLeft.getCallBack().getOldValue().setValue(pickerConfigEntityLeft.getOldValue().getValue());
        pickerConfigEntityLeft.getCallBack().getOldValue().setDisplay(pickerConfigEntityLeft.getOldValue().getDisplay());

        if (pickerConfigEntityMiddle.getCallBack() == null || pickerConfigEntityMiddle.getArrayList() == null || pickerConfigEntityMiddle.getOldValue() == null) {
            return;
        }
        pickerConfigEntityMiddle.getCallBack().getOldValue().setValue(pickerConfigEntityMiddle.getOldValue().getValue());
        pickerConfigEntityMiddle.getCallBack().getOldValue().setDisplay(pickerConfigEntityMiddle.getOldValue().getDisplay());

        if (pickerConfigEntityRight.getCallBack() == null || pickerConfigEntityRight.getArrayList() == null || pickerConfigEntityRight.getOldValue() == null) {
            return;
        }
        pickerConfigEntityRight.getCallBack().getOldValue().setValue(pickerConfigEntityRight.getOldValue().getValue());
        pickerConfigEntityRight.getCallBack().getOldValue().setDisplay(pickerConfigEntityRight.getOldValue().getDisplay());


        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_wheelpicker_three, null);
        final RelativeLayout rlContainer = (RelativeLayout) dialogView.findViewById(R.id.rlContainer);
        final CustomTextView ctvCancel = (CustomTextView) dialogView.findViewById(R.id.ctvCancel);
        final CustomTextView ctvSet = (CustomTextView) dialogView.findViewById(R.id.ctvSet);
        final WheelView wvLeft = (WheelView) dialogView.findViewById(R.id.wvLeft);
        final WheelView wvMiddle = (WheelView) dialogView.findViewById(R.id.wvMiddle);
        final WheelView wvRight = (WheelView) dialogView.findViewById(R.id.wvRight);
        ctvSet.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        wvLeft.setViewAdapter(wpaLeft);
        wvLeft.setVisibleItems(5);
        wvLeft.setCurrentItem(currentItem1);


        WheelPickerAdapter wpaMiddle = new WheelPickerAdapter(context, pickerConfigEntityMiddle.getArrayList());
        wvMiddle.setViewAdapter(wpaMiddle);
        wvMiddle.setVisibleItems(5);
        wvMiddle.setCurrentItem(currentItem2);

        WheelPickerAdapter wpaRight = new WheelPickerAdapter(context, pickerConfigEntityRight.getArrayList());
        wvRight.setViewAdapter(wpaRight);
        wvRight.setVisibleItems(5);
        wvRight.setCurrentItem(currentItem3);

        final Dialog customDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        Window dialogWindow = customDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);

        rlContainer.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallbackLeft;
            private WheelPickerCallback wheelPickerCallbackMiddle;
            private WheelPickerCallback wheelPickerCallbackRight;

            public View.OnClickListener init(Dialog d, WheelPickerCallback cl, WheelPickerCallback cm, WheelPickerCallback cr) {
                this.dialog = d;
                this.wheelPickerCallbackLeft = cl;
                this.wheelPickerCallbackMiddle = cm;
                this.wheelPickerCallbackRight = cr;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallbackLeft != null) {
                    wheelPickerCallbackLeft.onCancel();
                }
                if (wheelPickerCallbackMiddle != null) {
                    wheelPickerCallbackMiddle.onCancel();
                }
                if (wheelPickerCallbackRight != null) {
                    wheelPickerCallbackRight.onCancel();
                }
            }
        }.init(customDialog, pickerConfigEntityLeft.getCallBack(), pickerConfigEntityMiddle.getCallBack(), pickerConfigEntityRight.getCallBack()));
        ctvCancel.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallbackLeft;
            private WheelPickerCallback wheelPickerCallbackMiddle;
            private WheelPickerCallback wheelPickerCallbackRight;

            public View.OnClickListener init(Dialog d, WheelPickerCallback cl, WheelPickerCallback cm, WheelPickerCallback cr) {
                this.dialog = d;
                this.wheelPickerCallbackLeft = cl;
                this.wheelPickerCallbackMiddle = cm;
                this.wheelPickerCallbackRight = cr;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallbackLeft != null) {
                    wheelPickerCallbackLeft.onCancel();
                }
                if (wheelPickerCallbackMiddle != null) {
                    wheelPickerCallbackMiddle.onCancel();
                }
                if (wheelPickerCallbackRight != null) {
                    wheelPickerCallbackRight.onCancel();
                }
            }
        }.init(customDialog, pickerConfigEntityLeft.getCallBack(), pickerConfigEntityMiddle.getCallBack(), pickerConfigEntityRight.getCallBack()));
        ctvSet.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallbackLeft;
            private WheelPickerCallback wheelPickerCallbackMiddle;
            private WheelPickerCallback wheelPickerCallbackRight;

            public View.OnClickListener init(Dialog d, WheelPickerCallback cl, WheelPickerCallback cm, WheelPickerCallback cr) {
                this.dialog = d;
                this.wheelPickerCallbackLeft = cl;
                this.wheelPickerCallbackMiddle = cm;
                this.wheelPickerCallbackRight = cr;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallbackLeft != null) {
                    wheelPickerCallbackLeft.onDone(wheelPickerCallbackLeft.getOldValue(), wheelPickerCallbackLeft.getNewValue());
                }
                if (wheelPickerCallbackMiddle != null) {
                    wheelPickerCallbackMiddle.onDone(wheelPickerCallbackMiddle.getOldValue(), wheelPickerCallbackMiddle.getNewValue());
                }
                if (wheelPickerCallbackRight != null) {
                    wheelPickerCallbackRight.onDone(wheelPickerCallbackRight.getOldValue(), wheelPickerCallbackRight.getNewValue());
                }
            }
        }.init(customDialog, pickerConfigEntityLeft.getCallBack(), pickerConfigEntityMiddle.getCallBack(), pickerConfigEntityRight.getCallBack()));
        wvLeft.addChangingListener(new OnWheelChangedListener() {
            private ArrayList<WheelPickerEntity> array;
            private WheelPickerCallback wheelPickerCallback;

            public OnWheelChangedListener init(ArrayList<WheelPickerEntity> a, WheelPickerCallback c) {
                this.array = a;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (this.array != null && (newValue >= 0 && newValue < this.array.size()) && this.array.get(newValue) != null) {
                    if (wheelPickerCallback != null) {
                        final WheelPickerEntity newPickerEntity = array.get(newValue);
                        if (newPickerEntity != null) {
                            wheelPickerCallback.getNewValue().setValue(newPickerEntity.getValue());
                            wheelPickerCallback.getNewValue().setDisplay(newPickerEntity.getDisplay());
                        }
                    }
                }

                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onScrolling(wheelPickerCallback.getOldValue(), wheelPickerCallback.getNewValue());
                }
            }
        }.init(pickerConfigEntityLeft.getArrayList(), pickerConfigEntityLeft.getCallBack()));
        wvMiddle.addChangingListener(new OnWheelChangedListener() {
            private ArrayList<WheelPickerEntity> array;
            private WheelPickerCallback wheelPickerCallback;

            public OnWheelChangedListener init(ArrayList<WheelPickerEntity> a, WheelPickerCallback c) {
                this.array = a;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                wvLeft.setCurrentItem(0);
                if (this.array != null && (newValue >= 0 && newValue < this.array.size()) && this.array.get(newValue) != null) {
                    if (wheelPickerCallback != null) {
                        final WheelPickerEntity newPickerEntity = array.get(newValue);
                        if (newPickerEntity != null) {
                            wheelPickerCallback.getNewValue().setValue(newPickerEntity.getValue());
                            wheelPickerCallback.getNewValue().setDisplay(newPickerEntity.getDisplay());
                        }
                    }
                }

                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onScrolling(wheelPickerCallback.getOldValue(), wheelPickerCallback.getNewValue());
                }
            }
        }.init(pickerConfigEntityMiddle.getArrayList(), pickerConfigEntityMiddle.getCallBack()));
        wvRight.addChangingListener(new OnWheelChangedListener() {
            private ArrayList<WheelPickerEntity> array;
            private WheelPickerCallback wheelPickerCallback;

            public OnWheelChangedListener init(ArrayList<WheelPickerEntity> a, WheelPickerCallback c) {
                this.array = a;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                wvLeft.setCurrentItem(0);
                if (this.array != null && (newValue >= 0 && newValue < this.array.size()) && this.array.get(newValue) != null) {
                    if (wheelPickerCallback != null) {
                        final WheelPickerEntity newPickerEntity = array.get(newValue);
                        if (newPickerEntity != null) {
                            wheelPickerCallback.getNewValue().setValue(newPickerEntity.getValue());
                            wheelPickerCallback.getNewValue().setDisplay(newPickerEntity.getDisplay());
                        }
                    }
                }

                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onScrolling(wheelPickerCallback.getOldValue(), wheelPickerCallback.getNewValue());
                }
            }
        }.init(pickerConfigEntityRight.getArrayList(), pickerConfigEntityRight.getCallBack()));

        customDialog.setContentView(dialogView);
        customDialog.show();
    }


    public static void showWheelPickerThreeDialog(Context context, WheelPickerConfigEntity pickerConfigEntityLeft, WheelPickerConfigEntity pickerConfigEntityMiddle, WheelPickerConfigEntity pickerConfigEntityRight) {
        if (context == null || pickerConfigEntityLeft == null || pickerConfigEntityMiddle == null || pickerConfigEntityRight == null) {
            return;
        }

        if (pickerConfigEntityLeft.getCallBack() == null || pickerConfigEntityLeft.getArrayList() == null || pickerConfigEntityLeft.getOldValue() == null) {
            return;
        }
        pickerConfigEntityLeft.getCallBack().getOldValue().setValue(pickerConfigEntityLeft.getOldValue().getValue());
        pickerConfigEntityLeft.getCallBack().getOldValue().setDisplay(pickerConfigEntityLeft.getOldValue().getDisplay());

        if (pickerConfigEntityMiddle.getCallBack() == null || pickerConfigEntityMiddle.getArrayList() == null || pickerConfigEntityMiddle.getOldValue() == null) {
            return;
        }
        pickerConfigEntityMiddle.getCallBack().getOldValue().setValue(pickerConfigEntityMiddle.getOldValue().getValue());
        pickerConfigEntityMiddle.getCallBack().getOldValue().setDisplay(pickerConfigEntityMiddle.getOldValue().getDisplay());

        if (pickerConfigEntityRight.getCallBack() == null || pickerConfigEntityRight.getArrayList() == null || pickerConfigEntityRight.getOldValue() == null) {
            return;
        }
        pickerConfigEntityRight.getCallBack().getOldValue().setValue(pickerConfigEntityRight.getOldValue().getValue());
        pickerConfigEntityRight.getCallBack().getOldValue().setDisplay(pickerConfigEntityRight.getOldValue().getDisplay());


        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_wheelpicker_three, null);
        final RelativeLayout rlContainer = (RelativeLayout) dialogView.findViewById(R.id.rlContainer);
        final CustomTextView ctvCancel = (CustomTextView) dialogView.findViewById(R.id.ctvCancel);
        final CustomTextView ctvSet = (CustomTextView) dialogView.findViewById(R.id.ctvSet);
        final WheelView wvLeft = (WheelView) dialogView.findViewById(R.id.wvLeft);
        final WheelView wvMiddle = (WheelView) dialogView.findViewById(R.id.wvMiddle);
        final WheelView wvRight = (WheelView) dialogView.findViewById(R.id.wvRight);
        ctvSet.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        WheelPickerAdapter wpaLeft = new WheelPickerAdapter(context, pickerConfigEntityLeft.getArrayList());
        wvLeft.setViewAdapter(wpaLeft);
        wvLeft.setVisibleItems(5);


        WheelPickerAdapter wpaMiddle = new WheelPickerAdapter(context, pickerConfigEntityMiddle.getArrayList());
        wvMiddle.setViewAdapter(wpaMiddle);
        wvMiddle.setVisibleItems(5);

        WheelPickerAdapter wpaRight = new WheelPickerAdapter(context, pickerConfigEntityRight.getArrayList());
        wvRight.setViewAdapter(wpaRight);
        wvRight.setVisibleItems(5);

        final Dialog customDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        Window dialogWindow = customDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);

        rlContainer.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallbackLeft;
            private WheelPickerCallback wheelPickerCallbackMiddle;
            private WheelPickerCallback wheelPickerCallbackRight;

            public View.OnClickListener init(Dialog d, WheelPickerCallback cl, WheelPickerCallback cm, WheelPickerCallback cr) {
                this.dialog = d;
                this.wheelPickerCallbackLeft = cl;
                this.wheelPickerCallbackMiddle = cm;
                this.wheelPickerCallbackRight = cr;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallbackLeft != null) {
                    wheelPickerCallbackLeft.onCancel();
                }
                if (wheelPickerCallbackMiddle != null) {
                    wheelPickerCallbackMiddle.onCancel();
                }
                if (wheelPickerCallbackRight != null) {
                    wheelPickerCallbackRight.onCancel();
                }
            }
        }.init(customDialog, pickerConfigEntityLeft.getCallBack(), pickerConfigEntityMiddle.getCallBack(), pickerConfigEntityRight.getCallBack()));
        ctvCancel.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallbackLeft;
            private WheelPickerCallback wheelPickerCallbackMiddle;
            private WheelPickerCallback wheelPickerCallbackRight;

            public View.OnClickListener init(Dialog d, WheelPickerCallback cl, WheelPickerCallback cm, WheelPickerCallback cr) {
                this.dialog = d;
                this.wheelPickerCallbackLeft = cl;
                this.wheelPickerCallbackMiddle = cm;
                this.wheelPickerCallbackRight = cr;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallbackLeft != null) {
                    wheelPickerCallbackLeft.onCancel();
                }
                if (wheelPickerCallbackMiddle != null) {
                    wheelPickerCallbackMiddle.onCancel();
                }
                if (wheelPickerCallbackRight != null) {
                    wheelPickerCallbackRight.onCancel();
                }
            }
        }.init(customDialog, pickerConfigEntityLeft.getCallBack(), pickerConfigEntityMiddle.getCallBack(), pickerConfigEntityRight.getCallBack()));
        ctvSet.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallbackLeft;
            private WheelPickerCallback wheelPickerCallbackMiddle;
            private WheelPickerCallback wheelPickerCallbackRight;

            public View.OnClickListener init(Dialog d, WheelPickerCallback cl, WheelPickerCallback cm, WheelPickerCallback cr) {
                this.dialog = d;
                this.wheelPickerCallbackLeft = cl;
                this.wheelPickerCallbackMiddle = cm;
                this.wheelPickerCallbackRight = cr;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallbackLeft != null) {
                    wheelPickerCallbackLeft.onDone(wheelPickerCallbackLeft.getOldValue(), wheelPickerCallbackLeft.getNewValue());
                }
                if (wheelPickerCallbackMiddle != null) {
                    wheelPickerCallbackMiddle.onDone(wheelPickerCallbackMiddle.getOldValue(), wheelPickerCallbackMiddle.getNewValue());
                }
                if (wheelPickerCallbackRight != null) {
                    wheelPickerCallbackRight.onDone(wheelPickerCallbackRight.getOldValue(), wheelPickerCallbackRight.getNewValue());
                }
            }
        }.init(customDialog, pickerConfigEntityLeft.getCallBack(), pickerConfigEntityMiddle.getCallBack(), pickerConfigEntityRight.getCallBack()));
        wvLeft.addChangingListener(new OnWheelChangedListener() {
            private ArrayList<WheelPickerEntity> array;
            private WheelPickerCallback wheelPickerCallback;

            public OnWheelChangedListener init(ArrayList<WheelPickerEntity> a, WheelPickerCallback c) {
                this.array = a;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (this.array != null && (newValue >= 0 && newValue < this.array.size()) && this.array.get(newValue) != null) {
                    if (wheelPickerCallback != null) {
                        final WheelPickerEntity newPickerEntity = array.get(newValue);
                        if (newPickerEntity != null) {
                            wheelPickerCallback.getNewValue().setValue(newPickerEntity.getValue());
                            wheelPickerCallback.getNewValue().setDisplay(newPickerEntity.getDisplay());
                        }
                    }
                }

                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onScrolling(wheelPickerCallback.getOldValue(), wheelPickerCallback.getNewValue());
                }
            }
        }.init(pickerConfigEntityLeft.getArrayList(), pickerConfigEntityLeft.getCallBack()));
        wvMiddle.addChangingListener(new OnWheelChangedListener() {
            private ArrayList<WheelPickerEntity> array;
            private WheelPickerCallback wheelPickerCallback;

            public OnWheelChangedListener init(ArrayList<WheelPickerEntity> a, WheelPickerCallback c) {
                this.array = a;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (this.array != null && (newValue >= 0 && newValue < this.array.size()) && this.array.get(newValue) != null) {
                    if (wheelPickerCallback != null) {
                        final WheelPickerEntity newPickerEntity = array.get(newValue);
                        if (newPickerEntity != null) {
                            wheelPickerCallback.getNewValue().setValue(newPickerEntity.getValue());
                            wheelPickerCallback.getNewValue().setDisplay(newPickerEntity.getDisplay());
                        }
                    }
                }

                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onScrolling(wheelPickerCallback.getOldValue(), wheelPickerCallback.getNewValue());
                }
            }
        }.init(pickerConfigEntityMiddle.getArrayList(), pickerConfigEntityMiddle.getCallBack()));
        wvRight.addChangingListener(new OnWheelChangedListener() {
            private ArrayList<WheelPickerEntity> array;
            private WheelPickerCallback wheelPickerCallback;

            public OnWheelChangedListener init(ArrayList<WheelPickerEntity> a, WheelPickerCallback c) {
                this.array = a;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (this.array != null && (newValue >= 0 && newValue < this.array.size()) && this.array.get(newValue) != null) {
                    if (wheelPickerCallback != null) {
                        final WheelPickerEntity newPickerEntity = array.get(newValue);
                        if (newPickerEntity != null) {
                            wheelPickerCallback.getNewValue().setValue(newPickerEntity.getValue());
                            wheelPickerCallback.getNewValue().setDisplay(newPickerEntity.getDisplay());
                        }
                    }
                }

                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onScrolling(wheelPickerCallback.getOldValue(), wheelPickerCallback.getNewValue());
                }
            }
        }.init(pickerConfigEntityRight.getArrayList(), pickerConfigEntityRight.getCallBack()));

        customDialog.setContentView(dialogView);
        customDialog.show();
    }

    /**
     * show wheel picker of two
     *
     * @param context
     * @param pickerConfigEntityLeft
     * @param pickerConfigEntityMiddle
     */
    public static void showWheelPickerTwoDialog(Context context, WheelPickerConfigEntity pickerConfigEntityLeft, WheelPickerConfigEntity pickerConfigEntityMiddle) {
        if (context == null || pickerConfigEntityLeft == null || pickerConfigEntityMiddle == null) {
            return;
        }

        if (pickerConfigEntityLeft.getCallBack() == null || pickerConfigEntityLeft.getArrayList() == null || pickerConfigEntityLeft.getOldValue() == null) {
            return;
        }
        pickerConfigEntityLeft.getCallBack().getOldValue().setIndex(pickerConfigEntityLeft.getOldValue().getIndex());
        pickerConfigEntityLeft.getCallBack().getOldValue().setValue(pickerConfigEntityLeft.getOldValue().getValue());
        pickerConfigEntityLeft.getCallBack().getOldValue().setDisplay(pickerConfigEntityLeft.getOldValue().getDisplay());

        pickerConfigEntityLeft.getCallBack().getNewValue().setIndex(-1);
        pickerConfigEntityLeft.getCallBack().getNewValue().setValue(null);
        pickerConfigEntityLeft.getCallBack().getNewValue().setDisplay(null);

        if (pickerConfigEntityMiddle.getCallBack() == null || pickerConfigEntityMiddle.getArrayList() == null || pickerConfigEntityMiddle.getOldValue() == null) {
            return;
        }
        pickerConfigEntityMiddle.getCallBack().getOldValue().setIndex(pickerConfigEntityMiddle.getOldValue().getIndex());
        pickerConfigEntityMiddle.getCallBack().getOldValue().setValue(pickerConfigEntityMiddle.getOldValue().getValue());
        pickerConfigEntityMiddle.getCallBack().getOldValue().setDisplay(pickerConfigEntityMiddle.getOldValue().getDisplay());

        pickerConfigEntityMiddle.getCallBack().getNewValue().setIndex(-1);
        pickerConfigEntityMiddle.getCallBack().getNewValue().setValue(null);
        pickerConfigEntityMiddle.getCallBack().getNewValue().setDisplay(null);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_wheelpicker_two, null);
        final RelativeLayout rlContainer = (RelativeLayout) dialogView.findViewById(R.id.rlContainer);
        final CustomTextView ctvCancel = (CustomTextView) dialogView.findViewById(R.id.ctvCancel);
        final CustomTextView ctvSet = (CustomTextView) dialogView.findViewById(R.id.ctvSet);
        final WheelView wvLeft = (WheelView) dialogView.findViewById(R.id.wvLeft);
        final WheelView wvMiddle = (WheelView) dialogView.findViewById(R.id.wvMiddle);
        ctvSet.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        WheelPickerAdapter wpaLeft = new WheelPickerAdapter(context, pickerConfigEntityLeft.getArrayList());
        wvLeft.setViewAdapter(wpaLeft);
        wvLeft.setVisibleItems(5);
        wvLeft.setCurrentItem(pickerConfigEntityLeft.getIndex());

        WheelPickerAdapter wpaMiddle = new WheelPickerAdapter(context, pickerConfigEntityMiddle.getArrayList());
        wvMiddle.setViewAdapter(wpaMiddle);
        wvMiddle.setVisibleItems(5);
        wvMiddle.setCurrentItem(pickerConfigEntityMiddle.getIndex());

        final Dialog customDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        Window dialogWindow = customDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);

        rlContainer.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallbackLeft;
            private WheelPickerCallback wheelPickerCallbackMiddle;

            public View.OnClickListener init(Dialog d, WheelPickerCallback cl, WheelPickerCallback cm) {
                this.dialog = d;
                this.wheelPickerCallbackLeft = cl;
                this.wheelPickerCallbackMiddle = cm;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallbackLeft != null) {
                    wheelPickerCallbackLeft.onCancel();
                }
                if (wheelPickerCallbackMiddle != null) {
                    wheelPickerCallbackMiddle.onCancel();
                }

            }
        }.init(customDialog, pickerConfigEntityLeft.getCallBack(), pickerConfigEntityMiddle.getCallBack()));
        ctvCancel.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallbackLeft;
            private WheelPickerCallback wheelPickerCallbackMiddle;

            public View.OnClickListener init(Dialog d, WheelPickerCallback cl, WheelPickerCallback cm) {
                this.dialog = d;
                this.wheelPickerCallbackLeft = cl;
                this.wheelPickerCallbackMiddle = cm;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallbackLeft != null) {
                    wheelPickerCallbackLeft.onCancel();
                }
                if (wheelPickerCallbackMiddle != null) {
                    wheelPickerCallbackMiddle.onCancel();
                }

            }
        }.init(customDialog, pickerConfigEntityLeft.getCallBack(), pickerConfigEntityMiddle.getCallBack()));
        ctvSet.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallbackLeft;
            private WheelPickerCallback wheelPickerCallbackMiddle;
            private WheelPickerCallback wheelPickerCallbackRight;

            public View.OnClickListener init(Dialog d, WheelPickerCallback cl, WheelPickerCallback cm) {
                this.dialog = d;
                this.wheelPickerCallbackLeft = cl;
                this.wheelPickerCallbackMiddle = cm;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallbackLeft != null) {
                    wheelPickerCallbackLeft.onDone(wheelPickerCallbackLeft.getOldValue(), wheelPickerCallbackLeft.getNewValue());
                }
                if (wheelPickerCallbackMiddle != null) {
                    wheelPickerCallbackMiddle.onDone(wheelPickerCallbackMiddle.getOldValue(), wheelPickerCallbackMiddle.getNewValue());
                }
                if (wheelPickerCallbackRight != null) {
                    wheelPickerCallbackRight.onDone(wheelPickerCallbackRight.getOldValue(), wheelPickerCallbackRight.getNewValue());
                }
            }
        }.init(customDialog, pickerConfigEntityLeft.getCallBack(), pickerConfigEntityMiddle.getCallBack()));
        wvLeft.addChangingListener(new OnWheelChangedListener() {
            private ArrayList<WheelPickerEntity> array;
            private WheelPickerCallback wheelPickerCallback;

            public OnWheelChangedListener init(ArrayList<WheelPickerEntity> a, WheelPickerCallback c) {
                this.array = a;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (this.array != null && (newValue >= 0 && newValue < this.array.size()) && this.array.get(newValue) != null) {
                    if (wheelPickerCallback != null) {
                        final WheelPickerEntity newPickerEntity = array.get(newValue);
                        if (newPickerEntity != null) {
                            wheelPickerCallback.getNewValue().setIndex(newPickerEntity.getIndex());
                            wheelPickerCallback.getNewValue().setValue(newPickerEntity.getValue());
                            wheelPickerCallback.getNewValue().setDisplay(newPickerEntity.getDisplay());
                        }
                    }
                }

                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onScrolling(wheelPickerCallback.getOldValue(), wheelPickerCallback.getNewValue());
                }
            }
        }.init(pickerConfigEntityLeft.getArrayList(), pickerConfigEntityLeft.getCallBack()));
        wvMiddle.addChangingListener(new OnWheelChangedListener() {
            private ArrayList<WheelPickerEntity> array;
            private WheelPickerCallback wheelPickerCallback;

            public OnWheelChangedListener init(ArrayList<WheelPickerEntity> a, WheelPickerCallback c) {
                this.array = a;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (this.array != null && (newValue >= 0 && newValue < this.array.size()) && this.array.get(newValue) != null) {
                    if (wheelPickerCallback != null) {
                        final WheelPickerEntity newPickerEntity = array.get(newValue);
                        if (newPickerEntity != null) {
                            wheelPickerCallback.getNewValue().setIndex(newPickerEntity.getIndex());
                            wheelPickerCallback.getNewValue().setValue(newPickerEntity.getValue());
                            wheelPickerCallback.getNewValue().setDisplay(newPickerEntity.getDisplay());
                        }
                    }
                }

                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onScrolling(wheelPickerCallback.getOldValue(), wheelPickerCallback.getNewValue());
                }
            }
        }.init(pickerConfigEntityMiddle.getArrayList(), pickerConfigEntityMiddle.getCallBack()));

        customDialog.setContentView(dialogView);
        customDialog.show();
    }


    public static void showWhiteToast(Context context, String msg) {
        if (context == null) {
            return;
        }

        Toast toast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
        if (WhiteLabelApplication.getPhoneConfiguration() != null && WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() != 0) {
            toast.setGravity(Gravity.BOTTOM, 0, (int) (WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() * 0.25));
        }

        LinearLayout toastView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_prompt_productdetail_notenoughinventory, null);
        TextView textView = (TextView) toastView.findViewById(R.id.tv_text);
        textView.setText(msg);
        toast.setView(toastView);
        toast.show();
    }

    public static void updateCartCount(TextView textView, long cartCount) {
        if (cartCount > 0 && cartCount <= 99) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(cartCount + "");
        } else if (cartCount > 99) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("99+");
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}
