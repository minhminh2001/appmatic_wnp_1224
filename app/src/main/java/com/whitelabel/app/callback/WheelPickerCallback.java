package com.whitelabel.app.callback;

import com.whitelabel.app.model.WheelPickerEntity;

/**
 * Created by imaginato on 2015/7/1.
 */
public abstract class WheelPickerCallback {
    private final WheelPickerEntity oldValue = new WheelPickerEntity();
    private final WheelPickerEntity newValue = new WheelPickerEntity();

    public WheelPickerCallback() {
    }

    public WheelPickerEntity getOldValue() {
        return oldValue;
    }

    public WheelPickerEntity getNewValue() {
        return newValue;
    }

    public abstract void onCancel();
    public abstract void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue);
    public abstract void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue);
}
