package com.whitelabel.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.LoginRegisterActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by imaginato on 2015/6/11.
 */
public class CategoryListingFragment extends BaseFragment implements View.OnClickListener,View.OnTouchListener{
    private View contentView,scrollViewX;
    //所有的TextView顺序保持一致
    private int scrolls[]=new int[]{R.id.accessories, R.id.clothing, R.id.musim_wear, R.id.accessories2, R.id.accessories3, R.id.accessories4};
    private String scrolls2[]=new String[]{"accessories","clothing","musim_wear","accessories2","accessories3","accessories4"};
    private String strData[][]=new String[][]{
            {"Shrot Anarot with Furollar","Picuter Anorak","PM 339.0" },{"Shrot Anarot with Furollar","Picuter Anorak","PM 339.0"},{"Shrot Anarot with Furollar","Picuter Anorak","PM 339.0"},{"Shrot Anarot with Furollar","Picuter Anorak","PM 339.0"},{"Shrot Anarot with Furollar","Picuter Anorak","PM 339.0"},{"Shrot Anarot with Furollar","Picuter Anorak","PM 339.0"},{"Shrot Anarot with Furollar","Picuter Anorak","PM 339.0"},{"Shrot Anarot with Furollar","Picuter Anorak","PM 339.0"},{"Shrot Anarot with Furollar","Picuter Anorak","PM 339.0"}
    };
    private List<TextView> textViewList;

    private EditText seek;
    private int[] idData= null;
    private LoginRegisterActivity loginRegisterActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            loginRegisterActivity = (LoginRegisterActivity) activity;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView=inflater.inflate(R.layout.fragment_category_listing2,null);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //GridView
        GridView gridView = (GridView) contentView.findViewById(R.id.gridView);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int i=0;i<idData.length;i++){
            Map<String,String> map=new HashMap<String,String>();
            map.put("str1",strData[i][0]);
            map.put("str2",strData[i][1]);
            map.put("str3",strData[i][2]);
            map.put("imgId", String.valueOf(idData[i]));
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(
                loginRegisterActivity, list, R.layout.template_category_listing2,
                new String[]{"str1", "str2", "str3", "imgId"},
                new int[]{R.id.src1, R.id.src2, R.id.src3, R.id.img}
        );
        gridView.setAdapter(adapter);
        //cancel
        TextView cancel = (TextView) contentView.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        seek= (EditText) contentView.findViewById(R.id.seek);
        //horizontalScrollView
        scrollViewX=contentView.findViewById(R.id.scrollViewX);

        TextView accessories = (TextView) contentView.findViewById(R.id.accessories);
        TextView clothing = (TextView) contentView.findViewById(R.id.clothing);
        TextView musim_wear = (TextView) contentView.findViewById(R.id.musim_wear);
        TextView accessories2 = (TextView) contentView.findViewById(R.id.accessories2);
        TextView accessories3 = (TextView) contentView.findViewById(R.id.accessories3);
        TextView accessories4 = (TextView) contentView.findViewById(R.id.accessories4);
        accessories.setOnClickListener(this);
        clothing.setOnClickListener(this);
        musim_wear.setOnClickListener(this);
        accessories2.setOnClickListener(this);
        accessories3.setOnClickListener(this);
        accessories4.setOnClickListener(this);
        textViewList=new ArrayList<TextView>();
        textViewList.add(accessories);
        textViewList.add(clothing);
        textViewList.add(musim_wear);
        textViewList.add(accessories2);
        textViewList.add(accessories3);
        textViewList.add(accessories4);
    }
    int scrollViewX_x=0;
    int scrollView_toX=0;
    @Override
    public void onClick(View v) {
          if(v.getId()== R.id.cancel){
            seek.setText("");
          }
        for(int i=0;i<scrolls.length;i++){
            if(v.getId()==scrolls[i]){
                //恢复为默认颜色
                for(int j=0;j<scrolls.length;j++){
                    textViewList.get(j).setTextColor(getActivity().getResources().getColor(R.color.black000000));
                }
                //设置点击颜色
                textViewList.get(i).setTextColor(getActivity().getResources().getColor(R.color.grey970508));
                AnimationSet set=new AnimationSet(true);
                set.setFillAfter(true);
                scrollView_toX=textViewList.get(i).getLeft()-60+(textViewList.get(i).getWidth()-scrollViewX.getWidth())/2;//滑块要滑动位置
                Animation tran = new TranslateAnimation(
                        scrollViewX_x,scrollView_toX,
                        0,0);
                scrollViewX_x=textViewList.get(i).getLeft();
                tran.setDuration(1000);
                set.addAnimation(tran);
                scrollViewX.startAnimation(set);

                }

            }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {

    }
}
