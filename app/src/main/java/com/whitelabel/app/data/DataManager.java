package com.whitelabel.app.data;




public class DataManager {
    private static  volatile  DataManager dataManager;
    private static final Object lock = new Object();
    private DataManager(){
    }
    public static DataManager getInstance(){
        if(dataManager==null){
            synchronized (lock){
                if(dataManager==null) {
                    dataManager = new DataManager();
                }
            }
        }
        return dataManager;
    }
}
