package com.whitelabel.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HideFunctionActivity;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/10/28.
 */
public class SelectServiceFragment extends BaseFragment implements View.OnClickListener {
    private HideFunctionActivity activity;
    private View contentView;
    private Spinner selectService;
    private ArrayList<String> list;
    private Button cancel,okay;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity= (HideFunctionActivity) activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView=inflater.inflate(R.layout.fragment_select_service,null);
         selectService= (Spinner) contentView.findViewById(R.id.spinnerSelectService);
        cancel= (Button) contentView.findViewById(R.id.cancel);
        okay= (Button) contentView.findViewById(R.id.okay);
        return contentView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list=new ArrayList<String>();
        list.add("Live");
        list.add("Api");
        list.add("Uat");
        list.add("Appdev");
        selectService.setPrompt("Select Service");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectService.setAdapter(adapter);
        cancel.setOnClickListener(this);
        okay.setOnClickListener(this);

    }

    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:

                break;
            case R.id.okay:
//                int serviceNumbel=  selectService.getSelectedItemPosition();
//                Toast.makeText(activity,"serviceNumbel="+serviceNumbel,Toast.LENGTH_SHORT).show();
//                activity.myService=activity.AllService.get(serviceNumbel);
//                activity.switchFragment(HideFunctionActivity.FRAGMENT_TYPE_HIDE_LOGIN);
                break;
        }
    }
}
