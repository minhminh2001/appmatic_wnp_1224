package com.whitelabel.app.listener;

import com.whitelabel.app.widget.CustomRangeSeekBar;

/**
 * Callback listener interface to notify about changed range values.
 *
 * @param <T> The Number type the RangeSeekBar has been declared with.
 * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
 */
public interface OnRangeSeekBarChangeListener<T> {
    public void onRangeSeekBarValuesChanged(CustomRangeSeekBar<?> bar, T minValue, T maxValue);
    public void onRangeSeekBarTouchActionUp(CustomRangeSeekBar<?> bar, T minValue, T maxValue);
}
