package com.whitelabel.app.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.whitelabel.app.BuildConfig;
import com.whitelabel.app.R;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.dao.OtherDao;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JTimeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class CrashHandler implements UncaughtExceptionHandler {

    /**
     * Debug Log tag
     */
    public static final String TAG = "CrashHandler";
    /**
     * 是否开启日志输出,在Debug状态下开启, 在Release状态下关闭以提示程序性能
     */
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";
    /**
     * 错误报告文件的扩展名
     */
    private static final String CRASH_REPORTER_EXTENSION = ".cr";
    /**
     * CrashHandler实例
     */
    private static CrashHandler INSTANCE;
    /**
     * 系统默认的UncaughtException处理类
     */
    private UncaughtExceptionHandler mDefaultHandler;
    /**
     * 程序的Context对象
     */
    private Context mContext;
    /**
     * 使用Properties来保存设备的信息和错误堆栈信息
     */
    private Properties mDeviceCrashInfo = new Properties();

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        return error;
    }

    private Handler mHandler=new Handler();
    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (BuildConfig.DEBUG) {
            
            mDefaultHandler.uncaughtException(thread, ex);
            return;
        }


        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理  
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            JLogUtils.w(TAG, "handleException --- ex==null");
            return true;
        } else {
            JLogUtils.w(TAG, "handleException --- ex==null", ex);
        }
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String result = info.toString();
        printWriter.close();
        sendCrashReportsToServer(result);

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, mContext.getResources().getString(R.string.app_crash_friendly_hint), Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        //JViewUtils.showMessageDialog(mContext, mContext.getResources().getString(R.string.app_crash_friendly_hint));

        if (result == null) {
            return false;
        }

        collectCrashDeviceInfo(mContext);
        return true;
    }

    /**
     * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
     */
    // public void sendPreviousReportsToServer() {
    // sendCrashReportsToServer(mContext);
    // }

    /**
     * 把错误报告发送给服务器,包含新产生的和以前没发送的.
     */
    private void sendCrashReportsToServer(String msg) {
        OtherDao crashDao=new OtherDao("CrashHandler",mHandler);
        String servercommonaddress = GemfiveApplication.getAppConfiguration().getHttpServerAddress();
        String servercommonparameter = GemfiveApplication.getAppConfiguration().getHttpGlobalParameter();

        if (JDataUtils.isEmpty(servercommonaddress)) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                sb.append("\nVersonName: " + (pi.versionName == null ? "not set" : pi.versionName));
                sb.append("\nVersonCode: " + ("" + pi.versionCode));
            }
        } catch (Exception e) {
            JLogUtils.e(TAG, "sendCrashReportsToServer", e);
        }

        sb.append("\nOS-VERSION: Android" + Build.VERSION.RELEASE);
        sb.append("\nMOBILE: " + Build.MODEL);
