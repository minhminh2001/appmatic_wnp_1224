package com.whitelabel.app.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import com.whitelabel.app.R;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LanguageUtils {

    public enum LanguageType{
        LANGUAGE_AUTO,
        LANGUAGE_SIMPLIFIED_CHINESE,
        LANGUAGE_TRADITIONAL_CHINESE,
        LANGUAGE_ENGLISH,
    }

    public static final String LANGUAGE_CODE_AUTO = "auto";
    public static final String LANGUAGE_CODE_ENGLISH = "en_US";
    public static final String LANGUAGE_CODE_SIMPLIFIED_CHINESE = "ZH";
    public static final String LANGUAGE_CODE_TRADITIONAL_CHINESE = "zh_HK";

    public static final String LANGUAGE_CONFIG = "languga_config";
    public static final String CURRENT_LANGUAGE = "current_languga";

    public static final int ARR_INDEX_AUTO = 0;
    public static final int ARR_INDEX_ENGLISH = 1;
    public static final int ARR_INDEX_SIMPLIFIED_CHINESE = 2;
    public static final int ARR_INDEX_TRADITIONAL_CHINESE = 3;

    private static LanguageUtils instance;
    private Map<LanguageType, String> supportedLanguages = new HashMap<>();
    private Context context;
    private ContextWrapper contextWrapper;

    public static void init(Context context){
        getInstance().setContext(context);
    }

    public static Map<LanguageType, String> getSupportedLanguages(){
        return getInstance().getSupportedLanguageList();
    }

    public static void setSupportedLanguages(List<String> languageCode){
        getInstance().setSupportedLanguageList(languageCode);
    }

    public static LanguageType getCurrentLanguage(){
        return getInstance().getCurrentLanguageType();
    }

    public static String getCurrentLanguageCode(){
        LanguageType languageType = getInstance().getCurrentLanguageType();
        return getInstance().getLanguageCodeByType(languageType);
    }

    public static boolean changeLanguage(LanguageType languageType){

        if(languageType == null
                || getInstance().getContext() == null) {
            return false;
        }

        Locale locale = getInstance().getLocaleByLanguageType(languageType);
        if(locale == null){
            return false;
        }

        Context context = getInstance().getContext();
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        // Android 4.4 or above
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }

        // Android 7.0 or above
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

            context = context.createConfigurationContext(configuration);
            getInstance().setContextWrapper(new ContextWrapper(context));
        }
        // Android 7.0 or below
        else{
            resources.updateConfiguration(configuration, displayMetrics);
        }

        // save current use language to local
        getInstance().saveCurrentLanguageType(languageType);

        return true;
    }

    public static ContextWrapper getNewContextWrapper(){
       return getInstance().getContextWrapper();
    }

    private static LanguageUtils getInstance(){
        if(instance == null){
            instance = new LanguageUtils();
        }

        return instance;
    }

    private LanguageUtils(){

    }

    private void setContext(Context context){
        this.context = context;
    }

    private void setContextWrapper(ContextWrapper contextWrapper){
        this.contextWrapper = contextWrapper;
    }

    private ContextWrapper getContextWrapper(){
        return contextWrapper;
    }

    private Map<LanguageType, String> getSupportedLanguageList(){
        return supportedLanguages;
    }

    private void setSupportedLanguageList(List<String> languageCodes){

        clearSupportedLanguageList();
        if(languageCodes == null){
            return;
        }

        for(String languageCode : languageCodes){
            LanguageType languageType = getLanguageTypeByCode(languageCode);

            //  The language not supported at app
            if(languageType == null) {
                continue;
            }

            // The language is supported at app
            String languageName = getLanguageNameByType(languageType);
            addLanguageToSupportedLanguageList(languageType, languageName);
        }

        // Add auto follow system language
        addLanguageToSupportedLanguageList(LanguageType.LANGUAGE_AUTO
                , getLanguageNameByType(LanguageType.LANGUAGE_AUTO));
    }

    private void clearSupportedLanguageList(){
        supportedLanguages.clear();
    }

    private void addLanguageToSupportedLanguageList(LanguageType key, String value){

        if(key == null){
            return;
        }

        supportedLanguages.put(key, value);
    }

    private LanguageType getLanguageTypeByCode(String code){

        LanguageType languageType;
        switch (code){
            case LANGUAGE_CODE_AUTO:
                languageType = LanguageType.LANGUAGE_AUTO;
                break;
            case LANGUAGE_CODE_SIMPLIFIED_CHINESE:
                languageType = LanguageType.LANGUAGE_SIMPLIFIED_CHINESE;
                break;
            case LANGUAGE_CODE_TRADITIONAL_CHINESE:
                languageType = LanguageType.LANGUAGE_TRADITIONAL_CHINESE;
                break;
            case LANGUAGE_CODE_ENGLISH:
                languageType = LanguageType.LANGUAGE_ENGLISH;
                break;
            default:
                languageType = null;
        }

        return languageType;
    }

    private String getLanguageCodeByType(LanguageType type){

        String languageCode;
        switch (type){
            case LANGUAGE_AUTO:
                languageCode = LANGUAGE_CODE_AUTO;
                break;
            case LANGUAGE_SIMPLIFIED_CHINESE:
                languageCode = LANGUAGE_CODE_SIMPLIFIED_CHINESE;
                break;
            case LANGUAGE_TRADITIONAL_CHINESE:
                languageCode = LANGUAGE_CODE_TRADITIONAL_CHINESE;
                break;
            case LANGUAGE_ENGLISH:
                languageCode = LANGUAGE_CODE_ENGLISH;
                break;
            default:
                languageCode = null;
        }

        return languageCode;
    }

    private String getLanguageNameByType(LanguageType type){

        String languageNameArray[] = getContext().getResources().getStringArray(R.array.languages);

        String languageName;
        switch (type){
            case LANGUAGE_AUTO:
                languageName = languageNameArray[ARR_INDEX_AUTO];
                break;
            case LANGUAGE_SIMPLIFIED_CHINESE:
                languageName = languageNameArray[ARR_INDEX_SIMPLIFIED_CHINESE];
                break;
            case LANGUAGE_TRADITIONAL_CHINESE:
                languageName = languageNameArray[ARR_INDEX_TRADITIONAL_CHINESE];
                break;
            case LANGUAGE_ENGLISH:
                languageName = languageNameArray[ARR_INDEX_ENGLISH];
                break;
            default:
                languageName = null;
        }

        return languageName;
    }

    private Locale getLocaleByLanguageType(LanguageType type){

        if(type == null){
            return null;
        }

        Locale locale;
        switch (type){
            case LANGUAGE_AUTO:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    locale = LocaleList.getDefault().get(0);
                } else {
                    locale = Locale.getDefault();
                }
                break;
            case LANGUAGE_SIMPLIFIED_CHINESE:
                locale = new Locale(Locale.SIMPLIFIED_CHINESE.getLanguage());
                break;
            case LANGUAGE_TRADITIONAL_CHINESE:
                locale = new Locale("zh", "HK");
                break;
            case LANGUAGE_ENGLISH:
                locale = new Locale(Locale.ENGLISH.getLanguage());
                break;
            default:
                locale = null;
        }

        return locale;
    }

    private void saveCurrentLanguageType(LanguageType type){

        if(getContext() == null){
            return;
        }

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(LANGUAGE_CONFIG, getContext().MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(CURRENT_LANGUAGE, getLanguageCodeByType(type))
                .commit();
    }

    private LanguageType getCurrentLanguageType(){

        if(getContext() == null){
            // default return english
            return LanguageType.LANGUAGE_ENGLISH;
        }

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(LANGUAGE_CONFIG, getContext().MODE_PRIVATE);
        String languageCode = sharedPreferences.getString(CURRENT_LANGUAGE, "");

        LanguageType languageType = getLanguageTypeByCode(languageCode);
        if(languageType == null){
            // default return english
            return LanguageType.LANGUAGE_ENGLISH;
        }

        return languageType;
    }

    private Context getContext(){
        return this.context;
    }

    private Locale getDefault(Context context){

        Locale locale = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }

        return locale;
    }
}
