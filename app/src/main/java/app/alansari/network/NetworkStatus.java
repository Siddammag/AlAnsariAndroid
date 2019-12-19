/*********************************************************
 * NetworkStatus helps us to check the Internet connection
 * state of our handheld device.**************************
 ********************************************************/
package app.alansari.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.util.Collections;

import app.alansari.Utils.LogUtils;

/**
 * Created by Parveen Dala on 12 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class NetworkStatus {

    static Context context;
    private static NetworkStatus instance = new NetworkStatus();
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;

    public static NetworkStatus getInstance(Context ctx) {
        context = ctx;
        return instance;
    }

    public static boolean getVPNStatus() {
        try {
            for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0)
                    continue;

                if ("tun0".equals(intf.getName())) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean isOnline(Context con) {
        try {
            connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            if (!connected)
                Toast.makeText(context, "Check Network Connection", Toast.LENGTH_SHORT).show();
            return connected;
        } catch (Exception e) {
            Toast.makeText(context, "Check Connectivity Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            LogUtils.v("connectivity", e.toString());
        }
        return connected;
    }

    public boolean isOnline2(Context con) {
        try {
            connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connected;
        } catch (Exception e) {
            LogUtils.v("connectivity", e.toString());
        }
        return connected;
    }

    /**
     * Checking for all possible Internet providers
     **/
    public boolean isConnectedToInternet() {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
