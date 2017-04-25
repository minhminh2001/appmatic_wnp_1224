package com.whitelabel.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.GlobalData;
import com.whitelabel.app.adapter.SelectServiceAdapter;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.model.GetServiceEntity;
import com.whitelabel.app.model.GetServiceListEntity;
import com.whitelabel.app.model.SelectServiceEntity;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/10/28.
 */
public class HideFunctionActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener  {
    public ArrayList<String> serverAddress;
    public ArrayList<String> downloadImageServerAddress;
    public ArrayList<String> UploadImageServerAddress;
    public String myUploadImageServerAddress;
    public String myServerAddress;
    private ArrayList<SelectServiceEntity> list;
    private GetServiceEntity entity;
    private SelectServiceAdapter adapter;
    private  int serviceNumbel;
    private  boolean  clickList=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_function);
        initToolBar();
        Bundle bundle=getIntent().getExtras();
        entity = (GetServiceEntity) bundle.getSerializable("entity");

        list=new ArrayList<SelectServiceEntity>();
        for (int i=0;i<entity.getResult().size();i++){
            GetServiceListEntity myentity=entity.getResult().get(i);
            SelectServiceEntity entity=new SelectServiceEntity();
            entity.setServiceName(myentity.getServerName());
            list.add(entity);
            JLogUtils.i("Allen", "service==" + myentity.getServerName());
        }

        ListView myList = (ListView) findViewById(R.id.myList);
        adapter=new SelectServiceAdapter(this,list);
        myList.setAdapter(adapter);


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setCurrent(false);
                }
                list.get(position).setCurrent(true);
                adapter.notifyDataSetChanged();
                serviceNumbel = position;
                clickList = true;
                JLogUtils.i("Allen", "serviceNumbel=" + position);
            }
        });

        ImageView back = (ImageView) findViewById(R.id.vHeaderBarBack);
        findViewById(R.id.rl_vHeaderBarBack).setOnClickListener(this);
        back.setOnClickListener(this);
        TextView save = (TextView) findViewById(R.id.save);
        save.setOnClickListener(this);

        GetService();

    }
    private void initToolBar() {
        setTitle(getResources().getString(R.string.select_server));
        setLeftMenuIcon(R.drawable.action_back);
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    //所有的环境
    public void GetService(){
        serverAddress=new ArrayList<String>();
        downloadImageServerAddress=new ArrayList<String>();
        UploadImageServerAddress=new ArrayList<String>();
        for(int i=0;i<entity.getResult().size();i++){
            GetServiceListEntity myentity=entity.getResult().get(i);
            serverAddress.add(myentity.getServerDomain());
            downloadImageServerAddress.add(myentity.getServerDomain()+"/appservice/image/index");
            UploadImageServerAddress.add(myentity.getServerDomain());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:
                if(WhiteLabelApplication.getPhoneConfiguration().isConnect(this)) {
                    if(clickList) {
                        WhiteLabelApplication.getAppConfiguration().signOut(this);
                        myServerAddress = serverAddress.get(serviceNumbel);
                        String myDownloadImageServerAddress = downloadImageServerAddress.get(serviceNumbel);
                        myUploadImageServerAddress = UploadImageServerAddress.get(serviceNumbel);
                        String hashKey="123456";
                        JStorageUtils.clearLocalCartRepository(this);
                        GlobalData.updateGlobalData(myServerAddress, myDownloadImageServerAddress, myUploadImageServerAddress, hashKey);
                        WhiteLabelApplication.getAppConfiguration().init(getApplicationContext());
                        WhiteLabelApplication.InitappConfigurationEntity();
                        Intent intent=new Intent(HideFunctionActivity.this,HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Bundle bundle=new Bundle();
                        bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE,HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_START);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
//                        startNextActivity(null, StartActivity.class, true);
                        clickList=false;
                    }else{
                        Toast.makeText(this,"Please select the server",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,getString(R.string.Global_Error_Internet),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_vHeaderBarBack:
            case R.id.vHeaderBarBack:
                this.onBackPressed();
                break;
        }
    }


}
