package app.alansari;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.multidex.MultiDexApplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import app.alansari.Utils.LogUtils;
import app.alansari.preferences.RSASecurityUtils;
import app.alansari.preferences.SecureMe;
import io.fabric.sdk.android.Fabric;


/**
 * Created by Parveen Dala on 05 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */

public class AppController extends MultiDexApplication {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    //AppCheck
    private static boolean isPinVerified;
    public boolean wasInBackground;
    private RequestQueue mRequestQueue;
    private SecureMe secureMePref;


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static boolean isPinVerified() {
        return isPinVerified;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        mInstance = this;
        new RSASecurityUtils().setSecurityKeys(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {

                }


            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (wasInBackground) {
                    LogUtils.e("ok", "OnResume Application >>> Was In Background ");
                } else {
                    LogUtils.e(TAG, activity.getClass().getSimpleName() + ":OnResume Application >>> Was In Foreground");
                }

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static void setIsPinVerified(boolean status) {
        isPinVerified = status;
    }


    public SecureMe getSharedPreferencesContext() {
        try {
            if (secureMePref == null) {
                secureMePref = new SecureMe(this, "");
                secureMePref.setLoggingEnabled(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return secureMePref;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }




}