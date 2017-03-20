package com.common.widget.wheel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.common.R;
import com.common.widget.wheel.adapters.WheelPickerAdapter;
import com.common.widget.wheel.adapters.WheelViewAdapter;
import com.common.widget.wheel.model.WheelConfigModel;
public class WheelHelper {
    private WheelConfigModel leftWheelPickerEntity;
    private WheelConfigModel rightWheelPickEntity;
    private int leftIndex;
    private int rightIndex;
    private Dialog mCustomDialog;
    public WheelHelper(Context context, WheelConfigModel wheelPickerEntity){
        leftWheelPickerEntity=wheelPickerEntity;
        leftIndex=leftWheelPickerEntity.getCurrentIndex();
        initWheel(context);
    }
    public WheelHelper(Context context, WheelConfigModel wheelPickerEntity, WheelConfigModel rightWheelPickEntity){
        this.leftWheelPickerEntity=wheelPickerEntity;
        this.rightWheelPickEntity=rightWheelPickEntity;
        leftIndex=leftWheelPickerEntity.getCurrentIndex();
        rightIndex=rightWheelPickEntity.getCurrentIndex();
        initWheel(context);
    }

    private  void initWheel(Context context){
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_wheelpicker_two, null);
        WheelView wvLeft = (WheelView) dialogView.findViewById(R.id.wvLeft);
        TextView tvCancel= (TextView) dialogView.findViewById(R.id.ctvCancel);
        TextView tvSet= (TextView) dialogView.findViewById(R.id.ctvSet);
        WheelView wvMiddle = (WheelView) dialogView.findViewById(R.id.wvMiddle);
         mCustomDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        Window dialogWindow = mCustomDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        mCustomDialog.setContentView(dialogView);
        WheelViewAdapter leftAdapter=new WheelPickerAdapter(context,leftWheelPickerEntity.getData());
        wvLeft.setViewAdapter(leftAdapter);
        wvLeft.setCurrentItem(leftIndex);
        wvLeft.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                leftIndex=wheel.getCurrentItem();
            }
        });
        if(rightWheelPickEntity!=null) {
            WheelViewAdapter  rightAdapter=new WheelPickerAdapter(context,rightWheelPickEntity.getData());
            wvMiddle.setViewAdapter(rightAdapter);
            wvMiddle.setCurrentItem(rightIndex);
            wvMiddle.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    rightIndex=wheel.getCurrentItem();
                }
            });
        }else{
            wvMiddle.setVisibility(View.GONE);
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(leftWheelPickerEntity.getWheelDoneCallback()!=null){
                    leftWheelPickerEntity.getWheelDoneCallback().cancel();
                }
                if(rightWheelPickEntity!=null&&rightWheelPickEntity.getWheelDoneCallback()!=null){
                    rightWheelPickEntity.getWheelDoneCallback().cancel();
                }
                mCustomDialog.dismiss();
            }
        });
        mCustomDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(leftWheelPickerEntity.getWheelDoneCallback()!=null){
                    leftWheelPickerEntity.getWheelDoneCallback().cancel();
                }
                if(rightWheelPickEntity!=null&&rightWheelPickEntity.getWheelDoneCallback()!=null){
                    rightWheelPickEntity.getWheelDoneCallback().cancel();
                }
            }
        });
        tvSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(leftWheelPickerEntity.getWheelDoneCallback()!=null){
                    leftWheelPickerEntity.getWheelDoneCallback().done(leftIndex);
                }
                if(rightWheelPickEntity!=null&&rightWheelPickEntity.getWheelDoneCallback()!=null){
                    rightWheelPickEntity.getWheelDoneCallback().done(rightIndex);
                }

                mCustomDialog.dismiss();
            }
        });
    }
    public void showWheel(){
        mCustomDialog.show();
    }
    public void dimssWheel(){
        if(mCustomDialog!=null) {
            mCustomDialog.dismiss();
        }
    }
}
