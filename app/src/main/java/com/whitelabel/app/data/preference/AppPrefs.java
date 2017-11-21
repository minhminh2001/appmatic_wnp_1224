package com.whitelabel.app.data.preference;

import android.content.Context;

import com.whitelabel.app.WhiteLabelApplication;

import net.grandcentrix.tray.TrayPreferences;

/**
 * Created by img on 2017/11/19.
 */

public class AppPrefs {
    private static final int VERSION = 1;
    private static volatile TrayEMMPrefs  mPrefs;
    //private static final Object PrefsSyncObject = new Object();

    /**
     *  extends TrayPreferences change module name
     */
    private static class TrayEMMPrefs extends TrayPreferences {

        public TrayEMMPrefs(Context context) {
            super(context, context.getPackageName(), VERSION);
        }
    }

    private static TrayEMMPrefs getPrefs() {
        if (mPrefs == null) {
            synchronized(AppPrefs.class) {
                if (mPrefs == null) {
                    mPrefs = new TrayEMMPrefs(WhiteLabelApplication.getInstance());
                }
            }
        }
        return mPrefs;
    }

    public static void putBoolean(String key, boolean value) {
        TrayEMMPrefs prefs = getPrefs();
        prefs.put(key,value);
    }

    public static void putInt(String key, int value) {
        TrayEMMPrefs prefs = getPrefs();
        prefs.put(key,value);
    }

    public static void putLong(String key, long value) {
        TrayEMMPrefs prefs = getPrefs();
        prefs.put(key,value);
    }

    public static void putString(String key, String value) {
        TrayEMMPrefs prefs = getPrefs();
        prefs.put(key,value);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        TrayEMMPrefs prefs = getPrefs();
        return prefs.getBoolean(key, defaultValue);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        TrayEMMPrefs prefs = getPrefs();
        return prefs.getInt(key, defaultValue);
    }

    public static long getLong( String key) {
        return getLong(key, 0);
    }

    public static long getLong(String key, long defaultValue) {
        TrayEMMPrefs prefs = getPrefs();
        return prefs.getLong(key, defaultValue);
    }

    public static String getString(String key) {
        return getString(key, null);
    }

    public static String getString(String key, String defaultValue) {
        TrayEMMPrefs prefs = getPrefs();
        return prefs.getString(key, defaultValue);
    }

    public static void remove( String key) {
        TrayEMMPrefs prefs = getPrefs();
        if (key != null) {
            prefs.remove(key);
        }
    }

    /**
     * cleat all setting file
     */
    public static void clear() {
        TrayEMMPrefs prefs = getPrefs();
        prefs.clear();
    }
}
