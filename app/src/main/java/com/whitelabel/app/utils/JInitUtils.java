package com.whitelabel.app.utils;

import android.app.Activity;

import com.whitelabel.app.callback.INITCallback;
import com.whitelabel.app.handler.INITApp;
import com.whitelabel.app.task.INITExecutor;

/**
 * Created by imaginato on 2015/6/12.
 */
public class JInitUtils {
    private JInitUtils() {
    }

    public static JInitUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static void initApplication(Activity activity, INITCallback callback) {
        INITApp initApp = new INITApp(activity, callback);
        INITExecutor.getInstance().execute(initApp);
    }

    private Object readResolve() {
        return getInstance();
    }

    private static class SingletonHolder {
        static final JInitUtils INSTANCE = new JInitUtils();
    }
}
