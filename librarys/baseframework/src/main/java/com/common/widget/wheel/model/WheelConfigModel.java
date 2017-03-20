package com.common.widget.wheel.model;

import com.common.widget.wheel.IWheelCallback;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 */

public class WheelConfigModel implements Serializable {
    private  int currentIndex;
    private List<WheelPickerEntity> data;
    private IWheelCallback wheelDoneCallback;

    public WheelConfigModel(){
        data=new ArrayList<>();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public List<WheelPickerEntity> getData() {
        return data;
    }

    public void setData(List<WheelPickerEntity> data) {
        this.data = data;
    }

    public IWheelCallback getWheelDoneCallback() {
        return wheelDoneCallback;
    }
    public void setWheelDoneCallback(IWheelCallback wheelDoneCallback) {
        this.wheelDoneCallback = wheelDoneCallback;
    }
}
