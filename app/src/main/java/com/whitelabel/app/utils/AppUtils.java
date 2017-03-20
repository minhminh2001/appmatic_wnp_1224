package com.whitelabel.app.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class AppUtils {

	/**
	 * @return
	 * @see {@linkplain #getMyCacheDir(String)}
	 */
	public static String getMyCacheDir() {
		return getMyCacheDir(null);
	}

	/**
	 * 获取或创建Cache目录
	 *
	 * @param bucket
	 *            临时文件目录，bucket = "/cache/" ，则放在"sdcard/linked-joyrun/cache"; 如果bucket=""或null,则放在"sdcard/linked-joyrun/"
	 */
	public static String getMyCacheDir(String bucket) {
		String dir;

		// 保证目录名称正确
		if (bucket != null) {
			if (!bucket.equals("")) {
				if (!bucket.endsWith("/")) {
					bucket = bucket + "/";
				}
			}
		} else
			bucket = "";

		String joyrun_default = "/html/";

		if (FileUtils.isSDCardExist()) {
			dir = Environment.getExternalStorageDirectory().toString() + joyrun_default + bucket;
		} else {
			dir = Environment.getDownloadCacheDirectory().toString() + joyrun_default + bucket;
		}

		File f = new File(dir);
		if (!f.exists()) {
			f.mkdirs();
		}
		return dir;
	}

	/**
	 * dp转换为px
	 */
	public static int dpToPx(Context context, float adius) {
		float density = context.getResources().getDisplayMetrics().density;
		return (int) (adius * density);
	}

	/**
	 * 判断Activity是否存在
	 *
	 * @param context
	 * @param activityClass
	 * @return
	 */
	public static boolean isActivityExist(Context context, Class<? extends Activity> activityClass) {
		try {
			context = context.getApplicationContext();
			Intent intent = new Intent(context, activityClass);
			ComponentName cmpName = intent.resolveActivity(context.getPackageManager());

			if (cmpName != null) { // 说明系统中存在这个activity
				ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
				List<RunningTaskInfo> taskInfoList = am.getRunningTasks(70);

				for (RunningTaskInfo taskInfo : taskInfoList) {
					if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
						return true;
					}
				}
			}
		} catch (Exception e) {}

		return false;
	}

	/**
	 * 判断Service是否running
	 *
	 * @param context
	 * @param serviceClass
	 * @return
	 */
	public static boolean isServiceRunning(Context context, Class<? extends Service> serviceClass) {
		try {
			context = context.getApplicationContext();

			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(2000);

			for (ActivityManager.RunningServiceInfo info : serviceList) {
				String name = info.service.getClassName();

				if (name != null && name.contains(serviceClass.getName())) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 获得状态栏/通知栏的高度
	 */
	public static int getStatusBarHeight(Context context){
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * 获得导航栏高度
	 */
	public static int getNavigationBarHeight(Activity activity) {
		Resources resources = activity.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
		//获取NavigationBar的高度
		int height = resources.getDimensionPixelSize(resourceId);
		return height;
	}

	/** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
	public static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	/** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context */
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/databases"));
	}

	/**
	 * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
	 * context
	 */
	public static void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/shared_prefs"));
	}

	/** * 按名字清除本应用数据库 * * @param context * @param dbName */
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	/** * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context */
	public static void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}

	/**
	 * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
	 * context
	 */
	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}

	/** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
	public static void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(filePath));
	}

	/** * 清除本应用所有的数据 * * @param context * @param filepath */
	public static void cleanApplicationData(Context context, String... filepath) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanDatabases(context);
		cleanSharedPreference(context);
		cleanFiles(context);
		for (String filePath : filepath) {
			cleanCustomCache(filePath);
		}
	}
	/** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}


	/**
	 * 清除应用缓存的用户数据，同时停止所有服务和Alarm定时task
	 * String cmd = "pm clear " + packageName;
	 * String cmd = "pm clear " + packageName  + " HERE";
	 * Runtime.getRuntime().exec(cmd)
	 * @param packageName
	 * @return
	 */
	public static Process clearAppUserData(String packageName) {
		Process p = execRuntimeProcess("pm clear " + packageName);
		if (p == null) {
			JLogUtils.i("aaa","Clear app data packageName:" + packageName
					+ ", FAILED !");
		} else {
			JLogUtils.i("aaa","Clear app data packageName:" + packageName
					+ ", SUCCESS !");
		}
		return p;
	}
	public static Process execRuntimeProcess(String commond) {
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(commond);
		} catch (IOException e) {
			JLogUtils.i("aaa","exec Runtime commond:" + commond + ", IOException" + e);
			e.printStackTrace();
		}
		JLogUtils.i("aaa","exec Runtime commond:" + commond + ", Process:" + p);
		return p;
	}

	/**
	 * 返回当前程序版本名
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		int versionCode=0;
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			versionCode = pi.versionCode;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			JLogUtils.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

}
