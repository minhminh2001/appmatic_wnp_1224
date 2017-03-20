package com.common.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyBoardUtils {

    private KeyBoardUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 切换软键盘的状态
     * 如当前为收起变为弹出,若当前为弹出变为收起
     */
    public static void toggleKeybord(EditText edittext) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public static void hideKeybord(Activity activity) {
        try {
            if(activity!=null) {
                View decorView = activity.getWindow().peekDecorView();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(decorView.getWindowToken(), 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 强制隐藏输入法键盘
     */
    public static void hideKeybord(EditText edittext) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
        }
    }
    /**
     * 强制显示输入法键盘
     */
    public static void showKeybord(EditText edittext) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(edittext, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 输入法是否显示
     */
    public static boolean isKeybord(EditText edittext) {
        boolean bool = false;
        InputMethodManager inputMethodManager = (InputMethodManager)
                edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            bool = true;
        }
        return bool;
    }
}