//        if(GemfiveApplication.getPhoneConfiguration()!=null&&!TextUtils.isEmpty(GemfiveApplication.getPhoneConfiguration().getPhoneInfo(mContext))){
//            sb.append(GemfiveApplication.getPhoneConfiguration().getPhoneInfo(mContext));
//        }
        sb.append("\nERROR: " + msg);
        msg = sb.toString();

        String svrUrl = servercommonaddress + "appservice/crash/log?";
        ConcurrentHashMap<String, String> svrParams = new ConcurrentHashMap<String, String>();
        svrParams.put("crash_text", msg.replace("=", ":"));
        svrParams.put("client", "1");//1 means android
        try {
            if (!JDataUtils.isEmpty(servercommonparameter)) {
                String[] globalparameterlist = servercommonparameter.split("&");
                if (globalparameterlist != null && globalparameterlist.length > 0) {
                    int length = globalparameterlist.length;
                    for (int index = 0; index < length; ++index) {
                        String tmpparameter = globalparameterlist[index];
                        if (!JDataUtils.isEmpty(tmpparameter)) {
                            try {
                                String[] keyvalue = tmpparameter.split("=");
                                if (keyvalue != null && keyvalue.length == 2) {
                                    final String keystring = keyvalue[0];
                                    final String valuestring = keyvalue[1];
                                    svrParams.put(keystring, valuestring);
                                }
                            } catch (Exception ex) {
                                JLogUtils.e(TAG, "sendCrashReportsToServer", ex);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            JLogUtils.e(TAG, "sendCrashReportsToServer", ex);
        }
//        Map<String,String> map=new TreeMap<>();
//        if (svrParams != null) {
//            for (ConcurrentHashMap.Entry<String, String> entry : svrParams.entrySet()) {
//                if (entry != null) {
//                    final String keystring = entry.getKey();
//                    final String valuestring = entry.getValue();
//                    if (!JDataUtils.isEmpty(keystring) && !JDataUtils.isEmpty(valuestring)) {
//                        map.put(keystring, valuestring);
//                    }
//                }
//            }
//        }
//        JLogUtils.i("CrashHandler","============map==========="+map.toString());

        crashDao.sendCrash(svrParams);
//        HttpUtils http = new HttpUtils();
//        RequestParams params = new RequestParams();
//        if (svrParams != null) {
//            for (ConcurrentHashMap.Entry<String, String> entry : svrParams.entrySet()) {
//                if (entry != null) {
//                    final String keystring = entry.getKey();
//                    final String valuestring = entry.getValue();
//                    if (!JDataUtils.isEmpty(keystring) && !JDataUtils.isEmpty(valuestring)) {
//                        params.addBodyParameter(keystring, valuestring);
//                    }
//                }
//            }
//        }
//        http.send(HttpRequest.HttpMethod.POST, svrUrl, params, new RequestCallBack<String>() {
//
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                JLogUtils.i(TAG, "sendCrashReportsToServer-->onSuccess--->" + responseInfo);
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                JLogUtils.i(TAG, "sendCrashReportsToServer-->onFailure--->" + msg);
//            }
//        });
    }

    /**
     * 获取错误报告文件名
     *
     * @param ctx
     * @return
     */
    private String[] getCrashReportFiles(Context ctx) {
        File filesDir = ctx.getFilesDir();
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {return name.endsWith(CRASH_REPORTER_EXTENSION);
            }
        };
        return filesDir.list(filter);
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return
     */
    private String saveCrashInfoToFile(Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String result = info.toString();
        printWriter.close();
        mDeviceCrashInfo.put("EXEPTION", ex.getLocalizedMessage());
        mDeviceCrashInfo.put(STACK_TRACE, result);
        try {
            Calendar calendar = JTimeUtils.getCurrentCalendar();

            final int time_year = calendar.get(Calendar.YEAR);
            final int time_month = calendar.get(Calendar.MONTH) + 1;
            final int time_monthday = calendar.get(Calendar.DAY_OF_MONTH);

            final int time_hour = calendar.get(Calendar.HOUR);
            final int time_minute = calendar.get(Calendar.MINUTE);
            final int time_second = calendar.get(Calendar.SECOND);

            int date = time_year * 10000 + time_month * 100 + time_monthday;
            int time = time_hour * 10000 + time_minute * 100 + time_second;
            String fileName = "crash-" + date + "-" + time + CRASH_REPORTER_EXTENSION;
            FileOutputStream trace = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            mDeviceCrashInfo.store(trace, "");
            trace.flush();
            trace.close();
            return fileName;
        } catch (Exception e) {
            JLogUtils.e(TAG, "saveCrashInfoToFile", e);
        }
        return null;
    }

    /**
     * 收集程序崩溃的设备信息
     *
     * @param ctx
     */
    public void collectCrashDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);
                mDeviceCrashInfo.put(VERSION_CODE, "" + pi.versionCode);
            }
        } catch (NameNotFoundException e) {
            JLogUtils.e(TAG, "collectCrashDeviceInfo", e);
        }
        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        // 具体信息请参考后面的截图
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mDeviceCrashInfo.put(field.getName(), "" + field.get(null));
                if (BuildConfig.DEBUG) {
                    JLogUtils.d(TAG, field.getName() + " : " + field.get(null));
                }
            } catch (Exception e) {
                JLogUtils.e(TAG, "Error while collect crash info", e);
            }
        }
    }

}