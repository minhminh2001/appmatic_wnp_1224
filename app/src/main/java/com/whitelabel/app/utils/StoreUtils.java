package com.whitelabel.app.utils;

import java.util.HashMap;
import java.util.Map;

public class StoreUtils {

    private static StoreUtils instance;
    private Map<String, String> storeMap = new HashMap<>();
    private static final String DEFAULT_STORE_ID = "1";

    public static void updateStoreMap(Map<String, String> storeMap){
        getInstance().resetStoreMap(storeMap);
    }

    public static String getStoreIdByLanguageCode(String languageCode){
        return getInstance().findStoreIdByCode(languageCode);
    }

    private StoreUtils(){

    }

    private static StoreUtils getInstance(){
        if(instance == null){
            instance = new StoreUtils();
        }

        return instance;
    }

    private void resetStoreMap(Map<String, String> storeMap){
        clearStoreMap();

        if(storeMap == null) {
            return;
        }

        this.storeMap.putAll(storeMap);
    }

    private void clearStoreMap(){
        storeMap.clear();
    }

    private String findStoreIdByCode(String languageCode){

        if(storeMap.containsKey(languageCode)){
            return storeMap.get(languageCode);
        }

        return DEFAULT_STORE_ID;
    }
}
