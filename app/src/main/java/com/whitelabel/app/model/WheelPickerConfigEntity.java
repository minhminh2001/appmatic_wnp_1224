package com.whitelabel.app.model;

import com.whitelabel.app.callback.WheelPickerCallback;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/1.
 */
public class WheelPickerConfigEntity {
    private ArrayList<WheelPickerEntity> arrayList;
    private WheelPickerEntity oldValue;
    private int index;
    private WheelPickerCallback callBack;

    public ArrayList<WheelPickerEntity> getArrayList() {
        return arrayList;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setArrayList(ArrayList<WheelPickerEntity> arrayList) {
        this.arrayList = arrayList;
    }

    public WheelPickerEntity getOldValue() {
        return oldValue;
    }

    public void setOldValue(WheelPickerEntity oldValue) {
        this.oldValue = oldValue;
    }

    public WheelPickerCallback getCallBack() {
        return callBack;
    }

    public void setCallBack(WheelPickerCallback callBack) {
        this.callBack = callBack;
    }
}
