package app.alansari.Utils;

import android.util.Log;

import app.alansari.BuildConfig;

public class LogUtils {

    public static void d(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void i(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void v(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, message);
        }
    }

    public static void e(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void e(final String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg, tr);
        }
    }

    public static void w(final String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg, tr);
        }
    }

    public static void w(final String tag, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, tr);
        }
    }

    public static void w(final String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void printString(String data) {
        if (BuildConfig.DEBUG) {
            System.out.println(data);
        }
    }
}
