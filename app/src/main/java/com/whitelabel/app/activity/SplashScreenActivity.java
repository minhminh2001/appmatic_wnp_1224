package com.whitelabel.app.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.GlobalData;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imaginato on 2015/8/17.
 */
public class SplashScreenActivity extends com.whitelabel.app.BaseActivity {
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;//viewpager适配器
    private ArrayList<View> arrayList;
    private ArrayList<ImageView> imgList;
    private TextView start;
    private ImageView img1,img2;
    private boolean existVending=false;
    private View.OnClickListener updateListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
            for(int i=0;i<packages.size();i++) {
                PackageInfo packageInfo = packages.get(i);
                String packgeName="";
                packgeName=packageInfo.packageName;
                JLogUtils.i("Allen", "packge=" + packgeName);
                if(packgeName.contains("vending")){
                    //跳转进市场搜索的代码
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(GlobalData.jumpMarketUrl));
                    startActivity(intent);
                    existVending=true;
                }
            }
            if(!existVending){
                Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=com.wnp.app");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                existVending=false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        LayoutInflater inflater=getLayoutInflater();
        View page1=inflater.inflate(R.layout.splash_screen_page1, null);
        View page2=inflater.inflate(R.layout.splash_screen_page2,null);
        arrayList=new ArrayList<View>();
        arrayList.add(page1);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null&&bundle.getBoolean("updateVersion")) {
            String title=getResources().getString(R.string.versionCheckTitle);
            String hintmsg=getResources().getString(R.string.versionCheckMsg);
            String btnMsg=getResources().getString(R.string.update);
            JViewUtils.showMaterialDialog(SplashScreenActivity.this, title, hintmsg, btnMsg, updateListener, false);
        }else{
            arrayList.add(page2);
        }
        //数据适配(方法内参数大部分固定)
        pagerAdapter=new PagerAdapter(){
            @Override
            public int getCount() {
                return arrayList.size();
            }
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
            // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(arrayList.get(arg1));
            }

            // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的arrayList加入到ViewGroup中，然后作为返回值返回即可
            public Object instantiateItem(View arg0, int arg1){
                View view = arrayList.get(arg1);
                ((ViewPager)arg0).addView(view);
                return view;
            }
        };
        viewPager.setAdapter(pagerAdapter);
        img1= (ImageView) page1.findViewById(R.id.img1);
        img2= (ImageView) page2.findViewById(R.id.img2);


        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JStorageUtils.saveToSplashScreenMark(SplashScreenActivity.this);
                Bundle mBundle = new Bundle();
                mBundle.putString("Activity", "start");//压入数据
                startNextActivity(mBundle, LoginRegisterActivity.class, true);


            }
        });
//                int screenWidth=WhiteLabelApplication.getPhoneConfiguration().getScreenWidth();
//                int screenHeigh= WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance(this).activityStart(this);
//        EasyTracker easyTracker = EasyTracker.getInstance(this);
//        easyTracker.send(MapBuilder.createEvent("Splash Screen", // Event category (required)
//                null, // Event action (required)
//                null, // Event label
//                null) // Event value
//                .build());
    }

    @Override
    protected void onStop() {
        super.onStop();
//        EasyTracker.getInstance(this).activityStop(this);
       // if(mDialog!=null){ mDialog.cancel();}
    }
    //判断是否需要跳过Favorite功能
//    public boolean  isFavoratePass(){
//        //判断有本地数据
//        SharedPreferences shared=this.getSharedPreferences("likes", Activity.MODE_PRIVATE);
//        Set<String> set=new HashSet<String>();
//        set=shared.getStringSet("likes",null);
//        if(set!=null){
//            return true;
//        }
//        return false;
//    }
    //判断登陆
    public boolean isLogin(){
        return WhiteLabelApplication.getAppConfiguration().isSignIn(this);
    }
//    //判断是否更新app版本
//    public boolean isUpdateVersion(){
//        if(!getVersion().equals(getSaveVersion())){
//            return  true;
//        }
//        return false;
//    }
//    //获取存储app版本号
//    public String getSaveVersion(){
//        SharedPreferences sharedVersion= this.getSharedPreferences("Version", Context.MODE_PRIVATE);
//        return sharedVersion.getString("Version","0");
//    }
    //获取当前app版本
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }
//    boolean hasData=false;
    //判断Service 是否有Favorsite数据
//    public void isFavoriteData(){
//
//        String session_key=WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey();
//        SVRParameters parameters = new SVRParameters();
//        parameters.put("session_key", session_key);
//        SVRGetFavoriteDate svrhandler = new SVRGetFavoriteDate(this, parameters);
//        svrhandler.loadDatasFromServer(new SVRCallback() {
//            @Override
//            public void onSuccess(int resultCode, SVRReturnEntity result) {
//                SVRAppServiceCustomerGetFavorite entity = (SVRAppServiceCustomerGetFavorite) result;
//                if (entity.getHasFavorite()==1){
//                    hasData=true;
//                }else{
//                    hasData=false;
//                }
//
//                //判断跳过Favorite功能
//                Bundle mBundle = new Bundle();
//                mBundle.putString("Activity", "start");//压入数据
//                if(hasData) {
//                    startNextActivity(mBundle, HomeActivity.class, true);
//                }else {
//                    startNextActivity(mBundle, FavoriteActivity.class, true);
//                }
//            }
//
//            @Override
//            public void onFailure(int resultCode, String errorMsg) {
//                if (!JDataUtils.errorMsgHandler(SplashScreenActivity.this, errorMsg)) {
//                    if ((!JDataUtils.isEmpty(errorMsg)) && (errorMsg.contains("session expired,login again please"))) {
//                        startNextActivity(null, FavoriteActivity.class, true);
//                        return;
//                    }
//                    Toast.makeText(SplashScreenActivity.this, "error " + resultCode + " " + errorMsg, Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
}
