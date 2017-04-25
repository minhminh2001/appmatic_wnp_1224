package com.whitelabel.app.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.adapter.FavoriteAdapter;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.model.FavoriteSonEntity;
import com.whitelabel.app.utils.JViewUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by imaginato on 2015/8/19.
 */
public class FavoriteActivity extends BaseActivity {
    private List<FavoriteSonEntity> list;
    private FavoriteAdapter favoriteAdapter;
    private TextView favoriteOk;
    private SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Dialog mDialog = JViewUtils.showProgressDialog(this);

//        SharedPreferences shared=getSharedPreferences("favorite", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor=shared.edit();
//        editor.putBoolean("favorite",true);
//        editor.commit();

        list = new ArrayList<FavoriteSonEntity>();
        GridView mGridView = (GridView) findViewById(R.id.myGridView);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).isSelected()) {
                    list.get(position).setSelected(false);
                } else {
                    list.get(position).setSelected(true);
                }
                if (clickPass()) {
                    favoriteOk.setBackgroundColor(getResources().getColor(R.color.purple));
                    favoriteOk.setTextColor(getResources().getColor(R.color.white));
                } else {
                    favoriteOk.setBackgroundColor(getResources().getColor(R.color.greyCCCCCC));
                    favoriteOk.setTextColor(getResources().getColor(R.color.black000000));
                }
                // favoriteAdapter.notifyDataSetChanged();
                favoriteAdapter.updateView(position);
            }
        });
        favoriteOk = (TextView) findViewById(R.id.favoriteOK);
        favoriteOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickPass()) {
                    shared = getSharedPreferences("likes", MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    Set<String> set = new HashSet<String>();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isSelected()) {
                            set.add(list.get(i).getCategoryId());
                        }
                    }
                    editor.putStringSet("likes", set);
                    editor.commit();

                    Bundle mBundle = new Bundle();
                    mBundle.putString("Activity", "start");//压入数据
                    if (WhiteLabelApplication.getAppConfiguration().isSignIn(FavoriteActivity.this)) {
                        updateFavoriteData(set);
                        startNextActivity(mBundle, HomeActivity.class, true);

                    } else {
                        startNextActivity(mBundle, LoginRegisterActivity.class, true);
                    }
                }
            }
        });
        getFavoriteData();
    }

    public boolean clickPass() {
        boolean buttonClickPass = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                buttonClickPass = true;
                return true;
            }
        }
        return false;
    }

    public void getFavoriteData() {
//        SVRParameters parameters = new SVRParameters();
//        SVRFavorite svrhandler = new SVRFavorite(FavoriteActivity.this, parameters);
//        svrhandler.loadDatasFromServer(new SVRCallback() {
//            @Override
//            public void onSuccess(int resultCode, SVRReturnEntity result) {
//                //成功后将数据放到Entity中
//                FavoriteEntity entity = (FavoriteEntity) result;
//                list=((FavoriteEntity) result).getList();
//                favoriteAdapter =new FavoriteAdapter(FavoriteActivity.this,list,mGridView);
//                mGridView.setAdapter(favoriteAdapter);
//                if(mDialog!=null){ mDialog.cancel();}
//            }
//
//            @Override
//            public void onFailure(int resultCode, String errorMsg) {
//                if (!JDataUtils.errorMsgHandler(FavoriteActivity.this, errorMsg)) {
//                    Toast.makeText(FavoriteActivity.this, "error " + resultCode + " " + errorMsg, Toast.LENGTH_LONG).show();
//                    JLogUtils.i("Error","error " + resultCode + " " + errorMsg);
//                }
//                if(mDialog!=null){ mDialog.cancel();}
//            }
//        });
    }


    public void updateFavoriteData(Set<String> set) {
//        if(set!=null) {
//            SVRParameters parameters = new SVRParameters();
//            parameters.put("session_key", WhiteLabelApplication.getAppConfiguration().getUserInfo(FavoriteActivity.this).getSessionKey());
//
//            for (String str : set) {
//                String category_ids = "category_ids[" + str + "]";
//                parameters.put(category_ids, "1");
//            }
//
//            SVRFavoriteUpdate svrhandler = new SVRFavoriteUpdate(this, parameters);
//            svrhandler.loadDatasFromServer(new SVRCallback() {
//                @Override
//                public void onSuccess(int resultCode, SVRReturnEntity result) {
//                    //成功后将数据放到Entity中
//                    UpdateFavoriteEntity entity = (UpdateFavoriteEntity) result;
//
//                    //成功后将Favorite数据清空
//                    SharedPreferences.Editor edit = shared.edit();
//                    edit.putStringSet("likes", null);
//                    edit.commit();
//                }
//
//                @Override
//                public void onFailure(int resultCode, String errorMsg) {
//                    if (!JDataUtils.errorMsgHandler(FavoriteActivity.this, errorMsg)) {
//                        Toast.makeText(FavoriteActivity.this, "error " + resultCode + " " + errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//        }
    }
}
