package com.whitelabel.app.utils;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.HttpUrl;


public class JDataUtils {
    private static final String TAG = "JDataUtils";

    public static String formatDouble(String priceStr) {
        if (TextUtils.isEmpty(priceStr)) {
            return priceStr;
        }
        try {
            Double price = Double.parseDouble(priceStr);
            if (price == 0) {
                return "0.00";
            }
            DecimalFormat df = new DecimalFormat("#,##0.00");
            priceStr = df.format(price);
        } catch (Exception ex) {
            return priceStr;
        }
        return priceStr;
    }


    public static String formatThousand(String priceStr) {
        if (TextUtils.isEmpty(priceStr)) {
            return priceStr;
        }
        try {
            Double price = Double.parseDouble(priceStr);
            DecimalFormat df = new DecimalFormat("#,##0");
            priceStr = df.format(price);
        } catch (Exception ex) {
            return priceStr;
        }
        return priceStr;
    }

    public static int dp2px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);

    }

    public static int dp2Px(int dp) {
        try {
            return (int) (dp * WhiteLabelApplication.getPhoneConfiguration().getScreenDensity() + 0.5f);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "dp2Px", ex);
            return dp;
        }
    }

    public static int px2Dp(int px) {
        try {
            return (int) (px / WhiteLabelApplication.getPhoneConfiguration().getScreenDensity() + 0.5f);
        } catch (Exception e) {
            JLogUtils.e(TAG, "px2Dp", e);
            return px;
        }
    }

    public static int px2sp(int px) {
        try {
            return (int) (px / WhiteLabelApplication.getPhoneConfiguration().getScreenScaledDensity() + 0.5f);
        } catch (Exception e) {
            JLogUtils.e(TAG, "px2sp", e);
            return px;
        }
    }

    public static float px2sp(float px) {
        try {
            return (px / WhiteLabelApplication.getPhoneConfiguration().getScreenScaledDensity() + 0.5f);
        } catch (Exception e) {
            JLogUtils.e(TAG, "px2sp", e);
            return px;
        }
    }

    public static float sp2px(float sp) {
        try {
            return (int) (sp * WhiteLabelApplication.getPhoneConfiguration().getScreenScaledDensity() + 0.5f);
        } catch (Exception e) {
            JLogUtils.e(TAG, "sp2px", e);
            return sp;
        }
    }

    public static int string2int(String str) {
        if (str == null || str.equals("")) {
            return 0;
        }

        int result = 0;
        try {
            result = Integer.parseInt(str);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "string2int", ex);
        }
        return result;
    }

    public static String int2String(int value) {
        return ("" + value);
    }

    public static long string2long(String str) {
        if (str == null || str.equals("")) {
            return 0;
        }

        long result = 0;
        try {
            result = Long.parseLong(str);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "string2long", ex);
        }
        return result;
    }

    public static String long2string(long value) {
        return "" + value;
    }

    public static double string2Double(String value) {
        if (isEmpty(value)) {
            return 0.0;
        }

        double result = 0.0;
        try {
            result = Double.parseDouble(value);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "string2Double", ex);
        }
        return result;
    }

    public static String double2String(double value) {
        return "" + value;
    }

    public  static boolean isEmpty(String str) {
        if(str==null||"".equals(str)){
            return true;
        }
        return false;
    }

    public  static boolean isEmpty(EditText et) {
        if (et == null) {
            return true;
        }
        if (et.getText() == null) {
            return true;
        }
        String str = et.getText().toString().trim();
        return isEmpty(str);
    }

    public  static boolean isEmpty(TextView tv) {
        if (tv == null) {
            return true;
        }
        if (tv.getText() == null) {
            return true;
        }
        String str = tv.getText().toString().trim();
        return isEmpty(str);
    }

    public  static boolean isEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(email);
        return m.find();
    }

    public  static boolean isEmail(EditText et) {
        if (isEmpty(et)) {
            return false;
        }

        String email = et.getText().toString().trim();
        return isEmail(email);
    }

    public static boolean isAreaCode(String code) {
        if (isEmpty(code)) {
            return false;
        }
        return code.matches("^[+]\\d{2}|\\d{2}$");
    }

    public static boolean isAreaCode(EditText et) {
        if (isEmpty(et)) {
            return false;
        }

        String code = et.getText().toString().trim();
        return isAreaCode(code);
    }

    public static boolean isPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return phone.matches("^\\d{8,11}$");
    }

    //, only digits  length>=7
    public static boolean isPhoneNumber(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
//        return phone.matches("^\\d{7,15}$");
        return phone.length()>=7;
    }

    public static boolean isPhone(EditText et) {
        if (isEmpty(et)) {
            return false;
        }

        String phone = et.getText().toString().trim();
        return isPhone(phone);
    }

    /**
     * "+" + "62" + "3245674554"
     */
    public static boolean isFullPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return phone.matches("^[+]\\d{10,13}$");
    }

    /**
     * "+" + "62" + "3245674554"
     */
    public static boolean isFullPhone(EditText et) {
        if (isEmpty(et)) {
            return false;
        }

        String phone = et.getText().toString().trim();
        return isFullPhone(phone);
    }

    public static boolean isPassword(String password) {
        if (isEmpty(password)) {
            return false;
        }

        return password.length() >= 4;
    }

    public static boolean isPassword(EditText et) {
        if (isEmpty(et)) {
            return false;
        }

        String password = et.getText().toString().trim();
        return isPassword(password);
    }

    public  static String getMD5Result(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            JLogUtils.e(TAG, "getMD5Result", e);
            return s;
        }
    }

    /**
     * @param color   关键字颜色
     * @param text    文本
     * @param keyword 关键字
     * @return
     */
    public static SpannableString keyWordHighLighting(int color, String text,
                                                      String keyword) {
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(color), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;

    }

    /**
     * @param color   关键字颜色
     * @param text    文本
     * @param keyword 多个关键字
     * @return
     */
    public static SpannableString keyWordHighLighting(int color, String text,
                                                      String[] keyword) {
        SpannableString s = new SpannableString(text);
        for (int i = 0; i < keyword.length; i++) {
            Pattern p = Pattern.compile(keyword[i]);
            Matcher m = p.matcher(s);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                s.setSpan(new ForegroundColorSpan(color), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }
        return s;

    }
    /**
     * emoji表情替换
     *
     * @param source 原字符串
     * @return 过滤后的字符串
     */
    private static final String EMOJI_FORMAT1="[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]";
    private static final String EMOJI_FORMAT2="[\uD83C\uDC00-\uD83C\uDFFF]|[\uD83D\uDC00-\uD83D\uDFFF]|[☀-⟿]";
    public static String filterEmoji(String source) {
        if(!TextUtils.isEmpty(source)){
            if(isEmoji(source)){
                return source.replaceAll(EMOJI_FORMAT2, "");
            }else{
                return source;
            }
        }else{
            return source;
        }
    }
    public static boolean isEmoji(String string) {
        Pattern p = Pattern.compile(EMOJI_FORMAT2,
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }
    // 判断一个字符串是否含有中文
    public static boolean isChinese(String str) {
        if (str == null) return false;
        for (char c : str.toCharArray()) {
            if (isChinese(c)) return true;// 有一个中文字符就返回
        }
        return false;
    }

    // 判断一个字符是否是中文
    public static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }

    public static boolean isInteger(String integer) {
        if (isEmpty(integer)) {
            return false;
        }
        return integer.matches("^\\d+$");
    }

    public static boolean isLetters(String str) {
        if (isEmpty(str)) {
            return false;
        } else {
            return str.matches("^[a-zA-Z]+$");
        }
    }

    public static boolean isStringBlank(CharSequence charSequence) {
        return charSequence == null || TextUtils.isEmpty(charSequence.toString().trim());
    }

    public static String sqliteEscapeValue(String keyWord) {
        if (keyWord == null) {
            return null;
        }

        keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
        keyWord = keyWord.replace("[", "/[");
        keyWord = keyWord.replace("]", "/]");
        keyWord = keyWord.replace("%", "/%");
        keyWord = keyWord.replace("&", "/&");
        keyWord = keyWord.replace("_", "/_");
        keyWord = keyWord.replace("(", "/(");
        keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }

    public static Object sqliteEscapeValue(Object value) {
        Object valueObject = value;
        try {
            if (value instanceof String) {
                valueObject = sqliteEscapeValue((String) value);
            }
        } catch (Exception e) {
            JLogUtils.e(TAG, "sqliteEscapeValue", e);
        }
        return valueObject;
    }

    public static String sqliteDisEscapeValue(String keyWord) {
        if (keyWord == null) {
            return null;
        }

        keyWord = keyWord.replace("//", "/");
        keyWord = keyWord.replace("''", "'");
        keyWord = keyWord.replace("/[", "[");
        keyWord = keyWord.replace("/]", "]");
        keyWord = keyWord.replace("/%", "%");
        keyWord = keyWord.replace("/&", "&");
        keyWord = keyWord.replace("/_", "_");
        keyWord = keyWord.replace("/(", "(");
        keyWord = keyWord.replace("/)", ")");
        return keyWord;
    }

    public static Object sqliteDisEscapeValue(Object value) {
        Object valueObject = value;
        try {
            if (value instanceof String) {
                valueObject = sqliteDisEscapeValue((String) value);
            }
        } catch (Exception e) {
            JLogUtils.e(TAG, "sqliteDisEscapeValue", e);
        }
        return valueObject;
    }

    public static String convertUrl(String url) {
        if (url == null || url.equals("")) {
            return url;
        }

        String result = url;
        try {
            result = url.replaceAll("\\+", "%2B");
            result = result.replaceAll(" ", "%20");
            result = result.replaceAll("/", "%2F");
            result = result.replaceAll("\\?", "%3F");
            result = result.replaceAll("%", "%25");
            result = result.replaceAll("#", "%23");
            result = result.replaceAll("&", "%26");
            result = result.replaceAll("=", "%3D");
        } catch (Exception ex) {
            JLogUtils.e(TAG, "convertUrl", ex);
        }
        return result;
    }

    public static String deConvertUrl(String url) {
        if (url == null || url.equals("")) {
            return url;
        }

        String result = url;
        try {
            result = url.replaceAll("%2B", "+");
            result = result.replaceAll("%20", " ");
            result = result.replaceAll("%2F", "/");
            result = result.replaceAll("%3F", "?");
            result = result.replaceAll("%25", "%");
            result = result.replaceAll("%23", "#");
            result = result.replaceAll("%26", "&");
            result = result.replaceAll("%3D", "=");
        } catch (Exception ex) {
            JLogUtils.e(TAG, "deConvertUrl", ex);
        }
        return result;
    }

    public static String htmlEscapeValue(String html) {
        if (html == null || html.equals("")) {
            return html;
        }

        String result = html;
        try {
            result = html.replaceAll("&", "&amp;");
            result = result.replaceAll("<", "&lt;");
            result = result.replaceAll(">", "&gt;");
        } catch (Exception ex) {
            JLogUtils.e(TAG, "htmlEscapeValue", ex);
        }
        return result;
    }

    public static String htmlDisEscapeValue(String html) {
        if (html == null || html.equals("")) {
            return html;
        }

        String result = html;
        try {
            result = html.replaceAll("&amp;", "&");
            result = result.replaceAll("&lt;", "<");
            result = result.replaceAll("&gt;", ">");
        } catch (Exception ex) {
            JLogUtils.e(TAG, "htmlDisEscapeValue", ex);
        }
        return result;
    }


    public static String parserHtmlContent(String content) {
        if (!JDataUtils.isEmpty(content)) {
            Spanned spanned = Html.fromHtml(content);
            if (spanned != null) {
                Spanned otherspanned = Html.fromHtml(spanned.toString());
                if (otherspanned != null) {
                    content = otherspanned.toString();
                }
            }
        }
        return content;
    }

    public static Double formatDoubleWithSpecifiedDecimals(Double d, int scale) {

//        DecimalFormat df   = new DecimalFormat("######0.00");
//
//       return df.format(d);

        BigDecimal bigDecimal = new BigDecimal(d);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static String formatDoubleWithSpecifiedDecimals(Double d) {
        if (d == 0) {
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);

//        BigDecimal bigDecimal = new BigDecimal(d);
//        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static int compare(float leftValue, float rightValue) {
        Float leftValueFloat = Float.valueOf(leftValue);
        Float rightValueFloat = Float.valueOf(rightValue);

        return leftValueFloat.compareTo(rightValueFloat);
    }

    public static String formatFloatWithSpecifiedDecimals(float value) {
        String result = "" + value;
        try {
            result = String.format("%.2f", value);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "formatFloatWithSpecifiedDecimals", ex);
        }
        return result;
    }

    public static String formatPrice(int price) {
        return WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" " + price + ".00";
    }

    public static String getFirstLetterToUpperCase(String title) {
        if (!TextUtils.isEmpty(title)) {
            title = title.toLowerCase();
            String[] split = title.trim().split(" ");
            StringBuilder newTitle = new StringBuilder();
            if (split.length != 0) {
                for (String cell : split) {
                    if (cell.length() > 1) {
                        if (("rm").equals(cell.substring(0, 2)) || ("RM").equals(cell.substring(0, 2))) {
                            newTitle.append(cell.toUpperCase()).append(" ");
                        } else {
                            newTitle.append(cell.substring(0, 1).toUpperCase()).append(cell.substring(1)).append(" ");
                        }
                    } else {
                        newTitle.append(cell.toUpperCase()).append(" ");
                    }
                }
            }
            return newTitle.toString().trim();
        }
        return "";
    }

    public static boolean errorMsgHandler(Context context, String errorMsg) {
        if (TextUtils.isEmpty(errorMsg)) {
            return false;
        }
        if (errorMsg.indexOf("ConnectTimeoutException") != -1 || errorMsg.indexOf("SocketTimeoutException") != -1 || errorMsg.indexOf("HttpHostConnectException") != -1 ||
                errorMsg.indexOf("UnknownHostException") != -1 || errorMsg.indexOf("Timeout") != -1 || errorMsg.indexOf("javax.net") != -1) {
            if (context != null && !((Activity) context).isFinishing()) {
                Toast.makeText(context, context.getResources().getString(R.string.Global_Error_Internet), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }

    public static String extractId(String uri) {
        HttpUrl url = HttpUrl.parse(uri);
        return url.queryParameter("data");
    }

    public static String extractType(String uri) {
        HttpUrl url = HttpUrl.parse(uri);
        return url.queryParameter("actionType");
    }

    public static boolean isValidUrl(String url) {
        String quoted_url = Pattern.quote(url);
        String http = "http";
        String https = "https";
        String validDomain = "gemfive.com";

        return (Pattern.compile(http, Pattern.CASE_INSENSITIVE).matcher(quoted_url).find() || Pattern.compile(https, Pattern.CASE_INSENSITIVE).matcher(quoted_url).find()) &&
                Pattern.compile(validDomain, Pattern.CASE_INSENSITIVE).matcher(quoted_url).find();
    }
}
