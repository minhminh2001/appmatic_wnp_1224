package com.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.R;
import com.common.widget.CustomDialog;
import com.common.widget.CustomMyDialog;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */
public class DialogUtils {
    /**
     * 自定义弹窗询问带title
     * @return
     */
    public static  Dialog showCustomDialog(Context context , String title,String message,String btnLeft, String btnRight, android.content.DialogInterface.OnClickListener  btnLeftListener,android.content.DialogInterface.OnClickListener  btnRightListener){
        Dialog dialog=null;
        CustomMyDialog.Builder builder = new CustomMyDialog .Builder(context);
        if(!TextUtils.isEmpty(title)){
            builder.setTitle(title);
        }
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
        win.setWindowAnimations(R.style.alertDialogAnim);
        dialog.show();
        return dialog;
    }
    /**
     * 自定义弹窗询问
     * @return
     */
    public static  Dialog showCustomDialog(Context context , String message,String btnLeft, String btnRight, android.content.DialogInterface.OnClickListener  btnLeftListener,android.content.DialogInterface.OnClickListener  btnRightListener){
        return showCustomDialog(context,"",message,btnLeft,btnRight,btnLeftListener,btnRightListener);
    }
    /**
     * 自定义弹窗询问
     * @return
     */
    public static  Dialog showCustomListDialog(Context context ,
                                               String btnRight, android.content.DialogInterface.OnClickListener btnOnClickListener,
                                               List<String> list, AdapterView.OnItemClickListener onItemClickListener, ArrayAdapter<String> adapter
                                               ){

        CustomMyDialog.Builder builder = new CustomMyDialog .Builder(context);

        builder.setNegativeButton(btnRight, btnOnClickListener);
        builder.setItems(list,onItemClickListener);
        builder.setItemAdapter(adapter);

        Dialog dialog = builder.create();
        Window win = dialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        dialog.setCancelable(true);
        win.setWindowAnimations(R.style.alertDialogAnim);
        dialog.show();
        return dialog;
    }
    /**
     * 默认弹窗询问
     * @return
     */
    public static Dialog showDialog(Context context, String title, String message,
                                    String button1, DialogInterface.OnClickListener click1, String button2,DialogInterface.OnClickListener click2){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        if(!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if(!TextUtils.isEmpty(message)){
            builder.setMessage(message);
        }
        if(click1!=null) {
            builder.setPositiveButton(button1, click1);
        }
        if(click2!=null){
            builder.setNegativeButton(button2,click2);
        }
        Dialog dialog=builder.create();
        dialog.show();
        return dialog;
    }

    /**
     * 加载圈
     */
    public static Dialog showProgressDialog(Context context){
        Dialog dialog=new CustomDialog(context);
        dialog.show();
        return dialog;
    }
    public static Dialog showProgressDialog(Context context,Dialog dialog){
        if(dialog!=null) {
            dialog.show();
        }else{
            dialog=new CustomDialog(context);
        }
        return dialog;
    }
    public static Dialog showProgressDialog(Context context,String gravity){
        Dialog dialog=new CustomDialog(context,gravity);
        dialog.show();
        return dialog;
    }

    /**
     * 有取消按钮
     */
    public static   Dialog showListDialog(Context context ,String title,String [] list,DialogInterface.OnClickListener clickListener,
                                          String button1, DialogInterface.OnClickListener click1
                                          ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(list, clickListener);
        if(click1!=null) {
            builder.setNegativeButton(button1, click1);
        }

        Dialog dialog=builder.create();
        dialog.show();
        return dialog;
    }

    /**
     * 没有按钮
     */
    public static   Dialog showListDialog(Context context ,String title,String [] list,DialogInterface.OnClickListener clickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(list, clickListener);
        Dialog dialog=builder.create();
        dialog.show();
        return dialog;
    }

    /**
     * toast提示
     */
    public static void showToast(Context context, String title, String message) {
        if (context == null) {
            return;
        }
        Toast toast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, (int) (ScreenHelper.getHeight(context)* 0.25));
        LinearLayout toastView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_customtoast, null);
        TextView ctvTitle = (TextView) toastView.findViewById(R.id.vTextViewTitle);
        TextView ctvContent = (TextView) toastView.findViewById(R.id.vTextViewMessage);

        if (TextUtils.isEmpty(title)) {
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
}
