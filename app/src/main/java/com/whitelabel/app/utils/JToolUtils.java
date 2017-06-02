package com.whitelabel.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.BaseActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.fragment.BaseFragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

/**
 * Created by imaginato on 2015/6/10.
 */
public class JToolUtils {
    private final static String TAG = "JToolUtils";

    //输出流进行克隆
    public static Object cloneObject(Object obj) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(obj);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            return in.readObject();
        }catch (Exception e){
            e.printStackTrace();
            return obj;
        }
    }
    //获取千元分隔符
    public static String thousandsSeparators(String num){
        try {
            long n = Long.parseLong(num);
            DecimalFormat df = new DecimalFormat("#,###");
            String m = df.format(n);
            return m;
        }catch (Exception e){
            e.printStackTrace();
        }
        return num;
    }
    public static boolean isNotificationEnabled(Context context) {
        //api 4.4+
        try {
            boolean boo= NotificationManagerCompat.from(context).areNotificationsEnabled();
//            if (Build.VERSION.SDK_INT <Build.VERSION_CODES.KITKAT) {
//              return true;
//            }
//            反射获取
//            String CHECK_OP_NO_THROW = "checkOpNoThrow";
//            String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
//            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//            ApplicationInfo appInfo = context.getApplicationInfo();
//            String pkg = context.getApplicationContext().getPackageName();
//            int uid = appInfo.uid;
//            Class appOpsClass = null; /* Context.APP_OPS_MANAGER */
//            appOpsClass = Class.forName(AppOpsManager.class.getName());
//            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
//            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
//            int value = (int) opPostNotificationValue.get(Integer.class);
//            boolean boo = ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
              return boo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public static void showAPKKeyHash(Activity activity) {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo("com.whitelabel.app", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                JLogUtils.i("Martin", "KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException ex) {
            JLogUtils.e("Martin", "showAPKKeyHash", ex);
        } catch (NoSuchAlgorithmException ex) {
            JLogUtils.e("Martin", "showAPKKeyHash", ex);
        } catch (Exception ex) {
            JLogUtils.e("Martin", "showAPKKeyHash", ex);
        }
    }


    public  static void closeKeyBoard(Activity activity){
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean checkActivityIsFinish(BaseActivity baseActivity) {
        if (baseActivity != null && !baseActivity.checkIsFinished()) {
            return false;
        } else {
            return true;
        }
    }

    public static  String unescapeJavaString(String st) {

        StringBuilder sb = new StringBuilder(st.length());

        for (int i = 0; i < st.length(); i++) {
            char ch = st.charAt(i);
            if (ch == '\\') {
                char nextChar = (i == st.length() - 1) ? '\\' : st
                        .charAt(i + 1);
                // Octal escape?
                if (nextChar >= '0' && nextChar <= '7') {
                    String code = "" + nextChar;
                    i++;
                    if ((i < st.length() - 1) && st.charAt(i + 1) >= '0'
                            && st.charAt(i + 1) <= '7') {
                        code += st.charAt(i + 1);
                        i++;
                        if ((i < st.length() - 1) && st.charAt(i + 1) >= '0'
                                && st.charAt(i + 1) <= '7') {
                            code += st.charAt(i + 1);
                            i++;
                        }
                    }
                    sb.append((char) Integer.parseInt(code, 8));
                    continue;
                }
                switch (nextChar) {
                    case '\\':
                        ch = '\\';
                        break;
                    case 'b':
                        ch = '\b';
                        break;
                    case 'f':
                        ch = '\f';
                        break;
                    case 'n':
                        ch = '\n';
                        break;
                    case 'r':
                        ch = '\r';
                        break;
                    case 't':
                        ch = '\t';
                        break;
                    case '\"':
                        ch = '\"';
                        break;
                    case '\'':
                        ch = '\'';
                        break;
                    case 'u':
                        if (i >= st.length() - 5) {
                            ch = 'u';
                            break;
                        }
                        int code = Integer.parseInt(
                                "" + st.charAt(i + 2) + st.charAt(i + 3)
                                        + st.charAt(i + 4) + st.charAt(i + 5), 16);
                        sb.append(Character.toChars(code));
                        i += 5;
                        continue;
                }
                i++;
            }
            sb.append(ch);
        }
        return sb.toString();
    }


    public static boolean checkActivityIsInvisible(BaseActivity baseActivity) {
        if (baseActivity != null && !baseActivity.checkIsFinished() && !baseActivity.checkIsInvisible()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkActivityIsPaused(BaseActivity baseActivity) {
        if (baseActivity != null && !baseActivity.checkIsFinished() && !baseActivity.checkIsInvisible() && !baseActivity.checkIsPaused()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkFragmentIsAdded(BaseFragment baseFragment) {
        if (baseFragment != null && baseFragment.isAdded()) {
            return true;
        } else {
            return false;
        }
    }


    public static void  closeKeyBoard(Context context,EditText  etText){
        InputMethodManager  imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etText.getWindowToken(),0);

    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);


    }

    /**
     * 获得状态栏/通知栏的高度
     */
    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }



    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static int getColor(int id){
        return ContextCompat.getColor(WhiteLabelApplication.getInstance().getBaseContext(), id);
    }
    public static Drawable getDrawable(int id){
        return ContextCompat.getDrawable(WhiteLabelApplication.getInstance().getBaseContext(), id);
    }
    public static boolean  expireHandler(Activity context,String errorMsg,int requestCode){
        String SESSION_EXPIRED = "session expired,login again please";

            if ((!JDataUtils.isEmpty(errorMsg)) && (errorMsg.contains(SESSION_EXPIRED))) {

                Intent intent = new Intent();
                intent.setClass(context, LoginRegisterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("expire", true);
                intent.putExtras(bundle);
               context.startActivityForResult(intent, requestCode);
                return true;
            }
        return false;
    }
    public static  void webViewFont(Context context,WebView mWebView,String str ,float textSize){
        String html = FileUtils.readAssest(context, "html/product_detail_web.html");
        String colorStr=ColorUtils.toHexEncoding(context.getResources().getColor(R.color.appColorPrimary)).substring(2);
//        html = html.replace("@fontName0", "LatoRegular");
//        html = html.replace("@fontPath0", "../fonts/Lato-Regular.ttf");// assets相对路径
//        html = html.replace("@fontName1", "LatoBold");
//        html = html.replace("@fontPath1", "../fonts/Lato-Bold.ttf");// assets相对路径
        html = html.replace("@mytext", str);
        html = html.replace("@myTextsize", textSize+"px");
//        html = html.replace("@myClickColor", "#"+colorStr);
        String baseurl = "file:///android_asset/html/";
        mWebView.loadDataWithBaseURL(baseurl, html, "text/html", "UTF-8", null);
    }
    public static  void webViewFont(Context context,WebView mWebView,String str ){
        String html = FileUtils.readAssest(context, "html/content.html");
//        html = html.replace("@fontName0", "LatoRegular");
//        html = html.replace("@fontPath0", "../fonts/Lato-Regular.ttf");// assets相对路径
        html = html.replace("@mytext", str);
        String baseurl = "file:///android_asset/html/";
        mWebView.loadDataWithBaseURL(baseurl, html, "text/html", "UTF-8", null);
    }
    //处理service已经加了style的html
    public static  String replaceFont(String str){
        if(!TextUtils.isEmpty(str)) {
            str = str.replace("ArvoRegular.ttf", "");
            str = str.replace("PTSansRegular.ttf", "");
        }
        return str;
    }

    public static  void webViewFont(Context context,WebView mWebView,String str,String fileName){
        String html = FileUtils.readAssest(context, fileName);
//        html = html.replace("@fontName0", "MyLatoRegular");
//        html = html.replace("@fontPath0", "../fonts/Lato-Regular.ttf");// assets相对路径
        html = html.replace("@mytext", str);
        String baseurl = "file:///android_asset/html/";
        mWebView.loadDataWithBaseURL(baseurl, html, "text/html", "UTF-8", null);
    }


    public static boolean  expireHandler(Context context, Fragment fragment, String errorMsg, int requestCode){
        String SESSION_EXPIRED = "session expired,login again please";

        if ((!JDataUtils.isEmpty(errorMsg)) && (errorMsg.contains(SESSION_EXPIRED))) {

            Intent intent = new Intent();
            intent.setClass(context, LoginRegisterActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("expire", true);
            intent.putExtras(bundle);
            fragment.startActivityForResult(intent, requestCode);
            return true;
        }
        return false;
    }


    public static boolean  expireHandler(Context context, android.app.Fragment fragment,String errorMsg,int requestCode){
        String SESSION_EXPIRED = "session expired,login again please";

        if ((!JDataUtils.isEmpty(errorMsg)) && (errorMsg.contains(SESSION_EXPIRED))) {
            Intent intent = new Intent();
            intent.setClass(context, LoginRegisterActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("expire", true);
            intent.putExtras(bundle);
            fragment.startActivityForResult(intent, requestCode);
            return true;
        }
        return false;
    }

    public  static void setWebViewText(Context context,String str,WebView webView){
//    String html = FileUtils.readAssest(context, "html/content.html");
//    html = html.replace("@fontName0", "PTSansRegular");
//    html = html.replace("@fontPath0", "../fonts/PTSansRegular.ttf");// assets相对路径
//    html = html.replace("@mytext", str);
//        String baseurl = "file:///android_asset/html/";
        webView.loadDataWithBaseURL(null, str, "text/html", "UTF-8", null);
    }

    public static boolean hasNavBar(Context context) {
        Resources resources = context.getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            return resources.getBoolean(id);
        } else {    // Check for keys
            boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !hasMenuKey && !hasBackKey;
        }
    }




    public static String getAppVersion() {
        String version = "0";
        try {
            version = WhiteLabelApplication.getInstance().getPackageManager().getPackageInfo(
                    WhiteLabelApplication.getInstance().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
}
