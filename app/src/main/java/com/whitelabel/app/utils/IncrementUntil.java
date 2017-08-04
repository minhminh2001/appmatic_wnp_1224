package com.whitelabel.app.utils;

/**
 * Created by Administrator on 2017/8/4.
 */

public class IncrementUntil {
    private  static  IncrementUntil  incrementUntil;
    private static  int  num=1;
    private IncrementUntil(){

    }
    public static  IncrementUntil  newInstance(){
        if(incrementUntil==null){
            incrementUntil=new IncrementUntil();
        }
        return incrementUntil;
    }
    public  int  incrementNum(){
        num++;
        return num;
    }
}
